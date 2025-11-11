package model;

public class MealMealPlan {
    
    private int meal_id;
    private int plan_id;
    private String remarks;

    public MealMealPlan(int meal_id, int plan_id, String remarks){
        this.meal_id = meal_id;
        this.plan_id = plan_id;
        this.remarks = remarks;
    }
    public int getMeal_id() {
        return meal_id;
    }
    public int getPlan_id() {
        return plan_id;
    }
    public String getRemarks() {
        return remarks;
    }
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    

}
