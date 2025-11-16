package view;

import controller.*;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RiderPanel extends JPanel 
{
    private RiderController controller;
    
    // UI Components
    private JTabbedPane tabbedPane;
    private JPanel addPanel;
    private JPanel viewPanel;
    private JPanel searchPanel;
    
    // Components for adding riders
    private JTextField riderName;
    private JTextField hireDate;
    private JTextField contactNo;
    private JButton addButton;
    
    // Components for viewing riders
    private JTable riderTable;
    private DefaultTableModel tableModel;
    private JButton refreshButton;
    private JButton detailsButton;
    
    // Components for searching riders
    private JComboBox<String> searchTypeComboBox;
    private JTextField searchField;
    private JButton searchButton;
    private JTable searchResultTable;
    private DefaultTableModel searchTableModel;

    // Button to go back to main menu
    private JButton mainMenuButton;

    public RiderPanel(RiderController controller) 
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
        
        tabbedPane.addTab("Add Rider", addPanel);
        tabbedPane.addTab("View All", viewPanel);
        tabbedPane.addTab("Search", searchPanel);
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    // Create the panel for adding a rider
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
            
        // Create fields
        riderName = new JTextField(8);
        hireDate = new JTextField(8);
        contactNo = new JTextField(8);
        
        // Add fields to the panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Rider Name:"), gbc);

        gbc.gridx = 1;
        formPanel.add(riderName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Hire Date:"), gbc);

        gbc.gridx = 1;
        formPanel.add(hireDate, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Contact No:"), gbc);

        gbc.gridx = 1;
        formPanel.add(contactNo, gbc);

        // Create button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        mainMenuButton = new JButton("Return to Main Menu");
        mainMenuButton.addActionListener(e -> {
            SwingUtilities.getWindowAncestor(this).dispose();
            new AdminMainMenu().setVisible(true);
        });
        addButton = new JButton("Add Rider");
        addButton.addActionListener(e -> addRider());
        
        buttonPanel.add(mainMenuButton);
        buttonPanel.add(addButton);
        
        // Add panels to the main panel
        addPanel.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        addPanel.add(buttonPanel, BorderLayout.SOUTH);
    }
    
    // Create the panel for viewing all riders
    private void createViewPanel() 
    {
        viewPanel = new JPanel();
        viewPanel.setLayout(new BorderLayout());
        viewPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        String[] columnNames = {"Rider ID", "Rider Name", "Hire Date", "Contact No."};

        tableModel = new DefaultTableModel(columnNames, 0) 
        {
            @Override
            public boolean isCellEditable(int row, int column) 
            {
                return false;
            }
        };

        riderTable = new JTable(tableModel);
        riderTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        riderTable.getTableHeader().setReorderingAllowed(false);
        
        // Create button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshRiderTable());
        
        detailsButton = new JButton("View Details");
        detailsButton.addActionListener(e -> showRiderDetails());
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(detailsButton);
        
        viewPanel.add(new JScrollPane(riderTable), BorderLayout.CENTER);
        viewPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Populate the table
        refreshRiderTable();
    }
    
    // Create the panel for searching riders
    private void createSearchPanel() 
    {
        searchPanel = new JPanel();
        searchPanel.setLayout(new BorderLayout());
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create search panel
        JPanel searchControls = new JPanel();
        searchControls.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        searchTypeComboBox = new JComboBox<>(new String[]{"By Rider ID"});

        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchRider());
        
        searchControls.add(new JLabel("Search:"));
        searchControls.add(searchTypeComboBox);
        searchControls.add(searchField);
        searchControls.add(searchButton);
        
        // Create table
        String[] columnNames = {"Rider ID", "Rider Name", "Hire Date", "Contact No."};

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
    
    // Add new rider using the input fields
    private void addRider() 
    {
        try 
        {
            String name = riderName.getText().trim();
            Date hireDateSQL = java.sql.Date.valueOf(hireDate.getText().trim());             
            String contact = contactNo.getText().trim();

            controller.addRider(name, hireDateSQL, contact);

            JOptionPane.showMessageDialog(this, "Rider added successfully!");
            clearAddFields();
            refreshRiderTable();

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
        riderName.setText("");
        hireDate.setText("");
        contactNo.setText("");
    }
    
    // Refresh the Rider table with the latest data
    private void refreshRiderTable() 
    {
        // Clear the table
        tableModel.setRowCount(0);
        
        // Get all riders
        List<Rider> allRiders = controller.getAllRiders();
        
        // Populate the table
        for (Rider r : allRiders) 
        {
            Object[] row = new Object[] 
            {
                r.getRiderID(),
                r.getRiderName(),
                r.getHireDate(),
                r.getContactNo()
            };

            tableModel.addRow(row);
        }
    }
    
    // Show detailed information about the selected rider
    private void showRiderDetails() 
    {
        int selectedRow = riderTable.getSelectedRow();
        
        if (selectedRow == -1) 
        {
            JOptionPane.showMessageDialog(this, "Please select a rider to view details.",
                                        "No Selection", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int riderID = (int) tableModel.getValueAt(selectedRow, 0);
        Rider rider = controller.getRiderById(riderID);
        
        if (rider != null) {
            JTextArea textArea = new JTextArea(rider.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300));
            
            JOptionPane.showMessageDialog(this, scrollPane, "Rider Details", 
                                          JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    // Search for a rider based on the selected criteria
    private void searchRider() 
    {
        // Clear the table
        searchTableModel.setRowCount(0);
        
        String searchType = (String) searchTypeComboBox.getSelectedItem();
        String query = searchField.getText().trim();
        
        List<Rider> results = new ArrayList<>();
                
        try
        {
            switch (searchType) 
            {
                case "By Rider ID":
                    int riderID = Integer.parseInt(query);
                    Rider r = controller.getRiderById(riderID);
                    if (r != null) results.add(r);
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
        
        // Populate the table with search results
        for (Rider r : results) 
        {
            Object[] row = new Object[] 
            {
                r.getRiderID(),
                r.getRiderName(),
                r.getHireDate(),
                r.getContactNo()
            };

            searchTableModel.addRow(row);
        }
        
        if (results.isEmpty()) 
        {
            JOptionPane.showMessageDialog(this, "No riders found matching the search criteria.",
                                        "No Results", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    // Show detailed information about the selected search result.
    private void showSearchResultDetails() 
    {
        int selectedRow = searchResultTable.getSelectedRow();
        
        if (selectedRow == -1) 
        {
            JOptionPane.showMessageDialog(this, "Please select a rider to view details.",
                                        "No Selection", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int riderID = (int) searchTableModel.getValueAt(selectedRow, 0);
        Rider rider = controller.getRiderById(riderID);
        
        if (rider != null) 
        {
            JTextArea textArea = new JTextArea(rider.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300));
            
            JOptionPane.showMessageDialog(this, scrollPane, "Rider Details", 
                                          JOptionPane.INFORMATION_MESSAGE);
        }
    }

}
