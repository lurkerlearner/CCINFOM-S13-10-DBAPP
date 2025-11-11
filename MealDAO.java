package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MealDAO {

    /**
     * Retrieves all meals from the database.
     * @return A List of Meal objects, or an empty list if none are found.
     */
    public List<Meal> getAllMeals() {
        List<Meal> meals = new ArrayList<>();
    String query = "SELECT meal_id, meal_name, price, cost, preparation_time, calories, nutrients, date_added, diet_preference_id, ingredient_id FROM meal";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Meal meal = mapResultSetToMeal(resultSet);
                meals.add(meal);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all meals: " + e.getMessage());
            e.printStackTrace();
        }
        return meals;
    }

    /**
     * Retrieves a single meal by its ID.
     * @param mealId The ID of the meal to retrieve.
     * @return The Meal object, or null if not found.
     */
    public Meal getMealById(int mealId) {
        Meal meal = null;
    String query = "SELECT meal_id, meal_name, price, cost, preparation_time, calories, nutrients, date_added, diet_preference_id, ingredient_id FROM meal WHERE meal_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query))
        {


            preparedStatement.setInt(1, mealId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    meal = mapResultSetToMeal(resultSet);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving meal by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return meal;
    }

    public List<Meal> getMealsByDietPreference(int dietPreferenceId){
        List<Meal> meals = new ArrayList<>();
    String query = "SELECT meal_id, meal_name, price, cost, preparation_time, calories, nutrients, date_added, diet_preference_id, ingredient_id FROM meal WHERE diet_preference_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) 
        {

            // Set the value for the placeholder (the '?' in the query)
            preparedStatement.setInt(1, dietPreferenceId);

            try (ResultSet resultSet = preparedStatement.executeQuery())
            {
                // Iterate over the result set and map each row to a Meal object
                while (resultSet.next())
                {
                    Meal meal = mapResultSetToMeal(resultSet);
                    meals.add(meal);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving meals by diet preference ID (" + dietPreferenceId + "): " + e.getMessage());
            e.printStackTrace();
        }
        return meals;
        }

    public boolean addMeal(Meal meal) {
    String query = "INSERT INTO meal (meal_name, price, cost, preparation_time, calories, nutrients, date_added, diet_preference_id, ingredient_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) 
            {

                preparedStatement.setString(1, meal.getMeal_name());
                preparedStatement.setFloat(2, meal.getPrice());
                preparedStatement.setFloat(3, meal.getCost());
                preparedStatement.setInt(4, meal.getPreparation_time());
                preparedStatement.setInt(5, meal.getCalories());
                preparedStatement.setString(6, meal.getNutrients());
                preparedStatement.setString(7, meal.getDate_added()); // Consider using Date/Timestamp for robustness
                preparedStatement.setInt(8, meal.getDiet_preference_id());
                preparedStatement.setInt(9, meal.getIngredient_id());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error adding new meal: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateMeal(Meal meal) {
    String query = "UPDATE meal SET meal_name = ?, price = ?, cost = ?, preparation_time = ?, calories = ?, nutrients = ?, date_added = ?, diet_preference_id = ?, ingredient_id = ? WHERE meal_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, meal.getMeal_name());
            preparedStatement.setFloat(2, meal.getPrice());
            preparedStatement.setFloat(3, meal.getCost());
            preparedStatement.setInt(4, meal.getPreparation_time());
            preparedStatement.setInt(5, meal.getCalories());
            preparedStatement.setString(6, meal.getNutrients());
            preparedStatement.setString(7, meal.getDate_added());
            preparedStatement.setInt(8, meal.getDiet_preference_id());
            preparedStatement.setInt(9, meal.getIngredient_id());
            preparedStatement.setInt(10, meal.getMeal_id()); // WHERE clause

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error updating meal: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public boolean deleteMeal(int mealId) {
        String query = "DELETE FROM meal WHERE meal_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, mealId);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting meal: " + e.getMessage());
            // NOTE: If this meal is referenced by other tables (like meal_delivery),
            // a foreign key violation will occur. This logic should be handled by the business layer.
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Helper method to map a ResultSet row to a Meal object.
     */
    private Meal mapResultSetToMeal(ResultSet resultSet) throws SQLException {
        return new Meal(
                resultSet.getInt("meal_id"),
                resultSet.getString("meal_name"),
                resultSet.getFloat("price"),
                resultSet.getFloat("cost"),
                resultSet.getInt("preparation_time"),
                resultSet.getInt("calories"),
                resultSet.getString("nutrients"),
                resultSet.getString("date_added"),
                resultSet.getInt("diet_preference_id"),
                resultSet.getInt("ingredient_id")
        );
    }


}