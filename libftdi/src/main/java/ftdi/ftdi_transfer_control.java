package ftdi;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import ftdi.FtdiLibrary.libusb_transfer;
import java.util.Arrays;
import java.util.List;
/**
 * <i>native declaration : /usr/include/sys/time.h:351</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class ftdi_transfer_control extends Structure {
	public int completed;
	/** C type : unsigned char* */
	public Pointer buf;
	public int size;
	public int offset;
	/** C type : ftdi_context* */
	public ftdi.ftdi_context.ByReference ftdi;
	/** C type : libusb_transfer* */
	public libusb_transfer transfer;
	public ftdi_transfer_control() {
		super();
	}
	protected List<? > getFieldOrder() {
		return Arrays.asList("completed", "buf", "size", "offset", "ftdi", "transfer");
	}
	/**
	 * @param buf C type : unsigned char*<br>
	 * @param ftdi C type : ftdi_context*<br>
	 * @param transfer C type : libusb_transfer*
	 */
	public ftdi_transfer_control(int completed, Pointer buf, int size, int offset, ftdi.ftdi_context.ByReference ftdi, libusb_transfer transfer) {
		super();
		this.completed = completed;
		this.buf = buf;
		this.size = size;
		this.offset = offset;
		this.ftdi = ftdi;
		this.transfer = transfer;
	}
	public static class ByReference extends ftdi_transfer_control implements Structure.ByReference {
		
	};
	public static class ByValue extends ftdi_transfer_control implements Structure.ByValue {
		
	};
}
