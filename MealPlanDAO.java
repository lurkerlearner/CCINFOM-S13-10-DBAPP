package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class MealPlanDAO {

public List<Meal> getMealsInPlan(int mealPlanId) {
        List<Meal> meals = new ArrayList<>();
        
        // SQL JOIN: Links the meal table (m) and the junction table (mmp) using meal_id.
        String query = "SELECT m.meal_id, m.meal_name, m.price, m.cost, m.preparation_time, m.calories, m.nutrients, m.date_added, m.diet_preference_id " +
                       "FROM meal m " +
                       "JOIN meal_meal_plan mmp ON m.meal_id = mmp.meal_id " +
                       "WHERE mmp.meal_plan_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, mealPlanId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Meal meal = mapResultSetToMeal(resultSet); 
                    meals.add(meal);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving meals for meal plan " + mealPlanId + ": " + e.getMessage());
            e.printStackTrace();
        }
        return meals;
    }
    public List<MealPlan> getallMealPlans(){
        List<MealPlan> mealPlans = new ArrayList<>();
        String query = "SELECT plan_id, plan_name, description, total_price " +
                       "FROM meal_plan";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                MealPlan mealplans = mapResultSetToMealPlan(resultSet);
                mealPlans.add(mealplans);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all meal plans: " + e.getMessage());
            e.printStackTrace();
        }
        return mealPlans;
    }

    public MealPlan getMealPlanbyId(int plan_id) {
        MealPlan mealplans = null;
        String query = "SELECT plan_id, plan_name, description, total_price FROM meal_plan WHERE plan_id = ? ";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, plan_id);

            try (ResultSet resultSet = preparedStatement.executeQuery()){;
            if (resultSet.next()) {
                mealplans = mapResultSetToMealPlan(resultSet);
            }
        }
    }   catch (SQLException e)

    {
        System.err.println("Error retrieving meal plan by ID: " + e.getMessage());
        e.printStackTrace();
    }
        return mealplans;
    }

    public MealPlan getMealPlanByName(String name){
        MealPlan mealplans = null;
        String query = "SELECT plan_id, plan_name, description, total_price FROM meal_plan WHERE plan_name = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query))
        {

            preparedStatement.setString(1, name);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                   mealplans  = mapResultSetToMealPlan(resultSet);
                }
            }
        }
        catch (SQLException e) 
        {
            System.err.println("Error retrieving meal plan by name (" + name + "): " + e.getMessage());
            e.printStackTrace();
        }       
        
        return mealplans;
    }


    public boolean updateMealPlan(MealPlan plan) {
        String query = "UPDATE meal_plan SET plan_name = ?, description = ?, total_price = ? WHERE plan_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, plan.getPlan_name());
            preparedStatement.setString(2, plan.getDescription());
            preparedStatement.setFloat(3, plan.getTotal_price());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating diet preference: " + e.getMessage());
            return false;
        }
    }

    public boolean addMealPlan(MealPlan plan){

        String query = "INSERT INTO meal_plan (plan_name, description, total_price) VALUES (?,?,?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) 
        {

            preparedStatement.setString(1, plan.getPlan_name());
            preparedStatement.setString(2, plan.getDescription());
            preparedStatement.setFloat(3, plan.getTotal_price());
            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) 
        {
            System.err.println("Error adding diet preference: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteMealPlan(int id){

        String query = "DELETE FROM meal_plan WHERE plan_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) 
        {

            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) 
        {
            System.err.println("Error deleting meal plan: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    private MealPlan mapResultSetToMealPlan(ResultSet resultSet) throws SQLException{
        return new MealPlan(
                resultSet.getInt("plan_id"),
                resultSet.getString("plan_name"),
                resultSet.getString("description"),
                resultSet.getFloat("total_price"));
    }
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
