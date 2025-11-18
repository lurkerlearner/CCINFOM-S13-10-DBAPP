package model;

public class MealPerformance {
    
    private int mealId;
    private String mealName;
    private int TimesOrdered;
    private float TotalRevenue;
    private int distinctlocations;

    public MealPerformance(int mealId, String mealName, int TimesOrdered, float TotalRevenue, int distinctlocations){
        this.mealId = mealId;
        this.mealName = mealName;
        this.TimesOrdered = TimesOrdered;
        this.TotalRevenue = TotalRevenue;
        this.distinctlocations = distinctlocations;
    }

    public int getMealId(){
        return mealId;
    }
    public String getMealName(){
        return mealName;
    }

    public int getTimesOrdered(){
        return TimesOrdered;
    }
    public float getTotalRevenue(){
        return TotalRevenue;
    }
    public int getDistinctLocations(){
        return distinctlocations;
    }    
}
