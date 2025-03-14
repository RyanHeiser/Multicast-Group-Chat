import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.Scanner;

// TODO determine public/private for variables and methods

public class GroupChat {

    static volatile boolean finished = false;
    private final String CLOSE_MESSAGE = "!close";
    private final String HELP_MESSAGE = "!help";
    private final String CHANGE_NAME_MESSAGE = "!name";
    static String name = "";
    int multicastPort;
    String multicastAddress;
    MulticastSocket socket;
    InetAddress group;
    Scanner sc = new Scanner(System.in);


    public GroupChat(int multicastPort, String multicastAddress) {
        this.multicastPort = multicastPort;
        this.multicastAddress = multicastAddress;
    }

    public void join() {
        
        try {
            socket = new MulticastSocket(multicastPort);
            group = InetAddress.getByName(multicastAddress);
            socket.joinGroup(group);
            socket.setTimeToLive(5);

            changeName();

            System.out.println("Type '" + HELP_MESSAGE + "' for commands");

            Thread thread = new Thread(new ReadThread(socket, group, multicastPort));
            thread.start();

            while (!finished) {
                String message;
                message = sc.nextLine();

                if (message.equalsIgnoreCase(CLOSE_MESSAGE)) {
                    sendMessage(name + " has left the chat");
                    closeSocket();
                    break;
                } else if (message.equalsIgnoreCase(HELP_MESSAGE)) {
                    listCommands();
                } else if (message.equalsIgnoreCase(CHANGE_NAME_MESSAGE)) {
                    changeName();
                } else {
                    sendMessageAsUser(message);
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

    public void changeName() {
        String prevName = name;
        System.out.print("New name: ");
        name = sc.nextLine();
        while (!name.matches("^[A-Za-z0-9]*$") || name.length() == 0) {
            System.out.println("ERROR: name must only consist of letters and numbers");
            System.out.print("New name: ");
            name = sc.nextLine();
        }
        if (!name.equals(prevName) && prevName != "") {
            sendMessage(prevName + " changed their name to " + name);
        } else if (!name.equals(prevName)) {
            sendMessage(name + " has joined the chat");
        }
    }

    private void sendMessageAsUser(String message) {
        message = name + ": " + message;
        sendMessage(message);
    }

    private void sendMessage(String message) {
        byte[] buffer = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, multicastPort);
        try {
            socket.send(packet);
            Thread.sleep(500);
        } catch (IOException e) {
            System.out.println("Error writing to socket");
            e.printStackTrace();
        } catch (InterruptedException ie) {
            System.out.println("Thread interrupted");
            ie.printStackTrace();
        } 
    }

    private void closeSocket() {
        finished = true;
        sc.close();
        try {
            socket.leaveGroup(group);
        } catch (IOException e) {
            e.printStackTrace();
        }
        socket.close();
    }

    private void listCommands() {
        System.out.println(CLOSE_MESSAGE + ": leave the chat");
        System.out.println(CHANGE_NAME_MESSAGE + " <new name>: change name");
    }

}
