package DAO;

import model.*;
import app.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MealDAO {

    public List<Meal> getAllMeals() {
        List<Meal> meals = new ArrayList<>();
    String query = "SELECT meal_id, meal_name, price, cost, preparation_time, calories, nutrients, date_added, diet_preference_id FROM meal";

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

    public Meal getMealById(int mealId) {
        Meal meal = null;
    String query = "SELECT meal_id, meal_name, price, cost, preparation_time, calories, nutrients, date_added, diet_preference_id FROM meal WHERE meal_id = ?";

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
    String query = "SELECT meal_id, meal_name, price, cost, preparation_time, calories, nutrients, date_added, diet_preference_id FROM meal WHERE diet_preference_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) 
        {

  
            preparedStatement.setInt(1, dietPreferenceId);

            try (ResultSet resultSet = preparedStatement.executeQuery())
            {
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

    public List<Meal> getMealsSortedByPriceAscending() {
        List<Meal> meals = new ArrayList<>();
        String query = "SELECT meal_id, meal_name, price, cost, preparation_time, calories, nutrients, date_added, diet_preference_id FROM meal ORDER BY price ASC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Meal meal = mapResultSetToMeal(resultSet);
                meals.add(meal);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving meals sorted by price (ascending): " + e.getMessage());
            e.printStackTrace();
        }
        return meals;
    }

    public List<Meal> getMealsSortedByPriceDescending() {
        List<Meal> meals = new ArrayList<>();
        String query = "SELECT meal_id, meal_name, price, cost, preparation_time, calories, nutrients, date_added, diet_preference_id FROM meal ORDER BY price DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Meal meal = mapResultSetToMeal(resultSet);
                meals.add(meal);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving meals sorted by price (descending): " + e.getMessage());
            e.printStackTrace();
        }
        return meals;
    }

    public boolean addMeal(Meal meal) {
    String query = "INSERT INTO meal (meal_name, price, cost, preparation_time, calories, nutrients, date_added, diet_preference_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

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

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error adding new meal: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateMeal(Meal meal) {
    String query = "UPDATE meal SET meal_name = ?, price = ?, cost = ?, preparation_time = ?, calories = ?, nutrients = ?, date_added = ?, diet_preference_id = ? WHERE meal_id = ?";

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
            preparedStatement.setInt(9, meal.getMeal_id()); // WHERE clause

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error updating meal: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    public List<Meal> getMealsForClient(int clientId){
        List<Meal> meals = new ArrayList<>();
        String query = "SELECT diet_preference_id FROM client WHERE client_id = ?";
                try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) 
        {
            preparedStatement.setInt(1, clientId);

            try (ResultSet resultSet = preparedStatement.executeQuery())
            {
                if (resultSet.next())
                {
                    int dietPreferenceId = resultSet.getInt("diet_preference_id");
                    meals = getMealsByDietPreference(dietPreferenceId);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving diet preferences by client ID (" + clientId + "): " + e.getMessage());
            e.printStackTrace();
        }
        return meals;
    }

     public List<MealPerformance> getMealPerformanceByMonthYear(int year, int month){
        List<MealPerformance> report = new ArrayList<>();
        String query = "SELECT m.meal_id, m.meal_name, COUNT(md.transaction_id) AS TimesOrdered, " +
                       " (COUNT(md.transaction_id) * m.price) AS TotalRevenue, " +
                       " COUNT(DISTINCT c.location_id) AS DistinctLocations " +
                       " FROM meal m " +
                       " JOIN meal_delivery md ON m.meal_id = md.meal_id " +
                       " JOIN delivery d ON md.transaction_id = d.transaction_id " +
                       " JOIN client c ON d.client_id = c.client_id " +
                       " WHERE YEAR(d.order_date) = ? AND MONTH(d.order_date) = ? " +
                       " GROUP BY m.meal_id, m.meal_name " +
                       " ORDER BY TotalRevenue DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) 
        {
            preparedStatement.setInt(1, year);
            preparedStatement.setInt(2, month);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    MealPerformance performance = new MealPerformance(
                        resultSet.getInt("meal_id"),
                        resultSet.getString("meal_name"),
                        resultSet.getInt("TimesOrdered"),
                        resultSet.getFloat("TotalRevenue"),
                        resultSet.getInt("DistinctLocations")
                    );
                    report.add(performance);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error generating meal performance report for " + year  +  "/" + month + ":" + e.getMessage());
            e.printStackTrace();
        }
        return report;
    }  

    public List<MealPerformance> getMealPerformaceByYear(int year){
        List<MealPerformance> report = new ArrayList<>();
        String query = "SELECT m.meal_id, m.meal_name, COUNT(md.transaction_id) AS TimesOrdered, " +
                       " (COUNT(md.transaction_id) * m.price) AS TotalRevenue, " +
                       " COUNT(DISTINCT c.location_id) AS DistinctLocations " +
                       " FROM meal m " +
                       " JOIN meal_delivery md ON m.meal_id = md.meal_id " +
                       " JOIN delivery d ON md.transaction_id = d.transaction_id " +
                       " JOIN client c ON d.client_id = c.client_id " +
                       " WHERE YEAR(d.order_date) = ? "+
                       " GROUP BY m.meal_id, m.meal_name " +
                       " ORDER BY TotalRevenue DESC";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) 
        {
            preparedStatement.setInt(1, year);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    MealPerformance performance = new MealPerformance(
                        resultSet.getInt("meal_id"),
                        resultSet.getString("meal_name"),
                        resultSet.getInt("TimesOrdered"),
                        resultSet.getFloat("TotalRevenue"),
                        resultSet.getInt("DistinctLocations")
                    );
                    report.add(performance);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error generating meal performance report for " + year  + ":" + e.getMessage());
            e.printStackTrace();
        }
        return report;
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
                resultSet.getInt("diet_preference_id")
        );
    }


}
