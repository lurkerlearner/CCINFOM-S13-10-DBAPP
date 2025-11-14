package model;

public class MealIngredient {
    private int meal_id; // FK to meal table
    private int ingredient_id; // FK to ingredient table
    private double quantity;
    private String ingredient_name; // Added field for ingredient name

    public MealIngredient(int meal_id, int ingredient_id, double quantity){
        this.meal_id = meal_id;
        this.ingredient_id = ingredient_id;
        this.quantity = quantity;
    }

    public MealIngredient(int meal_id, int ingredient_id, double quantity, String ingredient_name){
        this.meal_id = meal_id;
        this.ingredient_id = ingredient_id;
        this.quantity = quantity;
        this.ingredient_name = ingredient_name;
    }

    public MealIngredient() {
        // Default constructor
    }

    public int getMeal_id() {
        return meal_id;
    }

    public int getIngredient_id() {
        return ingredient_id;
    }

    public double getQuantity() {
        return quantity;
    }

    private String getIngredient_name() {
        return ingredient_name;
    }
    
    public void setMeal_id(int meal_id) {
        this.meal_id = meal_id;
    }

    public void setIngredient_id(int ingredient_id) {
        this.ingredient_id = ingredient_id;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    private void setIngredient_name(String ingredient_name) {
        this.ingredient_name = ingredient_name;
    }
    
}