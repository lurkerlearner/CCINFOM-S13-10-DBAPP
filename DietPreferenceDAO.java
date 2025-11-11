package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DietPreferenceDAO {

    private DietPreference mapResultSetToDietPreference(ResultSet resultSet) throws SQLException {
        return new DietPreference(
                resultSet.getInt("diet_preference_id"),
                resultSet.getString("diet_name"),
                resultSet.getString("description")
        );
    }


    public boolean addDietPreference(DietPreference pref) {
        String query = "INSERT INTO diet_preference (diet_name, description) VALUES (?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, pref.getDiet_name());
            preparedStatement.setString(2, pref.getDescription());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding diet preference: " + e.getMessage());
            return false;
        }
    }

    public boolean updateDietPreference(DietPreference pref) {
        String query = "UPDATE diet_preference SET diet_name = ?, description = ? WHERE diet_preference_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, pref.getDiet_name());
            preparedStatement.setString(2, pref.getDescription());
            preparedStatement.setInt(3, pref.getDiet_preference_id());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating diet preference: " + e.getMessage());
            return false;
        }
    }

    public List<DietPreference> getAllDietPreferences() {
        List<DietPreference> preferences = new ArrayList<>();
        // Query to select all data, ordered alphabetically for presentation
        String query = "SELECT diet_preference_id, diet_name, description FROM diet_preference ORDER BY diet_name";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            // Iterate over the result set
            while (resultSet.next()) {
                DietPreference pref = mapResultSetToDietPreference(resultSet);
                preferences.add(pref);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all diet preferences: " + e.getMessage());
            e.printStackTrace();
        }
        return preferences;
    }
    public DietPreference getDietPreferenceById(int id) {
        DietPreference pref = null;
        String query = "SELECT diet_preference_id, diet_name, description FROM diet_preference WHERE diet_preference_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the value for the placeholder (?)
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // Check if a result was returned
                if (resultSet.next()) {
                    // Map the single row's data to a DietPreference object
                    pref = mapResultSetToDietPreference(resultSet);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving diet preference by ID (" + id + "): " + e.getMessage());
            e.printStackTrace();
        }
        return pref;
    }

    /**
     * Retrieves a DietPreference by name.
     * Returns null if not found.
     */
    public DietPreference getDietPreferenceByName(String name) {
        DietPreference pref = null;
        String query = "SELECT diet_preference_id, diet_name, description FROM diet_preference WHERE diet_name = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, name);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    pref = mapResultSetToDietPreference(resultSet);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving diet preference by name (" + name + "): " + e.getMessage());
            e.printStackTrace();
        }
        return pref;
    }

    /**
     * Deletes a diet preference by id.
     */
    public boolean deleteDietPreference(int id) {
        String query = "DELETE FROM diet_preference WHERE diet_preference_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting diet preference with id " + id + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}