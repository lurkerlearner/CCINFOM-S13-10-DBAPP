package view;

import controller.*;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.util.*;
import java.sql.Date;

public class SupplierPanel extends JPanel
{
    private SupplierController controller;

    // UI Components
    private JTabbedPane tabbedPane;
    private JPanel addPanel;
    private JPanel viewPanel;
    private JPanel searchPanel;

    // Components for adding suppliers
    private JTextField supplier_name;
    private JTextField contact_no;
    private JTextField alt_contact_no;
    private JTextField location_id;
    private JButton addButton;

    // Components for viewing suppliers
    private JTable supplierTable;
    private DefaultTableModel tableModel;
    private JButton refreshButton;
    private JButton detailsButton;

    // Components for searching suppliers
    private JButton searchButton;
    private JComboBox<String> searchTypeComboBox;
    private JTextField searchField;
    private JTable searchResultTable;
    private DefaultTableModel searchTableModel;

    // Button to return to main menu
    private JButton mainMenuButton;

    public SupplierPanel (SupplierController supplierController)
    {
        this.controller = supplierController;

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
        
        tabbedPane.addTab("Add Supplier", addPanel);
        tabbedPane.addTab("View All", viewPanel);
        tabbedPane.addTab("Search", searchPanel);
        
        add(tabbedPane, BorderLayout.CENTER);
    }

    // Create the panel for adding a supplier record
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
        
        // size of textfield na columns is the number of m that can fit in a text field
        supplier_name = new JTextField(20);
        contact_no = new JTextField(15);
        alt_contact_no = new JTextField(15);
        location_id = new JTextField(5);

