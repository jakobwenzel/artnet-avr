package usb;

import java.io.IOException;

public interface DeviceFinder {

	public FtdiDevice findDeviceA() throws IOException;
	public FtdiDevice findDeviceB() throws IOException;
}
