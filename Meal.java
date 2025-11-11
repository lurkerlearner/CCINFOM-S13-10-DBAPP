package model;


public class Meal {
    private int meal_id;
    private String meal_name;
    private float price;
    private float cost;
    private int preparation_time; //in minutes
    private int calories;
    private String nutrients;
    private String date_added;
    private int diet_preference_id;
    private int ingredient_id;



    public Meal(int meal_id, String meal_name, float price, float cost, int preparation_time,
                int calories, String nutrients, String date_added, int diet_preference_id, int ingredient_id){

        this.meal_id = meal_id;
        this.meal_name = meal_name;
        this.price = price;
        this.cost = cost;
        this.preparation_time = preparation_time;
        this.calories = calories;
        this.nutrients = nutrients;
        this.date_added = date_added;
        this.diet_preference_id = diet_preference_id;
        this.ingredient_id = ingredient_id;
    }
    public int getMeal_id(){
        return meal_id;
    }

    public String getMeal_name() {
        return meal_name;
    }

    public float getPrice() {
        return price;
    }

    public float getCost() {
        return cost;
    }

    public int getPreparation_time() {
        return preparation_time;
    }

    public int getCalories() {
        return calories;
    }

    public String getNutrients() {
        return nutrients;
    }

    public String getDate_added() {
        return date_added;
    }

    public int getDiet_preference_id() {
        return diet_preference_id;
    }
    public int getIngredient_id() {
        return ingredient_id;
    }

    public void setPrice(float price) {
        this.price = price;
    }
    public void setCost(float cost) {
        this.cost = cost;
    }

    public void setMeal_name(String meal_name) {
        this.meal_name = meal_name;
    }
    public void setCalories(int calories) {
        this.calories = calories;
    }
    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }
    public void setDiet_preference_id(int diet_preference_id) {
        this.diet_preference_id = diet_preference_id;
    }
    public void setIngredient_id(int ingredient_id) {
        this.ingredient_id = ingredient_id;
    }
    public void setNutrients(String nutrients) {
        this.nutrients = nutrients;
    }

    public void setPreparation_time(int preparation_time) {
        this.preparation_time = preparation_time;
    }

}
