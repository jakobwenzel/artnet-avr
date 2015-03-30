package usb.d2xx;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.InvalidParameterException;

import usb.MPSSEInit;
import usb.Util;
import jd2xx.JD2XX;

/**
 * Created by wilson on 16.10.13.
 */
public class D2xxFtdiDevice implements usb.FtdiDevice {

	private static Logger logger =  LogManager.getLogger(D2xxFtdiDevice.class);


    JD2XX.DeviceInfo info;

	public JD2XX getRawDevice() {
		return rawDevice;
	}

	private JD2XX rawDevice;

		/*uint dwRxBytes;


		FTResult result = FTD2XX.FT_GetQueueStatus(Handle, out dwRxBytes);
		if (result != FTResult.F
		T_OK)
			throw new FTDIException(result);

		return (int)dwRxBytes;
	}*/

	//public FTDIDeviceInfo Info { get; private set; }

	public D2xxFtdiDevice(JD2XX.DeviceInfo info)
	{
		this.info = info;

	}
	/*public IntPtr GetHandle()//TODO nur für DebugInterfaceDummy (Todo copied from Accemic's C# Code)
	{
		return Handle;
	}*/
	//Set PID & VID to 0 to get FTDI devices
	public static JD2XX.DeviceInfo[] getDevices(short vid,short pid) throws IOException {
        JD2XX jd = new JD2XX();
        if (vid!=0 && pid!=0) {
            //this is not implemented in jd2xx...
            throw new UnsupportedOperationException("Not implemented.");
        }

        int numDevices = jd.createDeviceInfoList();

        /*if (numDevices < 1) {
            throw new IOException("Device not found.");
        }*/

        JD2XX.DeviceInfo[] res = new JD2XX.DeviceInfo[numDevices];

        for (int i=0; i<numDevices;i++) {
            res[i] = jd.getDeviceInfoDetail(i);
        }
        return res;
    }
	/*	uint numDevices;
		FTResult result;
		if (PID != 0 && VID != 0)
		{
			result = FTD2XX.FT_SetVIDPID(VID, PID);
			if (result != FTResult.FT_OK)
				throw new FTDIException(result);
		}

		result = FTD2XX.FT_CreateDeviceInfoList(out numDevices);
		if (result != FTResult.FT_OK)
			throw new FTDIException(result);
		if (numDevices < 1)
		{
			return new FTDIDeviceInfo[0];
		}
		FTDIDeviceInfo[] list = new FTDIDeviceInfo[numDevices];
		for (uint i = 0; i < numDevices; i++)
		{
			list[i] = new FTDIDeviceInfo();
			IntPtr  ftHandle;
			byte[] pcSerialNumber = new byte[32];
			byte[] pcDescription = new byte[64];
			uint dummy;
			list[i].Index = i;
			result = FTD2XX.FT_GetDeviceInfoDetail(i, out list[i].Flags, out list[i].Type, out list[i].ID, out dummy,
					pcSerialNumber, pcDescription, out ftHandle);
			int l;
			for (l = 0; l < pcSerialNumber.Length; l++)
			{
				if (pcSerialNumber[l] == 0)
					break;
			}
			list[i].SerialNumber =  Encoding.ASCII.GetString(pcSerialNumber,0,l);
			for (l = 0; l < pcDescription.Length; l++)
			{
				if (pcDescription[l] == 0)
					break;
			}
			list[i].Description = Encoding.ASCII.GetString(pcDescription, 0, l);

			if (result != FTResult.FT_OK)
				throw new FTDIException(result);

		}
		return list;
	}*/
	@Override
	public void open() throws IOException {
        rawDevice = new JD2XX(info.index);
	/*	FTResult result = FTD2XX.FT_Open((uint)this.Info.Index,out Handle);
		if (result != FTResult.FT_OK)
			throw new FTDIException(result);*/

        //Was already commented out
            /*FT_PROGRAM_DATA data=new FT_PROGRAM_DATA();
            data.Signature2 = uint.MaxValue;
            data.Description = new string('\0', 64);
            data.Manufacturer = new string('\0', 64);
            data.SerialNumber = new string('\0', 64);
            FTD2XX.FT_EE_Read(Handle, ref data);*/

		getRawDevice().resetDevice();

		purge(true, true);

		getRawDevice().setUSBParameters(0x10000, 0x10000);//Set USB request transfer sizes to 64K
		getRawDevice().setChars(0, false, 0, false);//Disable event and error characters
		getRawDevice().setTimeouts(500, 0);
		getRawDevice().setLatencyTimer(2);//Set the latency timer to 1mS (default is 16mS)
		getRawDevice().setFlowControl(JD2XX.FLOW_RTS_CTS, 0, 0);//Turn on flow control to synchronize IN requests
	}
	@Override
	public void close() throws IOException {
		if (getRawDevice() == null)
			return;

        getRawDevice().close();
	}

