package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import DAO.*;
import app.DBConnection;
import model.*;
import controller.*;

public class GenerateReportFrame extends JFrame {
    // call all controllers, remove nlng mga d needs
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
    private ClientEngagementController clientEngagementController;

    // Main panels and panels of reports
    private JPanel mainPanel;
    private SalesReportPanel salesReportPanel;
    private ClientEngagementPanel clientEngagementPanel;
    private MenuPopularityPanel menuPopularityPanel;
    // padagdag nlng panels para sa reports nyo

    // nav panel and buttons
    private JPanel navPanel;
    private JButton salesReportBtn, clientEngagementBtn;
    private JButton menuPopularityBtn, floodImpactBtn;

    public GenerateReportFrame() {
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
        clientEngagementController = new ClientEngagementController();

        // Initialize panels
        salesReportPanel = new SalesReportPanel(deliveryController);
        clientEngagementPanel = new ClientEngagementPanel(clientEngagementController);
        menuPopularityPanel = new MenuPopularityPanel(mealController);
        floodImpactPanel = new FloodImpactReportPanel(floodDataController);

        // show sales report panel by default
        mainPanel.add(salesReportPanel, BorderLayout.CENTER);

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
        salesReportBtn = createNavButton("Sales Report", e -> switchToPanel(salesReportPanel));
        clientEngagementBtn = createNavButton("Client Engagement", e-> switchToPanel(clientEngagementPanel));
        menuPopularityBtn = createNavButton("Menu Popularity Report", e -> switchToPanel(menuPopularityPanel));
        floodImpactBtn = createNavButton("Flood Impact Report", e-> switchToPanel(floodImpactPanel));

        // Add buttons to navigation panel
        navPanel.add(salesReportBtn);
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navPanel.add(clientEngagementBtn);
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navPanel.add(menuPopularityBtn);
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navPanel.add(floodImpactBtn);
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));


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
