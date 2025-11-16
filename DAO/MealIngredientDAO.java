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

    // call this method before a delivery/order is made
    // ensure sufficient ingredients are present for the meal before it can be ordered
    // checks if stock quantity ng ingredient is sufficient for the meal it will be used in
    public boolean hasSufficientIngredientsForMeal(int mealId) {
        String sqlQuery = """
            SELECT mi.ingredient_id, mi.quantity AS required_qty, 
                i.stock_quantity AS available_qty, 
                i.ingredient_name
            FROM MEAL_INGREDIENT mi
            JOIN INGREDIENT i ON mi.ingredient_id = i.ingredient_id
            WHERE mi.meal_id = ?
        """;

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sqlQuery)) 
        {
            stmt.setInt(1, mealId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    double required = rs.getDouble("required_qty");
                    double available = rs.getDouble("available_qty");
                    String name = rs.getString("ingredient_name");

                    if (available < required) {
                        System.out.println("Insufficient: " + name +
                            " (required: " + required + ", available: " + available + ")");
                        return false;  
                    }
                }
            }

            // pag tapos yung loop, all ingredients are available
            System.out.println("All ingredients available for meal " + mealId);
            return true;
        } 
        catch (SQLException e) {
            System.err.println("Error checking ingredient sufficiency: " + e.getMessage());
            return false;
        }
    }

    // TO MODIFY, must be updated depending on checked out or delivered meals!!!!
    // call this method when a delivery/order is made
    public boolean updateStockQuantityBasedOnMealDelivery(int mealId) {
        String sqlQuery = """
            UPDATE INGREDIENT i
            JOIN MEAL_INGREDIENT mi ON i.ingredient_id = mi.ingredient_id
            SET i.stock_quantity = i.stock_quantity - mi.quantity
            WHERE mi.meal_id = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlQuery)) 
        {

            stmt.setInt(1, mealId);

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

    public ArrayList<MealIngredient> getAllMealIngredients() {
        ArrayList<MealIngredient> mealIngredients = new ArrayList<>();
        String sqlQuery = "SELECT * FROM MEAL_INGREDIENT";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlQuery)) 
        {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MealIngredient mealIngredient = new MealIngredient(
                        rs.getInt("meal_id"),
                        rs.getInt("ingredient_id"),
                        rs.getDouble("quantity")
                    );
                    mealIngredients.add(mealIngredient);
                }
            }
        } 
        catch (SQLException e) 
        {
            System.err.println("Error retrieving meal ingredients " + e.getMessage());
        }
        return mealIngredients;
    }

    // maybe modify eventually to return Meal objects instead of just meal IDs
    public ArrayList<MealIngredient> getMealsHavingIngredient(int ingredientId) {
        ArrayList<MealIngredient> mealIngredients = new ArrayList<>();

        String sqlQuery = "SELECT * FROM MEAL_INGREDIENT WHERE ingredient_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlQuery)) 
        {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MealIngredient mealIngredient = new MealIngredient(
                        rs.getInt("meal_id"),
                        rs.getInt("ingredient_id"),
                        rs.getDouble("quantity")
                    );
                    mealIngredients.add(mealIngredient);
                }
            }
        } 
        catch (SQLException e) {
            System.err.println("Error checking meals having ingredient " + ingredientId + ": " + e.getMessage());
        }

        return mealIngredients;
    }

    public ArrayList<MealIngredient> getIngredientsInMeal(int mealId) {
        ArrayList<MealIngredient> mealIngredients = new ArrayList<>();

        String sqlQuery = "SELECT * FROM MEAL_INGREDIENT WHERE meal_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlQuery)) 
        {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MealIngredient mealIngredient = new MealIngredient(
                        rs.getInt("meal_id"),
                        rs.getInt("ingredient_id"),
                        rs.getDouble("quantity")
                    );
                    mealIngredients.add(mealIngredient);
                }
            }
        } 
        catch (SQLException e) {
            System.err.println("Error checking ingredients in meal " + mealId + ": " + e.getMessage());
        }

        return mealIngredients;
    }

    // baka tanggalin later, difference neto from the getIngredientsInMeal 
    // is this one joins with INGREDIENT table to get ingredient name
    public ArrayList<MealIngredient> getIngredientsByMealId(int mealId) {
        ArrayList<MealIngredient> ingredients = new ArrayList<>();
        String sqlQuery = """
        SELECT mi.meal_id, i.ingredient_id, i.ingredient_name, mi.quantity 
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
