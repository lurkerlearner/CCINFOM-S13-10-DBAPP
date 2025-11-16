package controller;

import model.DietPreference;
import DAO.DietPreferenceDAO;
import java.util.List;

public class DietPreferenceController {

    private final DietPreferenceDAO dietPreferenceDAO;

    public DietPreferenceController() {
        this.dietPreferenceDAO = new DietPreferenceDAO();
    }

    public List<DietPreference> getAvailableDietPreferences() {
        return dietPreferenceDAO.getAllDietPreferences();
    }


    public DietPreference getPreferenceDetails(int id) {
        return dietPreferenceDAO.getDietPreferenceById(id);
    }

    public DietPreference getPreferenceByName(String name) {
        return dietPreferenceDAO.getDietPreferenceByName(name);
    }

    /**
     * Create a new diet preference after validating input and uniqueness.
     */
    public boolean createNewDietPreference(DietPreference preference) {
        // Business Rule: Diet Name must be unique and non-empty.
        if (preference == null || preference.getDiet_name() == null || preference.getDiet_name().trim().isEmpty()) {
            System.err.println("Validation Error: Diet name cannot be empty.");
            return false;
        }

        // Uniqueness check
        DietPreference existing = dietPreferenceDAO.getDietPreferenceByName(preference.getDiet_name());
        if (existing != null) {
            System.err.println("Validation Error: Diet name must be unique.");
            return false;
        }

        return dietPreferenceDAO.addDietPreference(preference);
    }

    /**
     * Update an existing diet preference after validation.
     */
    public String updateDietPreference(int diet_preference_id, String diet_name, String description) {


        DietPreference updatedDietPreference = new DietPreference(diet_preference_id, diet_name, description);
        
        if (dietPreferenceDAO.updateDietPreference(updatedDietPreference)) 
        {
            return "SUCCESS";
        } else 
        {
        return "Failed to update meal in database.";
        }
    }

    /**
     * Delete a diet preference by id.
     */
    public boolean deleteDietPreference(int id) {
        DietPreference existing = dietPreferenceDAO.getDietPreferenceById(id);
        if (existing == null) {
            System.err.println("Validation Error: Diet preference not found.");
            return false;
        }
        return dietPreferenceDAO.deleteDietPreference(id);
    }
}