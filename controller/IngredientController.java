package controller;

import java.sql.Date;

import DAO.IngredientDAO;
import java.util.ArrayList;
import model.*;

public class IngredientController {
    private final IngredientDAO ingredientDAO;

    public IngredientController(IngredientDAO dao) {
        this.ingredientDAO = dao;
    }

    public boolean addIngredient(int batch_no, String ingredient_name,
                      Category category, Storage_type storage_type, Measurement_unit measurement_unit,
                      double stock_quantity, Date expiry_date, int supplier_id) 
    {
        Ingredient ingredient = new Ingredient();
        ingredient.setBatch_no(batch_no);
        ingredient.setIngredient_name(ingredient_name);
        ingredient.setCategory(category);
        ingredient.setStorage_type(storage_type);
        ingredient.setMeasurement_unit(measurement_unit);
        ingredient.setStock_quantity(stock_quantity);
        ingredient.setExpiry_date(expiry_date);
        ingredient.setSupplier_id(supplier_id);

        return ingredientDAO.addIngredient(ingredient);
    }

    public ArrayList<Ingredient> getAllIngredients() {
        return ingredientDAO.getAllIngredients();
    }

    public ArrayList<Ingredient> getIngredientsByBatchNo(int batchNo) {
        return ingredientDAO.getIngredientsByBatchNo(batchNo);
    }

    public ArrayList<Ingredient> getIngredientsBySupplier(int supplierId) {
        return ingredientDAO.getIngredientsBySupplier(supplierId);
    }

    public ArrayList<Ingredient> getIngredientsByCategory(Category category) {
        return ingredientDAO.getIngredientsByCategory(category);
    }
}
