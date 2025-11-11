package controller;

import model.MealPlan;
import model.MealPlanDAO;
import java.util.List;


public class MealPlanController {

    private final MealPlanDAO mealPlanDAO;

    public MealPlanController() {
        this.mealPlanDAO = new MealPlanDAO();
    }

    public List<MealPlan> getAllMealPlans() {
        return mealPlanDAO.getallMealPlans();
    }
    public MealPlan getMealPlanDetails(int id) {
        if (id <= 0) {
            System.err.println("Validation Error: Invalid plan id.");
            return null;
        }
        return mealPlanDAO.getMealPlanbyId(id);
    }
    public MealPlan getPlanByName(String name){
        return mealPlanDAO.getMealPlanByName(name);
    }

    public boolean createNewMealPlan(MealPlan plan) {
        if (plan == null || plan.getPlan_name() == null || plan.getPlan_name().trim().isEmpty()) {
            System.err.println("Validation Error: Plan name cannot be empty.");
            return false;
        }

        MealPlan existing = mealPlanDAO.getMealPlanByName(plan.getPlan_name());
        if (existing != null) {
            System.err.println("Validation Error: Plan name must be unique.");
            return false;
        }
        return mealPlanDAO.addMealPlan(plan);
    }
    


    public boolean updateMealPlan(MealPlan plan) {
       
        if (plan == null || plan.getPlan_id() <= 0) {
            System.err.println("Validation Error: Invalid plan id.");
            return false;
        }
        if (plan.getPlan_name() == null || plan.getPlan_name().trim().isEmpty()) {
            System.err.println("Validation Error: Plan name cannot be empty.");
            return false;
        }
        return mealPlanDAO.updateMealPlan(plan);
    }
}
