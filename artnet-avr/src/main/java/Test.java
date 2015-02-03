import java.io.IOException;
import java.io.OutputStream;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

/**
 * Created by wenzel on 02.02.15.
 */
public class Test {
	private SerialPort serialPort;
	private CommPort commPort;

	public static void main(String[] args) {
		new Test().test();
	}

	void connect ( String portName ) throws Exception
	{
		System.setProperty("gnu.io.rxtx.SerialPorts", portName);
		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
		if ( portIdentifier.isCurrentlyOwned() )
		{
			System.out.println("Error: Port is currently in use");
		}
		else
		{
			commPort = portIdentifier.open(this.getClass().getName(),2000);

			if ( commPort instanceof SerialPort)
			{
				serialPort = (SerialPort) commPort;
				serialPort.setSerialPortParams(115200,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);

			}
			else
			{
				System.out.println("Error: Only serial ports are handled by this example.");
			}
		}
	}

	private void test() {
		try {
			connect("/dev/ttyACM0"); //TODO make configurable
			Thread.sleep(5000);
			while (true) output();
			/*System.out.println("Finished output");
			Thread.sleep(1000);
			commPort.close();*/
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	final static int channelCount = 4*3;

	int n = 0;
	int s= 0;
	boolean switched;
	public void output() {
		n++;
		//System.out.println(Math.sin(n++/500f)/2f+0.5);
		int t = (int) ((Math.sin(n++/100f)/2f+0.5)*255);
		/*if(lastt==t)
			return;
		lastt=t;*/
		System.out.println(t);

		if (t==0) {
			if (!switched) {
				switched=true;
				if (s<3)
					s++;
				else s=0;
			}
		} else switched=false;


		try {
			if (serialPort!=null) {
				OutputStream out = serialPort.getOutputStream();
				//for (int i=1;i<=channelCount;i++) {
				out.write(s==0||s==3?t:0);
				out.write(s==1||s==3?t:0);
				out.write(s==2||s==3?t:0);


				out.write(s==3||s==2?t:0);
				out.write(s==0||s==2?t:0);
				out.write(s==1||s==2?t:0);


				out.write(s==2||s==1?t:0);
				out.write(s==3||s==1?t:0);
				out.write(s==0||s==1?t:0);


				out.write(s==1||s==0?t:0);
				out.write(s==2||s==0?t:0);
				out.write(s==3||s==0?t:0);

				Thread.sleep(20);

				//}
			}
		} catch (IOException e) {
			e.printStackTrace();
		/*} catch (InterruptedException e) {
			e.printStackTrace();*/
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
