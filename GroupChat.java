import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.Scanner;

public class GroupChat {

    static volatile boolean finished = false;
    private static String name = "";
    private static int multicastPort;
    private static String multicastAddress;
    private static MulticastSocket socket;
    private static InetAddress group;
    private static Scanner sc = new Scanner(System.in);

    // doesn't allow other classes to instantiate GroupChat
    private GroupChat() {
        
    }

    public static void join() {
        
        try {
            socket = new MulticastSocket(multicastPort);
            group = InetAddress.getByName(multicastAddress);
            socket.joinGroup(group);
            socket.setTimeToLive(5);

            Thread thread = new Thread(new ReadThread(socket, group, multicastPort));
            thread.start();

        } catch (SocketException se) {
            System.out.println("Error creating socket");
            se.printStackTrace();
        } catch (IOException ie) {
            System.out.println("Error reading/writing from/to socket");
            ie.printStackTrace();
        }
    }

    public static boolean setName(String newName) {
        newName = newName.replace(" ", "");
        System.out.println(newName);
        while (!newName.matches("^[A-Za-z0-9]*$") || newName.length() == 0 || newName.length() > 16) {
            System.out.println("ERROR: name must only consist of letters and numbers and be no more than 15 characters");
            System.out.println(newName + ", " + name);
            return false;
        }
        // update the name in the GUI if this is not the initial name set
        if (name != "") {
            ChatGUI.updateName(newName);
            sendMessage(name + " has changed their name to " + newName);
        }
        name = newName;
        return true;
    }

    public static String getName() {
        return name;
    }

    public static void sendMessageAsUser(String message) {
        message = name + ": " + message;
        sendMessage(message);
    }

    public static void sendMessage(String message) {
        byte[] buffer = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, multicastPort);
        try {
            socket.send(packet);
            Thread.sleep(200);
        } catch (IOException e) {
            System.out.println("Error writing to socket");
            e.printStackTrace();
        } catch (InterruptedException ie) {
            System.out.println("Thread interrupted");
            ie.printStackTrace();
        } 
    }

    public static void closeSocket() {
        finished = true;
        sc.close();
        try {
            socket.leaveGroup(group);
        } catch (IOException e) {
            e.printStackTrace();
        }
        socket.close();
    }

    public static int getMulticastPort() {
        return multicastPort;
    }

    public static void setMulticastPort(int multicastPort) {
        GroupChat.multicastPort = multicastPort;
    }

    public static String getMulticastAddress() {
        return multicastAddress;
    }

    public static void setMulticastAddress(String multicastAddress) {
        GroupChat.multicastAddress = multicastAddress;
    }

    

}
