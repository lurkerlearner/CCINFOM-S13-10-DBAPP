package view;

import controller.*;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.util.*;
import java.sql.Date;

public class IngredientPanel extends JPanel
{
    private IngredientController controller;

    // UI Components
    private JTabbedPane tabbedPane;
    private JPanel addPanel;
    private JPanel viewPanel;
    private JPanel searchPanel;
    private JPanel editPanel;

    // Components for adding ingredients
    private JTextField batch_no;
    private JTextField ingredient_name;
    private JComboBox<String> category;
    private JComboBox<String> storage_type;
    private JComboBox<String> measurement_unit;
    private JTextField stock_quantity;  
    private JTextField expiry_date;
    private JTextField supplier_id;
    private JButton addButton;

    private JButton deleteBtn;

    // Components for viewing ingredients
    private JTable ingredientTable;
    private DefaultTableModel tableModel;
    private JButton refreshButton;
    private JButton detailsButton;

    // Components for searching ingredients
    private JButton searchButton;
    private JComboBox<String> searchTypeComboBox;
    private JTextField searchField;
    private JTable searchResultTable;
    private DefaultTableModel searchTableModel;

    // Components for editing ingredients
    private JTextField editBatchNo;
    private JTextField editIngredientName;
    private JComboBox<String> editCategory;
    private JComboBox<String> editStorageType;
    private JComboBox<String> editUnit;
    private JTextField editStockQuantity;
    private JTextField editExpiryDate;
    private JTextField editSupplierID;

    // Button to go back to main menu
    private JButton mainMenuButton;

    public IngredientPanel(IngredientController ingredientController)
    {
        this.controller = ingredientController;

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
        
        tabbedPane.addTab("Add Ingredient", addPanel);
        tabbedPane.addTab("View All", viewPanel);
        tabbedPane.addTab("Search", searchPanel);
        tabbedPane.addTab("Edit Ingredient", editPanel);
        
        add(tabbedPane, BorderLayout.CENTER);
    }

    // Create the panel for adding an ingredient record
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

        String[] categories = Arrays.stream(Category.values())
                                    .map(Category::getDbValue)
                                    .toArray(String[]::new);
        
        String[] storageTypes = Arrays.stream(Storage_type.values())
                                    .map(Storage_type::getDbValue)
                                    .toArray(String[]::new);
        
        String[] measurementUnits = Arrays.stream(Measurement_unit.values())
                                    .map(Measurement_unit::getDbValue)
                                    .toArray(String[]::new);

        // String[] restockStatuses if needed

        // size of textfield na columns is the number of m that can fit in a text field
        batch_no = new JTextField(5);
        ingredient_name = new JTextField(20);
        category = new JComboBox<>(categories);
        storage_type = new JComboBox<>(storageTypes);
        measurement_unit = new JComboBox<>(measurementUnits);
        stock_quantity = new JTextField(8);
        expiry_date = new JTextField("YYYY-MM-DD",10);
        supplier_id = new JTextField(5);

