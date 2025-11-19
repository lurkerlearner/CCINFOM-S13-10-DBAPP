package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import DAO.MealDAO;
import DAO.RiderDAO;
import app.DBConnection;
import controller.ClientController;
import controller.DeliveryController;
import controller.MealController;
import model.Client;
import model.Delivery;
import model.DeliveryMethod;
import model.DeliveryStatus;
import model.DietPreference;
import model.MealPlan;
import model.PaymentMode;
import model.PaymentStatus;

public class DeliveryPanel extends JPanel 
{
    private DeliveryController controller;
    
    // UI Components
    private JTabbedPane tabbedPane;
    private JPanel addPanel;
    private JPanel viewPanel;
    private JPanel searchPanel;
    private JPanel editPanel;
    
    // Components for adding deliveries
    private JTextField orderDate;
    private JTextField timeOrdered;
    private JTextField timeDelivered;
    private JComboBox<String> paymentMode;
    private JComboBox<String> paymentStatus;
    private JComboBox<String> deliveryMethod;
    private JComboBox<String> deliveryStatus;
    private JTextField clientID;
    private JTextField mealID;
    private JTextField riderID;
    private JButton addButton;
    
    // Components for viewing deliveries
    private JTable deliveryTable;
    private DefaultTableModel tableModel;
    private JButton refreshButton;
    private JButton detailsButton;
    
    // Components for searching deliveries
    private JComboBox<String> searchTypeComboBox;
    private JTextField searchField;
    private JTextField startDateField = new JTextField("YYYY-MM-DD", 10);
    private JTextField endDateField = new JTextField("YYYY-MM-DD", 10);
    private JButton searchButton;
    private JTable searchResultTable;
    private DefaultTableModel searchTableModel;

    // Button to go back main menu
    private JButton mainMenuButton;

    public DeliveryPanel(DeliveryController controller) 
    {
        this.controller = controller;
        
        setLayout(new BorderLayout());
        
        initComponents();
    }
    
