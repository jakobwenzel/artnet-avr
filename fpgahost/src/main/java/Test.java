import java.io.IOException;

import usb.DeviceFinder;
import usb.DeviceFinderFactory;
import usb.FtdiDevice;
import usb.MPSSEInit;

public class Test {
	public static void main(String[] args) throws IOException {
		System.out.println("test");
		final DeviceFinder finder = DeviceFinderFactory.getDeviceFinder();
		FtdiDevice devB = finder.findDeviceB();
		devB.open();

		MPSSEInit init0 = new MPSSEInit();
		init0.SpeedHz = 30000000;
		init0.DirLow = 0x3; //AD0: CCLK, AD1: DIN
		init0.ValHigh = 0x23;
		init0.DirHigh = 0x23;
		init0.Timeout = 1000;

		devB.InitMPSSE(init0);
		while(true) {

			if (System.console()!=null) {
				System.out.println("press enter");
				System.console().readLine();
				System.out.println("continuing");
			}
			byte[] data = new byte[900];
			for (int i=0;i<data.length;i++)
				data[i] = (byte) i;
			devB.MPSSE_ClockDataOut(true, true, data,data.length);
			devB.MPSSE_ClockDataOut(true, true, data,data.length);
			devB.MPSSE_ClockDataOut(true, true, data,data.length);
			devB.MPSSE_ClockDataOut(true, true, data,data.length);
			devB.MPSSE_ClockDataOut(true, true, data,data.length);
			devB.MPSSE_ClockDataOut(true, true, data,data.length);
			devB.MPSSE_ClockDataOut(true, true, data,data.length);
			devB.MPSSE_ClockDataOut(true, true, data,data.length);
		}
		//devB.close();
	}
}