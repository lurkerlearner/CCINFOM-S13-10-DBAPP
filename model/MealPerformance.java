package model;

public class MealPerformance {

    private int mealId;
    private String mealName;
    private int timesOrdered;
    private float totalRevenue;
    private int locationCount;

public MealPerformance(int mealId, String mealName,int timesOrdered, float totalRevenue, int locationCount) {
        this.mealId = mealId;
        this.mealName = mealName;
        this.timesOrdered = timesOrdered;
        this.totalRevenue = totalRevenue;
        this.locationCount = locationCount;
    }

    public int getMealId() {
        return mealId;
    }

    public String getMealName() {
        return mealName;
    }

    public int getTimesOrdered() {
        return timesOrdered;
    }

    public float getTotalRevenue() {
        return totalRevenue;
    }

    public int getLocationCount() {
        return locationCount;
    }
    public void setMealName(String mealName) {
        this.mealName = mealName;
    }
    public void setTimesOrdered(int timesOrdered) {
        this.timesOrdered = timesOrdered;
    }
    public void setTotalRevenue(float totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
    public void setLocationCount(int locationCount) {
        this.locationCount = locationCount;
    }
}
