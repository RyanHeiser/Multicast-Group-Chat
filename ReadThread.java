import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class ReadThread implements Runnable {
    
    private MulticastSocket socket;
    private InetAddress group;
    private int port;
    
    ReadThread(MulticastSocket socket, InetAddress group, int port) {
        this.socket = socket;
        this.group = group;
        this.port = port;
    }

    @Override
    public void run() {
        while (!GroupChat.finished) {
            byte[] buffer = new byte[1024]; 
            DatagramPacket datagram = new
            DatagramPacket(buffer,buffer.length,group,port); 
            String message; 
            try { 
                socket.receive(datagram); 
                message = new
                String(buffer,0,datagram.getLength(),"UTF-8"); 
                if (!message.startsWith(GroupChat.getName())) {
                    System.out.println(message);
                    ChatGUI.appendToChatArea(message);
                } else {
                    ChatGUI.appendToChatArea(message);
                }
            } catch(IOException e) { 
                System.out.println("Socket closed!"); 
            } 
        }
    }
}
