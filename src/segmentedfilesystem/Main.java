package segmentedfilesystem;
import java.net.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.io.*;

public class Main {

    static File file1 = new File();
    static File file2 = new File();
    static File file3 = new File();

    static ArrayList<Byte> fileNumbers = new ArrayList<>(3);

    private static void sortPackets(byte[] data) {
        if(fileNumbers.contains(data[1])) {
            if(fileNumbers.get(0) == data[1]) {
                if(data[0] % 2 == 0) {
                    byte[] name = Arrays.copyOfRange(data, 3, data.length);
                    file1.fileName = new String(name);
                } else {
                    file1.packets.add(new Packet(data));
                }
            }
            else if(fileNumbers.get(1) == data[1]) {
                if(data[0] % 2 == 0) {
                    byte[] name = Arrays.copyOfRange(data, 3, data.length);
                    file2.fileName = new String(name);
                } else {
                    file2.packets.add(new Packet(data));
                }
            }
            else if(fileNumbers.get(2) == data[1]) {
                if(data[0] % 2 == 0) {
                    byte[] name = Arrays.copyOfRange(data, 3, data.length);
                    file3.fileName = new String(name);
                } else {
                    file3.packets.add(new Packet(data));
                }
            }
        } else {
            fileNumbers.add(data[1]);
            sortPackets(data);
        }
    }

    private static void sortFiles(ArrayList<byte[]> file) {

    }

    public static void main(String[] args) {

        String hostname = "csci-4409.morris.umn.edu";
        int port = 6014;
        try {

            InetAddress address = InetAddress.getByName(hostname);
            DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(1000);
            byte[] buffer = new byte[1028];
            DatagramPacket request = new DatagramPacket(buffer, buffer.length, address, port);
            socket.send(request);

            while(true) {
                try {

                    DatagramPacket response = new DatagramPacket(buffer, buffer.length);
                    socket.receive(response);
                    byte[] data = response.getData();
//                    System.out.println(data[1]);
                    sortPackets(data);

                }
                catch (SocketTimeoutException ex) {
                    System.out.println("Timeout error: " + ex.getMessage());
                    socket.close();
            }
        }

        } catch (IOException ex) {
            System.out.println("Client error: " + ex.getMessage());
//            ex.printStackTrace();
        }
        System.out.println(file1.packets.size());
        System.out.println(file2.packets.size());
        System.out.println(file3.packets.size());
        System.out.println(file1.fileName);
    }

}



