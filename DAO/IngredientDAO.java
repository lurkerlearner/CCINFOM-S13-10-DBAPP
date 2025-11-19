package DAO;

import model.*;
import app.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class IngredientDAO {

    public boolean addIngredient(Ingredient ingredient) {
        // model.Restock_status restock_status = model.Restock_status.calculateStatus(ingredient.getStock_quantity());
        String sqlQuery = "INSERT INTO INGREDIENT (batch_no, ingredient_name, category, storage_type, " +
        "measurement_unit, stock_quantity, expiry_date, supplier_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        // RETURN_GENERATED_KEYS to get the auto generated ingredient_id from the database
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) 
        {
            stmt.setInt(1, ingredient.getBatch_no());
            stmt.setString(2, ingredient.getIngredient_name());
            stmt.setString(3, ingredient.getCategory().getDbValue());
            stmt.setString(4, ingredient.getStorage_type().getDbValue());
            stmt.setString(5, ingredient.getMeasurement_unit().getDbValue());
            stmt.setDouble(6, ingredient.getStock_quantity());
            stmt.setDate(7, ingredient.getExpiry_date());
            stmt.setInt(8, ingredient.getSupplier_id()); // ensure that supplier table already has records

            int affectedRows = stmt.executeUpdate();
            
            // successfully added/inserted if affectedRows > 1
            if (affectedRows > 0) {
                // Retrieve the auto-generated ingredient_id
                // go through the generated id for each ingredient (in sql), and assign it to the ingredient (in java)
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        ingredient.setIngredient_id(generatedId);
                    }
                }
                return true;
            }
            return false;
        }

        catch (SQLException e) 
        {
            System.err.println("Error adding ingredients: " + e.getMessage());
            return false;
        }
    } 

    public boolean updateIngredientAll(Ingredient ingredient) {
        String sqlQuery = "UPDATE INGREDIENT SET batch_no = ?, ingredient_name = ?, category = ?, " +
        "storage_type = ?, measurement_unit = ?, stock_quantity = ?, expiry_date = ?, " +
        "supplier_id = ? WHERE ingredient_id = ?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sqlQuery))
        {
            stmt.setInt(1, ingredient.getBatch_no());
            stmt.setString(2, ingredient.getIngredient_name());
            stmt.setString(3, ingredient.getCategory().getDbValue());
            stmt.setString(4, ingredient.getStorage_type().getDbValue());
            stmt.setString(5, ingredient.getMeasurement_unit().getDbValue());
            stmt.setDouble(6, ingredient.getStock_quantity());
            stmt.setDate(7, ingredient.getExpiry_date());
            stmt.setInt(8, ingredient.getSupplier_id());
            stmt.setInt(9, ingredient.getIngredient_id());

            int affectedRows = stmt.executeUpdate();
        
            if (affectedRows > 0) 
            {
                System.out.println("model.Ingredient ID " + ingredient.getIngredient_id() + " successfully updated!");
                return true;
            } 
            else 
            {
                System.out.println("No ingredient found with ID: " + ingredient.getIngredient_id());
                return false;
            }
        }
        catch (SQLException e) 
        {
            System.err.println("Error updating ingredient: " + e.getMessage());
            return false;
        }
    }

    public boolean updateBatchNo(int ingredient_id, int newBatchNo) {
        String sqlQuery = "UPDATE INGREDIENT SET batch_no = ? WHERE ingredient_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlQuery)) 
        {
            stmt.setInt(1, newBatchNo);
            stmt.setInt(2, ingredient_id);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
        catch (SQLException e) 
        {
            System.err.println("Error updating batch number: " + e.getMessage());
            return false;
        }
    }

    public boolean updateIngredientName(int ingredient_id, String newName) {
        String sqlQuery = "UPDATE INGREDIENT SET ingredient_name = ? WHERE ingredient_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlQuery)) 
        {
            stmt.setString(1, newName);
            stmt.setInt(2, ingredient_id);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
        catch (SQLException e) 
        {
            System.err.println("Error updating ingredient name: " + e.getMessage());
            return false;
        }
    }

    public boolean updateCategory(int ingredient_id, Category newCategory) {
        String sqlQuery = "UPDATE INGREDIENT SET category = ? WHERE ingredient_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlQuery)) 
        {
            stmt.setString(1, newCategory.getDbValue());
            stmt.setInt(2, ingredient_id);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
        catch (SQLException e) 
        {
            System.err.println("Error updating category: " + e.getMessage());
            return false;
        }
    }

    public boolean updateStorageType(int ingredient_id, Storage_type newStorageType) {
        String sqlQuery = "UPDATE INGREDIENT SET storage_type = ? WHERE ingredient_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlQuery)) 
        {
            stmt.setString(1, newStorageType.getDbValue());
            stmt.setInt(2, ingredient_id);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
        catch (SQLException e) 
        {
            System.err.println("Error updating storage type: " + e.getMessage());
            return false;
        }
    }

    public boolean updateMeasurementUnit(int ingredient_id, Measurement_unit newUnit) {
        String sqlQuery = "UPDATE INGREDIENT SET measurement_unit = ? WHERE ingredient_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlQuery)) 
        {
            stmt.setString(1, newUnit.getDbValue());
            stmt.setInt(2, ingredient_id);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
        catch (SQLException e) 
        {
            System.err.println("Error updating measurement unit: " + e.getMessage());
            return false;
        }
    }

    public boolean updateExpiryDate(int ingredient_id, java.sql.Date newExpiryDate) {
        String sqlQuery = "UPDATE INGREDIENT SET expiry_date = ? WHERE ingredient_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlQuery)) 
        {
            stmt.setDate(1, newExpiryDate);
            stmt.setInt(2, ingredient_id);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
        catch (SQLException e) 
        {
            System.err.println("Error updating expiry date: " + e.getMessage());
            return false;
        }
    }

    public boolean updateStockQuantity(int ingredient_id, double newQuantity) {
        String sqlQuery = "UPDATE INGREDIENT SET stock_quantity = ?, restock_status = ? WHERE ingredient_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlQuery))
        {
            stmt.setDouble(1, newQuantity);
            stmt.setString(2, Restock_status.calculateStatus(newQuantity).getDbValue());
            stmt.setInt(3, ingredient_id);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
        catch (SQLException e) 
        {
            System.err.println("Error updating stock quantity: " + e.getMessage());
            return false;
        }
    }

    public boolean updateSupplierId(int ingredient_id, int newSupplierId) {
        String sqlQuery = "UPDATE INGREDIENT SET supplier_id = ? WHERE ingredient_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlQuery)) 
        {
            stmt.setInt(1, newSupplierId);
            stmt.setInt(2, ingredient_id);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
        catch (SQLException e) 
        {
            System.err.println("Error updating supplier ID: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteIngredient(int ingredient_id) {
        String sqlQuery = "DELETE FROM INGREDIENT WHERE ingredient_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlQuery))
        {
            stmt.setInt(1, ingredient_id);

            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) 
            {
                System.out.println("model.Ingredient ID " + ingredient_id + " successfully deleted!");
                return true;
            } 
            else 
            {
                System.out.println("No ingredient found with ID: " + ingredient_id);
                return false;
            }
        }
        catch (SQLException e) 
        {
            System.err.println("Error deleting ingredient: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<Ingredient> getAllIngredients() {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        String sqlQuery = "SELECT * FROM INGREDIENT";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlQuery)) 
        {

            try(ResultSet rs = stmt.executeQuery())
            {
                // go thru each row and add it to the arraylist    
                while (rs.next()) 
                {
                    Ingredient ingredient = new Ingredient(
                        rs.getInt("ingredient_id"),
                        rs.getInt("batch_no"),
                        rs.getString("ingredient_name"),
                        Category.fromDbValue(rs.getString("category")),
                        Storage_type.fromDbValue(rs.getString("storage_type")),
                        Measurement_unit.fromDbValue(rs.getString("measurement_unit")),
                        rs.getDouble("stock_quantity"),
                        rs.getDate("expiry_date"),
                        Restock_status.fromDbValue(rs.getString("restock_status")),
                        rs.getInt("supplier_id")
                    );
                    ingredients.add(ingredient);
                }
            }
        } 

        catch (SQLException e) 
        {
            System.err.println("Error fetching ingredients: " + e.getMessage());
        }

        return ingredients;
    }

    public ArrayList<Ingredient> getIngredientsByBatchNo(int batch_no) {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        String sqlQuery = "SELECT * FROM INGREDIENT WHERE batch_no = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlQuery)) 
        {
            stmt.setInt(1, batch_no);

            try(ResultSet rs = stmt.executeQuery())
            {
                // go thru each row and add it to the arraylist    
                while (rs.next()) 
                {
                    Ingredient ingredient = new Ingredient(
                        rs.getInt("ingredient_id"),
                        rs.getInt("batch_no"),
                        rs.getString("ingredient_name"),
                        Category.fromDbValue(rs.getString("category")),
                        Storage_type.fromDbValue(rs.getString("storage_type")),
                        Measurement_unit.fromDbValue(rs.getString("measurement_unit")),
                        rs.getDouble("stock_quantity"),
                        rs.getDate("expiry_date"),
                        Restock_status.fromDbValue(rs.getString("restock_status")),
                        rs.getInt("supplier_id")
                    );
                    ingredients.add(ingredient);
                }
            }
        } 

        catch (SQLException e) 
        {
            System.err.println("Error fetching ingredients: " + e.getMessage());
        }

        return ingredients;
    }

    public ArrayList<Ingredient> getIngredientsBySupplier(int supplier_id) {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        String sqlQuery = "SELECT * FROM INGREDIENT WHERE supplier_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlQuery)) 
        {
            stmt.setInt(1, supplier_id);

            try(ResultSet rs = stmt.executeQuery())
            {
                // go thru each row and add it to the arraylist    
                while (rs.next()) 
                {
                    Ingredient ingredient = new Ingredient(
                        rs.getInt("ingredient_id"),
                        rs.getInt("batch_no"),
                        rs.getString("ingredient_name"),
                        Category.fromDbValue(rs.getString("category")),
                        Storage_type.fromDbValue(rs.getString("storage_type")),
                        Measurement_unit.fromDbValue(rs.getString("measurement_unit")),
                        rs.getDouble("stock_quantity"),
                        rs.getDate("expiry_date"),
                        Restock_status.fromDbValue(rs.getString("restock_status")),
                        rs.getInt("supplier_id")
                    );
                    ingredients.add(ingredient);
                }
            }
        } 

        catch (SQLException e) 
        {
            System.err.println("Error fetching ingredients: " + e.getMessage());
        }

        return ingredients;
    }

    public ArrayList<Ingredient> getIngredientsByCategory(Category category) {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        String sqlQuery = "SELECT * FROM INGREDIENT WHERE category = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlQuery)) 
        {
            
            stmt.setString(1, category.getDbValue()); 
            
            try(ResultSet rs = stmt.executeQuery()) 
            {
                 // go thru each row and add it to the arraylist  
                while (rs.next()) 
                {
                    Ingredient ingredient = new Ingredient(
                        rs.getInt("ingredient_id"),
                        rs.getInt("batch_no"),
                        rs.getString("ingredient_name"),
                        Category.fromDbValue(rs.getString("category")),
                        Storage_type.fromDbValue(rs.getString("storage_type")),
                        Measurement_unit.fromDbValue(rs.getString("measurement_unit")),
                        rs.getDouble("stock_quantity"),
                        rs.getDate("expiry_date"),
                        Restock_status.fromDbValue(rs.getString("restock_status")),
                        rs.getInt("supplier_id")
                    );
                    ingredients.add(ingredient);
                }
            }   
            
        } 
        catch (SQLException e) 
        {
            System.err.println("Error fetching ingredients: " + e.getMessage());
        }

        return ingredients;
    }

    public ArrayList<Ingredient> getIngredientsExpiringSoon() {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        String sqlQuery = "SELECT * FROM INGREDIENT WHERE expiry_date >= CURDATE() " +
        "ORDER BY expiry_date ASC LIMIT 30";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlQuery)) 
        {
            try(ResultSet rs = stmt.executeQuery())
            {
                // go thru each row and add it to the arraylist    
                while (rs.next()) 
                {
                    Ingredient ingredient = new Ingredient(
                        rs.getInt("ingredient_id"),
                        rs.getInt("batch_no"),
                        rs.getString("ingredient_name"),
                        Category.fromDbValue(rs.getString("category")),
                        Storage_type.fromDbValue(rs.getString("storage_type")),
                        Measurement_unit.fromDbValue(rs.getString("measurement_unit")),
                        rs.getDouble("stock_quantity"),
                        rs.getDate("expiry_date"),
                        Restock_status.fromDbValue(rs.getString("restock_status")),
                        rs.getInt("supplier_id")
                    );
                    ingredients.add(ingredient);
                }
            }
        } 

        catch (SQLException e) 
        {
            System.err.println("Error fetching ingredients: " + e.getMessage());
        }

        return ingredients;
    }

    public ArrayList<Ingredient> getIngredientsByRestockStatus(Restock_status status) {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        String sqlQuery = "SELECT * FROM INGREDIENT WHERE restock_status = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlQuery)) 
        {
            stmt.setString(1, status.getDbValue());
            try(ResultSet rs = stmt.executeQuery())
            {
                // go thru each row and add it to the arraylist    
                while (rs.next()) 
                {
                    Ingredient ingredient = new Ingredient(
                        rs.getInt("ingredient_id"),
                        rs.getInt("batch_no"),
                        rs.getString("ingredient_name"),
                        Category.fromDbValue(rs.getString("category")),
                        Storage_type.fromDbValue(rs.getString("storage_type")),
                        Measurement_unit.fromDbValue(rs.getString("measurement_unit")),
                        rs.getDouble("stock_quantity"),
                        rs.getDate("expiry_date"),
                        Restock_status.fromDbValue(rs.getString("restock_status")),
                        rs.getInt("supplier_id")
                    );
                    ingredients.add(ingredient);
                }
            }
        } 

        catch (SQLException e) 
        {
            System.err.println("Error fetching ingredients: " + e.getMessage());
        }

        return ingredients;
    }

    public ArrayList<Ingredient> getIngredientsByStorageType(Storage_type storage_type) {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        String sqlQuery = "SELECT * FROM INGREDIENT WHERE storage_type = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlQuery)) 
        {
            stmt.setString(1, storage_type.getDbValue());

            try(ResultSet rs = stmt.executeQuery())
            {
                // go thru each row and add it to the arraylist    
                while (rs.next()) 
                {
                    Ingredient ingredient = new Ingredient(
                        rs.getInt("ingredient_id"),
                        rs.getInt("batch_no"),
                        rs.getString("ingredient_name"),
                        Category.fromDbValue(rs.getString("category")),
                        Storage_type.fromDbValue(rs.getString("storage_type")),
                        Measurement_unit.fromDbValue(rs.getString("measurement_unit")),
                        rs.getDouble("stock_quantity"),
                        rs.getDate("expiry_date"),
                        Restock_status.fromDbValue(rs.getString("restock_status")),
                        rs.getInt("supplier_id")
                    );
                    ingredients.add(ingredient);
                }
            }
        } 

        catch (SQLException e) 
        {
            System.err.println("Error fetching ingredients: " + e.getMessage());
        }

        return ingredients;
    }

    public Ingredient getIngredientById(int ingredient_id) {
        String sqlQuery = "SELECT * FROM INGREDIENT WHERE ingredient_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlQuery)) {

            stmt.setInt(1, ingredient_id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Ingredient(
                        rs.getInt("ingredient_id"),
                        rs.getInt("batch_no"),
                        rs.getString("ingredient_name"),
                        Category.fromDbValue(rs.getString("category")),
                        Storage_type.fromDbValue(rs.getString("storage_type")),
                        Measurement_unit.fromDbValue(rs.getString("measurement_unit")),
                        rs.getDouble("stock_quantity"),
                        rs.getDate("expiry_date"),
                        Restock_status.fromDbValue(rs.getString("restock_status")),
                        rs.getInt("supplier_id")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Error fetching ingredient by ID: " + e.getMessage());
        }

        return null;
    }

}
