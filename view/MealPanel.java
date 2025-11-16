package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.TableModelEvent;

import java.util.ArrayList;
import java.util.List;

import controller.MealController;
import model.Meal;

import javax.swing.table.DefaultTableModel;

import controller.MealController;
import model.Meal;


public class MealPanel  extends JPanel {
    
    private final MealController controller;
    private JTabbedPane tabbedPane;
    private JPanel addPanel;
    private JPanel viewPanel;
    private JPanel searchPanel;
    private JComboBox<String> searchTypeComboBox;
    private JTextField searchField;
    private JTable searchResultTable;
    private DefaultTableModel searchTableModel;


    private JTextField mealName;
    private JTextField price;
    private JTextField cost;
    private JTextField prep_time;
    private JTextField calories;
    private JTextField nutrients;
    private JTextField date_added;
    private JTextField diet_preference_id;
    private JButton addButton;

    private JTable mealTable;
    private DefaultTableModel tableModel;

   // return to main menu
    private JButton mainMenuButton;

    public MealPanel(MealController controller) 
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
        
        tabbedPane.addTab("Add Meal", addPanel);
        tabbedPane.addTab("View All", viewPanel);
        tabbedPane.addTab("Search", searchPanel);
        
        add(tabbedPane, BorderLayout.CENTER);
    }


    private void createAddPanel(){

        addPanel = new JPanel();
        addPanel.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        addPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        mealName = new JTextField(20);
        price = new JTextField(10);
        cost = new JTextField(10);
        prep_time = new JTextField(5);
        calories = new JTextField(5);
        nutrients = new JTextField(20);
        date_added = new JTextField(10);
        diet_preference_id = new JTextField(5);

        int y = 0;

        gbc.gridx = 0;
        gbc.gridy = y++;
        formPanel.add(new JLabel("Meal Name:"), gbc);

        gbc.gridx = 1;
        formPanel.add(mealName, gbc);

        gbc.gridx = 0;
        gbc.gridy = y++;
        formPanel.add(new JLabel("Price:"), gbc);

        gbc.gridx = 1;
        formPanel.add(price, gbc);

        gbc.gridx = 0;
        gbc.gridy = y++;
        formPanel.add(new JLabel("Cost:"), gbc);

        gbc.gridx = 1;
        formPanel.add(cost, gbc);

        gbc.gridx = 0;
        gbc.gridy = y++;
        formPanel.add(new JLabel("Preparation Time (mins):"), gbc);

        gbc.gridx = 1;
        formPanel.add(prep_time, gbc);

        gbc.gridx = 0;
        gbc.gridy = y++;
        formPanel.add(new JLabel("Calories:"), gbc);

        gbc.gridx = 1;
        formPanel.add(calories, gbc);

        gbc.gridx = 0;
        gbc.gridy = y++;
        formPanel.add(new JLabel("Nutrients:"), gbc);

        gbc.gridx = 1;
        formPanel.add(nutrients, gbc);

        gbc.gridx = 0;
        gbc.gridy = y++;
        formPanel.add(new JLabel("Date Added (YYYY-MM-DD):"), gbc);

        gbc.gridx = 1;
        formPanel.add(date_added, gbc);

        gbc.gridx = 0;
        gbc.gridy = y++;
        formPanel.add(new JLabel("Diet Preference ID:"), gbc);

        gbc.gridx = 1;
        formPanel.add(diet_preference_id, gbc);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        mainMenuButton = new JButton("Return to Main Menu");
        mainMenuButton.addActionListener(e -> {
            SwingUtilities.getWindowAncestor(this).dispose();
            new AdminMainMenu().setVisible(true);
        });
        addButton = new JButton("Add Meal");
        addButton.addActionListener(e -> addMeal());

        buttonPanel.add(mainMenuButton);
        buttonPanel.add(addButton);
        
        addPanel.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        addPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        
    }
    private void addMeal() {

        try
        {

            String name = mealName.getText().trim();
            float price = Float.parseFloat(this.price.getText().trim());
            float cost = Float.parseFloat(this.cost.getText().trim());
            int prepTime = Integer.parseInt(this.prep_time.getText().trim());
            int calories = Integer.parseInt(this.calories.getText().trim());
            String nutrients = this.nutrients.getText().trim();
            String dateAdded = this.date_added.getText().trim();
            int dietPrefId = Integer.parseInt(this.diet_preference_id.getText().trim());

            String result = controller.addMeal(name, price, cost, prepTime, calories, nutrients, dateAdded, dietPrefId);

            if(result.equals("SUCCESS")){
                JOptionPane.showMessageDialog(this, "Meal added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearAddFields();
                refreshMealTable();
            }
            else{
                JOptionPane.showMessageDialog(this, "Failed to add meal: " + result, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }  
        catch (NumberFormatException e) 
            {
             JOptionPane.showMessageDialog(this, "Please check numeric fields (Price, Cost, Prep Time, Calories).", 
                                      "Invalid Input", JOptionPane.ERROR_MESSAGE);
            } 
            catch (IllegalArgumentException e) 
            {
                JOptionPane.showMessageDialog(this, "Date Added must be in YYYY-MM-DD format.", 
                                      "Invalid Date", JOptionPane.ERROR_MESSAGE);
            }
        }

    private void createViewPanel() {
        viewPanel = new JPanel();
        viewPanel.setLayout(new BorderLayout());
        
        String[] columnNames = {"Meal ID", "Name", "Price", "Cost", "Nutrients",
                                "Calories", "Preparation Time", "Date Added", "Diet Preference ID"};
        
            tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) 
            {
                return column !=0; 
            }
    };
        mealTable = new JTable(tableModel);

        tableModel.addTableModelListener(e -> {
            if (e.getType()== TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int column = e.getColumn();

                if (column != 0) {
                    updateMealTable(tableModel, row, column);
                }
            }
        }); 

        JScrollPane scrollPane = new JScrollPane(mealTable);
        viewPanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel();
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadAllMeals());
        buttonPanel.add(refreshButton);
        viewPanel.add(buttonPanel, BorderLayout.SOUTH);

        loadAllMeals();
    }
    private void loadAllMeals(){
        tableModel.setRowCount(0); 
        List<Meal> meals = controller.getAllMeals();
        for(Meal meal: meals)
        {
            Object[] row = new Object[]{
                meal.getMeal_id(),
                meal.getMeal_name(),
                meal.getPrice(),
                meal.getCost(),
                meal.getNutrients(),
                meal.getCalories(),
                meal.getPreparation_time(),
                meal.getDate_added(),
                meal.getDiet_preference_id()
            };
            tableModel.addRow(row);
        }
    }


    private void createSearchPanel(){
        searchPanel = new JPanel();
        searchPanel.setLayout(new BorderLayout());
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel searchControls = new JPanel();
        searchControls.setLayout(new FlowLayout(FlowLayout.LEFT));

        searchTypeComboBox = new JComboBox<>(new String[] {
            "By Meal ID","By Diet Preference ID"});

        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchMeal());

        searchControls.add(new JLabel("Search By:"));
        searchControls.add(searchTypeComboBox);
        searchControls.add(searchField);
        searchControls.add(searchButton);

        searchPanel.add(searchControls, BorderLayout.NORTH);
        String[] columnNames = {"Meal ID", "Name", "Price", "Cost", "Nutrients",
                                "Calories", "Preparation Time", "Date Added", "Diet Preference ID"};
        searchTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) 
            {
                return column !=0; 
            }
        };

        searchResultTable = new JTable(searchTableModel);
        
        searchTableModel.addTableModelListener(e -> {
            if (e.getType()== TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int column = e.getColumn();

                if (column != 0) {
                    updateMealTable(tableModel, row, column);
                }
            }
        }); 
        searchResultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        searchResultTable.getTableHeader().setReorderingAllowed(false);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton viewDetailsButton = new JButton("View Details");
        viewDetailsButton.addActionListener(e -> showSearchResultDetails());
        buttonPanel.add(viewDetailsButton);

        searchPanel.add(searchControls, BorderLayout.NORTH);
        searchPanel.add(new JScrollPane(searchResultTable), BorderLayout.CENTER);
        searchPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void clearAddFields(){
        mealName.setText("");
        price.setText("");
        cost.setText("");
        prep_time.setText("");
        calories.setText("");
        nutrients.setText("");
        date_added.setText("");
        diet_preference_id.setText("");
    }


    private void refreshMealTable(){

        tableModel.setRowCount(0); 
        List<Meal> meals = controller.getAllMeals();
        for(Meal meal: meals)
        {
            Object[] row = new Object[]{
                meal.getMeal_id(),
                meal.getMeal_name(),
                meal.getPrice(),
                meal.getCost(),
                meal.getNutrients(),
                meal.getCalories(),
                meal.getPreparation_time(),
                meal.getDate_added(),
                meal.getDiet_preference_id()
            };
            tableModel.addRow(row);
        }


    }

    private void searchMeal(){

        String searchType = (String) searchTypeComboBox.getSelectedItem();
        String searchText = searchField.getText().trim();
        List<Meal> results = new ArrayList<>();

        try {

            switch(searchType){

                case "By Meal ID":
                    int mealId = Integer.parseInt(searchText);
                    Meal meal = controller.getMealDetails(mealId);
                    if (meal != null) {
                        results.add(meal);
                    }
                    break;
                case "By Diet Preference ID":
                    int dietPrefId = Integer.parseInt(searchText);
                    results = controller.getFilteredMeals(dietPrefId);
                    break;
                default:
                    JOptionPane.showMessageDialog(this, "Invalid search type selected.", "Search Error", JOptionPane.ERROR_MESSAGE);
                    return;
            }
        }

        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for search.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        catch (IllegalArgumentException e) 
        {
            JOptionPane.showMessageDialog(this, "Please enter a valid status.", 
                                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for(Meal meal: results)
        {
            Object[] row = new Object[]{
                meal.getMeal_id(),
                meal.getMeal_name(),
                meal.getPrice(),
                meal.getCost(),
                meal.getNutrients(),
                meal.getCalories(),
                meal.getPreparation_time(),
                meal.getDate_added(),
                meal.getDiet_preference_id()
            };
            searchTableModel.addRow(row);
        }
        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No meals found matching the criteria.", "No Results", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showSearchResultDetails(){

        int selectedRow = searchResultTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a meal from the search results.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int mealId = (int) searchTableModel.getValueAt(selectedRow, 0);
        Meal meal = controller.getMealDetails(mealId);

        if(meal != null){

            JTextArea textArea = new JTextArea(meal.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300));

            JOptionPane.showMessageDialog(this, scrollPane, "Meal Details", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void updateMealTable(DefaultTableModel model, int row, int column) {
        int mealId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());

        try {
            String name = (String) tableModel.getValueAt(row, 1);
            float price = Float.parseFloat(tableModel.getValueAt(row, 2).toString());
            float cost = Float.parseFloat(tableModel.getValueAt(row, 3).toString());
            String nutrients = (String) tableModel.getValueAt(row, 4);
            int calories = Integer.parseInt(tableModel.getValueAt(row, 5).toString());
            int prepTime = Integer.parseInt(tableModel.getValueAt(row, 6).toString());
            String dateAdded = (String) tableModel.getValueAt(row, 7);
            int dietPrefId = Integer.parseInt(tableModel.getValueAt(row, 8).toString());

            String result = controller.updateMeal(mealId, name, price, cost, prepTime, calories, nutrients, dateAdded, dietPrefId);
    
           if ("SUCCESS".equals(result)) {
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update Meal ID " + mealId + ": " + result, "Database Error", JOptionPane.ERROR_MESSAGE);
                
                if (tableModel == this.tableModel) {
                     loadAllMeals(); 
                } else {
                     searchMeal(); 
                }
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Input Error: Invalid number format for Price, Cost, Calories, Prep Time, or Diet Preference ID.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            
            if (tableModel == this.tableModel) {
                 loadAllMeals(); 
            } else {
                 searchMeal(); 
            }
        }
    }
}