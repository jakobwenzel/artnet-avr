package usb;

import java.io.IOException;

public interface FtdiDevice {

	void open() throws IOException;

	void close() throws IOException;

	void purge(boolean RX, boolean TX) throws IOException;

	void InitSyncFIFO() throws IOException;

	void InitMPSSE(MPSSEInit init) throws IOException;

	int write(byte[] arr, int offset, int length) throws IOException;

	int read(byte[] arr, int offset, int length) throws IOException;

	void MPSEE_SetBitsLow(byte Direction, byte Value) throws IOException;

	void MPSEE_SetBitsHigh(byte Direction, byte Value) throws IOException;

	void MPSSE_ClockDataOut(boolean LSB, boolean OnNegativeClock, byte[] Data, int Length) throws IOException;

	byte MPSEE_GetBitsLow() throws IOException;

	byte MPSEE_GetBitsHigh() throws IOException;

	void MPSEE_SendImmediate() throws IOException;

	short Read16() throws IOException;

	void setLatencyTimer(int i) throws IOException;

	/**
	 * Write a complete byte array
	 * @param data
	 * @return
	 */
	int write(byte[] data) throws IOException;

	/**
	 * Write one byte
	 * @param data
	 */
	void write(byte data) throws IOException;

	/**
	 * Read the specified number of bytes
	 * @param num
	 * @return
	 */
	byte[] read(int num) throws IOException;

	/**
	 * Read single byte
	 * @return
	 */
	byte read() throws IOException;
}
