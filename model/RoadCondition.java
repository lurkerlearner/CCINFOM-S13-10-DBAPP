package model;

public enum RoadCondition
{
    ACCESSIBLE("Accessible"),
    PARTIALLY_FLOODED("Partially Flooded"),
    NOT_ACCESSIBLE("Not Accessible");

    private final String dbValue;

    RoadCondition(String dbValue) 
    {
        this.dbValue = dbValue;
    }

    public String getDbValue() 
    {
        return dbValue;
    }

    public static RoadCondition fromDbValue(String dbVal) 
    {
        return valueOf(dbVal.toUpperCase().replace(" ", "_"));
    }
}