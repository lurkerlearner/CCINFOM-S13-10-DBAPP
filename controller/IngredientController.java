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

    public boolean deleteIngredient(int ingredient_id) {
        return ingredientDAO.deleteIngredient(ingredient_id);
    }

    public boolean updateIngredient(Ingredient ingredient) {
        return ingredientDAO.updateIngredientAll(ingredient);
    }

    public boolean updateIngredientBatchNo(int ingredient_id, int newBatchNo) {
        return ingredientDAO.updateBatchNo(ingredient_id, newBatchNo);
    }

    public boolean updateIngredientName(int ingredient_id, String newName) {
        return ingredientDAO.updateIngredientName(ingredient_id, newName);
    }

    public boolean updateIngredientCategory(int ingredient_id, Category newCategory) {
        return ingredientDAO.updateCategory(ingredient_id, newCategory);
    }

    public boolean updateIngredientStorageType(int ingredient_id, Storage_type newStorageType) {
        return ingredientDAO.updateStorageType(ingredient_id, newStorageType);
    }

    public boolean updateIngredientMeasurementUnit(int ingredient_id, Measurement_unit newUnit) {
        return ingredientDAO.updateMeasurementUnit(ingredient_id, newUnit);
    }

    public boolean updateIngredientExpiryDate(int ingredient_id, java.sql.Date newExpiryDate) {
        return ingredientDAO.updateExpiryDate(ingredient_id, newExpiryDate);
    }

    public boolean updateIngredientStockQuantity(int ingredient_id, double newQuantity) {
        return ingredientDAO.updateStockQuantity(ingredient_id, newQuantity);
    }

    public boolean updateIngredientSupplierID(int ingredient_id, int newSupplierID) {
        return ingredientDAO.updateSupplierId(ingredient_id, newSupplierID);
    }

    public Ingredient getIngredientByID(int ingredientId) {
        return ingredientDAO.getIngredientById(ingredientId);
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

    public ArrayList<Ingredient> getIngredientsExpiringSoon() {
        return ingredientDAO.getIngredientsExpiringSoon();
    }

    public ArrayList<Ingredient> getIngredientsByStorageType(Storage_type type) {
        return ingredientDAO.getIngredientsByStorageType(type);
    }

    public ArrayList<Ingredient> getIngredientsByStatus(Restock_status status) {
        return ingredientDAO.getIngredientsByRestockStatus(status);
    }
}
