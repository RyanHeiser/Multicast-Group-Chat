import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class ChatGUI {

    private static final String CLOSE_MESSAGE = "!close";
    private static final String HELP_MESSAGE = "!help";
    private static final String CHANGE_NAME_MESSAGE = "!name";

    private static JFrame chatFrame;
    private static JPanel framePanel;
    private static JPanel bottomPanel;
    private static GridBagConstraints bottomC;
    private static GridBagConstraints frameC;
    private static Font chatFont;
    private static JLabel nameLabel;
    private static JTextField inputField;
    private static JButton sendButton;
    private static JTextArea chatArea;   
    private static JScrollPane scroll;

    /**
     * Sets up the window and makes it visible
     */
    public static void displayGUI() {

        chatFrame = new JFrame("Multicast Chat");
        framePanel = new JPanel(new GridBagLayout());
        bottomPanel = new JPanel(new GridBagLayout());
        bottomC = new GridBagConstraints();
        frameC = new GridBagConstraints();
        chatFont = new Font("Dialog", Font.BOLD, 16);

        // nameLabel
        nameLabel = new JLabel(GroupChat.getName());
        nameLabel.setPreferredSize(new Dimension(90, 40));
        nameLabel.setFont(chatFont);
        bottomC.gridx = 0;
        bottomPanel.add(nameLabel, bottomC);

        // inputField
        inputField = new JTextField();
        inputField.setPreferredSize(new Dimension(1920, 40));
        inputField.setFont(chatFont);
        bottomC.gridx = 1;
        bottomC.weightx = 1.0;
        bottomC.fill = GridBagConstraints.HORIZONTAL;
        bottomPanel.add(inputField, bottomC);

        // sendButton
        sendButton = new JButton("Send");
        sendButton.setPreferredSize(new Dimension(75, 40));
        sendButton.setActionCommand("send");
        bottomC.gridx = 2;
        bottomC.weightx = 0;
        bottomC.fill = GridBagConstraints.NONE;

        // sendButton action listener
        sendButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("send")) {
                    if (!isCommand(inputField.getText()) && !inputField.getText().equals("")) {
                        GroupChat.sendMessageAsUser(inputField.getText());
                    }
                    inputField.setText("");
                }
            }
        });

        bottomPanel.add(sendButton, bottomC);

        // chatArea
        chatArea = new JTextArea();
        chatArea.setFont(chatFont);
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setText("Use '!help' for list of commands\n");
        
        //scroll
        scroll = new JScrollPane(chatArea);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setAutoscrolls(true);
        frameC.weighty = 1.0;
        frameC.weightx = 1.0;
        frameC.gridx = 0;
        frameC.gridy = 1;
        frameC.fill = GridBagConstraints.BOTH;
        frameC.insets = new Insets(5, 5, 5, 5);
        framePanel.add(scroll, frameC);


        // framePanel
        frameC.weighty = 0;
        frameC.weightx = 1.0;
        frameC.weightx = 1.0;
        frameC.gridx = 0;
        frameC.gridy = 2;
        frameC.fill = GridBagConstraints.HORIZONTAL;
        frameC.insets = new Insets(0,0,0,0);
        framePanel.add(bottomPanel, frameC);

        // chatFrame
        chatFrame.getRootPane().setDefaultButton(sendButton);
        chatFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        chatFrame.setSize(800, 600);
        chatFrame.setLocationRelativeTo(null);
        chatFrame.setContentPane(framePanel);

        // send message, remove frame, and exit program when window is closed
        chatFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                closeChat();
            }
        });

        chatFrame.setVisible(true);
        inputField.requestFocus();
    }
    
    /**
     * Appends a message to the chat area
     * @param message: the message to append to the chat area
     */
    public static void appendToChatArea(String message) {
        chatArea.append('\n' + message + '\n');
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
        chatFrame.revalidate();
        chatFrame.repaint();
    }
    
    /**
     * Updates the name label in the bottom left of the GUI
     * @param newName the name to set the label to
     */
    public static void updateName(String newName) {
        nameLabel.setText(newName);
        chatFrame.revalidate();
        chatFrame.repaint();
    }

    /**
     * Called when chat is closed. Sends a message that the user has left, closes the window, closes the socket, and exits the program
     */
    private static void closeChat() {
        GroupChat.sendMessage(GroupChat.getName() + " has left the chat");
        chatFrame.dispose();
        GroupChat.closeSocket();
        System.exit(0);
    }

    /**
     * Checks if the message contains a command
     * @param message: The message to check a command for
     * @return: True if it is a command, false otherwise
     */
    private static Boolean isCommand(String message) {
        message = message.trim();
        if (message.equals(CLOSE_MESSAGE)) {
            closeChat();
            return true;
        } else if (message.equals(HELP_MESSAGE)) {
            listCommands();
            return true;
        } else if (message.startsWith(CHANGE_NAME_MESSAGE)) {
            GroupChat.setName(message.substring(CHANGE_NAME_MESSAGE.length(), message.length()));
            return true;
        }
        return false;
    }

    /**
     * Appends the available commands to the chat for the user to see
     */
    private static void listCommands() {
        appendToChatArea("********************");
        appendToChatArea(CLOSE_MESSAGE + ": leave the chat");
        appendToChatArea(CHANGE_NAME_MESSAGE + " <new name>: change name");
        appendToChatArea("********************");
    }
        
}
