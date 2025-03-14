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

public class PortalGUI {

    public static void displayGUI() {
        JFrame frame = new JFrame("Multicast Chat Portal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        JPanel fieldPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        GridBagConstraints cField = new GridBagConstraints();

        Label portLabel = new Label("Port: ");
        portLabel.setPreferredSize(new Dimension(50, 30));
        cField.gridx = 0;
        cField.gridy = 0;
        cField.insets = new Insets(5, 0, 5, 5);
        fieldPanel.add(portLabel, cField);

        JTextField portField = new JTextField();
        portField.setPreferredSize(new Dimension(100, 30));
        portField.setToolTipText("Must be between 1024 and 65535, inclusive");
        cField.gridx = 1;
        cField.gridy = 0;
        fieldPanel.add(portField, cField);

        Label addressLabel = new Label("Address: ");
        addressLabel.setPreferredSize(new Dimension(50, 30));
        cField.gridx = 0;
        cField.gridy = 1;
        fieldPanel.add(addressLabel, cField);

        JTextField addressField = new JTextField();
        addressField.setToolTipText("Must be between 224.0.0.1 and 239.255.255.255, inclusive");
        addressField.setPreferredSize(new Dimension(100, 30));
        cField.gridx = 1;
        cField.gridy = 1;
        fieldPanel.add(addressField, cField);

        Label nameLabel = new Label("Name: ");
        nameLabel.setPreferredSize(new Dimension(50, 30));
        cField.gridx = 0;
        cField.gridy = 2;
        cField.insets = new Insets(5, 0, 5, 5);
        fieldPanel.add(nameLabel, cField);

        JTextField nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(100, 30));
        nameField.setToolTipText("Name must consist of only letters and numbers and be less than 16 characters");
        cField.gridx = 1;
        cField.gridy = 2;
        fieldPanel.add(nameField, cField);

        JLabel title = new JLabel("Welcome to the Multicast Group Chat!");
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
                    if (!isValidPort(portField.getText())) {
                        System.out.println("ERROR: invalid port");
                        return;
                    }
                    if (!isValidAddress(addressField.getText())) {
                        System.out.println("ERROR: invalid address");
                        return;
                    }
                    if (!GroupChat.setName(nameField.getText())) {
                        return;
                    }
                    frame.setVisible(false);
                    ChatGUI.displayGUI();
                    
                    GroupChat.setMulticastPort(Integer.parseInt(portField.getText()));
                    GroupChat.setMulticastAddress(addressField.getText());
                    GroupChat.join();
                }
            }
        });

        c.gridx = 0;
        c.gridy = 2;
        panel.add(joinButton, c);
        
        frame.setSize(400, 400);
        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static boolean isValidPort(String portString) {
        int port = 0;
        try {
            port = Integer.parseInt(portString);
        } catch (Exception e) {
            System.out.println("Port must be an integer");
            e.printStackTrace();
            return false;
        }
        return port >= 1024 && port <= 65535;
    }

    private static boolean isValidAddress(String address) {
        address = address.trim();

        for (int i = 0; i < 4; i++) {
            String subStr;
            if (address.contains(".")) {
                subStr = address.substring(0, address.indexOf("."));  // creates a substring of the next number before the next period
                address = address.substring(address.indexOf(".") + 1, address.length()); // removes the substring from the address
            } else {
                subStr = address;
                address = "";
            }
            
            int subAddress;
            // tries to parse the substring, returns false if the parsing encounters an error
            try {
                subAddress = Integer.parseInt(subStr);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            // invalid and returns false if first number is less than 224 or greater than 239
            if (i == 0 && (subAddress < 224 || subAddress > 239)) {
                return false;
            // invalid and returns false if any number is not between 0 and 255
            } else if (subAddress < 0 || subAddress > 255) {
                return false;
            // invalid and returns false if the address is 224.0.0.0
            } else if (i == 0 && subAddress == 224 && address.charAt(address.length() - 1) == '0') {
                return false;
            }
        }

        // invalid and returns false if the address still has characters after being parsed
        if (address.length() > 0) {
            return false;
        }

        return true;
    }
}