package model;

public enum AltDeliveryMethod
{
    MOTORCYCLE("Motorcycle"), 
    TIKLING_TRICYCLE("Tikling Tricycle"), 
    DRONE("Drone"), 
    BOAT("Boat"), 
    TRUCK("Truck");

    private final String dbValue;

    AltDeliveryMethod(String dbValue) 
    {
        this.dbValue = dbValue;
    }

    public String getDbValue() 
    {
        return dbValue;
    }

    public static AltDeliveryMethod fromDbValue(String dbVal) 
    {
        return valueOf(dbVal.toUpperCase().replace(" ", "_"));
    }
}