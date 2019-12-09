package segmentedfilesystem;
import java.net.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.*;
import java.util.Collections;

public class Main {

    static dataFile file1 = new dataFile();
    static dataFile file2 = new dataFile();
    static dataFile file3 = new dataFile();

    static ArrayList<Byte> fileNumbers = new ArrayList<>(3);

    private static void sortPackets(byte[] data) {
        if(fileNumbers.contains(data[1])) {
            if(fileNumbers.get(0) == data[1]) {
                if(data[0] % 2 == 0) {
                    System.out.println("Found header");
                    byte[] name = Arrays.copyOfRange(data, 2, data.length);
                    file1.fileName = new String(name);
                } else {
                    file1.packets.add(new Packet(data));
                }
            }
            else if(fileNumbers.get(1) == data[1]) {
                if(data[0] % 2 == 0) {
                    byte[] name = Arrays.copyOfRange(data, 2, data.length);
                    file2.fileName = new String(name);
                } else {
                    file2.packets.add(new Packet(data));
                }
            }
            else if(fileNumbers.get(2) == data[1]) {
                if(data[0] % 2 == 0) {
                    byte[] name = Arrays.copyOfRange(data, 2, data.length);
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

    private static void sortFile(dataFile file) {
        Collections.sort(file.packets);
    }

    private static void writeFile(dataFile file) {
        File newFile = new File(file.fileName);
        FileOutputStream os;
        try {
            os = new FileOutputStream(newFile);
            for (int i = 0; i < file.packets.size(); i++) {
                os.write(file.packets.get(i).data);
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found" + e);
        }
        catch (IOException ioe) {
            System.out.println("Exception while writing file " + ioe);
        }
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

                    byte[] buf = new byte[1028];
                    DatagramPacket response = new DatagramPacket(buf, buf.length);
                    socket.receive(response);
                    int rL = response.getLength();
                    byte[] data = response.getData();
                    sortPackets(Arrays.copyOfRange(data, 0, rL));
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

//        System.out.println(file1.packets.size());
//        System.out.println(file2.packets.size());
//        System.out.println(file3.packets.size());
//        System.out.println(file1.fileName);
//        System.out.println(file1.packets.get(0).number);
//        System.out.println(file1.packets.get(0).data);
        sortFile(file1);
        sortFile(file2);
        sortFile(file3);
        writeFile(file1);
        System.out.println(file2.fileName);
        writeFile(file2);
        writeFile(file3);
    }

}