        // x and y refer to row and column respectively
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Supplier Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(supplier_name, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Contact Number:"), gbc);
        gbc.gridx = 1;
        formPanel.add(contact_no, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Alternative Contact Number:"), gbc);
        gbc.gridx = 1;
        formPanel.add(alt_contact_no, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Location ID:"), gbc);
        gbc.gridx = 1;
        formPanel.add(location_id, gbc);

        // Create button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        mainMenuButton = new JButton("Return to Main Menu");
        mainMenuButton.addActionListener(e -> {
            SwingUtilities.getWindowAncestor(this).dispose();
            new AdminMainMenu().setVisible(true);
        });
        addButton = new JButton("Add Supplier");
        addButton.addActionListener(e -> addSupplier());
        
        buttonPanel.add(mainMenuButton);
        buttonPanel.add(addButton);
        
        // Add panels to the main panel
        addPanel.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        addPanel.add(buttonPanel, BorderLayout.SOUTH);
    }    

    // Add a new supplier using the input fields
    private void addSupplier() 
    {
        try 
        {
            String name = supplier_name.getText().trim();
            int contactNo = Integer.parseInt(contact_no.getText().trim());
            int altContactNo = Integer.parseInt(alt_contact_no.getText().trim());
            int locationId = Integer.parseInt(location_id.getText().trim());

            controller.addSupplier(name, contactNo, altContactNo, locationId);

            JOptionPane.showMessageDialog(this, "Supplier added successfully!");
            clearAddFields();
            refreshSupplierTable();

        } 
        catch (IllegalArgumentException ex) 
        {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
        } 
        catch (Exception ex) 
        {
            JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Clear input fields after adding
    private void clearAddFields()
    {
        supplier_name.setText("");
        contact_no.setText("");
        alt_contact_no.setText("");
        location_id.setText("");
    }

    // Create the panel for viewing all suppliers
    private void createViewPanel()
    {
        viewPanel = new JPanel();
        viewPanel.setLayout(new BorderLayout());
        viewPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        String[] columnNames = {
            "Supplier ID", "Supplier Name", "Contact No", "Alt Contact No", "Location ID"
        };

        // create a view-only table of the suppliers
        tableModel = new DefaultTableModel(columnNames, 0) 
        {
            @Override
            public boolean isCellEditable(int row, int column) 
            {
                return false;
            }
        };

        supplierTable = new JTable(tableModel);
        supplierTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        supplierTable.getTableHeader().setReorderingAllowed(false);

        // Create button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshSupplierTable());
        
        detailsButton = new JButton("View Details");
        detailsButton.addActionListener(e -> showSupplierDetails());

        buttonPanel.add(refreshButton);
        buttonPanel.add(detailsButton);
        
        viewPanel.add(new JScrollPane(supplierTable), BorderLayout.CENTER);
        viewPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Populate the table
        refreshSupplierTable();
    }

    // refresh table with newly added supplier(s)
    private void refreshSupplierTable() 
    {
        tableModel.setRowCount(0); // Clear table

        // get all suppliers
        ArrayList<Supplier> supplier = controller.getAllSuppliers();
        
        // populate table with updated data
        for (Supplier sup : supplier) 
        {
            Object[] row = new Object[]
            {
                sup.getSupplier_id(),
                sup.getSupplier_name(),
                sup.getContact_no(),
                sup.getAlt_contact_no(),
                sup.getLocation_id()
            };

            tableModel.addRow(row);
        }
    }

    // Show detailed information about the selected supplier
    private void showSupplierDetails() 
    {
        int selectedRow = supplierTable.getSelectedRow();
        
        if (selectedRow == -1) 
        {
            JOptionPane.showMessageDialog(this, "Please select a supplier to view details.",
                                        "No Selection", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int supplierID = (int) tableModel.getValueAt(selectedRow, 0);
        Supplier supplier = controller.getSupplierByID(supplierID);
        
        if (supplier != null) {
            JTextArea textArea = new JTextArea(supplier.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300));
            
            JOptionPane.showMessageDialog(this, scrollPane, "Supplier Details", 
                                          JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Create the panel for searching suppliers
    private void createSearchPanel() 
    {
        searchPanel = new JPanel();
        searchPanel.setLayout(new BorderLayout());
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create search panel
        JPanel searchControls = new JPanel();
        searchControls.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        searchTypeComboBox = new JComboBox<>(new String[] {
            "By Supplier ID", "By Location ID"
        });

        searchField = new JTextField(15);
        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchSupplier());

        searchControls.add(new JLabel("Search:"));
        searchControls.add(searchTypeComboBox);
        searchControls.add(searchField);
        searchControls.add(searchButton);

        String[] columnNames = {
            "Supplier ID", "Supplier Name", "Contact No", "Alt Contact No", "Location ID"
        };

        // create a view-only table of the search/filtered results
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

        searchPanel.add(searchControls, BorderLayout.NORTH);
        searchPanel.add(new JScrollPane(searchResultTable), BorderLayout.CENTER);
        searchPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    // Search for a supplier record based on the selected criteria
    private void searchSupplier() {
        searchTableModel.setRowCount(0); // Clear previous results

        String searchType = (String) searchTypeComboBox.getSelectedItem();
        String query = searchField.getText().trim();

        ArrayList<Supplier> results = new ArrayList<>();

        try {
            switch (searchType) {
                case "By Supplier ID":
                    int supplierId = Integer.parseInt(query);
                    Supplier s = controller.getSupplierByID(supplierId);
                    if (s != null) results.add(s);
                    break;
                case "By Location ID":
                    int locationId = Integer.parseInt(query);
                    results = controller.getSuppliersByLocation(locationId);
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

        // Populate search results table
        for (Supplier sup : results) 
        {
            Object[] row = new Object[]
            {
                sup.getSupplier_id(),
                sup.getSupplier_name(),
                sup.getContact_no(),
                sup.getAlt_contact_no(),
                sup.getLocation_id()
            };

            searchTableModel.addRow(row);
        }

        if (results.isEmpty()) 
        {
            JOptionPane.showMessageDialog(this, "No suppliers found matching the search criteria.",
                                        "No Results", JOptionPane.INFORMATION_MESSAGE);
        }

    }

    // Show detailed information about the selected search result.
    private void showSearchResultDetails() 
    {
        int selectedRow = searchResultTable.getSelectedRow();
        
        if (selectedRow == -1) 
        {
            JOptionPane.showMessageDialog(this, "Please select a supplier to view details.",
                                        "No Selection", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int supplierID = (int) searchTableModel.getValueAt(selectedRow, 0);
        Supplier supplier = controller.getSupplierByID(supplierID);
        
        if (supplier != null) 
        {
            JTextArea textArea = new JTextArea(supplier.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300));
            
            JOptionPane.showMessageDialog(this, scrollPane, "Supplier Details", 
                                          JOptionPane.INFORMATION_MESSAGE);
        }
    }

    
}
