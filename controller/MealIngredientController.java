package controller;

import DAO.MealIngredientDAO;
import java.util.*;
import model.MealIngredient;

public class MealIngredientController {
    private final MealIngredientDAO mealIngredientDAO;

    public MealIngredientController(MealIngredientDAO dao) {
        this.mealIngredientDAO = dao;
    }

    public boolean addMealIngredient(int meal_id, int ingredient_id, double quantity) {
        MealIngredient mealIngredient = new MealIngredient();
        mealIngredient.setMeal_id(meal_id);
        mealIngredient.setIngredient_id(ingredient_id);
        mealIngredient.setQuantity(quantity);

        return mealIngredientDAO.addMealIngredient(mealIngredient);
    }

    public ArrayList<MealIngredient> getAllMealIngredients() {
        return mealIngredientDAO.getAllMealIngredients();
    }

    public ArrayList<MealIngredient> getMealsHavingIngredient(int ingredientId) {
        return mealIngredientDAO.getMealsHavingIngredient(ingredientId);
    }

    public ArrayList<MealIngredient> getIngredientsInMeal(int mealId) {
        return mealIngredientDAO.getIngredientsInMeal(mealId);
    }
}
