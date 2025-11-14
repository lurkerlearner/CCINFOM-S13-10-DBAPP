package view;

import controller.MealPlanController;
import model.MealPlan;
import model.Meal; // ASSUMPTION: This class is available
import DAO.MealPlanDAO; 

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MealPlanPanel extends JPanel 
{
    private MealPlanController controller;
    
    // UI Components
    private JTabbedPane tabbedPane;
    private JPanel addPanel;
    private JPanel viewPanel;
    private JPanel searchPanel;
    
    // Components for adding Meal Plans
    private JTextField planID; 
    private JTextField planNameField;
    private JTextField descriptionField;
    private JTextField totalPriceField;
    private JButton addButton;
    
    // Components for viewing Meal Plans
    private JTable planTable;
    private DefaultTableModel tableModel;
    private JButton refreshButton;
    private JButton detailsButton;
    private JButton deleteButton; 
    
    // Components for searching Meal Plans
    private JComboBox<String> searchTypeComboBox;
    private JTextField searchField;
    private JButton searchButton;
    private JTable searchResultTable;
    private DefaultTableModel searchTableModel;

    public MealPlanPanel(MealPlanController controller) 
    {
        this.controller = controller;
        
        setLayout(new BorderLayout());
        
        initComponents();
    }
    
    // Initialize all GUI components
    private void initComponents() 
    {
        tabbedPane = new JTabbedPane();
        
        createAddPanel();
        createViewPanel();
        createSearchPanel();
        
        tabbedPane.addTab("Add Meal Plan", addPanel);
        tabbedPane.addTab("View All / Modify", viewPanel);
        tabbedPane.addTab("Search", searchPanel);
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    // --- 1. Create the panel for adding a Meal Plan record ---
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
        
        // Fields based on MealPlan POJO
        planID = new JTextField(5); 
        planID.setEditable(false); 
        planNameField = new JTextField(25); 
        descriptionField = new JTextField(35); 
        totalPriceField = new JTextField(10); 
        
        int row = 0;
        
        // Plan ID (Auto-Generated)
        gbc.gridx = 0; gbc.gridy = row++;
        formPanel.add(new JLabel("Plan ID (Auto-Generated):"), gbc);
        gbc.gridx = 1;
        formPanel.add(planID, gbc);

        // Plan Name
        gbc.gridx = 0; gbc.gridy = row++;
        formPanel.add(new JLabel("Plan Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(planNameField, gbc);

        // Description
        gbc.gridx = 0; gbc.gridy = row++;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        formPanel.add(descriptionField, gbc);
        
        // Total Price
        gbc.gridx = 0; gbc.gridy = row++;
        formPanel.add(new JLabel("Total Price (0.00):"), gbc);
        gbc.gridx = 1;
        formPanel.add(totalPriceField, gbc);
        
        // Create button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        addButton = new JButton("Add Meal Plan");
        addButton.addActionListener(e -> addMealPlan());
        
        buttonPanel.add(addButton);
        
        addPanel.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        addPanel.add(buttonPanel, BorderLayout.SOUTH);
    }
    
    // --- 2. Create the panel for viewing all Meal Plans ---
    private void createViewPanel() 
    {
        viewPanel = new JPanel();
        viewPanel.setLayout(new BorderLayout());
        viewPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Column names matching the POJO
        String[] columnNames = {"ID", "Plan Name", "Description", "Total Price"};

        tableModel = new DefaultTableModel(columnNames, 0) 
        {
            @Override
            public boolean isCellEditable(int row, int column) 
            {
                // Allow editing of Plan Name, Description, and Price
                return column > 0; 
            }
        };

        planTable = new JTable(tableModel);
        planTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        planTable.getTableHeader().setReorderingAllowed(false);
        
        // Listener for cell edits to handle updates
        tableModel.addTableModelListener(e -> {
            if (e.getType() == javax.swing.event.TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                updateMealPlanFromTable(row);
            }
        });
        
        // Create button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshPlanTable());
        
        detailsButton = new JButton("View Details");
        detailsButton.addActionListener(e -> showPlanDetails(planTable, tableModel));
        
        deleteButton = new JButton("Delete Selected");
        deleteButton.addActionListener(e -> deleteMealPlan());
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(detailsButton);
        buttonPanel.add(deleteButton);
        
        viewPanel.add(new JScrollPane(planTable), BorderLayout.CENTER);
        viewPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Populate the table on load
        refreshPlanTable();
    }
    
    // --- 3. Create the panel for searching Meal Plans ---
    private void createSearchPanel() 
    {
        searchPanel = new JPanel();
        searchPanel.setLayout(new BorderLayout());
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create search panel
        JPanel searchControls = new JPanel();
        searchControls.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        searchTypeComboBox = new JComboBox<>(new String[]{"By ID", "By Name"});
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchMealPlan());
        
        searchControls.add(new JLabel("Search By:"));
        searchControls.add(searchTypeComboBox);
        searchControls.add(searchField);
        searchControls.add(searchButton);
        
        // Create table
        String[] columnNames = {"ID", "Plan Name", "Description", "Total Price"};

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
        detailsButton.addActionListener(e -> showPlanDetails(searchResultTable, searchTableModel));
        
        buttonPanel.add(detailsButton);
        
        // Add components to the panel
        searchPanel.add(searchControls, BorderLayout.NORTH);
        searchPanel.add(new JScrollPane(searchResultTable), BorderLayout.CENTER);
        searchPanel.add(buttonPanel, BorderLayout.SOUTH);
    }
    
    // --- 4. Logic Implementation (CRUD Operations) ---
    
    private void addMealPlan() 
    {
        try 
        {
            String name = planNameField.getText().trim();
            String description = descriptionField.getText().trim();
            float price = 0.0f;
            
            // Allow price field to be empty or zero for new plans
            if (!totalPriceField.getText().trim().isEmpty()) {
                 price = Float.parseFloat(totalPriceField.getText().trim());
            }

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Plan Name cannot be empty.",
                                             "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // ID is 0 for auto-generation
            MealPlan newPlan = new MealPlan(0, name, description, price);

            if (controller.createNewMealPlan(newPlan)) {
                JOptionPane.showMessageDialog(this, "Meal Plan added successfully!");
                clearAddFields();
                refreshPlanTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add meal plan. It might already exist.",
                                             "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        } 
        catch (NumberFormatException e) 
        {
            JOptionPane.showMessageDialog(this, "Invalid value for Total Price. Must be a number.",
                                         "Input Error", JOptionPane.ERROR_MESSAGE);
        }
        catch (Exception e) 
        {
            JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + e.getMessage(),
                                         "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateMealPlanFromTable(int row) 
    {
        try {
            int id = (int) tableModel.getValueAt(row, 0);
            String newName = tableModel.getValueAt(row, 1).toString().trim();
            String newDescription = tableModel.getValueAt(row, 2).toString().trim();
            float newPrice = 0.0f;
            
            try {
                newPrice = Float.parseFloat(tableModel.getValueAt(row, 3).toString().trim());
            } catch (NumberFormatException nfe) {
                 JOptionPane.showMessageDialog(this, "Invalid price format. Reverting cell.",
                                             "Input Error", JOptionPane.ERROR_MESSAGE);
                 refreshPlanTable();
                 return;
            }
            
            MealPlan updatedPlan = new MealPlan(id, newName, newDescription, newPrice);
            
            // Check for name emptiness before attempting update
            if (newName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Plan Name cannot be empty. Reverting cell.",
                                             "Validation Error", JOptionPane.ERROR_MESSAGE);
                refreshPlanTable();
                return;
            }
            
            if (controller.updateMealPlan(updatedPlan)) {
                // Success is handled silently
            } else {
                JOptionPane.showMessageDialog(this, "Update failed. Check if Plan Name is unique.",
                                             "Update Error", JOptionPane.ERROR_MESSAGE);
                refreshPlanTable();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid data or update error: " + e.getMessage(),
                                         "Error", JOptionPane.ERROR_MESSAGE);
            refreshPlanTable(); // Revert table state
        }
    }

    private void deleteMealPlan() {
        int selectedRow = planTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a meal plan to delete.",
                                         "No Selection", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete the selected meal plan? This may affect related meal associations.", 
            "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int idToDelete = (int) tableModel.getValueAt(selectedRow, 0);
                
                // FIX: Use the controller's public getter for the DAO access
                if (controller.getMealPlanDAO().deleteMealPlan(idToDelete)) {
                    JOptionPane.showMessageDialog(this, "Meal Plan deleted successfully.");
                    refreshPlanTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete meal plan. Check database constraints (e.g., linked orders).",
                                                 "Deletion Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "An error occurred during deletion: " + e.getMessage(),
                                             "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearAddFields()
    {
        planNameField.setText("");
        descriptionField.setText("");
        totalPriceField.setText("");
    }
    
    private void refreshPlanTable() 
    {
        tableModel.setRowCount(0);
        
        // Correct controller method: getAllMealPlans()
        List<MealPlan> allPlans = controller.getAllMealPlans();
        
        for (MealPlan mp : allPlans) 
        {
            Object[] row = new Object[] 
            {
                mp.getPlan_id(), // Correct getter
                mp.getPlan_name(), // Correct getter
                mp.getDescription(), // Correct getter
                mp.getTotal_price() // Correct getter
            };

            tableModel.addRow(row);
        }
    }
    
    private void showPlanDetails(JTable table, DefaultTableModel model) 
    {
        int selectedRow = table.getSelectedRow();
        
        if (selectedRow == -1) 
        {
            JOptionPane.showMessageDialog(this, "Please select a meal plan to view details.",
                                         "No Selection", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int planID = (int) model.getValueAt(selectedRow, 0);
        
        MealPlan plan = controller.getMealPlanDetails(planID); 
        
        if (plan != null) {
            
            // --- Logic to display Meals in Plan ---
            // FIX: Use the new controller method getMealsForPlan(planID)
            List<Meal> mealsInPlan = controller.getMealsForPlan(planID);
            
            // 2. Build the detailed string/layout
            StringBuilder mealDetails = new StringBuilder("Meals in this Plan:\n");
            
            if (mealsInPlan.isEmpty()) {
                mealDetails.append("  (No meals currently assigned to this plan.)");
            } else {
                for (Meal meal : mealsInPlan) {
                    // NOTE: Assumes Meal POJO methods based on DAO
                    mealDetails.append(String.format("  - %s (ID: %d | Price: $%.2f | Calories: %d)\n",
                        meal.getMeal_name(), 
                        meal.getMeal_id(), 
                        meal.getPrice(), 
                        meal.getCalories()
                    ));
                }
            }
            
            // 3. Combine Plan Details and Meal Details into a single component
            String planInfo = String.format("PLAN INFO:\nID: %d\nName: %s\nPrice: $%.2f\nDescription: %s\n\n",
                plan.getPlan_id(),
                plan.getPlan_name(),
                plan.getTotal_price(),
                plan.getDescription());
            
            JTextArea textArea = new JTextArea(planInfo + mealDetails.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 300));
            
            JOptionPane.showMessageDialog(this, scrollPane, "Meal Plan Details (" + plan.getPlan_name() + ")", 
                                         JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void searchMealPlan() 
    {
        searchTableModel.setRowCount(0);
        
        String searchType = (String) searchTypeComboBox.getSelectedItem();
        String query = searchField.getText().trim();
        
        if (query.isEmpty() && !searchType.equals("By ID")) {
            JOptionPane.showMessageDialog(this, "Please enter a search query.", 
                                         "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        List<MealPlan> results = new ArrayList<>();
        MealPlan singleResult = null;
        
        try
        {
            switch (searchType) 
            {
                case "By ID":
                    if (!query.isEmpty()) {
                        int id = Integer.parseInt(query);
                        // Correct controller method: getMealPlanDetails(id)
                        singleResult = controller.getMealPlanDetails(id);
                    }
                    break;

                case "By Name":
                    // Correct controller method: getPlanByName(name)
                    singleResult = controller.getPlanByName(query);
                    break;

                default:
                    return; // Should not happen
            }
            
            // Handle single results retrieved from search methods
            if (singleResult != null) {
                 results.add(singleResult);
            }
        } 
        catch (NumberFormatException e) 
        {
            JOptionPane.showMessageDialog(this, "Please enter a valid ID.", 
                                         "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        } 
        catch (Exception e) 
        {
             JOptionPane.showMessageDialog(this, "Search error: " + e.getMessage(), 
                                         "Error", JOptionPane.ERROR_MESSAGE);
             return;
        }
        
        // Populate the table with search results
        for (MealPlan mp : results) 
        {
            Object[] row = new Object[] 
            {
                mp.getPlan_id(), 
                mp.getPlan_name(), 
                mp.getDescription(),
                mp.getTotal_price()
            };

            searchTableModel.addRow(row);
        }
        
        if (results.isEmpty()) 
        {
            JOptionPane.showMessageDialog(this, "No meal plans found matching the search criteria.",
                                         "No Results", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}