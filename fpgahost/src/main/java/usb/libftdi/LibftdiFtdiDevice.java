package usb.libftdi;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.InvalidParameterException;

import ftdi.FtdiLibrary;
import ftdi.ftdi_context;
import usb.DeviceIdentification;
import usb.MPSSEInit;
import usb.Util;

public class LibftdiFtdiDevice implements usb.FtdiDevice{



	private ftdi_context context;
	private final int interfaceIdx;

	private int checkForError(int ret) throws IOException{
		if (ret<0) {
			String p = FtdiLibrary.INSTANCE.ftdi_get_error_string(context);
			System.err.println("Got libftdi error: \""+p+"\"");
			System.out.println("context is "+context);
			throw new IOException(p+" ("+ret+")");
		}
		return ret;
	}


	public LibftdiFtdiDevice(int interfaceIdx) {
		this.interfaceIdx = interfaceIdx;
	}

	@Override
	public void open() throws IOException {

		context = FtdiLibrary.INSTANCE.ftdi_new();
		checkForError(FtdiLibrary.INSTANCE.ftdi_set_interface(context,interfaceIdx));

		checkForError(FtdiLibrary.INSTANCE.ftdi_usb_open_desc(context, DeviceIdentification.VID,
				DeviceIdentification.PID, null, (String) null));
	}

	@Override
	public void close() throws IOException {
		checkForError(FtdiLibrary.INSTANCE.ftdi_usb_close(context));
		FtdiLibrary.INSTANCE.ftdi_free(context);
		context = null;
	}

	@Override
	public void purge(boolean RX, boolean TX) throws IOException {
		if (RX)
			checkForError(FtdiLibrary.INSTANCE.ftdi_usb_purge_rx_buffer(context));
		if (TX)
			checkForError(FtdiLibrary.INSTANCE.ftdi_usb_purge_tx_buffer(context));
	}

	@Override
	public void InitSyncFIFO() throws IOException {

		checkForError(FtdiLibrary.INSTANCE.ftdi_set_bitmode(context, (byte) 0xFF, (byte) FtdiLibrary.ftdi_mpsse_mode.BITMODE_SYNCFF));
		purge(true, false);

		setLatencyTimer(2);

		checkForError(FtdiLibrary.INSTANCE.ftdi_read_data_set_chunksize(context, 0x10000));
		checkForError(FtdiLibrary.INSTANCE.ftdi_write_data_set_chunksize(context, 0x10000));

		checkForError(FtdiLibrary.INSTANCE.ftdi_setflowctrl(context, FtdiLibrary.SIO_RTS_CTS_HS));
	}

	@Override
	public void InitMPSSE(MPSSEInit init) throws IOException {

		purge(true, true);

		checkForError(FtdiLibrary.INSTANCE.ftdi_set_latency_timer(context,(byte)init.Timeout));



		checkForError(FtdiLibrary.INSTANCE.ftdi_set_bitmode(context, (byte) 0xFF, (byte) FtdiLibrary.ftdi_mpsse_mode.BITMODE_RESET));


		Util.sleep(20);

		checkForError(FtdiLibrary.INSTANCE.ftdi_set_bitmode(context, (byte) 0xFF, (byte) FtdiLibrary.ftdi_mpsse_mode.BITMODE_MPSSE));
		purge(true, true);


		Util.sleep(100);


		byte[] buf=new byte[100];
		int ToSend=0;
		if (init.Enable3Phase)
			buf[ToSend++] = (byte)0x8C;
		else
			buf[ToSend++] = (byte)0x8D;

		if (init.EnableDivide5)
			buf[ToSend++] = (byte)0x8B;
		else
			buf[ToSend++] = (byte)0x8A;

		if (init.EnableAdaptivClocking)
			buf[ToSend++] = (byte)0x96;
		else
			buf[ToSend++] = (byte)0x97;

		int dwClockDivisor = (60000000) / (2 * init.SpeedHz) - 1;// Value of clock divisor, SCL Frequency = 60/((1+0x05DB)*2) (MHz) = 20khz

		int aSpeed=60000000/((dwClockDivisor+1)*2);
		if (aSpeed != init.SpeedHz)
			throw new InvalidParameterException("Cannot apply speed");

		buf[ToSend++] = (byte)0x86;
		buf[ToSend++] = (byte)(dwClockDivisor & 0xFF);
		buf[ToSend++] = (byte)((dwClockDivisor >> 8) & 0xFF);

		write(buf, 0, ToSend);

		MPSEE_SetBitsLow(init.DirLow, init.ValLow);
		MPSEE_SetBitsHigh(init.DirHigh, init.ValHigh);
	}

