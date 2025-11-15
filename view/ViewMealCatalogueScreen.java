package view;

import DAO.MealIngredientDAO;
import DAO.IngredientDAO;
import DAO.LocationDAO;
import DAO.DietPreferenceDAO;
import controller.MealController;
import model.Meal;
import model.Client;


import javax.swing.*;
import java.awt.*;
import java.util.List;


public class ViewMealCatalogueScreen extends JFrame {

private final MealController controller;
private final MealIngredientDAO mealIngredientDAO;
private final IngredientDAO ingredientDAO;
private final LocationDAO locationDAO;
private final DietPreferenceDAO dietPreferenceDAO;
private Client client;
private List<Meal> currentMeals;


private JTextField searchField;
private JComboBox<String> sortComboBox;
private JComboBox<String> filterComboBox;
private JPanel mealDisplayPanel; 

    public ViewMealCatalogueScreen(MealController controller, MealIngredientDAO mealIngredientDAO, IngredientDAO ingredientDAO, Client client, LocationDAO locationDAO, DietPreferenceDAO dietPreferenceDAO){
        this.controller = controller;
        this.mealIngredientDAO = mealIngredientDAO;
        this.ingredientDAO = ingredientDAO;
        this.client = client;
        this.locationDAO = locationDAO;
        this.dietPreferenceDAO = dietPreferenceDAO;

        setTitle("FloodPanda - Meal Catalogue");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        createHeader();

        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        createSearchFilterPanel(contentPanel);
        createMealDisplay(contentPanel);

        add(contentPanel, BorderLayout.CENTER);
        loadAndDisplayMeals(controller.getAllMeals());
    }

    private void createHeader(){
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.setBackground(Color.WHITE);

        // Logo style matching AccountManagementScreen
        JLabel logo = new JLabel("FloodPanda", SwingConstants.LEFT);
        logo.setFont(new Font("Arial", Font.BOLD, 30));
        logo.setForeground(new Color(220, 31, 127));
        topPanel.add(logo, BorderLayout.WEST);
        

        JButton backButton = new JButton("Back to Main");
        backButton.addActionListener(e -> {dispose();  
        new ClientMainMenu(client, locationDAO.getLocationById(client.getLocationID())).setVisible(true); 
        });
        topPanel.add(backButton, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);
    }

private void createSearchFilterPanel(JPanel parentPanel){
    JPanel controlPanel = new JPanel();
    controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
    controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 30));
    controlPanel.setPreferredSize(new Dimension(250, 0));

    controlPanel.add(new JLabel("Search Meal Catalogue"));
    controlPanel.add(Box.createVerticalStrut(10));

    JPanel searchInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
    searchInputPanel.add(new JLabel("Search: "));
    searchField= new JTextField(15);
    searchInputPanel.add(searchField);

    controlPanel.add(searchInputPanel);
    controlPanel.add(Box.createVerticalStrut(20));

    controlPanel.add(new JLabel("Sort By Price: "));
    sortComboBox = new JComboBox<>(new String[]{"Low to High", "High to Low"});
    controlPanel.add(sortComboBox);
    controlPanel.add(Box.createVerticalStrut(20));

    controlPanel.add(new JLabel("Filter By Diet Preference: "));
    filterComboBox = new JComboBox<>(new String[]{"All", "Vegetarian", "Vegan", "Gluten-Free", "Keto", "High Protein", "Diabetic-Friendly" +
                                     "Low Fat", "Low Sodium", "Mediterranean", "Paleo"});
    controlPanel.add(filterComboBox);
    controlPanel.add(Box.createVerticalStrut(20));

 
    JButton applyButton = new JButton("Apply Changes");
    applyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    applyButton.setBackground(new Color(220, 31, 127)); 
    applyButton.setForeground(Color.WHITE);
    applyButton.setFont(new Font("Arial", Font.BOLD, 14));
    

    applyButton.addActionListener(e -> updateDisplay());

    controlPanel.add(applyButton);
    controlPanel.add(Box.createVerticalStrut(10)); 

    controlPanel.add(Box.createVerticalGlue());


    parentPanel.add(controlPanel, BorderLayout.WEST);
}


    private void createMealDisplay(JPanel parentPanel){

        mealDisplayPanel = new JPanel();
        mealDisplayPanel.setLayout(new GridLayout(0,2,20,20));

        JScrollPane scrollPane = new JScrollPane(mealDisplayPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        parentPanel.add(scrollPane, BorderLayout.CENTER);
    }

    private void loadAndDisplayMeals(List<Meal> meals){
        mealDisplayPanel.removeAll();
        this.currentMeals = meals;

        if (meals == null || meals.isEmpty()) {
            mealDisplayPanel.add(new JLabel("No meals found"));
            mealDisplayPanel.revalidate();
            mealDisplayPanel.repaint();
            return;            
        }
        for (Meal meal : meals){
            JButton mealButton = new JButton(meal.getMeal_name() + " - ₱" + meal.getPrice());

            mealButton.setPreferredSize(new Dimension(200,60));
            mealButton.setFont(new Font("Arial", Font.BOLD, 16));
            mealButton.setBackground(new Color(255, 214, 221));
            mealButton.setOpaque(true);
            mealButton.setContentAreaFilled(true);
            mealButton.setFocusPainted(true);
            mealButton.setBorder(BorderFactory.createLineBorder(Color.PINK, 2));

            mealButton.addActionListener(e -> showMealDetails(meal));

           
            mealDisplayPanel.add(mealButton);
        }
        mealDisplayPanel.revalidate();
        mealDisplayPanel.repaint();
    }
    private void updateDisplay(){
        String searchTerm = searchField.getText();
        String sortOrder = (String) sortComboBox.getSelectedItem();
        String dietPreference = (String) filterComboBox.getSelectedItem(); 

        List<Meal> updatedMeals = controller.getFilteredAndSortedMeals(searchTerm, sortOrder, dietPreference);

        loadAndDisplayMeals(updatedMeals);
    }

    private void showMealDetails(Meal meal) {
        new MealDetailsScreen(meal, mealIngredientDAO, ingredientDAO);
    }
}
