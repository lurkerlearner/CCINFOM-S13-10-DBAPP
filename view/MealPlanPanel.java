package view;

import controller.MealPlanController;
import model.MealPlan;
import model.Meal; 
import DAO.MealPlanDAO; 

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors; // Added for list filtering

public class MealPlanPanel extends JPanel 
{
    private MealPlanController controller;
    
    // UI Components
    private JTabbedPane tabbedPane;
    private JPanel addPanel;
    private JPanel viewPanel;
    private JPanel searchPanel;
    
    // --- Meal Management Components (NEW TAB CONTENT) ---
    private JPanel manageMealsTab;
    private JTable availableMealsTable;
    private JTable currentMealsTable;
    private DefaultTableModel availableMealsModel;
    // 🛠️ MODIFIED: Added remarks column
    private DefaultTableModel currentMealsModel; 
    private JLabel manageMealsTitleLabel;
    private MealPlan currentPlanForManagement; // Holds the plan being edited
    // ---------------------------------------------------
    
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
    private JButton manageMealsButton; 
    
    // Components for searching Meal Plans
    private JComboBox<String> searchTypeComboBox;
    private JTextField searchField;
    private JButton searchButton;
    private JTable searchResultTable;
    private DefaultTableModel searchTableModel;

    // button to return to main menu
    private JButton mainMenuButton;

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
        createManageMealsPanel(); 
        
        tabbedPane.addTab("Add Meal Plan", addPanel);
        tabbedPane.addTab("View All / Modify", viewPanel);
        tabbedPane.addTab("Search", searchPanel);
        tabbedPane.addTab("Manage Meals", manageMealsTab); 
        
        // Initially disable the Manage Meals tab until a plan is selected
        tabbedPane.setEnabledAt(3, false); 
        
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
        