    // Initialize all GUI components
    private void initComponents() 
    {
        // Create the tabbed pane
        tabbedPane = new JTabbedPane();
        
        // Create and add panels for each tab
        createAddPanel();
        createViewPanel();
        createSearchPanel();
        createEditPanel();
        
        tabbedPane.addTab("Add Delivery", addPanel);
        tabbedPane.addTab("View All Deliveries", viewPanel);
        tabbedPane.addTab("Search Deliveries", searchPanel);
        tabbedPane.addTab("Edit Delivery", editPanel);
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    // Create the panel for adding a delivery record
    private void createAddPanel() 
    {
        addPanel = new JPanel();
        addPanel.setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        String[] paymentModes = Arrays.stream(PaymentMode.values())
                                  .map(PaymentMode::getDbValue)
                                  .toArray(String[]::new);

        String[] paymentStatuses = Arrays.stream(PaymentStatus.values())
                                        .map(PaymentStatus::getDbValue)
                                        .toArray(String[]::new);

        String[] deliveryMethods = Arrays.stream(DeliveryMethod.values())
                                        .map(DeliveryMethod::getDbValue)
                                        .toArray(String[]::new);

        String[] deliveryStatuses = Arrays.stream(DeliveryStatus.values())
                                        .map(DeliveryStatus::getDbValue)
                                        .toArray(String[]::new);
            
        // Create fields
        orderDate = new JTextField("YYYY-MM-DD", 10);
        timeOrdered = new JTextField("HH:MM:SS", 8);
        timeDelivered = new JTextField("HH:MM:SS", 8);
        paymentMode = new JComboBox<>(paymentModes);
        paymentStatus = new JComboBox<>(paymentStatuses);
        deliveryMethod = new JComboBox<>(deliveryMethods);
        deliveryStatus = new JComboBox<>(deliveryStatuses);
        clientID = new JTextField(5);
        mealID = new JTextField(5);
        riderID = new JTextField(5);
        
        // Add fields to the panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Order Date:"), gbc);

        gbc.gridx = 1;
        formPanel.add(orderDate, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Time Ordered:"), gbc);

        gbc.gridx = 1;
        formPanel.add(timeOrdered, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Time Delivered:"), gbc);

        gbc.gridx = 1;
        formPanel.add(timeDelivered, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Payment Mode:"), gbc);

        gbc.gridx = 1;
        formPanel.add(paymentMode, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Payment Status:"), gbc);

        gbc.gridx = 1;
        formPanel.add(paymentStatus, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Delivery Method:"), gbc);

        gbc.gridx = 1;
        formPanel.add(deliveryMethod, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(new JLabel("Delivery Status:"), gbc);

        gbc.gridx = 1;
        formPanel.add(deliveryStatus, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        formPanel.add(new JLabel("Client ID:"), gbc);

        gbc.gridx = 1;
        formPanel.add(clientID, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        formPanel.add(new JLabel("Meal ID:"), gbc);

        gbc.gridx = 1;
        formPanel.add(mealID, gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        formPanel.add(new JLabel("Rider ID:"), gbc);

        gbc.gridx = 1;
        formPanel.add(riderID, gbc);
        
        // Create button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        mainMenuButton = new JButton("Return to Main Menu");
        mainMenuButton.addActionListener(e -> {
            SwingUtilities.getWindowAncestor(this).dispose();
            new AdminMainMenu().setVisible(true);
        });
        addButton = new JButton("Add Delivery");
        addButton.addActionListener(e -> addDelivery());
        
        buttonPanel.add(mainMenuButton);
        buttonPanel.add(addButton);
        
        // Add panels to the main panel
        addPanel.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        addPanel.add(buttonPanel, BorderLayout.SOUTH);
    }
    
    // Create the panel for viewing all deliveries
    private void createViewPanel() 
    {
        viewPanel = new JPanel();
        viewPanel.setLayout(new BorderLayout());
        viewPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        String[] columnNames = {"Transaction ID", "Order Date", "Time Ordered", "Time Delivered", 
                                "Payment Mode", "Payment Status", "Delivery Method", 
                                "Delivery Status", "Client ID", "Meal ID", "Rider ID"};

        tableModel = new DefaultTableModel(columnNames, 0) 
        {
            @Override
            public boolean isCellEditable(int row, int column) 
            {
                return false;
            }
        };

        deliveryTable = new JTable(tableModel);
        deliveryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        deliveryTable.getTableHeader().setReorderingAllowed(false);
        
        // Create button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshDeliveryTable());
        
        detailsButton = new JButton("View Details");
        detailsButton.addActionListener(e -> showDeliveryDetails());
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(detailsButton);
        
        viewPanel.add(new JScrollPane(deliveryTable), BorderLayout.CENTER);
        viewPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Populate the table
        refreshDeliveryTable();
    }
    
    // Create the panel for searching deliveries
    private void createSearchPanel() 
    {
        searchPanel = new JPanel();
        searchPanel.setLayout(new BorderLayout());
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create search panel
        JPanel searchControls = new JPanel();
        searchControls.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        searchTypeComboBox = new JComboBox<>(new String[]{"By Transaction ID", 
                                                          "By Delivery Status", 
                                                          "By Payment Status",
                                                          "By Client ID", "By Meal ID",
                                                          "By Date Range"});
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchDelivery());

        startDateField = new JTextField("YYYY-MM-DD", 10);
        startDateField.setEnabled(false);
        endDateField = new JTextField("YYYY-MM-DD", 10);
        endDateField.setEnabled(false);
        
        searchControls.add(new JLabel("Search:"));
        searchControls.add(searchTypeComboBox);
        searchControls.add(searchField);
        searchControls.add(searchButton);
        searchControls.add(new JLabel("From:"));
        searchControls.add(startDateField);
        searchControls.add(new JLabel("To:"));
        searchControls.add(endDateField);
        
        
        // Create table
        String[] columnNames = {"Transaction ID", "Order Date", "Time Ordered", "Time Delivered", 
                                "Payment Mode", "Payment Status", "Delivery Method", 
                                "Delivery Status", "Client ID", "Meal ID", "Rider ID"};

        searchTableModel = new DefaultTableModel(columnNames, 0) 
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };

        searchResultTable = new JTable(searchTableModel);
        searchResultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        searchResultTable.getTableHeader().setReorderingAllowed(false);
        
        // Create button for details
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        JButton detailsButton = new JButton("View Details");
        detailsButton.addActionListener(e -> showSearchResultDetails());
        
        buttonPanel.add(detailsButton);
        
        // Add components to the panel
        searchPanel.add(searchControls, BorderLayout.NORTH);
        searchPanel.add(new JScrollPane(searchResultTable), BorderLayout.CENTER);
        searchPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    // Create the panel for editing deliveries
    private void createEditPanel() {
        editPanel = new JPanel(new BorderLayout());
        editPanel.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);

        JComboBox<Delivery> deliveryDropdown = new JComboBox<>();
        for (Delivery d : controller.getAllDeliveries()) {
            deliveryDropdown.addItem(d);
        }
        deliveryDropdown.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Delivery delivery) {
                    setText("Transaction #" + delivery.getTransactionID());
                }
                return this;
            }
        });

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Select Delivery:"), gbc);
        gbc.gridx = 1;
        formPanel.add(deliveryDropdown, gbc);

        JLabel editOrderDateLabel = new JLabel("Order Date: ");
        JLabel editTimeOrderedLabel = new JLabel("Time Ordered: ");
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(editOrderDateLabel, gbc);
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(editTimeOrderedLabel, gbc);

        SpinnerDateModel timeModel = new SpinnerDateModel();
        JSpinner editTimeDeliveredSpinner = new JSpinner(timeModel);
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(editTimeDeliveredSpinner, "HH:mm:ss");
        editTimeDeliveredSpinner.setEditor(timeEditor);

        JComboBox<PaymentMode> editPaymentMode = new JComboBox<>(new PaymentMode[]
                                                    {
                                                        PaymentMode.BANK,
                                                        PaymentMode.GCASH,
                                                        PaymentMode.CASH_ON_DELIVERY
                                                    });
        
        JComboBox<PaymentStatus> editPaymentStatus = new JComboBox<>(new PaymentStatus[]
                                                    {
                                                        PaymentStatus.PAID,
                                                        PaymentStatus.PENDING,
                                                        PaymentStatus.FAILED
                                                    });
                                                    
        JComboBox<DeliveryMethod> editDeliveryMethod = new JComboBox<DeliveryMethod>(new DeliveryMethod[]
                                                    {
                                                        DeliveryMethod.MOTORCYCLE,
                                                        DeliveryMethod.TIKLING_TRICYCLE,
                                                        DeliveryMethod.DRONE,
                                                        DeliveryMethod.BOAT,
                                                        DeliveryMethod.TRUCK
                                                    });

        JComboBox<DeliveryStatus> editDeliveryStatus = new JComboBox<>(new DeliveryStatus[]
                                                    {
                                                        DeliveryStatus.ON_TIME,
                                                        DeliveryStatus.DELAYED,
                                                        DeliveryStatus.CANCELLED
                                                    });

        JTextField editClientField = new JTextField(5);
        JTextField editMealField = new JTextField(5);
        JTextField editRiderField = new JTextField(5);

        int row = 3;
        addField(formPanel, gbc, row++, "Time Delivered:", editTimeDeliveredSpinner);
        addField(formPanel, gbc, row++, "Payment Mode:", editPaymentMode);
        addField(formPanel, gbc, row++, "Payment Status:", editPaymentStatus);
        addField(formPanel, gbc, row++, "Delivery Method:", editDeliveryMethod);
        addField(formPanel, gbc, row++, "Delivery Status:", editDeliveryStatus);
        addField(formPanel, gbc, row++, "Client ID:", editClientField);
        addField(formPanel, gbc, row++, "Meal ID:", editMealField);
        addField(formPanel, gbc, row, "Rider ID:", editRiderField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBtn = new JButton("Save Changes");
        JButton deleteBtn = new JButton("Delete Delivery");

        buttonPanel.add(saveBtn);
        buttonPanel.add(deleteBtn);

        editPanel.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        editPanel.add(buttonPanel, BorderLayout.SOUTH);

        deliveryDropdown.addActionListener(e -> 
        {
            Delivery selected = (Delivery) deliveryDropdown.getSelectedItem();
            if (selected != null) 
            {
                editOrderDateLabel.setText("Order Date: " + (java.sql.Date)selected.getOrderDate());
                editTimeOrderedLabel.setText("Time Ordered: " + (Time)selected.getTimeOrdered());

                Date now = new Date();
                editTimeDeliveredSpinner.setValue(now);
                editPaymentMode.setSelectedItem(selected.getPaymentMode());
                editPaymentStatus.setSelectedItem(selected.getPaymentStatus());
                editDeliveryMethod.setSelectedItem(selected.getDeliveryMethod());
                editDeliveryStatus.setSelectedItem(selected.getDeliveryStatus());
                editClientField.setText("" + selected.getClientID());
                editMealField.setText("" + selected.getMealID());
                editRiderField.setText("" + selected.getRiderID());
            }
        });

        saveBtn.addActionListener(e -> 
        {
            try 
            {
                Delivery selected = (Delivery) deliveryDropdown.getSelectedItem();
                if (selected == null) 
                {
                    JOptionPane.showMessageDialog(this, "Please select a delivery transaction.");
                    return;
                }

                Date spinnerDate = (Date) editTimeDeliveredSpinner.getValue();
                Time newTD = new Time(spinnerDate.getTime());
                PaymentMode newPM = (PaymentMode) editPaymentMode.getSelectedItem();
                PaymentStatus newPS = (PaymentStatus) editPaymentStatus.getSelectedItem();
                DeliveryMethod newDM = (DeliveryMethod) editDeliveryMethod.getSelectedItem();
                DeliveryStatus newDS = (DeliveryStatus) editDeliveryStatus.getSelectedItem();
                int newClient = Integer.parseInt(editClientField.getText());
                int newMeal = Integer.parseInt(editMealField.getText());
                int newRider = Integer.parseInt(editRiderField.getText());

                int key = selected.getTransactionID();

                if (newTD != null && newTD != selected.getTimeDelivered())
                {
                    boolean TD_ok = controller.changeDeliveryColumn(key, "time_delivered", newTD);
                    if (TD_ok)
                        JOptionPane.showMessageDialog(this, "Time delivered updated successfully!");
                    refreshDeliveryTable();
                }

                if (newPM != null && newPM != selected.getPaymentMode())
                {
                    boolean PM_ok = controller.updatePaymentMode(key, newPM);
                    if (PM_ok)
                        JOptionPane.showMessageDialog(this, "Payment mode updated successfully!");
                    refreshDeliveryTable();
                }

                if (newPS != null && newPS != selected.getPaymentStatus())
                {
                    boolean PS_ok = controller.updatePaymentStatus(key, newPS);
                    if (PS_ok)
                        JOptionPane.showMessageDialog(this, "Payment status updated successfully!");
                    refreshDeliveryTable();
                }

                if (newDM != null && newDM != selected.getDeliveryMethod())
                {
                    boolean DM_ok = controller.updateDeliveryMethod(key, newDM);
                    if (DM_ok)
                        JOptionPane.showMessageDialog(this, "Delivery method updated successfully!");
                    refreshDeliveryTable();
                }

                if (newDS != null && newDS != selected.getDeliveryStatus())
                {
                    boolean DS_ok = controller.updateDeliveryStatus(key, newDS);
                    if (DS_ok)
                        JOptionPane.showMessageDialog(this, "Delivery status updated successfully!");
                    refreshDeliveryTable();
                }
                
                if (editClientField.getText() != null)
                {
                    ClientController cc = new ClientController();
                    boolean C_ok = false;
                    if (cc.searchClientsById(editClientField.getText()) != null
                        && newClient != selected.getClientID())
                        C_ok = controller.changeDeliveryColumn(key, "client_id", newClient);
                    if (C_ok)
                        JOptionPane.showMessageDialog(this, "Client updated successfully!");
                    refreshDeliveryTable();
                }

                
                if (editMealField.getText() != null)
                {
                    MealDAO md = new MealDAO();
                    boolean M_ok = false;
                    if (md.getMealById(newMeal) != null && newMeal != selected.getMealID())
                        M_ok = controller.changeDeliveryColumn(key, "meal_id", newMeal);
                    if (M_ok)
                        JOptionPane.showMessageDialog(this, "Meal updated successfully!");
                    refreshDeliveryTable();
                }

                if (editRiderField.getText() != null)
                {
                    RiderDAO rd = new RiderDAO(DBConnection.getConnection());
                    boolean R_ok = false;
                    if (rd.getRiderByKey(newRider) != null && newRider != selected.getRiderID()) 
                        R_ok = controller.changeDeliveryColumn(key, "rider_id", newRider);
                    if (R_ok)
                        JOptionPane.showMessageDialog(this, "Rider updated successfully!");
                    refreshDeliveryTable();
                }
            } 
            catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage());
            }
        });

        deleteBtn.addActionListener(e -> 
        {
            try 
            {
                Delivery selected = (Delivery) deliveryDropdown.getSelectedItem();
                if (selected == null) 
                {
                    JOptionPane.showMessageDialog(this, "Please select a delivery transaction.");
                    return;
                }
                else
                {
                    int key = selected.getTransactionID();
                    boolean dlt_ok = controller.deleteDelivery(key);
                    if (dlt_ok)
                        JOptionPane.showMessageDialog(this, "Delivery record deleted successfully!");
                    refreshDeliveryTable();
                }
            } 
            catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error deleting delivery record! " + ex.getMessage());
            }
        });
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }
    
    // Add a new delivery using the input fields
    private void addDelivery() 
    {
        try 
        {
            java.sql.Date orderDateSQL = java.sql.Date.valueOf(orderDate.getText().trim()); 
            Time timeOrderedSQL = java.sql.Time.valueOf(timeOrdered.getText().trim()); 
            Time timeDeliveredSQL = null;
            String td = timeDelivered.getText().trim();
            if (!td.isEmpty())
                timeDeliveredSQL = Time.valueOf(td);

            PaymentMode payMode = PaymentMode.fromDbValue((String) paymentMode.getSelectedItem());
            PaymentStatus payStatus = PaymentStatus.fromDbValue((String) paymentStatus.getSelectedItem());
            DeliveryMethod delMethod = DeliveryMethod.fromDbValue((String) deliveryMethod.getSelectedItem());
            DeliveryStatus delStatus = DeliveryStatus.fromDbValue((String) deliveryStatus.getSelectedItem());

            int clientIdInt = Integer.parseInt(clientID.getText().trim());
            int mealIdInt = Integer.parseInt(mealID.getText().trim());
            int riderIdInt = Integer.parseInt(riderID.getText().trim());

            controller.addDelivery(orderDateSQL, timeOrderedSQL, timeDeliveredSQL,
                                payMode, payStatus, delMethod, delStatus,
                                clientIdInt, mealIdInt, riderIdInt);

            JOptionPane.showMessageDialog(this, "Delivery added successfully!");
            clearAddFields();
            refreshDeliveryTable();

        } 
        catch (IllegalArgumentException e) 
        {
            JOptionPane.showMessageDialog(this, "Invalid value/s cannot be added.",
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Clear all input fields in the add panel 
    private void clearAddFields()
    {
        orderDate.setText("");
        timeOrdered.setText("HH:MM:SS");
        timeDelivered.setText("HH:MM:SS");
        paymentMode.setSelectedItem(null);
        paymentStatus.setSelectedItem(null);
        deliveryMethod.setSelectedItem(null);
        deliveryStatus.setSelectedItem(null);
        clientID.setText("");
        mealID.setText("");
        riderID.setText("");
    }
    
    // Refresh the Delivery table with the latest data
    private void refreshDeliveryTable() 
    {
        // Clear the table
        tableModel.setRowCount(0);
        
        // Get all Deliveries
        List<Delivery> allDeliveries = controller.getAllDeliveries();
        
        // Populate the table
        for (Delivery d : allDeliveries) 
        {
            Object[] row = new Object[] 
            {
                d.getTransactionID(),  
                d.getOrderDate(),
                d.getTimeOrdered(),
                d.getTimeDelivered(),
                d.getPaymentMode().getDbValue(),
                d.getPaymentStatus().getDbValue(),
                d.getDeliveryMethod().getDbValue(),
                d.getDeliveryStatus().getDbValue(),
                d.getClientID(),
                d.getMealID(),
                d.getRiderID()
            };

            tableModel.addRow(row);
        }
    }
    
    // Show detailed information about the selected delivery
    private void showDeliveryDetails() 
    {
        int selectedRow = deliveryTable.getSelectedRow();
        
        if (selectedRow == -1) 
        {
            JOptionPane.showMessageDialog(this, "Please select a delivery to view details.",
                                        "No Selection", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int transactionID = (int) tableModel.getValueAt(selectedRow, 0);
        Delivery delivery = controller.getDeliveryByID(transactionID);
        
        if (delivery != null) {
            JTextArea textArea = new JTextArea(delivery.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300));
            
            JOptionPane.showMessageDialog(this, scrollPane, "Delivery Details", 
                                          JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    // Search for a delivery record based on the selected criteria
    private void searchDelivery() 
    {
        // Clear the table
        searchTableModel.setRowCount(0);
        
        String searchType = (String) searchTypeComboBox.getSelectedItem();
        String query = searchField.getText().trim();

        boolean isDateRange = "By Date Range".equals(searchType);
        startDateField.setEnabled(isDateRange);
        endDateField.setEnabled(isDateRange);
        
        List<Delivery> results = new ArrayList<>();
                
        try
        {
            switch (searchType) 
            {
                case "By Transaction ID":
                    int transactionID = Integer.parseInt(query);
                    Delivery d = controller.getDeliveryByID(transactionID);
                    if (d != null) results.add(d);
                    break;

                case "By Delivery Status":
                    DeliveryStatus delStatus = DeliveryStatus.fromDbValue(query);
                    results = controller.getDeliveriesByDeliveryStatus(delStatus);
                    break;

                case "By Payment Status":
                    PaymentStatus payStatus = PaymentStatus.fromDbValue(query);
                    results = controller.getDeliveriesByPaymentStatus(payStatus);
                    break;

                case "By Client ID":
                    int clientID = Integer.parseInt(query);
                    results = controller.getDeliveriesByClient(clientID);
                    break;

                case "By Meal ID":
                    int mealID = Integer.parseInt(query);
                    results = controller.getDeliveriesByMeal(mealID);
                    break;

                case "By Date Range":
                    java.sql.Date startDate = java.sql.Date.valueOf(startDateField.getText().trim());
                    java.sql.Date endDate = java.sql.Date.valueOf(endDateField.getText().trim());
                    results = controller.getDeliveriesWithinDateRange(startDate, endDate);
                    break;

                default:
                    JOptionPane.showMessageDialog(this, "Unknown search type.", 
                                                "Error", JOptionPane.ERROR_MESSAGE);
                    return;
            }
        } 
        catch (NumberFormatException e) 
        {
            JOptionPane.showMessageDialog(this, "Please enter a valid ID.", 
                                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        } 
        catch (IllegalArgumentException e) 
        {
            JOptionPane.showMessageDialog(this, "Please enter a valid status.", 
                                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Populate the table with search results
        for (Delivery d : results) 
        {
            Object[] row = new Object[] 
            {
                d.getTransactionID(),  
                d.getOrderDate(),
                d.getTimeOrdered(),
                d.getTimeDelivered(),
                d.getPaymentMode().getDbValue(),
                d.getPaymentStatus().getDbValue(),
                d.getDeliveryMethod().getDbValue(),
                d.getDeliveryStatus().getDbValue(),
                d.getClientID(),
                d.getMealID(),
                d.getRiderID()
            };

            searchTableModel.addRow(row);
        }
        
        if (results.isEmpty()) 
        {
            JOptionPane.showMessageDialog(this, "No deliveries found matching the search criteria.",
                                        "No Results", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    // Show detailed information about the selected search result.
    private void showSearchResultDetails() 
    {
        int selectedRow = searchResultTable.getSelectedRow();
        
        if (selectedRow == -1) 
        {
            JOptionPane.showMessageDialog(this, "Please select a delivery to view details.",
                                        "No Selection", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int transactionID = (int) searchTableModel.getValueAt(selectedRow, 0);
        Delivery delivery = controller.getDeliveryByID(transactionID);
        
        if (delivery != null) 
        {
            JTextArea textArea = new JTextArea(delivery.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300));
            
            JOptionPane.showMessageDialog(this, scrollPane, "Delivery Details", 
                                          JOptionPane.INFORMATION_MESSAGE);
        }
    }

}
