package view;

import controller.*;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import DAO.MealDAO;
import DAO.RiderDAO;
import app.DBConnection;

import java.awt.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class FloodDataPanel extends JPanel 
{
    private FloodDataController controller;
    
    // UI Components
    private JTabbedPane tabbedPane;
    private JPanel addPanel;
    private JPanel viewPanel;
    private JPanel searchPanel;
    private JPanel editPanel;
    
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

    // Button to return to Main Menu
    private JButton mainMenuButton;

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
        createEditPanel();
        
        tabbedPane.addTab("Add Flood Data", addPanel);
        tabbedPane.addTab("View All", viewPanel);
        tabbedPane.addTab("Search Flood Data", searchPanel);
        tabbedPane.addTab("Edit Flood Data", editPanel);
        
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
        
        mainMenuButton = new JButton("Return to Main Menu");
        mainMenuButton.addActionListener(e -> {
            SwingUtilities.getWindowAncestor(this).dispose();
            new AdminMainMenu().setVisible(true);
        });
        addButton = new JButton("Add Flood Data");
        addButton.addActionListener(e -> addFloodData());
        
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

    // Create the panel for editing flood data records
    private void createEditPanel() {
        editPanel = new JPanel(new BorderLayout());
        editPanel.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);

        JComboBox<FloodData> floodDropdown = new JComboBox<>();
        for (FloodData f : controller.getAllFloodData()) {
            floodDropdown.addItem(f);
        }
        floodDropdown.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof FloodData flood) {
                    setText("Flood ID " + flood.getFloodID());
                }
                return this;
            }
        });

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Select Flood Data Record:"), gbc);
        gbc.gridx = 1;
        formPanel.add(floodDropdown, gbc);

        JComboBox<FloodFactor> editFloodFactor = new JComboBox<>(new FloodFactor[]
                                                    {
                                                        FloodFactor.LOW,
                                                        FloodFactor.MODERATE,
                                                        FloodFactor.HIGH,
                                                        FloodFactor.SEVERE
                                                    });
        
        JComboBox<RoadCondition> editRoadCondition = new JComboBox<>(new RoadCondition[]
                                                    {
                                                        RoadCondition.ACCESSIBLE,
                                                        RoadCondition.PARTIALLY_FLOODED,
                                                        RoadCondition.NOT_ACCESSIBLE
                                                    });
                                                    
        JComboBox<AltDeliveryMethod> editAltDeliveryMethod = new JComboBox<>(new AltDeliveryMethod[]
                                                    {
                                                        AltDeliveryMethod.MOTORCYCLE,
                                                        AltDeliveryMethod.TIKLING_TRICYCLE,
                                                        AltDeliveryMethod.TIKLING_TRICYCLE,
                                                        AltDeliveryMethod.DRONE,
                                                        AltDeliveryMethod.BOAT,
                                                        AltDeliveryMethod.TRUCK
                                                    });

        JTextField editAWLField = new JTextField(5);
        JTextField editAHField = new JTextField(5);
        JTextField editSPField = new JTextField(5);
        JTextField editLocationField = new JTextField(5);

        int row = 1;
        addField(formPanel, gbc, row++, "Flood Factor:", editFloodFactor);
        addField(formPanel, gbc, row++, "Average Water Level:", editAWLField);
        addField(formPanel, gbc, row++, "Affected Households:", editAHField);
        addField(formPanel, gbc, row++, "Road Condition:", editRoadCondition);
        addField(formPanel, gbc, row++, "Special Packaging:", editSPField);
        addField(formPanel, gbc, row++, "Alt Delivery Method:", editAltDeliveryMethod);
        addField(formPanel, gbc, row++, "Location ID:", editLocationField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBtn = new JButton("Save Changes");
        JButton deleteBtn = new JButton("Delete Flood Data");

        buttonPanel.add(saveBtn);
        buttonPanel.add(deleteBtn);

        editPanel.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        editPanel.add(buttonPanel, BorderLayout.SOUTH);

        floodDropdown.addActionListener(e -> 
        {
            FloodData selected = (FloodData) floodDropdown.getSelectedItem();
            if (selected != null) 
            {
                editFloodFactor.setSelectedItem(selected.getFloodFactor());
                editAWLField.setText("" + selected.getAvgWaterLevel());
                editAHField.setText("" + selected.getAffectedHouseholds());
                editRoadCondition.setSelectedItem(selected.getRoadCondition());
                editSPField.setText("" + selected.getSpecialPackaging());
                editAltDeliveryMethod.setSelectedItem(selected.getAltDeliveryMethod());
                editLocationField.setText("" + selected.getLocationID());
            }
        });

        saveBtn.addActionListener(e -> {
            try 
            {
                FloodData selected = (FloodData) floodDropdown.getSelectedItem();
                if (selected == null) 
                {
                    JOptionPane.showMessageDialog(this, "Please select a flood data record.");
                    return;
                }

                FloodFactor newFF = (FloodFactor) editFloodFactor.getSelectedItem();
                float newAWL = Float.parseFloat(editAWLField.getText());
                int newAH = Integer.parseInt(editAHField.getText());
                RoadCondition newRC = (RoadCondition) editRoadCondition.getSelectedItem();
                boolean newSP = Boolean.parseBoolean(editSPField.getText());
                AltDeliveryMethod newADM = (AltDeliveryMethod) editAltDeliveryMethod.getSelectedItem();
                int newLocation = Integer.parseInt(editLocationField.getText());

                int key = selected.getFloodID();

                if (newFF != null && newFF != selected.getFloodFactor())
                {
                    boolean FF_ok = controller.updateFloodFactor(key, newFF);
                    if (FF_ok)
                        JOptionPane.showMessageDialog(this, "Flood factor updated successfully!");
                    refreshFloodTable();
                }

                if (editAWLField.getText() != null &&
                Math.abs(newAWL - selected.getAvgWaterLevel()) > 0.0001)
                {
                    boolean AWL_ok = controller.changeFloodDataColumn(key, 
                    "avg_water_level", newAWL);
                    if (AWL_ok)
                        JOptionPane.showMessageDialog(this, "Average water level updated successfully!");
                    refreshFloodTable();
                }

                if (editAHField.getText() != null && newAH != selected.getAffectedHouseholds())
                {
                    boolean AH_ok = controller.changeFloodDataColumn(key, 
                    "affected_households", newAH);
                    if (AH_ok)
                        JOptionPane.showMessageDialog(this, "Number of affected households updated successfully!");
                    refreshFloodTable();
                }

                if (newRC != null && newRC != selected.getRoadCondition())
                {
                    boolean RC_ok = controller.updateRoadCondition(key, newRC);
                    if (RC_ok)
                        JOptionPane.showMessageDialog(this, "Road condition updated successfully!");
                    refreshFloodTable();
                }

                if (newSP != selected.getSpecialPackaging())
                {
                    boolean SP_ok = controller.changeFloodDataColumn(key, 
                    "special_packaging", newSP);
                    if (SP_ok)
                        JOptionPane.showMessageDialog(this, "Special packaging updated successfully!");
                    refreshFloodTable();
                }

                if (newADM != null && newADM != selected.getAltDeliveryMethod())
                {
                    boolean ADM_ok = controller.updateAltDeliveryMethod(key, newADM);
                    if (ADM_ok)
                        JOptionPane.showMessageDialog(this, "Alternative delivery method updated successfully!");
                    refreshFloodTable();
                }
                
                if (editLocationField.getText() != null)
                {
                    LocationController lc = new LocationController();
                    boolean l_ok = false;
                    if (lc.searchLocationById(editLocationField.getText()) != null
                        && newLocation != selected.getLocationID())
                        l_ok = controller.changeFloodDataColumn(key, 
                        "location_id", newLocation);
                    if (l_ok)
                        JOptionPane.showMessageDialog(this, "Location updated successfully!");
                    refreshFloodTable();
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
                FloodData selected = (FloodData) floodDropdown.getSelectedItem();
                if (selected == null) 
                {
                    JOptionPane.showMessageDialog(this, "Please select a flood data record.");
                    return;
                }
                else
                {
                    int key = selected.getFloodID();
                    boolean dlt_ok = controller.deleteFloodData(key);
                    if (dlt_ok)
                        JOptionPane.showMessageDialog(this, "Flood data record deleted successfully!");
                    refreshFloodTable();
                }
            } 
            catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error deleting flood data record! " + ex.getMessage());
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
                f.getRoadCondition().getDbValue(),
                f.getSpecialPackaging(),
                f.getAltDeliveryMethod().getDbValue(),
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
                f.getRoadCondition().getDbValue(),
                f.getSpecialPackaging(),
                f.getAltDeliveryMethod().getDbValue(),
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

