import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ChatGUI {

    private static JFrame chatFrame;
    private static JPanel framePanel;
    private static JPanel bottomPanel;
    private static GridBagConstraints bottomC;
    private static GridBagConstraints frameC;
    private static Font chatFont;
    private static JLabel nameLabel;
    private static JTextField inputField;
    private static JButton sendButton;
    private static JTextField chatField;   

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
                    GroupChat.sendMessageAsUser(inputField.getText());
                }
            }
        });

        bottomPanel.add(sendButton, bottomC);

        // chatField
        chatField = new JTextField();
        chatField.setFont(chatFont);
        chatField.setEnabled(false);
        frameC.weighty = 1.0;
        frameC.weightx = 1.0;
        frameC.gridx = 0;
        frameC.gridy = 1;
        frameC.fill = GridBagConstraints.BOTH;
        frameC.insets = new Insets(5, 5, 5, 5);
        framePanel.add(chatField, frameC);

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
        //chatFrame.add(framePanel);
        chatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chatFrame.setSize(800, 600);
        chatFrame.setLocationRelativeTo(null);
        chatFrame.setContentPane(framePanel);
        chatFrame.setVisible(true);
    }
    
    public static void updateChatField(String message) {
        chatField.setText(chatField.getText() + "\n\n" + message);
        chatFrame.revalidate();
        chatFrame.repaint();
    }
    
    public static void updateName(String newName) {
        nameLabel.setText(newName);
        chatFrame.revalidate();
        chatFrame.repaint();
    }
        
}
