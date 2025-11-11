package model;

public class MealPlan {


    private int plan_id;
    private String plan_name;
    private String description;
    private float total_price;

    public MealPlan(int plan_id,String plan_name, String description, float total_price){


        this.plan_id = plan_id;
        this.plan_name = plan_name;
        this.description = description;
        this.total_price = total_price;
    }


    public int getPlan_id() {
        return plan_id;
    }

    public String getPlan_name() {
        return plan_name;
    }

    public String getDescription() {
        return description;
    }

    public float getTotal_price() {
        return total_price;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public void setPlan_name(String plan_name) {
        this.plan_name = plan_name;
    }
    public void setTotal_price(float total_price) {
        this.total_price = total_price;
    }
}