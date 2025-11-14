package view;

import DAO.*;
import controller.*;
import model.*;

import javax.swing.*;
import java.awt.*;

public class LoginScreen extends JFrame {

    private LoginController controller = new LoginController();
    private LocationDAO locationDAO = new LocationDAO();

    public LoginScreen() {
        setTitle("FloodPanda - Login");
        setSize(600, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- TITLE ---
        JLabel title = new JLabel("FloodPanda - Login", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(new Color(220, 31, 127));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        // --- FORM PANEL (CENTER) ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField contactField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JCheckBox showPassword = new JCheckBox("Show Password");

        Dimension fieldSize = new Dimension(300, 30);
        contactField.setPreferredSize(fieldSize);
        passwordField.setPreferredSize(fieldSize);

        // CONTACT
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Contact Number:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        formPanel.add(contactField, gbc);

        // PASSWORD
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        formPanel.add(passwordField, gbc);

        // SHOW PASSWORD
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 0;
        formPanel.add(showPassword, gbc);

        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(formPanel, BorderLayout.CENTER);

        // --- SOUTH PANEL (BUTTONS) ---
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton loginBtn = new JButton("Login");
        JButton exitBtn = new JButton("Exit");

        Dimension btnSize = new Dimension(120, 40);
        loginBtn.setPreferredSize(btnSize);
        exitBtn.setPreferredSize(btnSize);

        southPanel.add(loginBtn);
        southPanel.add(exitBtn);
        add(southPanel, BorderLayout.SOUTH);

        // --- ACTION LISTENERS ---

        showPassword.addActionListener(e -> {
            if (showPassword.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('*');
            }
        });

        // LOGIN BUTTON
        loginBtn.addActionListener(e -> {
            String contact = contactField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            Client c = controller.login(contact, password);

            if (c == null) {
                JOptionPane.showMessageDialog(this, "Invalid contact number or password.");
                return;
            }

            Location l = locationDAO.getLocationById(c.getLocationID());
            if (l == null) {
                JOptionPane.showMessageDialog(this, "Client location not found.");
                return;
            }

            JOptionPane.showMessageDialog(this, "Welcome, " + c.getName() + "!");
            dispose();
            new ClientMainMenu(c, l); // redirect to client main menu
        });

        // EXIT BUTTON
        exitBtn.addActionListener(e -> {
            dispose();
            new FloodPandaWelcome().setVisible(true);
        });

        setVisible(true);
    }
}