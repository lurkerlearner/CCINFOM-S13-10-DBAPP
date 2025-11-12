public enum DeliveryStatus 
{
    ON_TIME("On-Time"), 
    DELAYED("Delayed"), 
    CANCELLED("Cancelled");

    private final String dbValue;

    DeliveryStatus(String dbValue) 
    {
        this.dbValue = dbValue;
    }

    public String getDbValue() 
    {
        return dbValue;
    }

    public static DeliveryStatus fromDbValue(String dbVal) 
    {
        return valueOf(dbVal.toUpperCase().replace(" ", "_"));
    }
}