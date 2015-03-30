package usb;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class Util {
	private static Logger logger =  LogManager.getLogger(Util.class);
	public static void sleep(long duration) {
		try {
			Thread.sleep(duration);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}

	}

	public static String toHexString(byte[] arr) {
		StringBuilder sb = new StringBuilder();
		boolean first = true;

		sb.append('[');
		for(byte b:arr) {
			if (first) first = false; else sb.append(',');
			String s = Integer.toHexString(b&0xFF);
			if (s.length()==1) sb.append('0');
			sb.append(s);
		}
		sb.append(']');
		return sb.toString();
	}
	public static void fillByteArray(byte[] data, int startIdx, long content) {
		data[startIdx  ] = (byte)((content>>24)&0xFF);
		data[startIdx+1] = (byte)((content>>16)&0xFF);
		data[startIdx+2] = (byte)((content>> 8)&0xFF);
		data[startIdx+3] = (byte)((content    )&0xFF);
	}

	public static long readUInt32(byte[] data, int startIdx) {
		return  (
					(data[startIdx  ]&0xFF)<<24 |
					(data[startIdx+1]&0xFF)<<16 |
					(data[startIdx+2]&0xFF)<<8  |
					(data[startIdx+3]&0xFF)
				)&0xFFFFFFFFl; //Neccessary, because otherwise java calculates everything in int and sign extends...
	}

	/**
	 * Set specific bit in a byte array, counting from the byte with highest index as LSB
	 * @param data
	 * @param num
	 * @param content
	 */
	public static void writeBit(byte[] data, int num, boolean content) {
		int addr = data.length-1-num/8;
		int inByte = num % 8;

		byte mask = (byte)(1<<inByte);
		if (content)
			data[addr] |= mask;
		else
			data[addr] &= ~mask;
	}
	/**
	 * Set specific bits in a byte array, counting from the byte with highest index as LSB
	 * @param data
	 * @param from lsb of data to set
	 * @param length length in bits of data to set
	 * @param content
	 */
	public static void fillBitsInByteArray(byte[] data, int from, int length, int content) {
		for (int i=0;i<length;i++) {
			writeBit(data,from+i, (content&1)>0);
			content >>= 1;
		}
	}


	public static byte[] toNumberOfBytes(int content, int size) throws IOException {
		byte[] output = new byte[size];
		int totalShift = (size-1)*8;
		for (int i=0;i<size;i++)
			output[i] = (byte) ((content >> (totalShift-i*8)) & 0xFF);
		return output;
	}
}
