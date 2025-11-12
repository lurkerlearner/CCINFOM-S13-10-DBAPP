public enum PaymentStatus 
{
    PAID("Paid"), 
    PENDING("Pending"), 
    FAILED("Failed");

    private final String dbValue;

    PaymentStatus(String dbValue) 
    {
        this.dbValue = dbValue;
    }

    public String getDbValue() 
    {
        return dbValue;
    }

    public static PaymentStatus fromDbValue(String dbVal) 
    {
        return valueOf(dbVal.toUpperCase().replace(" ", "_"));
    }
}