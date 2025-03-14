import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.Scanner;

public class GroupChat {

    static volatile boolean finished = false;
    private static final String CLOSE_MESSAGE = "!close";
    public static void main(String[] args) {
        int multicastPort = 5000;
        String multicastAddress = "230.0.0.1";
        Scanner sc = new Scanner(System.in);
        try {
            MulticastSocket socket = new MulticastSocket(multicastPort);
            InetAddress group = InetAddress.getByName(multicastAddress);
            socket.joinGroup(group);
            socket.setTimeToLive(5);

            Thread thread = new Thread(new ReadThread(socket, group, multicastPort));
            thread.start();

            while (!finished) {
                String message;
                message = sc.nextLine();

                if (message.equalsIgnoreCase(CLOSE_MESSAGE)) {
                    finished = true;
                    socket.leaveGroup(group);
                    socket.close();
                    break;
                }
                byte[] buffer = message.getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, multicastPort);
                socket.send(packet);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                    System.out.println("Thread interrupted");
                    ie.printStackTrace();
                }
            }
            
        } catch (SocketException se) {
            System.out.println("Error creating socket");
            se.printStackTrace();
        } catch (IOException ie) {
            System.out.println("Error reading/writing from/to socket");
            ie.printStackTrace();
        }
    }

}
