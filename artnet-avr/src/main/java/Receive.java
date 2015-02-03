import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;

import artnet4j.ArtNet;
import artnet4j.ArtNetException;
import artnet4j.ArtNetServer;
import artnet4j.events.ArtNetServerListener;
import artnet4j.packets.ArtDmxPacket;
import artnet4j.packets.ArtNetPacket;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

/**
 * Created by Jakob on 30.01.2015.
 */
public class Receive implements ArtNetServerListener {


    private SerialPort serialPort;

    public static void main(String[] args) {
        new Receive().test();
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
            CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);

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
        ArtNet artnet = new ArtNet();
        try {
            try {
                connect("/dev/ttyACM0"); //TODO make configurable
            } catch (Exception e) {
                e.printStackTrace();
            }
            artnet.start();
            //artnet.setBroadCastAddress("192.168.178.96");

            artnet.addServerListener(this);

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (ArtNetException e) {
            e.printStackTrace();
        } /*catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void artNetPacketBroadcasted(ArtNetPacket packet) {

    }

    final static int channelCount = 60*3;
    @Override
    public void artNetPacketReceived(ArtNetPacket packet) {
        if (packet instanceof ArtDmxPacket) {
            ArtDmxPacket dmx = (ArtDmxPacket) packet;
            //System.out.println("dmx.getNumChannels() = " + dmx.getNumChannels());
            System.out.println("New packet");

            try {
                if (serialPort!=null) {
                    OutputStream out = serialPort.getOutputStream();
                    for (int i=1;i<=channelCount;i++) {
                        System.out.println("dmx.getDmx("+i+") = " + dmx.getDmx(i));
                        out.write(dmx.getDmx(i));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void artNetPacketUnicasted(ArtNetPacket packet) {

    }

    @Override
    public void artNetServerStarted(ArtNetServer server) {

    }

    @Override
    public void artNetServerStopped(ArtNetServer server) {

    }
}

