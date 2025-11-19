package controller;

import model.Meal;
import model.MealPlan;
import DAO.MealDAO;
import DAO.MealPlanDAO;
import DAO.MealMealPlanDAO;

import java.util.ArrayList;
import java.util.List;


public class MealPlanController {

    private final MealPlanDAO mealPlanDAO;
    private final MealMealPlanDAO mealMealPlanDAO;
    private final MealDAO mealDAO;

    public MealPlanController() {
        this.mealPlanDAO = new MealPlanDAO();
        this.mealMealPlanDAO = new MealMealPlanDAO();
        this.mealDAO = new MealDAO();
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

    public MealPlanDAO getMealPlanDAO() {
        return mealPlanDAO;
    }

    public List<Meal> getAllMeals(){
        return mealDAO.getAllMeals();
    }

    public boolean addMealtoPlan(int mealId, int planId, String remarks){
        if(mealId <= 0 || planId <=0){
            System.err.println("Validation Error: Invalid meal or plan id.");
            return false;
        }
        if (mealMealPlanDAO.addMealToPlan(mealId, planId, remarks)) {
            Meal meal = mealDAO.getMealById(mealId);
            MealPlan plan = mealPlanDAO.getMealPlanbyId(planId);
            
            if (meal !=null && plan != null) {
                float newTotalPrice = plan.getTotal_price() + meal.getPrice();
                plan.setTotal_price(newTotalPrice);
                return mealPlanDAO.updatePlanTotalPrice(plan);
            }
        }
        return false;
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
    public List<Meal> getMealsForPlan(int planId) {
        if (planId <= 0) {
            System.err.println("Validation Error: Invalid plan id for meal lookup.");
            return new ArrayList<>();
        }
        return mealPlanDAO.getMealsInPlan(planId);
    }

    public boolean removeMealFromPlan(int mealId, int planId,  String remarks){
        if (mealId<=0 || planId <= 0) {
            System.err.println("Validation Error: Invalid meal or plan id for removal.");
            return false;
        }
        return mealMealPlanDAO.removeMealFromPlan(mealId, planId, remarks);
    }


    public String updateMealPlan(int id, String name, String description, float total_price) {    if (name == null || name.trim().isEmpty()) {
        return "FAILURE: Plan name cannot be empty.";
    }
    if (total_price < 0) {
        return "FAILURE: Total price cannot be negative.";
    }
        MealPlan existing = mealPlanDAO.getMealPlanByName(name);
        if (existing!=null && existing.getPlan_id() !=id) {
            return "FAILURE: Plan name must be unique.";
        }
    MealPlan updatedPlan = new MealPlan(id, name, description, total_price);
        if (mealPlanDAO.updateMealPlan(updatedPlan)) {
        return "SUCCESS";
    } else {
        return "Failed to update plan in database.";
    }
    }
    public boolean deleteMealPlan(int planId) {
        MealPlan existing = mealPlanDAO.getMealPlanbyId(planId);
        if (existing == null) {
            System.err.println("Validation Error: Meal plan not found.");
            return false;
        }
        mealMealPlanDAO.deleteByMealPlanId(planId);
        return mealPlanDAO.deleteMealPlan(planId);
    }
}
