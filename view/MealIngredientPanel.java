package view;

import controller.*;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.util.*;

public class MealIngredientPanel extends JPanel
{
    private MealIngredientController controller;

    // UI Components
    private JTabbedPane tabbedPane;
    private JPanel addPanel;
    private JPanel viewPanel;
    private JPanel searchPanel;

    // Components for adding ingredients
    private JTextField meal_id;
    private JTextField ingredient_id;
    private JTextField quantity;
    private JButton addButton;

    // Components for viewing meal ingredients
    private JTable mealIngredientTable;
    private DefaultTableModel tableModel;
    private JButton refreshButton;

    // Components for searching meals and their ingredients
    private JButton searchButton;
    private JComboBox<String> searchTypeComboBox;
    private JTextField searchField;
    private JTable searchResultTable;
    private DefaultTableModel searchTableModel;

    // Button to go back to main menu
    private JButton mainMenuButton;

    public MealIngredientPanel (MealIngredientController mealIngredientController)
    {
        this.controller = mealIngredientController;

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
        
        tabbedPane.addTab("Add Record", addPanel);
        tabbedPane.addTab("View All", viewPanel);
        tabbedPane.addTab("Search", searchPanel);
        
        add(tabbedPane, BorderLayout.CENTER);
    }

    // Create the panel for adding a meal ingredient record
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
        meal_id = new JTextField(10);
        ingredient_id = new JTextField(10);
        quantity = new JTextField(10);

        // x and y refer to row and column respectively
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Meal ID:"), gbc);
        gbc.gridx = 1;
        formPanel.add(meal_id, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Ingredient ID:"), gbc);
        gbc.gridx = 1;
        formPanel.add(ingredient_id, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Quantity:"), gbc);
        gbc.gridx = 1;
        formPanel.add(quantity, gbc);

        // Create button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        mainMenuButton = new JButton("Return to Main Menu");
        mainMenuButton.addActionListener(e -> {
            SwingUtilities.getWindowAncestor(this).dispose();
            new AdminMainMenu().setVisible(true);
        });
        addButton = new JButton("Add Record");
        addButton.addActionListener(e -> addMealIngredient());
        
        buttonPanel.add(mainMenuButton);
        buttonPanel.add(addButton);
        
        // Add panels to the main panel
        addPanel.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        addPanel.add(buttonPanel, BorderLayout.SOUTH);
    }    

    // Add a new meal ingredient using the input fields
    private void addMealIngredient() 
    {
        try 
        {
            int mealID = Integer.parseInt(meal_id.getText().trim());
            int ingredientID = Integer.parseInt(ingredient_id.getText().trim());
            double qty = Double.parseDouble(quantity.getText().trim());          

            controller.addMealIngredient(mealID, ingredientID, qty);

            JOptionPane.showMessageDialog(this, "Meal Ingredient added successfully!");
            clearAddFields();
            refreshMealIngredientTable();

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
        meal_id.setText("");
        ingredient_id.setText("");
        quantity.setText("");
    }

    // Create the panel for viewing all meals and their ingredients
    private void createViewPanel()
    {
        viewPanel = new JPanel();
        viewPanel.setLayout(new BorderLayout());
        viewPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        String[] columnNames = {
            "Meal ID", "Ingredient ID", "Quantity"
        };

        // create a view-only table of the meal ingredients
        tableModel = new DefaultTableModel(columnNames, 0) 
        {
            @Override
            public boolean isCellEditable(int row, int column) 
            {
                return false;
            }
        };

        mealIngredientTable = new JTable(tableModel);
        mealIngredientTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mealIngredientTable.getTableHeader().setReorderingAllowed(false);

        // Create button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshMealIngredientTable());
        
        // no view details

        buttonPanel.add(refreshButton);
        
        viewPanel.add(new JScrollPane(mealIngredientTable), BorderLayout.CENTER);
        viewPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Populate the table
        refreshMealIngredientTable();
    }

    // refresh table with newly added ingredient(s)
    private void refreshMealIngredientTable() 
    {
        tableModel.setRowCount(0); // Clear table

        // get all ingredients
        ArrayList<MealIngredient> mealIngredients = controller.getAllMealIngredients();
        
        // populate table with updated data
        for (MealIngredient mi : mealIngredients) {
            Object[] row = new Object[]
            {
                mi.getMeal_id(),
                mi.getIngredient_id(),
                mi.getQuantity()                
            };

            tableModel.addRow(row);
        }
    }

    // Create the panel for searching meal ingredients
    private void createSearchPanel() 
    {
        searchPanel = new JPanel();
        searchPanel.setLayout(new BorderLayout());
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create search panel
        JPanel searchControls = new JPanel();
        searchControls.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        searchTypeComboBox = new JComboBox<>(new String[] {
            "By Meal ID", "By Ingredient ID"
        });

        searchField = new JTextField(15);
        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchMealIngredient());

        searchControls.add(new JLabel("Search:"));
        searchControls.add(searchTypeComboBox);
        searchControls.add(searchField);
        searchControls.add(searchButton);

        String[] columnNames = {
            "Meal ID", "Ingredient ID", "Quantity"
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
        
        // no view details

        searchPanel.add(searchControls, BorderLayout.NORTH);
        searchPanel.add(new JScrollPane(searchResultTable), BorderLayout.CENTER);
        searchPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    // Search for an ingredient record based on the selected criteria
    private void searchMealIngredient() {
        searchTableModel.setRowCount(0); // Clear previous results

        String searchType = (String) searchTypeComboBox.getSelectedItem();
        String query = searchField.getText().trim();

        ArrayList<MealIngredient> results = new ArrayList<>();

        try {
            switch (searchType) {
                case "By Meal ID":
                    int mealId = Integer.parseInt(query);
                    results = controller.getIngredientsInMeal(mealId);
                    break;
                case "By Ingredient ID":
                    int ingredientId = Integer.parseInt(query);
                    results = controller.getMealsHavingIngredient(ingredientId);
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
        for (MealIngredient mi : results) {
            Object[] row = new Object[]
            {
                mi.getMeal_id(),
                mi.getIngredient_id(),
                mi.getQuantity()                
            };

            searchTableModel.addRow(row);
        }

        if (results.isEmpty()) 
        {
            JOptionPane.showMessageDialog(this, "No results found matching the search criteria.",
                                        "No Results", JOptionPane.INFORMATION_MESSAGE);
        }

    }

    
}
