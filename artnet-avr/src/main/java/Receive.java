import artnet4j.ArtNet;
import artnet4j.ArtNetException;
import artnet4j.ArtNetServer;
import artnet4j.events.ArtNetServerListener;
import artnet4j.packets.ArtDmxPacket;
import artnet4j.packets.ArtNetPacket;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.IOException;
import java.net.SocketException;

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
                serialPort.setSerialPortParams(9600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);

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
                connect("COM3"); //TODO make configurable
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

    @Override
    public void artNetPacketReceived(ArtNetPacket packet) {
        if (packet instanceof ArtDmxPacket) {
            ArtDmxPacket dmx = (ArtDmxPacket) packet;
            //System.out.println("dmx.getNumChannels() = " + dmx.getNumChannels());
            //System.out.println("dmx.getDmx(1) = " + dmx.getDmx(1));

            try {
                if (serialPort!=null)
                    serialPort.getOutputStream().write(dmx.getDmx(1));
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

