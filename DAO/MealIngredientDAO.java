package DAO;

import app.*;
import java.sql.*;
import java.util.*;
import model.MealIngredient;

public class MealIngredientDAO {
    
    public boolean addMealIngredient(MealIngredient mealIngredient) {
        String sqlQuery = "INSERT INTO MEAL_INGREDIENT (meal_id, ingredient_id, quantity) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlQuery)) 
        {

            stmt.setInt(1, mealIngredient.getMeal_id());
            stmt.setInt(2, mealIngredient.getIngredient_id());
            stmt.setDouble(3, mealIngredient.getQuantity());

            int affectedRows = stmt.executeUpdate();
        
            if (affectedRows > 0) 
            {
                System.out.println("MEAL_INGREDIENT table successfully updated!");
                return true;
            } 
            else 
            {
                System.out.println("No rows affected while adding meal ingredient.");
                return false;
            }
        } 
        catch (SQLException e) 
        {
            System.err.println("Error adding meal ingredient: " + e.getMessage());
            return false;
        }
    }

    public boolean updateMealIngredient(MealIngredient mealIngredient) {
        String sqlQuery = "UPDATE MEAL_INGREDIENT SET quantity = ? WHERE meal_id = ? AND ingredient_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlQuery)) 
        {

            stmt.setDouble(1, mealIngredient.getQuantity());
            stmt.setInt(2, mealIngredient.getMeal_id());
            stmt.setInt(3, mealIngredient.getIngredient_id());

            int affectedRows = stmt.executeUpdate();
        
            if (affectedRows > 0) 
            {
                System.out.println("MEAL_INGREDIENT successfully updated!");
                return true;
            } 
            else 
            {
                System.out.println("No rows affected while updating meal ingredient.");
                return false;
            }
        } 
        catch (SQLException e) 
        {
            System.err.println("Error updating meal ingredient: " + e.getMessage());
            return false;
        }
    }

    // TO MODIFY, must be updated depending on checked out or delivered meals!!!!
    // call this method when a delivery is made
    public boolean updateStockQuantityBasedOnMeal(int mealId) {
        String sqlQuery = """
            UPDATE INGREDIENT i
            JOIN MEAL_INGREDIENT mi ON i.ingredient_id = mi.ingredient_id
            SET i.stock_quantity = i.stock_quantity - mi.quantity
            WHERE mi.MealID = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlQuery)) 
        {

            stmt.setInt(1, mealId);
            stmt.setInt(2, mealId);

            int affectedRows = stmt.executeUpdate();
        
            if (affectedRows > 0) 
            {
                System.out.println("Stock quantities successfully updated based on meal!");
                return true;
            } 
            else 
            {
                System.out.println("No rows affected while updating stock quantities.");
                return false;
            }
        } 
        catch (SQLException e) 
        {
            System.err.println("Error updating stock quantities: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteMealIngredient(int mealId, int ingredientId) {
        String sqlQuery = "DELETE FROM MEAL_INGREDIENT WHERE meal_id = ? AND ingredient_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlQuery)) 
        {

            stmt.setInt(1, mealId);
            stmt.setInt(2, ingredientId);

            int affectedRows = stmt.executeUpdate();
        
            if (affectedRows > 0) 
            {
                System.out.println("MealIngredient successfully deleted!");
                return true;
            } 
            else 
            {
                System.out.println("No rows affected while deleting meal ingredient.");
                return false;
            }
        } 
        catch (SQLException e) 
        {
            System.err.println("Error deleting meal ingredient: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<MealIngredient> getIngredientsByMealId(int mealId) {
        ArrayList<MealIngredient> ingredients = new ArrayList<>();
        String sqlQuery = """
        SELECT i.ingredient_id, i.ingredient_name, mi.quantity 
        FROM MEAL_INGREDIENT mi JOIN INGREDIENT i ON mi.ingredient_id = i.ingredient_id
        WHERE mi.meal_id = ?      
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlQuery)) 
        {

            stmt.setInt(1, mealId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MealIngredient mealIngredient = new MealIngredient(
                        rs.getInt("meal_id"),
                        rs.getInt("ingredient_id"),
                        rs.getDouble("quantity"),
                        rs.getString("ingredient_name")
                    );
                    ingredients.add(mealIngredient);
                }
            }
        } 
        catch (SQLException e) 
        {
            System.err.println("Error retrieving ingredients for meal " + mealId + ": " + e.getMessage());
        }
        return ingredients;
    }

}
