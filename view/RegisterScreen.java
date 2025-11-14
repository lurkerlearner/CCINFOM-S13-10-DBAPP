package view;

import app.DBConnection;
import controller.LoginController;

import javax.swing.*;
import java.awt.*;

public class RegisterScreen extends JFrame {

    private LoginController controller = new LoginController();

    public RegisterScreen() {
        setTitle("Create Account - FloodPanda");
        setSize(600, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ADD TITLE AT TOP
        JLabel title = new JLabel("FloodPanda - Create Account", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(new Color(220, 31, 127));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // padding around components
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        JTextField fullNameField = new JTextField();
        JTextField contactField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JPasswordField confirmPasswordField = new JPasswordField();
        JTextField unitField = new JTextField();
        JComboBox<LocationItem> locationDrop = new JComboBox<>();
        loadLocations(locationDrop);

        JCheckBox showPassword = new JCheckBox("Show Password");

        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(fullNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(new JLabel("Contact Number:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(contactField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        mainPanel.add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(confirmPasswordField, gbc);


        gbc.gridx = 1; gbc.gridy = 4;
        mainPanel.add(showPassword, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        mainPanel.add(new JLabel("Unit / House No.:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(unitField, gbc);


        gbc.gridx = 0; gbc.gridy = 6;
        mainPanel.add(new JLabel("Street / Area:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(locationDrop, gbc);

        add(mainPanel, BorderLayout.CENTER);


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton registerBtn = new JButton("Create Account");
        JButton exitBtn = new JButton("Exit");

        buttonPanel.add(registerBtn);
        buttonPanel.add(exitBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        showPassword.addActionListener(e -> {
            if (showPassword.isSelected()) {
                passwordField.setEchoChar((char) 0);
                confirmPasswordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('*');
                confirmPasswordField.setEchoChar('*');
            }
        });


        registerBtn.addActionListener(e -> {
            String name = fullNameField.getText().trim();
            String contact = contactField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String confirmPassword = new String(confirmPasswordField.getPassword()).trim();
            String unit = unitField.getText().trim();

            if (name.isEmpty() || contact.isEmpty() || password.isEmpty() ||
                    confirmPassword.isEmpty() || unit.isEmpty() || locationDrop.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.");
                return;
            }

            if (!contact.matches("^09\\d{9}$")) {
                JOptionPane.showMessageDialog(this, "Contact number must start with 09 and be 11 digits.");
                return;
            }

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match.");
                return;
            }

            int locationId = ((LocationItem) locationDrop.getSelectedItem()).id;

            boolean success = controller.register(name, contact, password, unit, locationId);

            if (success) {
                JOptionPane.showMessageDialog(this, "Account successfully created!");
                dispose();
                new LoginScreen();
            } else {
                if (controller.getClientDAO().isContactExists(contact)) {
                    JOptionPane.showMessageDialog(this, "This contact number is already registered.");
                } else {
                    JOptionPane.showMessageDialog(this, "Registration failed.");
                }
            }
        });


        exitBtn.addActionListener(e -> {
            dispose();
            new FloodPandaWelcome();
        });

        setVisible(true);
    }

    private void loadLocations(JComboBox<LocationItem> comboBox) {
        try {
            var conn = DBConnection.getConnection();
            var stmt = conn.prepareStatement("SELECT location_id, street_address, city FROM location");
            var rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("location_id");
                String text = rs.getString("street_address").trim() + ", " + rs.getString("city").trim();
                comboBox.addItem(new LocationItem(id, text));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class LocationItem {
        int id;
        String text;
        public LocationItem(int id, String text) { this.id = id; this.text = text; }
        public String toString() { return text; }
    }
}

