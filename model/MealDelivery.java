package model;

public class MealDelivery 
{
    private int meal_id;
    private int transaction_id;
    private String remarks;

    public MealDelivery(int meal_id, int transaction_id, String remarks)
    {
        this.meal_id = meal_id;
        this.transaction_id = transaction_id;
        this.remarks = remarks;
    }

    public int getMealID() 
    {
        return meal_id;
    }

    public int getTransactionID() 
    {
        return transaction_id;
    }

    public String getRemarks() 
    {
        return remarks;
    }

    public void setRemarks(String remarks) 
    {
        this.remarks = remarks;
    }
}
