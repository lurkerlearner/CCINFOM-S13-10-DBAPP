package view;

import model.Client;
import model.PaymentMode;
import controller.DeliveryController;
import controller.MealController;
import DAO.*;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class OrderScreen extends JFrame
{
    private final Client c;
    private final DeliveryController dc;
    private final DeliveryDAO delDAO;
    private final ClientDAO cDAO;
    private final MealDAO mDAO;
    private final MealDeliveryDAO mdDAO;
    private final FloodDataDAO fdDAO;
    private final RiderDAO rDAO;
    private final LocationDAO lDAO;

    public OrderScreen(Client c, DeliveryController dc, DeliveryDAO delDAO,
                       ClientDAO cDAO, MealDAO mDAO, MealDeliveryDAO mdDAO,
                       FloodDataDAO fdDAO, RiderDAO rDAO, LocationDAO lDAO)
    {
        this.c = c;
        this.dc = dc;
        this.delDAO = delDAO;
        this.cDAO = cDAO;
        this.mDAO = mDAO;
        this.mdDAO = mdDAO;
        this.fdDAO = fdDAO;
        this.rDAO = rDAO;
        this.lDAO = lDAO;

        setTitle("FloodPanda - Order Transaction");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        createHeader();

        JPanel centerPanel = new JPanel();
        createCenterPanel(centerPanel);
        JPanel buttonPanel = new JPanel();
        createButtonPanel(buttonPanel);

        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void createHeader()
    {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.setBackground(Color.WHITE);

        // Logo style matching AccountManagementScreen
        JLabel logo = new JLabel("FloodPanda", SwingConstants.LEFT);
        logo.setFont(new Font("Arial", Font.BOLD, 30));
        logo.setForeground(new Color(220, 31, 127));
        topPanel.add(logo, BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);
    }

    public void createCenterPanel(JPanel cp)
    {
        cp.setLayout(new BoxLayout(cp, BoxLayout.Y_AXIS)); 

        // add spacer on top
        cp.add(Box.createRigidArea(new Dimension(0, 50)));

        JLabel orderLabel = new JLabel("What would you like to order?");
        orderLabel.setFont(new Font("Arial", Font.BOLD, 15));
        orderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // text field for entering meal id
        JTextField mealIdField = new JTextField();
        Dimension mealIdSize = new Dimension(100, 30);
        mealIdField.setPreferredSize(mealIdSize);
        mealIdField.setMaximumSize(mealIdSize);
        mealIdField.setAlignmentX(Component.CENTER_ALIGNMENT);

        cp.add(orderLabel);
        cp.add(mealIdField); 
                                  
        // add spacer in between
        cp.add(Box.createRigidArea(new Dimension(0, 25)));

        JLabel paymentLabel = new JLabel("How would you like to pay?");
        paymentLabel.setFont(new Font("Arial", Font.BOLD, 15));
        paymentLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // choices for which payment method client will use
        String[] paymentModes = Arrays.stream(PaymentMode.values())
                                  .map(PaymentMode::getDbValue)
                                  .toArray(String[]::new);
        JComboBox<String> paymentDropdown = new JComboBox<>(paymentModes);
        Dimension paymentSize = new Dimension(200, 30);
        paymentDropdown.setPreferredSize(paymentSize);
        paymentDropdown.setMaximumSize(paymentSize);
        paymentDropdown.setAlignmentX(Component.CENTER_ALIGNMENT);

        cp.add(paymentLabel);
        cp.add(paymentDropdown);

        // add spacer on bottom
        cp.add(Box.createRigidArea(new Dimension(0, 67)));

        JButton placeOrderButton = new JButton("Place Order");
        Dimension orderButtonSize = new Dimension(155, 40);
        placeOrderButton.setPreferredSize(orderButtonSize);
        placeOrderButton.setMaximumSize(orderButtonSize);
        placeOrderButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        placeOrderButton.setBackground(new Color(220, 31, 127)); 
        placeOrderButton.setForeground(Color.WHITE);
        placeOrderButton.setOpaque(true);
        placeOrderButton.setContentAreaFilled(true);
        placeOrderButton.setFocusPainted(true);
        placeOrderButton.setFont(new Font("Arial", Font.BOLD, 20));

        placeOrderButton.addActionListener(e -> 
        {
            // retrieve client's order
            String mealIDentered = mealIdField.getText();
            int mealID;
            try 
            {
                mealID = Integer.parseInt(mealIDentered);
            }
            catch (NumberFormatException ex) 
            {
                JOptionPane.showMessageDialog(cp, 
                    "Please enter a valid numeric Meal ID.", 
                    "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // retrieve client's payment mode
            String selectedPaymentMode = (String) paymentDropdown.getSelectedItem();
            PaymentMode pm = PaymentMode.fromDbValue(selectedPaymentMode);


            boolean success = dc.startOrderTransaction(mealID, pm, c.getClientID()); 
            if (success)
            {
                mealIdField.setText("");
                paymentDropdown.setSelectedItem(null);
            }
        });

        cp.add(placeOrderButton);
    }

    public void createButtonPanel(JPanel bp)
    {
        bp.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20)); // Adjust spacing (Hgap, Vgap)

        JButton backButton = new JButton("Back to Main Menu");
        Dimension backButtonSize = new Dimension(200, 27);
        backButton.setPreferredSize(backButtonSize);
        backButton.setMaximumSize(backButtonSize);
        backButton.setBackground(new Color(255, 214, 221));
        backButton.setOpaque(true);
        backButton.setContentAreaFilled(true);
        backButton.setFocusPainted(true);
        backButton.setBorder(BorderFactory.createLineBorder(Color.PINK, 2));
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.addActionListener(e -> {dispose();  
        new ClientMainMenu(c, lDAO.getLocationById(c.getLocationID())).setVisible(true); 
        });

        JButton viewCatalogueButton = new JButton("View Meal Catalogue");
        Dimension viewCatalogueSize = new Dimension(200, 27);
        viewCatalogueButton.setPreferredSize(viewCatalogueSize);
        viewCatalogueButton.setMaximumSize(viewCatalogueSize);
        viewCatalogueButton.setBackground(new Color(255, 214, 221));
        viewCatalogueButton.setOpaque(true);
        viewCatalogueButton.setContentAreaFilled(true);
        viewCatalogueButton.setFocusPainted(true);
        viewCatalogueButton.setBorder(BorderFactory.createLineBorder(Color.PINK, 2));
        viewCatalogueButton.setFont(new Font("Arial", Font.BOLD, 14));
        viewCatalogueButton.addActionListener(e -> {
            this.dispose();
            new ViewMealCatalogueScreen(new MealController(), 
                                        new MealIngredientDAO(), 
                                        new IngredientDAO(),
                                        c,lDAO, 
                                        new DietPreferenceDAO()
                                        ).setVisible(true);
        });

        bp.add(backButton);
        bp.add(viewCatalogueButton);
    }
}