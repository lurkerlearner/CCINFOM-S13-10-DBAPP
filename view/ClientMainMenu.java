package view;

import javax.swing.*;
import java.awt.*;

import model.*;
import controller.MealController;
import java.text.SimpleDateFormat;
import java.util.Date;
import view.ViewMealCatalogueScreen;
import DAO.*;


public class ClientMainMenu extends JFrame {

    private Client client;
    private Location location;
    
    private MealIngredientDAO mealIngredientDAO;
    private IngredientDAO ingredientDAO;
    private LocationDAO locationDAO;
    private DietPreferenceDAO dietPreferenceDAO;
    private DeliveryDAO delDAO;
    private ClientDAO cDAO;
    private MealDAO mDAO;
    private MealDeliveryDAO mdDAO;
    private FloodDataDAO fdDAO;
    private RiderDAO rDAO;

    private DeliveryController dc;

    public ClientMainMenu(Client client, Location location) {
        this.client = client;
        this.location = location;

        this.mealIngredientDAO = new MealIngredientDAO();
        this.ingredientDAO = new IngredientDAO();
        this.locationDAO = new LocationDAO();
        this.dietPreferenceDAO = new DietPreferenceDAO();
        this.delDAO = new DeliveryDAO(DBConnection.getConnection());
        this.cDAO = new ClientDAO();
        this.mDAO = new MealDAO();
        this.mdDAO = new MealDeliveryDAO(DBConnection.getConnection());
        this.fdDAO = new FloodDataDAO(DBConnection.getConnection());
        this.rDAO = new RiderDAO(DBConnection.getConnection());

        this.dc = new DeliveryController(delDAO);

        setTitle("FloodPanda - Client Main Menu");
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

        JLabel welcomeLabel = new JLabel("Welcome, " + client.getName() + "! What are you craving today?");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 22));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setForeground(new Color(51, 51, 51));

        String fullAddress = client.getUnitDetails();
        if (location != null) {
            fullAddress += ", " + location.getStreet() + ", " + location.getCity();
        }

        JLabel locationLabel = new JLabel("Location: " + fullAddress);
        locationLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        locationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);


        String today = new SimpleDateFormat("MMMM dd, yyyy").format(new Date());
        JLabel dateLabel = new JLabel("Date Today: " + today);
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(welcomeLabel);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(locationLabel);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(dateLabel);
        centerPanel.add(Box.createVerticalStrut(40));

        //==PLACEHOLDER BUTTONS PANG ORDER AND DELIVERY AND VIEW SHIT
        JButton viewCatalogueBtn = new JButton("View Meal Catalogue");
        JButton orderNowBtn = new JButton("Order Now");
        JButton accountMgmtBtn = new JButton("Account Management");

        // create controller for catalogue view
        MealController mealController = new MealController();

        this.mealIngredientDAO = new DAO.MealIngredientDAO();
        this.ingredientDAO = new DAO.IngredientDAO();

        //==PAM PA DESIGN SA BUTTONS
        Dimension btnSize = new Dimension(250, 50);
        for (JButton btn : new JButton[]{viewCatalogueBtn, orderNowBtn, accountMgmtBtn}) {
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
        accountMgmtBtn.addActionListener(e -> {
            this.dispose();
            new AccountManagementScreen(client).setVisible(true); // --elishas
        });

        viewCatalogueBtn.addActionListener(e -> {
            this.dispose();
            new ViewMealCatalogueScreen(mealController, mealIngredientDAO, ingredientDAO,client,locationDAO, dietPreferenceDAO).setVisible(true); // Theo
        });


        orderNowBtn.addActionListener(e -> {
            this.dispose();
            //JOptionPane.showMessageDialog(this, "Order Now clicked."); 
            new OrderScreen(client, dc, delDAO, cDAO, mDAO, mdDAO, fdDAO, rDAO, locationDAO).setVisible(true); // Rahnee
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