        // x and y refer to row and column respectively
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Batch No:"), gbc);
        gbc.gridx = 1;
        formPanel.add(batch_no, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Ingredient Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(ingredient_name, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1;
        formPanel.add(category, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Storage Type:"), gbc);
        gbc.gridx = 1;
        formPanel.add(storage_type, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Measurement Unit:"), gbc);
        gbc.gridx = 1;
        formPanel.add(measurement_unit, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Stock Quantity:"), gbc);
        gbc.gridx = 1;
        formPanel.add(stock_quantity, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new JLabel("Expiry Date:"), gbc);
        gbc.gridx = 1;
        formPanel.add(expiry_date, gbc);

        gbc.gridx = 0; gbc.gridy = 7;
        formPanel.add(new JLabel("Supplier ID:"), gbc);
        gbc.gridx = 1;
        formPanel.add(supplier_id, gbc);

        // Create button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        mainMenuButton = new JButton("Return to Main Menu");
        mainMenuButton.addActionListener(e -> {
            SwingUtilities.getWindowAncestor(this).dispose();
            new AdminMainMenu().setVisible(true);
        });
        addButton = new JButton("Add Ingredient");
        addButton.addActionListener(e -> addIngredient());
        
        buttonPanel.add(mainMenuButton);
        buttonPanel.add(addButton);
        
        // Add panels to the main panel
        addPanel.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        addPanel.add(buttonPanel, BorderLayout.SOUTH);
    }    

    // Add a new ingredient using the input fields
    private void addIngredient() 
    {
        try 
        {
            int batchNo = Integer.parseInt(batch_no.getText().trim());
            String name = ingredient_name.getText().trim();
            Category categ = Category.fromDbValue((String) category.getSelectedItem());
            Storage_type storageType = Storage_type.fromDbValue((String) storage_type.getSelectedItem());
            Measurement_unit measureUnit = Measurement_unit.fromDbValue((String) measurement_unit.getSelectedItem());
            double stockQty = Double.parseDouble(stock_quantity.getText().trim());
            Date expiryDate = java.sql.Date.valueOf(expiry_date.getText().trim());
            int supplierId = Integer.parseInt(supplier_id.getText().trim());

            controller.addIngredient(batchNo, name, categ, storageType, measureUnit, stockQty, expiryDate, supplierId);

            JOptionPane.showMessageDialog(this, "Ingredient added successfully!");
            clearAddFields();
            refreshIngredientTable();

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
        batch_no.setText("");
        ingredient_name.setText("");
        category.setSelectedItem(null);
        storage_type.setSelectedItem(null);
        measurement_unit.setSelectedItem(null);
        stock_quantity.setText("");
        expiry_date.setText("");
        supplier_id.setText("");
    }

    // Create the panel for viewing all ingredients
    private void createViewPanel()
    {
        viewPanel = new JPanel();
        viewPanel.setLayout(new BorderLayout());
        viewPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        String[] columnNames = {
            "Ingredient ID", "Batch No", "Ingredient Name", "Category", "Storage Type", 
            "Measurement Unit", "Stock Quantity", "Expiry Date", 
            "Restock Status", "Supplier ID"
        };

        // create a view-only table of the ingredients
        tableModel = new DefaultTableModel(columnNames, 0) 
        {
            @Override
            public boolean isCellEditable(int row, int column) 
            {
                return false;
            }
        };

        ingredientTable = new JTable(tableModel);
        ingredientTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ingredientTable.getTableHeader().setReorderingAllowed(false);

        // Create button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshIngredientTable());
        
        deleteBtn = new JButton("Delete Selected");
        deleteBtn.addActionListener(e -> deleteIngredient());

        detailsButton = new JButton("View Details");
        detailsButton.addActionListener(e -> showIngredientDetails());

        buttonPanel.add(refreshButton);
        buttonPanel.add(detailsButton);
        buttonPanel.add(deleteBtn);
        
        viewPanel.add(new JScrollPane(ingredientTable), BorderLayout.CENTER);
        viewPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Populate the table
        refreshIngredientTable();
    }

    // refresh table with newly added ingredient(s)
    private void refreshIngredientTable() 
    {
        tableModel.setRowCount(0); // Clear table

        // get all ingredients
        ArrayList<Ingredient> ingredients = controller.getAllIngredients();
        
        // populate table with updated data
        for (Ingredient ing : ingredients) {
            Object[] row = new Object[]
            {
                ing.getIngredient_id(),
                ing.getBatch_no(),
                ing.getIngredient_name(),
                ing.getCategory().getDbValue(),
                ing.getStorage_type().getDbValue(),
                ing.getMeasurement_unit().getDbValue(),
                ing.getStock_quantity(),
                ing.getExpiry_date(),
                ing.getRestock_status().getDbValue(),
                ing.getSupplier_id()
            };

            tableModel.addRow(row);
        }
    }

    // Show detailed information about the selected ingredient
    private void showIngredientDetails() 
    {
        int selectedRow = ingredientTable.getSelectedRow();
        
        if (selectedRow == -1) 
        {
            JOptionPane.showMessageDialog(this, "Please select an ingredient to view details.",
                                        "No Selection", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int ingredientID = (int) tableModel.getValueAt(selectedRow, 0);
        Ingredient ingredient = controller.getIngredientByID(ingredientID);
        
        if (ingredient != null) {
            JTextArea textArea = new JTextArea(ingredient.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300));
            
            JOptionPane.showMessageDialog(this, scrollPane, "Ingredient Details", 
                                          JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Create the panel for searching ingredients
    private void createSearchPanel() 
    {
        searchPanel = new JPanel();
        searchPanel.setLayout(new BorderLayout());
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create search panel
        JPanel searchControls = new JPanel();
        searchControls.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        // add Expiring Soon, Restock status (available, low, out of stock) options if needed
        searchTypeComboBox = new JComboBox<>(new String[] {
            "By Ingredient ID", "By Category", "By Batch No", "By Supplier ID",
            "By Nearest Expiry Dates", "By Restock Status", "By Storage Type"
        });

        searchField = new JTextField(15);
        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchIngredient());

        searchControls.add(new JLabel("Search:"));
        searchControls.add(searchTypeComboBox);
        searchControls.add(searchField);
        searchControls.add(searchButton);

        String[] columnNames = {
            "Ingredient ID", "Batch No", "Ingredient Name", "Category", "Storage Type", 
            "Measurement Unit", "Stock Quantity", "Expiry Date", 
            "Restock Status", "Supplier ID"
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

    // Search for an ingredient record based on the selected criteria
    private void searchIngredient() {
        searchTableModel.setRowCount(0); // Clear previous results

        String searchType = (String) searchTypeComboBox.getSelectedItem();
        String query = searchField.getText().trim();

        ArrayList<Ingredient> results = new ArrayList<>();

        try {
            switch (searchType) {
                case "By Ingredient ID":
                    int ingredientId = Integer.parseInt(query);
                    Ingredient i = controller.getIngredientByID(ingredientId);
                    if (i != null) results.add(i);
                    break;
                case "By Category":
                    Category category = Category.fromDbValue(query);
                    results = controller.getIngredientsByCategory(category);
                    break;
                case "By Storage Type":
                    Storage_type type = Storage_type.fromDbValue(query);
                    results = controller.getIngredientsByStorageType(type);
                    break;
                case "By Batch No":
                    int batchNo = Integer.parseInt(query);
                    results = controller.getIngredientsByBatchNo(batchNo);
                    break;
                case "By Supplier ID":
                    int supplierId = Integer.parseInt(query);
                    results = controller.getIngredientsBySupplier(supplierId);
                    break;
                case "By Nearest Expiry Dates":
                    results = controller.getIngredientsExpiringSoon(); 
                    break;
                case "By Restock Status":
                    Restock_status status = Restock_status.fromDbValue(query);
                    results = controller.getIngredientsByStatus(status);
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
        for (Ingredient ing : results) 
        {
            Object[] row = new Object[]
            {
                ing.getIngredient_id(),
                ing.getBatch_no(),
                ing.getIngredient_name(),
                ing.getCategory().getDbValue(),
                ing.getStorage_type().getDbValue(),
                ing.getMeasurement_unit().getDbValue(),
                ing.getStock_quantity(),
                ing.getExpiry_date(),
                ing.getRestock_status().getDbValue(),
                ing.getSupplier_id()
            };

            searchTableModel.addRow(row);
        }

        if (results.isEmpty()) 
        {
            JOptionPane.showMessageDialog(this, "No ingredients found matching the search criteria.",
                                        "No Results", JOptionPane.INFORMATION_MESSAGE);
        }

    }

    // Show detailed information about the selected search result.
    private void showSearchResultDetails() 
    {
        int selectedRow = searchResultTable.getSelectedRow();
        
        if (selectedRow == -1) 
        {
            JOptionPane.showMessageDialog(this, "Please select an ingredient to view details.",
                                        "No Selection", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int ingredientID = (int) searchTableModel.getValueAt(selectedRow, 0);
        Ingredient ingredient = controller.getIngredientByID(ingredientID);
        
        if (ingredient != null) 
        {
            JTextArea textArea = new JTextArea(ingredient.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300));
            
            JOptionPane.showMessageDialog(this, scrollPane, "Ingredient Details", 
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

        JComboBox<Ingredient> ingredientDropdown = new JComboBox<>();
        for (Ingredient i : controller.getAllIngredients()) {
            ingredientDropdown.addItem(i);
        }
        ingredientDropdown.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Ingredient ingredient) {
                    setText(ingredient.getIngredient_id() + " - " + ingredient.getIngredient_name());
                }
                return this;
            }
        });

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Select Ingredient:"), gbc);
        gbc.gridx = 1;
        formPanel.add(ingredientDropdown, gbc);

        String[] categories = Arrays.stream(Category.values())
                                    .map(Category::getDbValue)
                                    .toArray(String[]::new);
        
        String[] storageTypes = Arrays.stream(Storage_type.values())
                                    .map(Storage_type::getDbValue)
                                    .toArray(String[]::new);
        
        String[] measurementUnits = Arrays.stream(Measurement_unit.values())
                                    .map(Measurement_unit::getDbValue)
                                    .toArray(String[]::new);

        editBatchNo = new JTextField(20);
        editIngredientName = new JTextField(20);
        editCategory = new JComboBox<>(categories);
        editStorageType = new JComboBox<>(storageTypes);
        editUnit = new JComboBox<>(measurementUnits);
        editStockQuantity = new JTextField(10);
        editExpiryDate = new JTextField(20);
        editSupplierID = new JTextField(10);

        int row = 1;
        addField(formPanel, gbc, row++, "Batch No:", editBatchNo);
        addField(formPanel, gbc, row++, "Name:", editIngredientName);
        addField(formPanel, gbc, row++, "Category:", editCategory);
        addField(formPanel, gbc, row++, "Storage Type:", editStorageType);
        addField(formPanel, gbc, row++, "Measurement Unit:", editUnit);
        addField(formPanel, gbc, row++, "Stock Quantity:", editStockQuantity);
        addField(formPanel, gbc, row++, "Expiry Date:", editExpiryDate);
        addField(formPanel, gbc, row++, "Supplier ID:", editSupplierID);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBtn = new JButton("Save Changes");

        buttonPanel.add(saveBtn);

        editPanel.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        editPanel.add(buttonPanel, BorderLayout.SOUTH);

        ingredientDropdown.addActionListener(e -> {
            Ingredient selected = (Ingredient) ingredientDropdown.getSelectedItem();
            if(selected != null) {
                editBatchNo.setText(String.valueOf(selected.getBatch_no()));
                editIngredientName.setText(selected.getIngredient_name());
                editCategory.setSelectedItem(selected.getCategory().getDbValue());
                editStorageType.setSelectedItem(selected.getStorage_type().getDbValue());
                editUnit.setSelectedItem(selected.getMeasurement_unit().getDbValue());
                editStockQuantity.setText(String.valueOf(selected.getStock_quantity()));
                editExpiryDate.setText(selected.getExpiry_date().toString());
                editSupplierID.setText(String.valueOf(selected.getSupplier_id()));
            }
        });

        // Save changes
        saveBtn.addActionListener(e -> {
            try {
                Ingredient selected = (Ingredient) ingredientDropdown.getSelectedItem();
                if (selected == null) {
                    JOptionPane.showMessageDialog(this, "Please select an ingredient.");
                    return;
                }

                // Create updated ingredient object
                Ingredient updatedIngredient = new Ingredient();
                updatedIngredient.setIngredient_id(selected.getIngredient_id()); // Keep the same ID
                updatedIngredient.setBatch_no(Integer.parseInt(editBatchNo.getText()));
                updatedIngredient.setIngredient_name(editIngredientName.getText());
                updatedIngredient.setCategory(Category.fromDbValue((String) editCategory.getSelectedItem()));
                updatedIngredient.setStorage_type(Storage_type.fromDbValue((String) editStorageType.getSelectedItem()));
                updatedIngredient.setMeasurement_unit(Measurement_unit.fromDbValue((String) editUnit.getSelectedItem()));
                updatedIngredient.setStock_quantity(Double.parseDouble(editStockQuantity.getText()));
                updatedIngredient.setExpiry_date(java.sql.Date.valueOf(editExpiryDate.getText()));
                updatedIngredient.setSupplier_id(Integer.parseInt(editSupplierID.getText()));

                boolean success = controller.updateIngredient(updatedIngredient);

                if (success) {
                    JOptionPane.showMessageDialog(this, "Ingredient updated successfully!");
                    refreshIngredientTable();
                    // Refresh dropdown
                    ingredientDropdown.removeAllItems();
                    for (Ingredient i : controller.getAllIngredients()) {
                        ingredientDropdown.addItem(i);
                    }
                    // Re-select the updated ingredient
                    for (int i = 0; i < ingredientDropdown.getItemCount(); i++) {
                        if (ingredientDropdown.getItemAt(i).getIngredient_id() == selected.getIngredient_id()) {
                            ingredientDropdown.setSelectedIndex(i);
                            break;
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update ingredient.");
                }

            } 
            catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage());
            }
        });
    }

    private void deleteIngredient() {
        int selectedRow = ingredientTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an ingredient to delete.",
                    "No Selection", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete the selected ingredient? This may affect related records (e.g., meal ingredient).",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int ingredientIdToDelete = (int) tableModel.getValueAt(selectedRow, 0);

                if (controller.deleteIngredient(ingredientIdToDelete)) {
                    JOptionPane.showMessageDialog(this, "Ingredient deleted successfully.");
                    refreshIngredientTable();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Failed to delete ingredient. Check database constraints.",
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
