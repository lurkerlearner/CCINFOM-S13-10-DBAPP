package model;

public enum DeliveryMethod
{
    MOTORCYCLE("Motorcycle"), 
    TIKLING_TRICYCLE("Tikling Tricycle"), 
    DRONE("Drone"), 
    BOAT("Boat"), 
    TRUCK("Truck");

    private final String dbValue;

    DeliveryMethod(String dbValue) 
    {
        this.dbValue = dbValue;
    }

    public String getDbValue() 
    {
        return dbValue;
    }

    public static DeliveryMethod fromDbValue(String dbVal) 
    {
        return valueOf(dbVal.toUpperCase().replace(" ", "_"));
    }
}