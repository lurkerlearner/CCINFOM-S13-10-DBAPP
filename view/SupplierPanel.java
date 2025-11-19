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
    private JPanel editPanel;

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

    private JButton deleteBtn;

    // Components for searching suppliers
    private JButton searchButton;
    private JComboBox<String> searchTypeComboBox;
    private JTextField searchField;
    private JTable searchResultTable;
    private DefaultTableModel searchTableModel;

    // Components for editing suppliers
    private JTextField editSupplierName;
    private JTextField editContactNo;
    private JTextField editAltContactNo;
    private JTextField editLocationID;

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
        createEditPanel();
        
        tabbedPane.addTab("Add Supplier", addPanel);
        tabbedPane.addTab("View All", viewPanel);
        tabbedPane.addTab("Search", searchPanel);
        tabbedPane.addTab("Edit Supplier", editPanel);
        
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
            String contactNo = contact_no.getText().trim();
            String altContactNo = alt_contact_no.getText().trim();
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

        deleteBtn = new JButton("Delete Selected");
        deleteBtn.addActionListener(e -> deleteSupplier());
        
        detailsButton = new JButton("View Details");
        detailsButton.addActionListener(e -> showSupplierDetails());

        buttonPanel.add(refreshButton);
        buttonPanel.add(detailsButton);
        buttonPanel.add(deleteBtn);
        
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

    private void createEditPanel() {
        editPanel = new JPanel(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);

        JComboBox<Supplier> supplierDropdown = new JComboBox<>();
        for (Supplier s : controller.getAllSuppliers()) {
            supplierDropdown.addItem(s);
        }
        supplierDropdown.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Supplier supplier) {
                    setText(supplier.getSupplier_id() + " - " + supplier.getSupplier_name());
                }
                return this;
            }
        });

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Select Supplier:"), gbc);
        gbc.gridx = 1;
        formPanel.add(supplierDropdown, gbc);

        editSupplierName = new JTextField(20);
        editContactNo = new JTextField(15);
        editAltContactNo = new JTextField(15);
        editLocationID = new JTextField(5);

        int row = 1;
        addField(formPanel, gbc, row++, "Supplier Name:", editSupplierName);
        addField(formPanel, gbc, row++, "Contact Number:", editContactNo);
        addField(formPanel, gbc, row++, "Alternative Contact Number:", editAltContactNo);
        addField(formPanel, gbc, row++, "Location ID:", editLocationID);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBtn = new JButton("Save Changes");

        buttonPanel.add(saveBtn);

        editPanel.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        editPanel.add(buttonPanel, BorderLayout.SOUTH);

        supplierDropdown.addActionListener(e -> {
            Supplier selected = (Supplier) supplierDropdown.getSelectedItem();
            if(selected != null) {
                editSupplierName.setText(selected.getSupplier_name());
                editContactNo.setText(selected.getContact_no());
                editAltContactNo.setText(selected.getAlt_contact_no());
                editLocationID.setText(String.valueOf(selected.getLocation_id()));
            }
        });

        // Save changes
        saveBtn.addActionListener(e -> {
            try {
                Supplier selected = (Supplier) supplierDropdown.getSelectedItem();
                if (selected == null) {
                    JOptionPane.showMessageDialog(this, "Please select a supplier.");
                    return;
                }

                // Create updated supplier object
                Supplier updatedSupplier = new Supplier();
                updatedSupplier.setSupplier_id(selected.getSupplier_id()); // Keep the same ID
                updatedSupplier.setSupplier_name(editSupplierName.getText());
                updatedSupplier.setContact_no(editContactNo.getText());
                updatedSupplier.setAlt_contact_no(editAltContactNo.getText());
                updatedSupplier.setLocation_id(Integer.parseInt(editLocationID.getText()));

                // Use the updateSupplierAll method that takes a Supplier object
                boolean success = controller.updateSupplierAll(updatedSupplier);

                if (success) {
                    JOptionPane.showMessageDialog(this, "Supplier updated successfully!");
                    refreshSupplierTable();
                    // Refresh dropdown
                    supplierDropdown.removeAllItems();
                    for (Supplier s : controller.getAllSuppliers()) {
                        supplierDropdown.addItem(s);
                    }
                    // Re-select the updated supplier
                    for (int i = 0; i < supplierDropdown.getItemCount(); i++) {
                        if (supplierDropdown.getItemAt(i).getSupplier_id() == selected.getSupplier_id()) {
                            supplierDropdown.setSelectedIndex(i);
                            break;
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update supplier.");
                }

            } 
            catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage());
            }
        });
    }

    private void deleteSupplier() {
        int selectedRow = supplierTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a supplier to delete.",
                    "No Selection", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete the selected supplier? This may affect related records (e.g., ingredient).",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int supplierIdToDelete = (int) tableModel.getValueAt(selectedRow, 0);

                if (controller.deleteSupplier(supplierIdToDelete)) {
                    JOptionPane.showMessageDialog(this, "Supplier deleted successfully.");
                    refreshSupplierTable();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Failed to delete supplier. Check database constraints.",
                            "Deletion Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "An error occurred during deletion: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }

}
