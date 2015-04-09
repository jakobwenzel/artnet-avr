package usb;

/**
 * Created by Jakob on 20.03.2015.
 */
public class PacketBuffer<T> {
    T buf;
    public synchronized T get() {
        while (buf==null)
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        T res = buf;
        buf = null;
        return res;
    }
    public synchronized void put(T p) {
        buf = p;
        notify();
    }
}
