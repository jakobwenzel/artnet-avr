import artnet4j.ArtNet;
import artnet4j.ArtNetException;
import artnet4j.ArtNetServer;
import artnet4j.events.ArtNetServerListener;
import artnet4j.packets.ArtDmxPacket;
import artnet4j.packets.ArtNetPacket;
import usb.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

/**
 * Created by Jakob on 30.01.2015.
 */
public class Receive implements ArtNetServerListener {
    public static final boolean verbose = false;
    private static final int numLeds = 170;
    private static final int baudrate = 115200;
    private static final String port = "COM3";

    PacketBuffer<byte[]> buf = new PacketBuffer<>();
    private FtdiDevice devB;

    public static void main(String[] args) {
        new Receive().test();
    }



    private void test() {
        ArtNet artnet = new ArtNet();
        try {
            try {
                connect();
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            artnet.start();
            //artnet.setBroadCastAddress("192.168.178.96");

            artnet.addServerListener(this);

            Runnable senderThread = new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        if (verbose)System.out.println("try to get package");
                        byte[] packet = buf.get();
                        if (verbose)System.out.println("got package");
                        try {
                            devB.MPSSE_ClockDataOut(false, true, packet,packet.length);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            new Thread(senderThread).start();

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (ArtNetException e) {
            e.printStackTrace();
        }
    }

    private void connect() throws IOException {
        final DeviceFinder finder = DeviceFinderFactory.getDeviceFinder();
        devB = finder.findDeviceB();
        devB.open();

        MPSSEInit init0 = new MPSSEInit();
        init0.SpeedHz = 30000000;
        init0.DirLow = 0x3; //AD0: CCLK, AD1: DIN
        init0.ValHigh = 0x23;
        init0.DirHigh = 0x23;
        init0.Timeout = 1000;

        devB.InitMPSSE(init0);
    }

    @Override
    public void artNetPacketBroadcasted(ArtNetPacket packet) {

    }

    final static int channelCount = numLeds*3;
    @Override
    public void artNetPacketReceived(ArtNetPacket packet) {
        if (packet instanceof ArtDmxPacket) {
            ArtDmxPacket dmx = (ArtDmxPacket) packet;
            //System.out.println("dmx.getNumChannels() = " + dmx.getNumChannels());
            if (verbose) System.out.println("New packet");

            byte[] data = new byte[channelCount];
            for (int i=0;i<channelCount;i++) {
                int m = i%3;
                if (m==0)
                    data[i]=(byte)dmx.getDmx(i+2);
                else if (m==1)
                    data[i]=(byte)dmx.getDmx(i);
                else
                    data[i]=(byte)dmx.getDmx(i+1);
            }

            buf.put(data);
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

