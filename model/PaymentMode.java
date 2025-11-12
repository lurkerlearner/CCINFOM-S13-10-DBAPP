package model;

public enum PaymentMode
{
    BANK("Bank"), 
    GCASH("GCash"), 
    CASH_ON_DELIVERY("Cash on Delivery");

    private final String dbValue;

    PaymentMode(String dbValue) 
    {
        this.dbValue = dbValue;
    }

    public String getDbValue() 
    {
        return dbValue;
    }

    public static PaymentMode fromDbValue(String dbVal) 
    {
        return valueOf(dbVal.toUpperCase().replace(" ", "_"));
    }
}