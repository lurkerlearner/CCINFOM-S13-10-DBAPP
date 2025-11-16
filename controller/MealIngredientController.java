package controller;

import DAO.MealIngredientDAO;

public class MealIngredientController {
    private final MealIngredientDAO mealIngredientDAO;

    public MealIngredientController(MealIngredientDAO dao) {
        this.mealIngredientDAO = dao;
    }
}
