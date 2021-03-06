package ftdi;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
/**
 * <i>native declaration : /usr/include/sys/time.h</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class FTDIProgressInfo extends Structure {
	/** C type : size_and_time */
	public size_and_time first;
	/** C type : size_and_time */
	public size_and_time prev;
	/** C type : size_and_time */
	public size_and_time current;
	public double totalTime;
	public double totalRate;
	public double currentRate;
	public FTDIProgressInfo() {
		super();
	}
	protected List<? > getFieldOrder() {
		return Arrays.asList("first", "prev", "current", "totalTime", "totalRate", "currentRate");
	}
	/**
	 * @param first C type : size_and_time<br>
	 * @param prev C type : size_and_time<br>
	 * @param current C type : size_and_time
	 */
	public FTDIProgressInfo(size_and_time first, size_and_time prev, size_and_time current, double totalTime, double totalRate, double currentRate) {
		super();
		this.first = first;
		this.prev = prev;
		this.current = current;
		this.totalTime = totalTime;
		this.totalRate = totalRate;
		this.currentRate = currentRate;
	}
	public static class ByReference extends FTDIProgressInfo implements Structure.ByReference {
		
	};
	public static class ByValue extends FTDIProgressInfo implements Structure.ByValue {
		
	};
}
