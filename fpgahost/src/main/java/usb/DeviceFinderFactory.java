package usb;

import java.util.Properties;

import usb.d2xx.D2xxDeviceFinder;
import usb.libftdi.LibftdiDeviceFinder;

public class DeviceFinderFactory {
	private static DeviceFinder instance;
	public static DeviceFinder getDeviceFinder() {
		if (instance==null)
			instance = createInstance();
		return instance;
	}

	private static DeviceFinder createInstance() {
		//Maybe we should allow the user to change to the other driver.
		if (runningOnWindows())
			return new D2xxDeviceFinder();
		else
			return new LibftdiDeviceFinder();
	}

	private static boolean runningOnWindows() {

		Properties prop = System.getProperties( );
		String os = prop.getProperty("os.name");
		return os.startsWith("Windows");
	}

	/**
	 * Sets the instance to be used.
	 * Only to be used during simulation.
	 * @param f
	 */
	public static void setSimulationFinder(DeviceFinder f) {
		instance = f;
	}
}
