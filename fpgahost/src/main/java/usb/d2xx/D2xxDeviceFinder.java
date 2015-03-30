package usb.d2xx;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

import usb.DeviceFinder;
import usb.DeviceIdentification;
import usb.FtdiDevice;
import jd2xx.JD2XX;

public class D2xxDeviceFinder implements DeviceFinder {
	private static Logger logger =  LogManager.getLogger(D2xxDeviceFinder.class);

	private static JD2XX.DeviceInfo[] infos;


	private static D2xxFtdiDevice findDevice(String serial) throws IOException {
		//Get device infos if not done
		if (infos == null)
			infos = D2xxFtdiDevice.getDevices((short) 0, (short) 0);

		D2xxFtdiDevice res = null;
		//Search in all devices
		for (JD2XX.DeviceInfo i : infos) {
			logger.debug("device has serial {}",i.serial);
			if (i.serial.equals(serial)) {
				if (res!=null)
					throw new RuntimeException("Multiple devices with serial number "+serial+" found.");
				res = new D2xxFtdiDevice(i);
				logger.debug("found device {}",i);
			}
		}

		if (res==null)
			throw new RuntimeException("No device with serial number "+serial+" found.");

		return res;
	}

	public D2xxFtdiDevice findDeviceA() throws IOException {
		return findDevice(DeviceIdentification.SERIAL_A);
	}


	public FtdiDevice findDeviceB() throws IOException {
		return findDevice(DeviceIdentification.SERIAL_B);
	}

}
