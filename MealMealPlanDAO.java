package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MealMealPlanDAO {

    public boolean addMealToPlan(int mealId, int mealPlanId) {
        String query = "INSERT INTO meal_meal_plan (meal_id, meal_plan_id) VALUES (?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, mealId);
            preparedStatement.setInt(2, mealPlanId);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding meal " + mealId + " to plan " + mealPlanId + ": " + e.getMessage());
            return false;
        }
    }


    public List<Integer> getMealIdsByPlan(int mealPlanId) {
        List<Integer> mealIds = new ArrayList<>();
        String query = "SELECT meal_id FROM meal_meal_plan WHERE meal_plan_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, mealPlanId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    mealIds.add(resultSet.getInt("meal_id"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving meals for plan " + mealPlanId + ": " + e.getMessage());
        }
        return mealIds;
    }


    public boolean deleteByMealPlanId(int mealPlanId) {
        String query = "DELETE FROM meal_meal_plan WHERE meal_plan_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, mealPlanId);
            
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting associations for plan " + mealPlanId + ": " + e.getMessage());
            return false;
        }
    }


    public boolean isMealInAnyPlan(int mealId) {
        String query = "SELECT 1 FROM meal_meal_plan WHERE meal_id = ? LIMIT 1";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, mealId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next(); 
            }
        } catch (SQLException e) {
            System.err.println("Error checking meal plan dependency: " + e.getMessage());
            return true; 
        }
    }
}