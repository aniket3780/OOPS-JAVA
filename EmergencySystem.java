import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EmergencyAlertSystem {
    private JFrame frame;
    private JTextArea messageArea, historyArea;
    private JTextField locationField, usernameField, fireNumberField, policeNumberField, medicalNumberField, additionalMessageField;
    private JPasswordField passwordField;
    private JCheckBox showPasswordCheckBox;
    private JComboBox<String> emergencyTypeCombo, priorityCombo;
    private JButton sendButton, clearButton, loginButton, logoutButton;
    private JPanel mainPanel, loginPanel;
    private boolean isAuthenticated = false;
    private List<String> messageHistory;
    private String captchaText;

    // Emergency numbers for Fire, Police, and Medical (we'll ask for these during login)
    private String fireContact;
    private String policeContact;
    private String medicalContact;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                EmergencyAlertSystem window = new EmergencyAlertSystem();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public EmergencyAlertSystem() {
        messageHistory = new ArrayList<>();
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Emergency Alert System");

        // Initialize panels
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        frame.getContentPane().add(mainPanel);

        // Create Login Panel
        loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(8, 2, 10, 10));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(20);
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);  // Use JPasswordField for password input
        loginButton = new JButton("Login");
        loginButton.addActionListener(new LoginActionListener());

        // Show password checkbox
        showPasswordCheckBox = new JCheckBox("Show Password");
        showPasswordCheckBox.addActionListener(new ShowPasswordActionListener());

        // Captcha generation
        generateCaptcha();
        JLabel captchaLabel = new JLabel("Enter Captcha: " + captchaText);
        JTextField captchaField = new JTextField(10);

        // Emergency Numbers Inputs
        JLabel fireNumberLabel = new JLabel("Fire Department Number:");
        fireNumberField = new JTextField(20);
        JLabel policeNumberLabel = new JLabel("Police Department Number:");
        policeNumberField = new JTextField(20);
        JLabel medicalNumberLabel = new JLabel("Medical Services Number:");
        medicalNumberField = new JTextField(20);

        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(showPasswordCheckBox);
        loginPanel.add(new JLabel("")); // Empty cell
        loginPanel.add(captchaLabel);
        loginPanel.add(captchaField);
        loginPanel.add(fireNumberLabel);
        loginPanel.add(fireNumberField);
        loginPanel.add(policeNumberLabel);
        loginPanel.add(policeNumberField);
        loginPanel.add(medicalNumberLabel);
        loginPanel.add(medicalNumberField);
        loginPanel.add(loginButton);

        mainPanel.add(loginPanel, BorderLayout.CENTER);
    }

    // Captcha generation method
    private void generateCaptcha() {
        captchaText = generateRandomString(6);  // Random string of 6 characters
    }

    // Helper method to generate random strings for CAPTCHA
    private String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder captcha = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            captcha.append(chars.charAt(random.nextInt(chars.length())));
        }
        return captcha.toString();
    }

    // Action Listener for Login button
    private class LoginActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String enteredCaptcha = ((JTextField) loginPanel.getComponent(7)).getText();
            if (enteredCaptcha.equals(captchaText)) {
                // Authenticate user (For simplicity, we are skipping this part)
                fireContact = fireNumberField.getText();
                policeContact = policeNumberField.getText();
                medicalContact = medicalNumberField.getText();
                JOptionPane.showMessageDialog(frame, "Login Successful!");
                isAuthenticated = true;
                showMainPanel();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid CAPTCHA!");
            }
        }
    }

    // Action Listener for Show Password checkbox
    private class ShowPasswordActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (showPasswordCheckBox.isSelected()) {
                passwordField.setEchoChar((char) 0);  // Show the password
            } else {
                passwordField.setEchoChar('*');  // Hide the password
            }
        }
    }

    // Method to show the main panel after login
    private void showMainPanel() {
        mainPanel.remove(loginPanel);  // Remove login panel
        JPanel emergencyPanel = new JPanel();
        emergencyPanel.setLayout(new GridLayout(6, 2, 10, 10));

        JLabel locationLabel = new JLabel("Location:");
        locationField = new JTextField(20);
        JLabel emergencyTypeLabel = new JLabel("Emergency Type:");
        emergencyTypeCombo = new JComboBox<>(new String[]{"Fire", "Police", "Medical"});
        JLabel priorityLabel = new JLabel("Priority:");
        priorityCombo = new JComboBox<>(new String[]{"Low", "Medium", "High"});
        JLabel additionalMessageLabel = new JLabel("Additional Message:");
        additionalMessageField = new JTextField(20);

        sendButton = new JButton("Send Alert");
        sendButton.addActionListener(e -> sendAlert());

        clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> clearForm());

        emergencyPanel.add(locationLabel);
        emergencyPanel.add(locationField);
        emergencyPanel.add(emergencyTypeLabel);
        emergencyPanel.add(emergencyTypeCombo);
        emergencyPanel.add(priorityLabel);
        emergencyPanel.add(priorityCombo);
        emergencyPanel.add(additionalMessageLabel);
        emergencyPanel.add(additionalMessageField);
        emergencyPanel.add(sendButton);
        emergencyPanel.add(clearButton);

        mainPanel.add(emergencyPanel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    // Method to send an emergency alert
    private void sendAlert() {
        String location = locationField.getText();
        String emergencyType = (String) emergencyTypeCombo.getSelectedItem();
        String priority = (String) priorityCombo.getSelectedItem();
        String additionalMessage = additionalMessageField.getText();

        String alertMessage = "Emergency Type: " + emergencyType + "\nLocation: " + location + "\nPriority: " + priority + "\nAdditional Message: " + additionalMessage;

        messageHistory.add(alertMessage);  // Add to message history
        JOptionPane.showMessageDialog(frame, "Alert Sent! \n" + alertMessage);

        // Simulate sending the message to the right emergency number
        if (emergencyType.equals("Fire")) {
            JOptionPane.showMessageDialog(frame, "Alert sent to Fire Department: " + fireContact);
        } else if (emergencyType.equals("Police")) {
            JOptionPane.showMessageDialog(frame, "Alert sent to Police: " + policeContact);
        } else if (emergencyType.equals("Medical")) {
            JOptionPane.showMessageDialog(frame, "Alert sent to Medical Services: " + medicalContact);
        }

        // Clear fields
        clearForm();
    }

    // Method to clear the form
    private void clearForm() {
        locationField.setText("");
        emergencyTypeCombo.setSelectedIndex(0);
        priorityCombo.setSelectedIndex(0);
        additionalMessageField.setText("");
    }
}
