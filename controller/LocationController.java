package controller;

import DAO.*;
import model.*;
import java.util.*;

public class LocationController {
    private LocationDAO locationDAO;

    public LocationController() {
        locationDAO = new LocationDAO();
    }

    public boolean addLocation(String street, String city, String zip) {
        Location location = new Location();
        location.setStreet(street);
        location.setCity(city);
        location.setZip(zip);

        return locationDAO.addLocation(location) > 0;
    }

    public List<Location> getAllLocations() {
        return locationDAO.getAllLocations();
    }

    public List<Location> searchLocations(String type, String value) {
        return locationDAO.searchLocations(type, value);
    }

    public Location getLocationById(int id) {
        return locationDAO.getLocationById(id);
    }
}

