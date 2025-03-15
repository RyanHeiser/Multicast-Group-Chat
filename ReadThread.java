import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class ReadThread implements Runnable {
    
    private MulticastSocket socket;
    private InetAddress group;
    private int port;

    /**
     * Method for a separate thread to listen for messages
     * @param socket: The Multicast socket
     * @param group: The IP address of the group
     * @param port: The port of the chat
     */
    ReadThread(MulticastSocket socket, InetAddress group, int port) {
        this.socket = socket;
        this.group = group;
        this.port = port;
    }

    @Override
    public void run() {
        // continuosly check for messages while the chat is running
        while (!GroupChat.finished) {
            byte[] buffer = new byte[1024]; 
            DatagramPacket datagram = new DatagramPacket(buffer,buffer.length,group,port); // create datagram to receive message
            String message; 
            try { 
                socket.receive(datagram); // attempts to receive a datagram packet
                message = new String(buffer,0,datagram.getLength(),"UTF-8"); // convert the datagram to a string
                ChatGUI.appendToChatArea('\n' + message); // add received message to chat area
            } catch(IOException e) { 
                System.out.println("Socket closed!"); 
            } 
        }
    }
}
