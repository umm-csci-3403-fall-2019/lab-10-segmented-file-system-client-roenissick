package segmentedfilesystem;

import java.util.Arrays;

public class Packet {
    static int type = -1;
    static int number = -1;
    static byte[] data;

    public Packet(byte[] packet) {
        type = packet[0];
        if (type == 1){
            number = ((packet[2] & 0xff) << 8) | (packet[3] & 0xff);
            data = Arrays.copyOfRange(packet, 4, packet.length);
        }
    }
}
