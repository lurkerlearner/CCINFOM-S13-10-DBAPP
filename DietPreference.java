package model;


public class DietPreference {


    private int diet_preference_id;
    private String diet_name;
    private String description;

    public DietPreference(int diet_preference_id, String diet_name, String description){

        this.diet_preference_id = diet_preference_id;
        this.diet_name = diet_name;
        this.description = description;

    }


    public int getDiet_preference_id() {
        return diet_preference_id;
    }

    public String getDiet_name() {
        return diet_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDiet_name(String diet_name) {
        this.diet_name = diet_name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}