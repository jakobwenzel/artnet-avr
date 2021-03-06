package ftdi;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
/**
 * \brief Progress Info for streaming read<br>
 * <i>native declaration : /usr/include/sys/time.h:557</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class size_and_time extends Structure {
	public long totalBytes;
	/** C type : timeval */
	public timeval time;
	public size_and_time() {
		super();
	}
	protected List<? > getFieldOrder() {
		return Arrays.asList("totalBytes", "time");
	}
	/** @param time C type : timeval */
	public size_and_time(long totalBytes, timeval time) {
		super();
		this.totalBytes = totalBytes;
		this.time = time;
	}
	public static class ByReference extends size_and_time implements Structure.ByReference {
		
	};
	public static class ByValue extends size_and_time implements Structure.ByValue {
		
	};
}
