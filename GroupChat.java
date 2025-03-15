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

    /**
     * Joins the multicast group. Creates a separate thread to listen for messages.
     */
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

    /**
     * Changes the name of the user if the 'newName' argument is valid. The name must consist only of letter and numbers and be no more than 16 characters
     * @param newName: The new name for the user
     * @return: True if and only if the name is valid
     */
    public static boolean setName(String newName) {
        newName = newName.replace(" ", "");
        System.out.println(newName);
        while (!newName.matches("^[A-Za-z0-9]*$") || newName.length() == 0 || newName.length() > 16) {
            ChatGUI.appendToChatArea("(ERROR): name must only consist of letters and numbers and be no more than 16 characters");
            return false;
        }
        // Update the name in the GUI if this is not the initial name set
        if (name != "") {
            ChatGUI.updateName(newName);
            sendMessage(name + " has changed their name to " + newName);
        }
        name = newName;
        return true;
    }

    /**
     * Getter for name
     * @return: name
     */
    public static String getName() {
        return name;
    }

    /**
     * Send a message in the chat with the user name attached
     * @param message: The message to be sent
     */
    public static void sendMessageAsUser(String message) {
        message = name + ": " + message;
        sendMessage(message);
    }

    /**
     * Sends a message in the chat
     * @param message: The message to be sent
     */
    public static void sendMessage(String message) {
        byte[] buffer = message.getBytes(); // convert string to bytes array
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, multicastPort); // datagram packet to send
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

    /**
     * Closes the socket
     */
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

    /**
     * Getter for the multicast port
     * @return: the multicast port
     */
    public static int getMulticastPort() {
        return multicastPort;
    }

    /**
     * Setter for the multicast port
     * @param multicastPort
     */
    public static void setMulticastPort(int multicastPort) {
        GroupChat.multicastPort = multicastPort;
    }

    /**
     * Getter for the multicast address
     * @return: the multicast address
     */
    public static String getMulticastAddress() {
        return multicastAddress;
    }

    /**
     * Setter for the multicast address
     * @param multicastAddress
     */
    public static void setMulticastAddress(String multicastAddress) {
        GroupChat.multicastAddress = multicastAddress;
    }

}
