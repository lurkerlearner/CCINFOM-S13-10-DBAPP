package view;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;
import java.awt.*;

public class AdminMainMenu extends JFrame {

	/**
	 * Create the frame.
	 */
	public AdminMainMenu() {
		setTitle("FloodPanda - Admin Main Menu");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        //==TOP PANEL WHERE LOGO/TITLE STAYS
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.setBackground(Color.WHITE);

        //== APP NAME
        JLabel logo = new JLabel("FloodPanda", SwingConstants.RIGHT);
        logo.setFont(new Font("Arial", Font.BOLD, 24));
        logo.setForeground(new Color(220, 31, 127));
        topPanel.add(logo, BorderLayout.WEST);

        add(topPanel, BorderLayout.NORTH);

        //==CENTER (BUTTONS)
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        JLabel welcomeLabel = new JLabel("Welcome, Admin!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 22));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setForeground(new Color(51, 51, 51));

        String today = new SimpleDateFormat("MMMM dd, yyyy").format(new Date());
        JLabel dateLabel = new JLabel("Date Today: " + today);
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(welcomeLabel);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(dateLabel);
        centerPanel.add(Box.createVerticalStrut(80));

        JButton manageRecordsBtn = new JButton("Manage records");
        JButton generateReportBtn = new JButton("Generate a report");

        //==PAM PA DESIGN SA BUTTONS
        Dimension btnSize = new Dimension(400, 80);
        for (JButton btn : new JButton[]{manageRecordsBtn, generateReportBtn}) {
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setPreferredSize(btnSize);
            btn.setMaximumSize(btnSize);
            btn.setMinimumSize(btnSize);
            btn.setFont(new Font("Arial", Font.BOLD, 16));

            btn.setBackground(new Color(255, 214, 221));
            btn.setOpaque(true);
            btn.setContentAreaFilled(true);
            btn.setFocusPainted(true);
            btn.setBorder(BorderFactory.createLineBorder(Color.PINK, 2));

            centerPanel.add(btn);
            centerPanel.add(Box.createVerticalStrut(15));
        }

        add(centerPanel, BorderLayout.CENTER);

        //==LOGOUT SOUTH PANEL
        JPanel southPanel = new JPanel();
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("Arial", Font.BOLD, 14));
        logoutBtn.setBackground(Color.LIGHT_GRAY);
        southPanel.add(logoutBtn);
        add(southPanel, BorderLayout.SOUTH);

        //NGL ALL THE BUTTONS HERE ARE PLACEHOLDERS KASI I DONT WANT TO TOUCH THE ORDER STUFF ANYMORE
        //THE ONLY THING THAT'S MINE IS ACCOUNT MANAGEMENT - ELISHA

        //==ACTION LISTENERS
        manageRecordsBtn.addActionListener(e -> {
            this.dispose();
            new ManageRecordsFrame().setVisible(true); // Faith
        });

        generateReportBtn.addActionListener(e -> {
            // TODO: implement whatever
            JOptionPane.showMessageDialog(this, "Generate a report clicked."); // just to test if button works
        });


        logoutBtn.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Do you want to log out of FloodPanda?",
                    "Logout Confirmation",
                    JOptionPane.YES_NO_OPTION
            );
            if (choice == JOptionPane.YES_OPTION) {
                this.dispose();
                new FloodPandaWelcome().setVisible(true);
            }
        });

        setVisible(true);
	}

}
