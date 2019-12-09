package segmentedfilesystem;

import java.util.Arrays;
import java.util.Comparator;

class Packet implements Comparable<Packet> {
    int number = -1;
    byte[] data;

    public Packet(byte[] packet) {
        number = ((packet[2] << 8) | (packet[3] & 0xff));
        data = Arrays.copyOfRange(packet, 4, packet.length);
    }

    @Override
    public int compareTo(Packet o) {
        return this.number-o.number;
    }
}
