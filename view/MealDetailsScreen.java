package view;

import DAO.MealIngredientDAO;
import DAO.IngredientDAO;
import model.Meal;
import model.Ingredient;
import model.MealIngredient;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class MealDetailsScreen extends JFrame {

    private final Meal meal;
    private final MealIngredientDAO mealIngredientDAO;
    private final IngredientDAO ingredientDAO;

    public MealDetailsScreen(Meal meal, MealIngredientDAO mealIngredientDAO, IngredientDAO ingredientDAO) {
        this.meal = meal;
        this.mealIngredientDAO = mealIngredientDAO;
        this.ingredientDAO = ingredientDAO;

        setTitle("Meal Details: " + meal.getMeal_name());
        setSize(700, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        createTopPanel();

        createDetailsPanel();

        setVisible(true);
    }

    private void createTopPanel() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton returnButton = new JButton("Return to other meals");
        returnButton.addActionListener(e -> dispose());
        
        // Use the app's established pink/red styling
        Color appColor = new Color(220, 31, 127);
        returnButton.setBackground(new Color(255, 182, 193)); 
        returnButton.setForeground(appColor);
        returnButton.setBorder(BorderFactory.createLineBorder(appColor, 2));

        topPanel.add(returnButton);
        add(topPanel, BorderLayout.NORTH);
    }

    private void createDetailsPanel() {

        JPanel mainContent = new JPanel(new GridLayout(1, 2, 20, 0));
        mainContent.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));


        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Placeholder for the image
        Dimension imageSize= new Dimension(300, 300);
        String imageName = "default_meal.jpg";



        switch ((meal.getMeal_name().toLowerCase())) {
            case "vegan salad": 
                imageName = "vegan_salad.jpg";
                break;
            case "grilled chicken":
                imageName = "grilled_chicken.jpg";
                break;
            case "salmon bowl":
                imageName = "salmon_bowl.jpg";
                break;
            case "beef stir fry":
                imageName = "beef_stirfry.jpg";
                break;   
            case "quinoa veggie":
                imageName = "quinoa_veggie.jpg";
                break;    
            case "keto omelette":
                imageName = "keto_omelette.jpg";
                break;    
            case "gluten-free pasta":
                imageName = "gluten_free_pasta.jpg";
                break;    
            case "mediterranean bowl":
                imageName = "mediterranean_bowl.jpg";
                break;    
            case "vegetarian pizza":
                imageName = "vegetarian_pizza.jpg";
                break;    
            case "diabetic salad":
                imageName = "diabetic_salad.jpg";
                break;    
            case "protein shake":
                imageName = "protein_shake.jpg";
                break;    
            default:
                imageName = "default_meal.jpg";
                break;
        }

        String imagePath = "resources/" + imageName;

            JLabel imageLabel; 
        
        try{
            File imgFile = new File(imagePath);
            if (!imgFile.exists()) {
                throw new FileNotFoundException("Image file not found at path: " + imagePath);
            }

            ImageIcon originalIcon = new ImageIcon(imagePath);
            Image originalImage = originalIcon.getImage();

            Image scaledImage = originalImage.getScaledInstance(
                imageSize.width, 
                imageSize.height, 
                Image.SCALE_SMOOTH
            );

            // 4. Create the final JLabel with the scaled image
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            imageLabel = new JLabel(scaledIcon, SwingConstants.CENTER);

        } catch(Exception e){
            // Fallback for any error (FileNotFound or general I/O error)
            imageLabel = new JLabel("Image Not Available", SwingConstants.CENTER);
            imageLabel.setForeground(Color.RED);
            System.err.println("Error loading image for " + meal.getMeal_name() + ": " + e.getMessage());
        }

        imageLabel.setPreferredSize(imageSize);
        imageLabel.setMaximumSize(imageSize);
        imageLabel.setMinimumSize(imageSize);
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Meal Name Button (styled like the meal catalogue button)
        JLabel mealNameLabel = new JLabel(meal.getMeal_name());
        Color appColor = new Color(220, 31, 127);
        // mealNameLabel.setPreferredSize(new Dimension(200, 40));
        // mealNameLabel.setBackground(new Color(240, 240, 240));
        // mealNameLabel.setBorder(BorderFactory.createLineBorder(appColor, 2));
        // mealNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        // mealNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);


        mealNameLabel.setPreferredSize(new Dimension(200,60));
        mealNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        mealNameLabel.setBackground(new Color(255, 214, 221));
        mealNameLabel.setOpaque(true);
        mealNameLabel.setBorder(BorderFactory.createLineBorder(Color.PINK, 2));
        mealNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        leftPanel.add(imageLabel);
        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(mealNameLabel);

        // --- RIGHT COLUMN: Meal Information and Ingredients ---
        JPanel rightPanel = new JPanel(new BorderLayout());
        
        // Top section for main meal info (using GridBagLayout for alignment)
        JPanel infoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        int row = 0;
        
        // 1. Meal ID
        gbc.gridx = 0; gbc.gridy = row++; infoPanel.add(new JLabel("Meal ID:"), gbc);
        gbc.gridx = 1; infoPanel.add(new JLabel(String.valueOf(meal.getMeal_id())), gbc);
        
        // 2. Price
        gbc.gridx = 0; gbc.gridy = row++; infoPanel.add(new JLabel("Price:"), gbc);
        gbc.gridx = 1; infoPanel.add(new JLabel(String.format("PHP %.2f", meal.getPrice())), gbc);
        
        // 3. Calories
        gbc.gridx = 0; gbc.gridy = row++; infoPanel.add(new JLabel("Calories:"), gbc);
        gbc.gridx = 1; infoPanel.add(new JLabel(meal.getCalories() + " kcal"), gbc);
        
        // 4. Nutrients
        gbc.gridx = 0; gbc.gridy = row++; infoPanel.add(new JLabel("Nutrients:"), gbc);
        gbc.gridx = 1; infoPanel.add(new JLabel(meal.getNutrients()), gbc);

        // 5. Ingredients Label
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2; infoPanel.add(new JLabel("<html><br><b>Ingredients:</b></html>"), gbc);
        
        rightPanel.add(infoPanel, BorderLayout.NORTH);
        
        // Bottom section for scrollable ingredient list
        JTextArea ingredientArea = new JTextArea(fetchIngredientDetails());
        ingredientArea.setEditable(false);
        ingredientArea.setWrapStyleWord(true);
        ingredientArea.setLineWrap(true);
        ingredientArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(ingredientArea);
        scrollPane.setPreferredSize(new Dimension(250, 150));
        
        rightPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Add panels to the main content
        mainContent.add(leftPanel);
        mainContent.add(rightPanel);
        
        add(mainContent, BorderLayout.CENTER);
    }
    

    private String fetchIngredientDetails() {
        List<MealIngredient> mealIngredients = mealIngredientDAO.getIngredientsByMealId(meal.getMeal_id()); 
        StringBuilder sb = new StringBuilder();
        
        if (mealIngredients == null || mealIngredients.isEmpty()) {
            return "No ingredients listed.";
        }
        
        for (MealIngredient mi : mealIngredients) {
            Ingredient ingredient = ingredientDAO.getIngredientById(mi.getIngredient_id());
            
            if (ingredient != null) {
                sb.append(String.format("%.2f %s (%s)\n", 
                    mi.getQuantity(), 
                    ingredient.getMeasurement_unit().name(), 
                    ingredient.getIngredient_name()));
            }
        }
        
        return sb.toString();
    }
}