        mainMenuButton = new JButton("Return to Main Menu");
        mainMenuButton.addActionListener(e -> {
            // Placeholder: Assume AdminMainMenu exists
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window != null) {
                window.dispose();
            }
            // new AdminMainMenu().setVisible(true); // Uncomment if AdminMainMenu class is available
        });
        addButton = new JButton("Add Meal Plan");
        addButton.addActionListener(e -> addMealPlan());
        
        buttonPanel.add(mainMenuButton);
        buttonPanel.add(addButton);
        
        addPanel.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        addPanel.add(buttonPanel, BorderLayout.SOUTH);
    }
    
    // --- 2. Create the panel for viewing all Meal Plans (Updated) ---
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
                return column > 0; 
            }
        };

        planTable = new JTable(tableModel);
        planTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        planTable.getTableHeader().setReorderingAllowed(false);
        
        tableModel.addTableModelListener(e -> {
            if (e.getType() == javax.swing.event.TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int column = e.getColumn();
                updateMealPlanFromTable(tableModel,row, column);
            }
        });
        
        // Create button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshPlanTable());
        
        detailsButton = new JButton("View Details");
        detailsButton.addActionListener(e -> showPlanDetails(planTable, tableModel));

        // NEW BUTTON: Edit Meals (switches to the new tab)
        JButton editMealsButton = new JButton("Edit Meals");
        editMealsButton.addActionListener(e -> goToManageMealsTab());
        
        deleteButton = new JButton("Delete Selected");
        deleteButton.addActionListener(e -> deleteMealPlan());
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(detailsButton);
        buttonPanel.add(editMealsButton); // <-- ADDED EDIT MEALS BUTTON
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
                return column > 0;
            }
        };

        searchResultTable = new JTable(searchTableModel);

        searchTableModel.addTableModelListener(e -> {
            if (e.getType()== TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int column = e.getColumn();

                if (column != 0) {
                    updateMealPlanFromTable(searchTableModel, row, column);
                }
            }
        }); 

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
    
    // --- 4. NEW: Create the dedicated management tab content (Fourth Tab) ---
    private void createManageMealsPanel() {
        manageMealsTab = new JPanel(new BorderLayout(10, 10));
        
        // --- North Panel: Title ---
        JPanel northPanel = new JPanel(new BorderLayout());
        manageMealsTitleLabel = new JLabel("Please select a Meal Plan from 'View All / Modify' tab.", JLabel.CENTER);
        manageMealsTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        northPanel.add(manageMealsTitleLabel, BorderLayout.CENTER);
        manageMealsTab.add(northPanel, BorderLayout.NORTH);

        // --- Tables Initialization ---
        String[] mealColumns = {"ID", "Name", "Price"};
        // 🛠️ MODIFIED: Added Remarks column for current meals table
        String[] currentMealColumns = {"ID", "Name", "Price", "Remarks"};
        
        availableMealsModel = new DefaultTableModel(mealColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        availableMealsTable = new JTable(availableMealsModel);
        availableMealsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        currentMealsModel = new DefaultTableModel(currentMealColumns, 0) { // 🛠️ USING NEW COLUMNS
            // 🛠️ MODIFIED: Remarks column (index 3) is editable
            @Override
            public boolean isCellEditable(int row, int column) { return column == 3; } 
        };
        currentMealsTable = new JTable(currentMealsModel);
        currentMealsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // 🛠️ NEW: Add listener to allow updating remarks directly in the table
        currentMealsModel.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 3) {
                updateMealRemarksFromTable(currentMealsModel, e.getFirstRow());
            }
        });

        // --- Button Panel (Center Controls) ---
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        JButton addButton = new JButton("<< Add Meal");
        JButton removeButton = new JButton("Remove Meal >>");

        addButton.addActionListener(e -> addMealToPlan()); // 🛠️ LOGIC UPDATED HERE
        removeButton.addActionListener(e -> removeMealFromPlan());
        
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        
        // --- Main Content Split Pane ---
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.5);

        JPanel availablePanel = new JPanel(new BorderLayout());
        availablePanel.add(new JLabel("Available Meals (Not in Plan):", JLabel.CENTER), BorderLayout.NORTH);
        availablePanel.add(new JScrollPane(availableMealsTable), BorderLayout.CENTER);
        
        JPanel currentPanel = new JPanel(new BorderLayout());
        currentPanel.add(new JLabel("Meals in Current Plan:", JLabel.CENTER), BorderLayout.NORTH);
        currentPanel.add(new JScrollPane(currentMealsTable), BorderLayout.CENTER);

        splitPane.setLeftComponent(availablePanel);
        splitPane.setRightComponent(currentPanel);

        JPanel centerContent = new JPanel(new BorderLayout(10, 10));
        centerContent.add(splitPane, BorderLayout.CENTER);
        centerContent.add(buttonPanel, BorderLayout.EAST);
        
        manageMealsTab.add(centerContent, BorderLayout.CENTER);
    }
    
    // --- 5. Logic Implementation (CRUD Operations & Management) ---
    
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
    
    private void updateMealPlanFromTable(DefaultTableModel model, int row, int column) 
    {
        try {
            int id = Integer.parseInt(model.getValueAt(row, 0).toString());
            String newName = (String)model.getValueAt(row, 1).toString();
            String newDescription = (String)model.getValueAt(row, 2).toString();
            float cost = Float.parseFloat(model.getValueAt(row, 3).toString());
            String result = controller.updateMealPlan(id, newName, newDescription, cost);
            
            if (newName.trim().isEmpty()) {
              JOptionPane.showMessageDialog(this, "Plan Name cannot be empty.",
                       "Validation Error", JOptionPane.ERROR_MESSAGE);
              refreshPlanTable(); 
              return;
            }
            if ("SUCCESS".equals(result)) {
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update Plan ID " + id + ": " + result, "Database Error", JOptionPane.ERROR_MESSAGE);
                
                if (model == this.tableModel) {
                      refreshPlanTable(); 
                    } else {
                      searchMealPlan(); 
                    }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid data or update error: " + e.getMessage(),
                                         "Error", JOptionPane.ERROR_MESSAGE);
            
            if (model == this.tableModel) {
                  refreshPlanTable(); 
                } else {
                  searchMealPlan(); 
                } 
        }
    }

    private void updateMealRemarksFromTable(DefaultTableModel model, int row) {
        if (currentPlanForManagement == null) return;
        
        try {
            int planId = currentPlanForManagement.getPlan_id();
            int mealId = (int) model.getValueAt(row, 0);
            String newRemarks = model.getValueAt(row, 3).toString();
            
            // ⚠️ Controller and DAO must have a method like updateMealPlanRemarks(planId, mealId, remarks)
            if (controller.updateMealPlanRemarks(planId, mealId, newRemarks)) {
                
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update remarks.", "Database Error", JOptionPane.ERROR_MESSAGE);
                loadManageMealsTables(); // Reload to revert the change
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating remarks: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            loadManageMealsTables(); // Reload to revert the change
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
            
            // 1. Fetch the meals for the selected plan using the Controller
            // ⚠️ Assuming the Meal object returned here now includes the REMARKS field
            List<Meal> mealsInPlan = controller.getMealsForPlan(planID);
            
            // 2. Build the meal details string
            StringBuilder mealDetails = new StringBuilder("Meals in this Plan:\n\n");
            
            if (mealsInPlan.isEmpty()) {
                mealDetails.append("  (No meals currently assigned to this plan.)");
            } else {
                for (Meal meal : mealsInPlan) {
                    // Formats the meal data neatly
                    mealDetails.append(String.format(" - %s (ID: %d)\n Price: $%.2f | Calories: %d\n",
                        meal.getMeal_id(), 
                        meal.getPrice(), 
                        meal.getCalories()
                    ));
                }
            }
            
            // 3. Format Plan Details and combine with Meal Details
            String planInfo = String.format("PLAN INFO:\n--------------------------------\nID: %d\nName: %s\nPrice: $%.2f\nDescription: %s\n\n",
                plan.getPlan_id(),
                plan.getPlan_name(),
                plan.getTotal_price(),
                plan.getDescription());
            
            JTextArea textArea = new JTextArea(planInfo + mealDetails.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            
            JScrollPane scrollPane = new JScrollPane(textArea);
            // Set a reasonable size for the dialog content
            scrollPane.setPreferredSize(new Dimension(450, 350));
            
            JOptionPane.showMessageDialog(this, scrollPane, "Meal Plan Details (" + plan.getPlan_name() + ")", 
                                         JOptionPane.INFORMATION_MESSAGE);
        } else {
              JOptionPane.showMessageDialog(this, "Failed to retrieve plan details.",
                                         "Error", JOptionPane.ERROR_MESSAGE);
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

    // NEW METHOD: Switches to the Manage Meals tab and loads the selected plan
    private void goToManageMealsTab() {
        int selectedRow = planTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a meal plan to manage its meals.",
                                         "No Selection", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int planID = (int) tableModel.getValueAt(selectedRow, 0);
        MealPlan plan = controller.getMealPlanDetails(planID); 
        
        if (plan != null) {
            // 1. Store the plan currently being edited
            this.currentPlanForManagement = plan;
            
            // 2. Update the UI content
            manageMealsTitleLabel.setText("Managing Meals for Plan: " + plan.getPlan_name() + " (ID: " + plan.getPlan_id() + ")");
            loadManageMealsTables(); 
            
            // 3. Enable the tab
            tabbedPane.setEnabledAt(3, true); 
            
            // 4. Switch to the new tab (index 3)
            tabbedPane.setSelectedIndex(3); 
        } else {
              JOptionPane.showMessageDialog(this, "Failed to load plan details.",
                                         "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // NEW METHOD: Loads data into both Available and Current tables
    private void loadManageMealsTables() {
        if (currentPlanForManagement == null) return;
        
        // Clear models
        availableMealsModel.setRowCount(0);
        currentMealsModel.setRowCount(0);

        // Fetch data via Controller (Requires controller to have getAllMeals())
        List<Meal> allMeals = controller.getAllMeals(); 
        // ⚠️ ASSUMING getMealsForPlan returns Meal objects that include the REMARKS field
        List<Meal> mealsInPlan = controller.getMealsForPlan(currentPlanForManagement.getPlan_id());

        // Get the IDs of meals currently in the plan for filtering
        List<Integer> currentMealIds = mealsInPlan.stream()
            .map(Meal::getMeal_id)
            .collect(Collectors.toList());

        // Populate the Current Meals Table (Now includes Remarks)
        for (Meal meal : mealsInPlan) {
            // 🛠️ MODIFIED: Added meal.getRemarks() to the row data
            currentMealsModel.addRow(new Object[]{meal.getMeal_id(), meal.getMeal_name(), meal.getPrice()}); 
        }

        // Populate the Available Meals Table (Only meals NOT in the plan)
        for (Meal meal : allMeals) {
            if (!currentMealIds.contains(meal.getMeal_id())) {
                availableMealsModel.addRow(new Object[]{meal.getMeal_id(), meal.getMeal_name(), meal.getPrice()});
            }
        }
    }
    
    // NEW METHOD: Handles adding a selected meal (Now prompts for remarks)
    private void addMealToPlan() {
        if (currentPlanForManagement == null) return;
        int selectedRow = availableMealsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an available meal to add.");
            return;
        }

        int mealId = (int) availableMealsModel.getValueAt(selectedRow, 0);
        String mealName = (String) availableMealsModel.getValueAt(selectedRow, 1);
        
        // 🛠️ NEW: Prompt user for remarks
        String remarks = JOptionPane.showInputDialog(this, 
            "Enter remarks for adding '" + mealName + "':", 
            "Add Meal Remarks", JOptionPane.QUESTION_MESSAGE);
            
        // Handle Cancel or empty/null input gracefully
        if (remarks == null) { 
            return; // User cancelled
        }
        if (remarks.trim().isEmpty()) {
            remarks = ""; // Store as empty string if user left it blank
        }

        // ⚠️ Requires controller to have addMealToPlan(mealId, planId, remarks)
        if (controller.addMealtoPlan(mealId, currentPlanForManagement.getPlan_id(), remarks)) { 
            JOptionPane.showMessageDialog(this, "Meal added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadManageMealsTables(); // Reload tables to update the lists
            refreshPlanTable(); // Also refresh the main table as total price might change
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add meal. It might already be in the plan.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // NEW METHOD: Handles removing a selected meal
    private void removeMealFromPlan() {
        if (currentPlanForManagement == null) return;
        int selectedRow = currentMealsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a meal in the current plan to remove.");
            return;
        }

        int mealId = (int) currentMealsModel.getValueAt(selectedRow, 0);

        // Requires controller to have removeMealFromPlan(mealId, planId)
        if (controller.removeMealFromPlan(mealId, currentPlanForManagement.getPlan_id())) {
            JOptionPane.showMessageDialog(this, "Meal removed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadManageMealsTables(); // Reload tables to update the lists
            refreshPlanTable(); // Also refresh the main table as total price might change
        } else {
            JOptionPane.showMessageDialog(this, "Failed to remove meal.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}