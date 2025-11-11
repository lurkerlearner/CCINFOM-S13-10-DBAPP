package controller;

import model.Meal;
import model.MealDAO;
import model.MealMealPlanDAO;
import java.util.List;

public class MealController {

    private final MealDAO mealDAO;
    private final MealMealPlanDAO mealMealPlanDAO;

    //paadd nalang akez nung ingredient DAO
    public MealController() {
        this.mealDAO = new MealDAO();
        this.mealMealPlanDAO = new MealMealPlanDAO();
        // Initialize other necessary DAOs here
    }


    public List<Meal> getFilteredMeals(int dietPreferenceId) {
        return mealDAO.getMealsByDietPreference(dietPreferenceId);
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



}