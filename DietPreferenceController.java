package controller;

import model.DietPreference;
import model.DietPreferenceDAO;
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
    public boolean updateDietPreference(DietPreference preference) {
        if (preference == null || preference.getDiet_preference_id() <= 0) {
            System.err.println("Validation Error: Invalid preference id.");
            return false;
        }
        if (preference.getDiet_name() == null || preference.getDiet_name().trim().isEmpty()) {
            System.err.println("Validation Error: Diet name cannot be empty.");
            return false;
        }

        DietPreference existing = dietPreferenceDAO.getDietPreferenceById(preference.getDiet_preference_id());
        if (existing == null) {
            System.err.println("Validation Error: Diet preference not found.");
            return false;
        }

        // If name changed, ensure uniqueness
        DietPreference byName = dietPreferenceDAO.getDietPreferenceByName(preference.getDiet_name());
        if (byName != null && byName.getDiet_preference_id() != preference.getDiet_preference_id()) {
            System.err.println("Validation Error: Diet name must be unique.");
            return false;
        }

        return dietPreferenceDAO.updateDietPreference(preference);
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