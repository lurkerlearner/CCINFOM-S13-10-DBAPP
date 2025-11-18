package controller;

import model.*;
import DAO.MealDAO;
import DAO.IngredientDAO;
import DAO.MealIngredientDAO;
import DAO.MealMealPlanDAO;
import DAO.DietPreferenceDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MealController {

    private final MealDAO mealDAO;
    private final MealMealPlanDAO mealMealPlanDAO;
    private final IngredientDAO ingredientDAO;
    private final MealIngredientDAO mealIngredientDAO;
    private final DietPreferenceDAO dietPreferenceDAO;

    public MealController() {
        this.mealDAO = new MealDAO();
        this.mealMealPlanDAO = new MealMealPlanDAO();
        this.ingredientDAO = new IngredientDAO();
        this.mealIngredientDAO = new MealIngredientDAO();
        this.dietPreferenceDAO = new DietPreferenceDAO();
        
    }

    public String addMeal(String name, float price, float cost, int prepTime,
                           int calories, String nutrients, String dateAdded,
                           int dietPreferenceId) {
        
        Meal meal = new Meal();
        meal.setMeal_name(name);
        meal.setPrice(price);
        meal.setCost(cost);
        meal.setPreparation_time(prepTime);
        meal.setCalories(calories);
        meal.setNutrients(nutrients);
        meal.setDate_added(dateAdded);
        meal.setDiet_preference_id(dietPreferenceId);

        boolean added = mealDAO.addMeal(meal);
        return added ? "SUCCESS: Meal added." : "FAILURE: Could not add meal.";
    }


public List<Meal> getFilteredAndSortedMeals(String searchTerm, String sortOrder, String dietPreferenceName) {
        
        List<Meal> meals;
        
  
        if (dietPreferenceName != null && !"All".equalsIgnoreCase(dietPreferenceName)) {
            DietPreference pref = dietPreferenceDAO.getDietPreferenceByName(dietPreferenceName);
            
            if (pref != null) {
                meals = mealDAO.getMealsByDietPreference(pref.getDiet_preference_id());
            } else {

                meals = mealDAO.getAllMeals();
            }
        } else {
            meals = mealDAO.getAllMeals();
        }
        
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            String lowerCaseTerm = searchTerm.trim().toLowerCase();
            meals = meals.stream()
                .filter(meal -> meal.getMeal_name().toLowerCase().contains(lowerCaseTerm))
                .collect(Collectors.toList());
        }
        
        if ("Low to High".equalsIgnoreCase(sortOrder)) {
            return meals.stream()
                .sorted((m1, m2) -> Float.compare(m1.getPrice(), m2.getPrice()))
                .collect(Collectors.toList());
        } else if ("High to Low".equalsIgnoreCase(sortOrder)) {
            return meals.stream()
                .sorted((m1, m2) -> Float.compare(m2.getPrice(), m1.getPrice()))
                .collect(Collectors.toList());
        }
        
        return meals; 
    }

    public Meal getMealDetails(int mealId) {
        return mealDAO.getMealById(mealId);
    }

    public List<Meal> getAllMeals(){
        return mealDAO.getAllMeals();
    }

    public boolean deleteMeal(int mealId) {
        Meal existing = mealDAO.getMealById(mealId);
        if (existing == null) {
            System.err.println("Validation Error: Meal not found.");
            return false;
        }
        if (mealMealPlanDAO.isMealInAnyPlan(mealId)) {
            System.err.println("Validation Error: Meal is part of an active plan.");
            return false;
        }

        return mealDAO.deleteMeal(mealId);
    }

    public String addMealToCatalogue(Meal meal, List<Integer> ingredientIds) {

        if (ingredientIds == null || ingredientIds.isEmpty()) {
            return "FAILURE: A meal must use one or more ingredients. Please associate ingredients.";
        }
        if (meal.getDiet_preference_id() <= 0) {
            return "FAILURE: Meal must be assigned to one main diet preference.";
        }

        if (meal.getPrice() <= meal.getCost()) {

            return "FAILURE: Meal price (" + meal.getPrice() + ") must be greater than or equal to cost (" + meal.getCost() + ").";
        }
        boolean mealAdded = mealDAO.addMeal(meal);

        if (mealAdded) {
            return "SUCCESS: Meal '" + meal.getMeal_name() + "' and ingredients successfully added to catalogue.";
        } else {
            return "FAILURE: Database error occurred while adding the meal record.";
        }
    }

    public List<Meal> getFilteredMeals(int dietPreferenceId) {
        if (dietPreferenceId <= 0) {
            return mealDAO.getAllMeals();
        } else {
            return mealDAO.getMealsByDietPreference(dietPreferenceId);
        }
    }
    
    public List<Meal> getMealsForClient(int clientId) {
        List<Meal> meals = mealDAO.getMealsForClient(clientId);
        if (meals == null || meals.isEmpty()) {
            meals = mealDAO.getAllMeals();
        }
        return meals;
    }
public String updateMeal(int mealId, String name, float price, float cost, int prepTime, 
                         int calories, String nutrients, String dateAdded, int dietPrefId) {

    Meal updatedMeal = new Meal(mealId, name,price,cost,prepTime,calories,nutrients,dateAdded,dietPrefId);

    // 2. Call the DAO method
    if (mealDAO.updateMeal(updatedMeal)) {
        return "SUCCESS";
    } else {
        return "Failed to update meal in database."; 
    }
}


    public List<MealPerformance> getMealPerformanceByMonthYear(int year, int month) {
        return mealDAO.getMealPerformanceByMonthYear(year, month);
    }

    public List<MealPerformance> getMealPerformaceByYear(int year) {
        return mealDAO.getMealPerformaceByYear(year);
    }

}