	/*public void setBitMode(0xFF, FT_BitMode Mode, byte ucMask)
	{
		FTResult result = FTD2XX.FT_setBitMode(0xFF, Handle,ucMask, Mode);
		if (result != FTResult.FT_OK)
			throw new FTDIException(result);
	}
	public void SetDevisor(ushort Devisor)
	{
		FTResult result = FTD2XX.FT_SetDivisor(Handle, Devisor);
		if (result != FTResult.FT_OK)
			throw new FTDIException(result);
	}
	public void SetLatencyTimer(byte TimeMs)
	{
		FTResult result = FTD2XX.FT_SetLatencyTimer(Handle, TimeMs);
		if (result != FTResult.FT_OK)
			throw new FTDIException(result);
	}

	public void Write(byte[] buf, int Length)
	{
		rawDevice.write
		uint lpBytesWritten;
		FTResult result = FTD2XX.FT_Write(Handle, buf, (uint)Length, out lpBytesWritten);
		if (result != FTResult.FT_OK)
			throw new FTDIException(result);
		if (lpBytesWritten != Length)
			throw new FTDIException("Write failed");
	}
	public void Read(byte[] buf, int Length)
	{
		uint lpBytesReturned;
		FTResult result = FTD2XX.FT_Read(Handle, buf, (uint)Length, out lpBytesReturned);
		if (result != FTResult.FT_OK)
			throw new FTDIException(result);
		if (Length != lpBytesReturned)
			throw new FTDITimeoutException(string.Format("FTDI:Read failed. Read {0} form {1} bytes", lpBytesReturned, Length));
	}*/
	@Override
	public void purge(boolean RX, boolean TX) throws IOException {

		int flags = JD2XX.PURGE_NONE;
		if(RX)
			flags|=JD2XX.PURGE_RX;
		if(TX)
			flags|=JD2XX.PURGE_TX;
		getRawDevice().purge(flags);
	}
	@Override
	public void InitSyncFIFO() throws IOException {
		//Purge(true, true);

		/*rawDevice.setBitMode(0xFF, JD2XX.BITMODE_RESET);

		sleep(20);*/
		//sleep(1000);

		getRawDevice().setBitMode(0xFF, JD2XX.BITMODE_SYNC_FIFO);
		purge(true, false);

		getRawDevice().setLatencyTimer(2);

		getRawDevice().setUSBParameters(0x10000, 0x10000);

		getRawDevice().setFlowControl(JD2XX.FLOW_RTS_CTS, 0, 0);
	}

