import java.sql.*;
import java.util.*;

public class IngredientDAO {

    /* public boolean addIngredient(Ingredient ingredient) {
        // String sqlQuery = 
    } */

    public ArrayList<String> getIngredientsByBatchNo(int batch_no) {
        ArrayList<String> ingredients = new ArrayList<>();
        String sqlQuery = "SELECT ingredient_name FROM INGREDIENT WHERE batch_no = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlQuery);
             ResultSet rs = stmt.executeQuery()) {

            // go thru each row and add it to the arraylist    
            while (rs.next()) {
                ingredients.add(rs.getString("ingredient_name"));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching ingredients: " + e.getMessage());
        }

        return ingredients;
    }

    public ArrayList<String> getIngredientsByCategory(Category category) {
        ArrayList<String> ingredients = new ArrayList<>();
        String sqlQuery = "SELECT ingredient_name FROM INGREDIENT WHERE category = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlQuery);
             ResultSet rs = stmt.executeQuery()) {

            // go thru each row and add it to the arraylist    
            while (rs.next()) {
                ingredients.add(rs.getString("ingredient_name"));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching ingredients: " + e.getMessage());
        }

        return ingredients;
    }

}
