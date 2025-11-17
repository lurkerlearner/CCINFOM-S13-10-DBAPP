package model;/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */

/*
 *
 * @author faith
 */

public enum Restock_status {
    AVAILABLE("Available"), 
    LOW_STOCK("Low Stock"), 
    OUT_OF_STOCK("Out of Stock");

    private final String dbValue;

    Restock_status(String dbValue) 
    {
        this.dbValue = dbValue;
    }

    public String getDbValue() 
    {
        return dbValue;
    }

    public static Restock_status calculateStatus(double stock_quantity) {
    if (stock_quantity == 0) {
            return OUT_OF_STOCK;
        } else if (stock_quantity > 0 && stock_quantity <= 1000) {
            return LOW_STOCK;
        } else {
            return AVAILABLE;
        }
    }

    // converts db string value to java enum because java enums needs to be all caps and no spaces
    public static Restock_status fromDbValue(String dbVal) 
    {
        return valueOf(dbVal.toUpperCase().replace(" ", "_"));
    }
}

