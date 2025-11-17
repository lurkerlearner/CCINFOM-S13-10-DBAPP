package model;/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */

/**
 *
 * @author faith
 */
public enum Measurement_unit {
    GRAMS("grams"), 
    MILLILITRES("millilitres");

    private final String dbValue;

    Measurement_unit(String dbValue) 
    {
        this.dbValue = dbValue;
    }

    public String getDbValue() 
    {
        return dbValue;
    }

    // converts db string value to java enum because java enums needs to be all caps and no spaces
    public static Measurement_unit fromDbValue(String dbVal) 
    {
        return valueOf(dbVal.toUpperCase());
    }
}
