import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.event.*;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
       
        displayGUI();

        /* 
        GroupChat gc = new GroupChat(5000, "230.0.0.1");
        gc.join();
        */
    }

    private static void displayGUI() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        JPanel fieldPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        GridBagConstraints cField = new GridBagConstraints();

        Label portLabel = new Label("Port: ");
        //portLabel.setLocation(50, 100);
        portLabel.setPreferredSize(new Dimension(50, 30));
        cField.gridx = 0;
        cField.gridy = 0;
        cField.insets = new Insets(5, 0, 5, 5);
        fieldPanel.add(portLabel, cField);

        JTextField portField = new JTextField();
        //portField.setBounds(100, 100, 100, 30);
        portField.setPreferredSize(new Dimension(100, 30));
        portField.setToolTipText("Must be between 1024 and 65535, inclusive");
        cField.gridx = 1;
        cField.gridy = 0;
        fieldPanel.add(portField, cField);

        Label addressLabel = new Label("Address: ");
        //addressLabel.setBounds(50, 250, 50, 30);
        addressLabel.setPreferredSize(new Dimension(50, 30));
        cField.gridx = 0;
        cField.gridy = 1;
        fieldPanel.add(addressLabel, cField);

        JTextField addressField = new JTextField();
        addressField.setToolTipText("Must be between 224.0.0.1 and 239.255.255.255, inclusive");
        //addressField.setBounds(100, 250, 100, 30);
        addressField.setPreferredSize(new Dimension(100, 30));
        cField.gridx = 1;
        cField.gridy = 1;
        fieldPanel.add(addressField, cField);

        Label title = new Label("Welcome to the Multicast Group Chat!");
        title.setFont(new Font("Dialog", Font.BOLD, 20));
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(10, 0, 10, 0);
        panel.add(title, c);

        c.gridx = 0;
        c.gridy = 1;
        panel.add(fieldPanel, c);

        JButton joinButton = new JButton("Join");
        //joinButton.setBounds(100, 350, 75, 40);
        joinButton.setPreferredSize(new Dimension(75, 40));
        joinButton.setActionCommand("join");

        joinButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("join")) {
                    displayChatGUI();
                    frame.setVisible(false);
                }
            }
        });

        c.gridx = 0;
        c.gridy = 2;
        panel.add(joinButton, c);
        
        frame.setSize(400, 300);
        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    private static void displayChatGUI() {

    }
    
}
