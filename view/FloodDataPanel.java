package view;

import controller.*;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FloodDataPanel extends JPanel 
{
    private FloodDataController controller;
    
    // UI Components
    private JTabbedPane tabbedPane;
    private JPanel addPanel;
    private JPanel viewPanel;
    private JPanel searchPanel;
    
    // Components for adding flood data
    private JComboBox<String> floodFactor;
    private JTextField avgWaterLevel;
    private JTextField affectedHouseholds;
    private JComboBox<String> roadCondition;
    private JTextField specialPackaging;
    private JComboBox<String> altDeliveryMethod;
    private JTextField locationID;
    private JButton addButton;
    
    // Components for viewing flood data
    private JTable floodTable;
    private DefaultTableModel tableModel;
    private JButton refreshButton;
    private JButton detailsButton;
    
    // Components for searching flood data
    private JComboBox<String> searchTypeComboBox;
    private JTextField searchField;
    private JButton searchButton;
    private JTable searchResultTable;
    private DefaultTableModel searchTableModel;

    public FloodDataPanel(FloodDataController controller) 
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
        
        tabbedPane.addTab("Add Flood Data", addPanel);
        tabbedPane.addTab("View All", viewPanel);
        tabbedPane.addTab("Search", searchPanel);
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    // Create the panel for adding a flood data record
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
        
        String[] floodFactors = Arrays.stream(FloodFactor.values())
                                  .map(FloodFactor::name)
                                  .toArray(String[]::new);

        String[] roadConditions = Arrays.stream(RoadCondition.values())
                                        .map(RoadCondition::getDbValue)
                                        .toArray(String[]::new);

        String[] altDeliveryMethods = Arrays.stream(AltDeliveryMethod.values())
                                        .map(AltDeliveryMethod::getDbValue)
                                        .toArray(String[]::new);
            
        // Create fields
        floodFactor = new JComboBox<>(floodFactors);
        avgWaterLevel = new JTextField(8);
        affectedHouseholds = new JTextField(8);
        roadCondition = new JComboBox<>(roadConditions);
        specialPackaging = new JTextField(5);
        altDeliveryMethod = new JComboBox<>(altDeliveryMethods);
        locationID = new JTextField(5);
        
        // Add fields to the panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Flood Factor:"), gbc);

        gbc.gridx = 1;
        formPanel.add(floodFactor, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Average Water Level:"), gbc);

        gbc.gridx = 1;
        formPanel.add(avgWaterLevel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Affected Households:"), gbc);

        gbc.gridx = 1;
        formPanel.add(affectedHouseholds, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Road Condition:"), gbc);

        gbc.gridx = 1;
        formPanel.add(roadCondition, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Special Packaging:"), gbc);

        gbc.gridx = 1;
        formPanel.add(specialPackaging, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Alt Delivery Method:"), gbc);

        gbc.gridx = 1;
        formPanel.add(altDeliveryMethod, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(new JLabel("Location ID:"), gbc);

        gbc.gridx = 1;
        formPanel.add(locationID, gbc);

        // Create button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        addButton = new JButton("Add Flood Data");
        addButton.addActionListener(e -> addFloodData());
        
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
        
        String[] columnNames = {"Flood ID", "Flood Factor", "Average Water Level", 
                                "Affected Households", "Road Condition", "Special Packaging", 
                                "Alt Delivery Method", "Location ID"};

        tableModel = new DefaultTableModel(columnNames, 0) 
        {
            @Override
            public boolean isCellEditable(int row, int column) 
            {
                return false;
            }
        };

        floodTable = new JTable(tableModel);
        floodTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        floodTable.getTableHeader().setReorderingAllowed(false);
        
        // Create button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshFloodTable());
        
        detailsButton = new JButton("View Details");
        detailsButton.addActionListener(e -> showFloodDetails());
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(detailsButton);
        
        viewPanel.add(new JScrollPane(floodTable), BorderLayout.CENTER);
        viewPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Populate the table
        refreshFloodTable();
    }
    
    // Create the panel for searching flood data
    private void createSearchPanel() 
    {
        searchPanel = new JPanel();
        searchPanel.setLayout(new BorderLayout());
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create search panel
        JPanel searchControls = new JPanel();
        searchControls.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        searchTypeComboBox = new JComboBox<>(new String[]{"By Flood ID", 
                                                          "By Flood Factor", 
                                                          "By Affected Households",
                                                          "By Road Condition", 
                                                          "By Location ID"});
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchFloodData());
        
        searchControls.add(new JLabel("Search:"));
        searchControls.add(searchTypeComboBox);
        searchControls.add(searchField);
        searchControls.add(searchButton);
        
        // Create table
        String[] columnNames = {"Flood ID", "Flood Factor", "Average Water Level", 
                                "Affected Households", "Road Condition", "Special Packaging", 
                                "Alt Delivery Method", "Location ID"};

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
    
    // Add new flood data using the input fields
    private void addFloodData() 
    {
        try 
        {
            FloodFactor ff = FloodFactor.valueOf(((String) floodFactor.getSelectedItem()).toUpperCase());
            RoadCondition rc = RoadCondition.fromDbValue((String) roadCondition.getSelectedItem());
            AltDeliveryMethod adm = AltDeliveryMethod.fromDbValue((String) altDeliveryMethod.getSelectedItem());

            float avgWaterLvlFloat = Float.parseFloat(avgWaterLevel.getText().trim());
            int affectedHouseholdsInt = Integer.parseInt(affectedHouseholds.getText().trim());
            String text = specialPackaging.getText().trim();
            boolean specialPackaging = text.equals("1") || text.equalsIgnoreCase("true");
            int locationIdInt = Integer.parseInt(locationID.getText().trim());

            controller.addFloodData(ff, avgWaterLvlFloat, affectedHouseholdsInt, rc,
                                    specialPackaging, adm, locationIdInt);

            JOptionPane.showMessageDialog(this, "Flood data added successfully!");
            clearAddFields();
            refreshFloodTable();

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
        floodFactor.setSelectedItem(null);
        avgWaterLevel.setText("");
        affectedHouseholds.setText("");
        roadCondition.setSelectedItem(null);
        specialPackaging.setText("");
        altDeliveryMethod.setSelectedItem(null);
        locationID.setText("");
    }
    
    // Refresh the Flood table with the latest data
    private void refreshFloodTable() 
    {
        // Clear the table
        tableModel.setRowCount(0);
        
        // Get all flood data
        List<FloodData> allFloodData = controller.getAllFloodData();
        
        // Populate the table
        for (FloodData f : allFloodData) 
        {
            Object[] row = new Object[] 
            {
                f.getFloodID(),
                f.getFloodFactor(),
                f.getAvgWaterLevel(),
                f.getAffectedHouseholds(),
                f.getRoadCondition(),
                f.getSpecialPackaging(),
                f.getAltDeliveryMethod(),
                f.getLocationID()
            };

            tableModel.addRow(row);
        }
    }
    
    // Show detailed information about the selected flood data
    private void showFloodDetails() 
    {
        int selectedRow = floodTable.getSelectedRow();
        
        if (selectedRow == -1) 
        {
            JOptionPane.showMessageDialog(this, "Please select a flood record to view details.",
                                        "No Selection", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int floodID = (int) tableModel.getValueAt(selectedRow, 0);
        FloodData flooddata = controller.getFloodDataById(floodID);
        
        if (flooddata != null) {
            JTextArea textArea = new JTextArea(flooddata.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300));
            
            JOptionPane.showMessageDialog(this, scrollPane, "Flood Data Details", 
                                          JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    // Search for a flood data record based on the selected criteria
    private void searchFloodData() 
    {
        // Clear the table
        searchTableModel.setRowCount(0);
        
        String searchType = (String) searchTypeComboBox.getSelectedItem();
        String query = searchField.getText().trim();
        
        List<FloodData> results = new ArrayList<>();
                
        try
        {
            switch (searchType) 
            {
                case "By Flood ID":
                    int floodID = Integer.parseInt(query);
                    FloodData f = controller.getFloodDataById(floodID);
                    if (f != null) results.add(f);
                    break;

                case "By Flood Factor":
                    FloodFactor ff = FloodFactor.valueOf(query.toUpperCase());
                    results = controller.getByFloodFactor(ff);
                    break;

                case "By Affected Households":
                    int houses = Integer.parseInt(query);
                    results = controller.getByHousesAffected(houses);
                    break;

                case "By Road Condition":
                    RoadCondition rc = RoadCondition.fromDbValue(query);
                    results = controller.getByRoadCondition(rc);
                    break;

                case "By Location ID":
                    int locationID = Integer.parseInt(query);
                    results = controller.getByLocation(locationID);
                    break;

                default:
                    JOptionPane.showMessageDialog(this, "Unknown search type.", 
                                                "Error", JOptionPane.ERROR_MESSAGE);
                    return;
            }
        } 
        catch (NumberFormatException e) 
        {
            JOptionPane.showMessageDialog(this, "Please enter a valid ID/number.", 
                                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        } 
        catch (IllegalArgumentException e) 
        {
            JOptionPane.showMessageDialog(this, "Please enter a valid choice.", 
                                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Populate the table with search results
        for (FloodData f : results) 
        {
            Object[] row = new Object[] 
            {
                f.getFloodID(),
                f.getFloodFactor(),
                f.getAvgWaterLevel(),
                f.getAffectedHouseholds(),
                f.getRoadCondition(),
                f.getSpecialPackaging(),
                f.getAltDeliveryMethod(),
                f.getLocationID()
            };

            searchTableModel.addRow(row);
        }
        
        if (results.isEmpty()) 
        {
            JOptionPane.showMessageDialog(this, "No records found matching the search criteria.",
                                        "No Results", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    // Show detailed information about the selected search result.
    private void showSearchResultDetails() 
    {
        int selectedRow = searchResultTable.getSelectedRow();
        
        if (selectedRow == -1) 
        {
            JOptionPane.showMessageDialog(this, "Please select a record to view details.",
                                        "No Selection", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int floodID = (int) searchTableModel.getValueAt(selectedRow, 0);
        FloodData flooddata = controller.getFloodDataById(floodID);
        
        if (flooddata != null) 
        {
            JTextArea textArea = new JTextArea(flooddata.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300));
            
            JOptionPane.showMessageDialog(this, scrollPane, "Flood Data Details", 
                                          JOptionPane.INFORMATION_MESSAGE);
        }
    }

}