	@Override
	public void InitMPSSE(MPSSEInit init) throws IOException {
		//This was already commented out in original code

		//rawDevice.resetDevice();

		purge(true, true);

		getRawDevice().setTimeouts(init.Timeout, 0);


		getRawDevice().setBitMode(0xFF, JD2XX.BITMODE_RESET);


		Util.sleep(20);

		getRawDevice().setBitMode(0xFF, JD2XX.BITMODE_MPSSE);
		purge(true, true);


		Util.sleep(100);


		//The following code checks if we were successful in settings mpsse mide by sending an
		//invalid command and checking for an error response. but the fpga may still be running and
		//sending bogus data, therefore making this test fail. Therefore it is commented out.

		/*logger.debug("Got status {}", Arrays.toString(rawDevice.getStatus()));
		rawDevice.write(0x84);//Enable loopback
		logger.debug("Got status {}",Arrays.toString(rawDevice.getStatus()));

		//Check if the device is working by sending an invalid command and checking for expected return value
		rawDevice.write(0x0AB);
		logger.debug("Got status {}",Arrays.toString(rawDevice.getStatus()));


		int ret = Read16() & 0xFFFF; //Java only has signed values, anding makes it unsigned again
		if (ret == 0x6032) {
			logger.debug("read again");
			ret = Read16() & 0xFFFF;
		}

		if (ret != 0xabfa)
			throw new IOException("MPSSE invalid return");

		rawDevice.write(0x85);//Disable loopback*/

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

		getRawDevice().write(buf, 0, ToSend);

		MPSEE_SetBitsLow(init.DirLow, init.ValLow);
		MPSEE_SetBitsHigh(init.DirHigh, init.ValHigh);
	}

	@Override
	public int write(byte[] arr, int offset, int length) throws IOException {
		return getRawDevice().write(arr,offset,length);
	}

	@Override
	public int read(byte[] arr, int offset, int length) throws IOException {
		return getRawDevice().read(arr, offset, length);
	}

	@Override
	public void MPSEE_SetBitsLow(byte Direction, byte Value) throws IOException {
		byte[] buf = { (byte)0x80, Value, Direction };

		getRawDevice().write(buf, 0, 3);
	}
	@Override
	public void MPSEE_SetBitsHigh(byte Direction, byte Value) throws IOException {
		byte[] buf = { (byte)0x82, Value, Direction };

		getRawDevice().write(buf, 0, 3);
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

		getRawDevice().write(buf, 0, 3);
		getRawDevice().write(Data, 0, Length);
	}
	@Override
	public byte MPSEE_GetBitsLow() throws IOException {
		byte[] buf ={(byte)0x81};
		getRawDevice().write(buf, 0, 1);
		getRawDevice().read(buf, 0, 1);
		return buf[0];
	}
	@Override
	public byte MPSEE_GetBitsHigh() throws IOException {
		byte[] buf = { (byte)0x83 };
		getRawDevice().write(buf, 0, 1);
		getRawDevice().read(buf, 0, 1);
		return buf[0];
	}
	@Override
	public void MPSEE_SendImmediate() throws IOException {
		byte[] buf = { (byte)0x87 };
		getRawDevice().write(buf, 0, 1);
	}
	/*public void Write(byte val)
	{
		byte[] buf = { val };
		Write(buf, 1);
	}
	public byte Read8()
	{
		byte[] buf = new byte[1];
		Read(buf, 1);
		return buf[0];
	}*/
	@Override
	public short Read16() throws IOException {
		byte[] buf = new byte[2];
		getRawDevice().read(buf, 0, 2);
		logger.debug("{}{}",Integer.toHexString(buf[1] & 0xFF),Integer.toHexString(buf[0] & 0xFF));
		ByteBuffer bb = ByteBuffer.allocate(2);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.put(buf[0]);
		bb.put(buf[1]);
		return bb.getShort(0);
	}

	@Override
	public void setLatencyTimer(int i) throws IOException {
		getRawDevice().setLatencyTimer(i);
	}

	@Override
	public int write(byte[] data) throws IOException {
		return getRawDevice().write(data);
	}

	@Override
	public void write(byte data) throws IOException {
		getRawDevice().write(data);
	}

	@Override
	public byte[] read(int num) throws IOException {
		return getRawDevice().read(num);
	}

	@Override
	public byte read() throws IOException {
		return (byte)getRawDevice().read();
	}
}
