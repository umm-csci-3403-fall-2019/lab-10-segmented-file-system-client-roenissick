package segmentedfilesystem;
import java.net.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.io.*;

public class Main {

    static ArrayList<byte[]> file1 = new ArrayList<>();
    static ArrayList<byte[]> file2 = new ArrayList<>();
    static ArrayList<byte[]> file3 = new ArrayList<>();
    static ArrayList<Byte> fileNumbers = new ArrayList<>();

    private static void sortFiles(byte[] data) {
        if(fileNumbers.size() > 0) {

            if (fileNumbers.get(0) == data[1]) {
                System.out.println("Adding to file1");
                file1.add(data);
            }

            else if (fileNumbers.get(1) == data[1]) {
                file2.add(data);
            }

            else if (fileNumbers.get(2) == data[1]) {
                file3.add(data);
            }

            else {
                fileNumbers.add(data[1]);
                sortFiles(data);
            }
        }
    }

    public static void main(String[] args) {

        String hostname = "csci-4409.morris.umn.edu";
        int port = 6014;
        try {

            InetAddress address = InetAddress.getByName(hostname);
            DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(1000);

            while(true) {
                try {
                    byte[] buffer = new byte[1028];
                    DatagramPacket request = new DatagramPacket(buffer, buffer.length, address, port);
                    socket.send(request);

                    DatagramPacket response = new DatagramPacket(buffer, buffer.length);
                    socket.receive(response);
                    byte[] data = response.getData();
//                    System.out.println(data[1]);
                    sortFiles(data);
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
        System.out.println(file1);
    }

}



