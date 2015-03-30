package usb.libftdi;

import java.io.IOException;

import ftdi.FtdiLibrary;
import usb.FtdiDevice;

public class LibftdiDeviceFinder implements usb.DeviceFinder {


	@Override
	public FtdiDevice findDeviceA() throws IOException {

		return new LibftdiFtdiDevice(FtdiLibrary.ftdi_interface.INTERFACE_A);
	}

	@Override
	public FtdiDevice findDeviceB() throws IOException {
		return new LibftdiFtdiDevice(FtdiLibrary.ftdi_interface.INTERFACE_B);
	}
}
