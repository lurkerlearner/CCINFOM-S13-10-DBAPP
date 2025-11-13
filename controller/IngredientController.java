package controller;

import DAO.IngredientDAO;
import model.Ingredient;

public class IngredientController {
    private IngredientDAO ingredientDAO;

    public IngredientController(IngredientDAO dao) {
        this.ingredientDAO = dao;
    }
}
