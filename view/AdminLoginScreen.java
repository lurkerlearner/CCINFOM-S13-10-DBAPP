package view;

import javax.swing.*;
import java.awt.*;

public class AdminLoginScreen extends JFrame {

    //HARDCODED NA SIYA HERE
    private final String ADMIN_USERNAME = "admin";
    private final String ADMIN_PASSWORD = "admin123";

    public AdminLoginScreen() {
        setTitle("FloodPanda - Admin Login");
        setSize(600, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        //==TITLE
        JLabel title = new JLabel("FloodPanda - Admin Login", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(new Color(220, 31, 127));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        //==FORM
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JCheckBox showPassword = new JCheckBox("Show Password");

        Dimension fieldSize = new Dimension(300, 30);
        usernameField.setPreferredSize(fieldSize);
        passwordField.setPreferredSize(fieldSize);

        //==USERNAME
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        formPanel.add(usernameField, gbc);

        //==PASSWORD
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        formPanel.add(passwordField, gbc);

        //==SHOW PASSWORD
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 0;
        formPanel.add(showPassword, gbc);

        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(formPanel, BorderLayout.CENTER);

        //==SOUTH PANEL
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10)); // 20px gap

        JButton loginBtn = new JButton("Login");
        loginBtn.setPreferredSize(new Dimension(120, 40));

        JButton exitBtn = new JButton("Exit");
        exitBtn.setPreferredSize(new Dimension(120, 40));

        southPanel.add(loginBtn);
        southPanel.add(exitBtn);
        add(southPanel, BorderLayout.SOUTH);

        //==ACTION LISTENERS
        showPassword.addActionListener(e -> {
            if (showPassword.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('*');
            }
        });


        loginBtn.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            //todo: palagay here thanks
            if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
                JOptionPane.showMessageDialog(this, "Welcome, Admin!");
                dispose();
                new AdminMainMenu();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid admin credentials.");
            }
        });

        exitBtn.addActionListener(e -> {
            new FloodPandaWelcome().setVisible(true);
            dispose();
        });

        setVisible(true);
    }
}
