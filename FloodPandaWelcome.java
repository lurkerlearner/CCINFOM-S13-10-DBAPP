import javax.swing.*;
import java.awt.*;

public class FloodPandaWelcome extends JFrame {

    public FloodPandaWelcome() {

        setTitle("FloodPanda");
        setSize(600, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());


        JLabel title = new JLabel("FloodPanda", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setForeground(new Color(220, 31, 127));
        title.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        add(title, BorderLayout.NORTH);


        //==BUTTONS
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        JButton login = new JButton("Login");
        JButton register = new JButton("Create New Account");
        JButton admin = new JButton("Admin Module");
        JButton exit = new JButton("Exit");


        Dimension buttonSize = new Dimension(400, 80);
        JButton[] buttons = {login, register, admin, exit};
        for (JButton b : buttons) {
            b.setPreferredSize(buttonSize);
            b.setMaximumSize(buttonSize);
            b.setMinimumSize(buttonSize);
        }

        //==PANG SPACING
        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(login);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(register);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(admin);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(exit);
        buttonPanel.add(Box.createVerticalGlue());


        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.add(buttonPanel);

        add(centerWrapper, BorderLayout.CENTER);

        //==ACTION LISTENERS
        login.addActionListener(e -> {
            new LoginScreen().setVisible(true);
            dispose();
        });

        register.addActionListener(e -> {
            new RegisterScreen().setVisible(true);
            dispose();
        });

        admin.addActionListener(e->{
            new AdminLoginScreen().setVisible(true);
            dispose();
        });

        exit.addActionListener(e -> System.exit(0));

        setVisible(true);
    }
}