	@Override
	public int write(byte[] arr, int offset, int length) throws IOException {
		ByteBuffer buff = ByteBuffer.wrap(arr,offset,length);
		return checkForError(FtdiLibrary.INSTANCE.ftdi_write_data(context, buff, length));
	}


	@Override
	public int read(byte[] arr, int offset, int length) throws IOException {
		ByteBuffer buff = ByteBuffer.wrap(arr,offset,length);
		return checkForError(FtdiLibrary.INSTANCE.ftdi_read_data(context, buff, length));
	}

	@Override
	public void MPSEE_SetBitsLow(byte Direction, byte Value) throws IOException {
		byte[] buf = { (byte)0x80, Value, Direction };

		write(buf, 0, 3);
	}

	@Override
	public void MPSEE_SetBitsHigh(byte Direction, byte Value) throws IOException {
		byte[] buf = { (byte)0x82, Value, Direction };

		write(buf, 0, 3);
	}

	@Override
	public void MPSSE_ClockDataOut(boolean LSB, boolean OnNegativeClock, byte[] Data, int Length) throws IOException {
		if (Length > 0x10000)
			throw new InvalidParameterException("Out of range.");

		byte[] buf = { 0, (byte)(Length-1), (byte)((Length-1)>>8) };
		if (LSB)
			buf[0] =(byte)(OnNegativeClock ? 0x19:0x18);

		else
			buf[0] =(byte)(OnNegativeClock ? 0x11 : 0x10);

		write(buf, 0, 3);
		write(Data, 0, Length);
	}

	@Override
	public byte MPSEE_GetBitsLow() throws IOException {
		byte[] buf ={(byte)0x81};
		write(buf, 0, 1);
		read(buf, 0, 1);
		return buf[0];
	}

	@Override
	public byte MPSEE_GetBitsHigh() throws IOException {
		byte[] buf = { (byte)0x83 };
		write(buf, 0, 1);
		read(buf, 0, 1);
		return buf[0];
	}

	@Override
	public void MPSEE_SendImmediate() throws IOException {
		byte[] buf = { (byte)0x87 };
		write(buf, 0, 1);
	}

	@Override
	public short Read16() throws IOException {
		byte[] buf = new byte[2];
		read(buf, 0, 2);

		ByteBuffer bb = ByteBuffer.allocate(2);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.put(buf[0]);
		bb.put(buf[1]);
		return bb.getShort(0);
	}

	@Override
	public void setLatencyTimer(int i) throws IOException {
		checkForError(FtdiLibrary.INSTANCE.ftdi_set_latency_timer(context,(byte)i));
	}

	@Override
	public int write(byte[] data) throws IOException {
		return write(data,0,data.length);
	}

	@Override
	public void write(byte data) throws IOException {
		byte[] arr = new byte[1];
		arr[0] = data;
		write(arr,0,1);
	}

	@Override
	public byte[] read(int num) throws IOException {
		byte[] result = new byte[num];
		int read = read(result,0,num);
		if (read<num) { //If we could not read enough data
			byte[] smaller = new byte[read];
			System.arraycopy(result, 0, smaller, 0, read);
			return smaller;
		}
		return result;
	}

	@Override
	public byte read() throws IOException {
		byte[] res = new byte[0];
		//As long as we could not read anything, try again
		//this is an endless loop if the fpga is not configured.
		while(res.length==0)
			res = read(1);

		return res[0];
	}
}
