package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import DAO.*;
import app.DBConnection;
import model.*;
import controller.*;

public class ManageRecordsFrame extends JFrame {

    // call all controllers
    private ClientController clientController;
    private MealController mealController;
    private DeliveryController deliveryController;
    private IngredientController ingredientController;
    private FloodDataController floodDataController;
    private LocationController locationController;
    private MealPlanController mealPlanController;
    private DietPreferenceController dietPreferenceController;
    private SupplierController supplierController;
    private RiderController riderController;
    private MealIngredientController mealIngredientController;

    // main panel or container, and each record or table's panels/tables
    private JPanel mainPanel;
    private ClientPanel clientPanel;
    private MealPanel mealPanel;
    private DeliveryPanel deliveryPanel;
    private IngredientPanel ingredientPanel;
    private FloodDataPanel floodDataPanel;
    private LocationPanel locationPanel;
    private MealPlanPanel mealPlanPanel;
    private DietPreferencePanel dietPreferencePanel;
    private SupplierPanel supplierPanel;
    private RiderPanel riderPanel;
    private MealIngredientPanel mealIngredientPanel;

    // navigation panel, and buttons to trigger each panel
    private JPanel navPanel;
    private JButton clientBtn;
    private JButton mealBtn;
    private JButton deliveryBtn;
    private JButton ingredientBtn;
    private JButton floodDataBtn;
    private JButton locationBtn;
    private JButton mealPlanBtn;
    private JButton dietPreferenceBtn;
    private JButton supplierBtn;
    private JButton riderBtn;
    private JButton mealIngredientBtn;
    
    public ManageRecordsFrame() {
        setTitle("FloodPanda - Admin / Manage Records");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        initComponents();

        setVisible(true);
    }

    private void initComponents() {
        // Create the main panel
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Create the navigation panel
        createNavPanel();
        mainPanel.add(navPanel, BorderLayout.WEST);

        // Instantiate controllers
        clientController = new ClientController();
        mealController = new MealController();
        deliveryController = new DeliveryController(new DeliveryDAO(DBConnection.getConnection()));
        ingredientController = new IngredientController(new IngredientDAO());
        floodDataController = new FloodDataController(new FloodDataDAO(DBConnection.getConnection()));
        locationController = new LocationController();
        mealPlanController = new MealPlanController();
        dietPreferenceController = new DietPreferenceController();
        supplierController = new SupplierController(new SupplierDAO());
        riderController = new RiderController(new RiderDAO(DBConnection.getConnection()));
        mealIngredientController = new MealIngredientController(new MealIngredientDAO());


        // Initialize each record panel
        clientPanel = new ClientPanel(clientController);
        mealPanel = new MealPanel(mealController);
        deliveryPanel = new DeliveryPanel(deliveryController);
        ingredientPanel = new IngredientPanel(ingredientController);
        floodDataPanel = new FloodDataPanel(floodDataController);
        locationPanel = new LocationPanel(locationController);
        mealPlanPanel = new MealPlanPanel(mealPlanController);
        dietPreferencePanel = new DietPreferencePanel(dietPreferenceController);
        supplierPanel = new SupplierPanel(supplierController);
        riderPanel = new RiderPanel(riderController);
        mealIngredientPanel = new MealIngredientPanel(mealIngredientController);

        // show the client panel by default
        mainPanel.add(clientPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    private void createNavPanel() {
        // Create navigation panel and buttons
        navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setPreferredSize(new Dimension(200, 600));
        navPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        navPanel.setBackground(new Color(240, 240, 240));

        // Create a logo/title panel
        JPanel logoPanel = new JPanel();
        logoPanel.setLayout(new BorderLayout());
        logoPanel.setMaximumSize(new Dimension(180, 100));
        logoPanel.setBackground(new Color(240, 240, 240));

        JLabel logo = new JLabel("FloodPanda");
        logo.setFont(new Font("Arial", Font.BOLD, 20));
        logo.setForeground(new Color(220, 31, 127));
        logo.setHorizontalAlignment(JLabel.CENTER);

        logoPanel.add(logo, BorderLayout.CENTER);
        navPanel.add(logoPanel);
        navPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Initialize buttons for each record panel
        clientBtn = createNavButton("Client", e -> switchToPanel(clientPanel));
        mealBtn = createNavButton("Meal", e -> switchToPanel(mealPanel));
        deliveryBtn = createNavButton("Delivery", e -> switchToPanel(deliveryPanel));
        ingredientBtn = createNavButton("Ingredient", e -> switchToPanel(ingredientPanel));
        floodDataBtn = createNavButton("Flood Data", e -> switchToPanel(floodDataPanel));
        locationBtn = createNavButton("Location", e -> switchToPanel(locationPanel));
        mealPlanBtn = createNavButton("Meal Plan", e -> switchToPanel(mealPlanPanel));
        dietPreferenceBtn = createNavButton("Diet Preference", e -> switchToPanel(dietPreferencePanel));
        supplierBtn = createNavButton("Supplier", e -> switchToPanel(supplierPanel));       
        riderBtn = createNavButton("Rider", e -> switchToPanel(riderPanel));
        mealIngredientBtn = createNavButton("Meal Ingredient", e -> switchToPanel(mealIngredientPanel));


        // Add buttons to navigation panel
        navPanel.add(clientBtn);
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navPanel.add(mealBtn);
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navPanel.add(deliveryBtn);
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navPanel.add(ingredientBtn);
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navPanel.add(floodDataBtn);
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navPanel.add(locationBtn);
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navPanel.add(mealPlanBtn);
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navPanel.add(dietPreferenceBtn);
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navPanel.add(supplierBtn);
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navPanel.add(riderBtn);
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navPanel.add(mealIngredientBtn);

        navPanel.add(Box.createVerticalGlue());
    }

    private JButton createNavButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 40));
        button.setPreferredSize(new Dimension(180, 40));
        button.setFocusPainted(false);
        button.setBackground(new Color(255, 214, 221));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorder(BorderFactory.createLineBorder(Color.PINK, 2));

        button.addActionListener(listener);
        
        return button;
    }

    private void switchToPanel(JPanel panel) {
        // Remove the current panel
        Component currentPanel = ((BorderLayout) mainPanel.getLayout()).getLayoutComponent(BorderLayout.CENTER);
        if (currentPanel != null) {
            mainPanel.remove(currentPanel);
        }
        
        // Add the new panel
        mainPanel.add(panel, BorderLayout.CENTER);
        
        // Refresh the frame
        mainPanel.revalidate();
        mainPanel.repaint();
    }
}
