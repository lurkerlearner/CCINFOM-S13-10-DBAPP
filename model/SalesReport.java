package model;

/*
 * Represents a sales report with the number of meals sold.
 * must get the difference between the cost and price of each meal
 * sold in order to get the gross income (sum of all prices of the meals ordered)
 * and profit made (price - cost).
 */
public class SalesReport {

    // no. of meals successfully delivered/placed
    private int salesMade;
    private int mealID;
    private double mealCost;
    private double mealPrice;

    public SalesReport(int salesMade, int mealID, double mealCost, double mealPrice) {
        this.salesMade = salesMade;
        this.mealID = mealID;
        this.mealCost = mealCost;
        this.mealPrice = mealPrice;
    }

    public int getSalesMade() {
        return salesMade;
    }

    public void setSalesMade(int salesMade) {
        this.salesMade = salesMade;
    }

    public int getMealID() {
        return mealID;
    }

    public void setMealID(int mealID) {
        this.mealID = mealID;
    }

    public double getMealCost() {
        return mealCost;
    }

    public void setMealCost(double mealCost) {
        this.mealCost = mealCost;
    }

    public double getMealPrice() {
        return mealPrice;
    }

    public void setMealPrice(double mealPrice) {
        this.mealPrice = mealPrice;
    }

}
