package controller;

import DAO.LocationDAO;
import model.Location;
import java.util.*;

public class LocationController {
    private final LocationDAO locationDAO;

    public LocationController(LocationDAO dao) {
        this.locationDAO = dao;
    }

    /* public boolean addLocation(String street, String city, String zip) {
        Location location = new Location();
        location.setStreet(street);
        location.setCity(city);
        location.setZip(zip);

        
    } */
}
