package DAO;

import model.*;
import app.*;
import java.sql.*;
import java.util.*;

public class LocationDAO {

    public int addLocation(String street, String city, String zip) {
        String sql = "INSERT INTO LOCATION (street_address, city, zip_code) VALUES (?,?,?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, street);
            ps.setString(2, city);
            ps.setString(3, zip);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public Location getLocationById(int id) {
        String sql = "SELECT * FROM LOCATION WHERE location_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            System.out.println("Looking for location ID: " + id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Location(
                        rs.getInt("location_id"),
                        rs.getString("street_address"),
                        rs.getString("city"),
                        rs.getString("zip_code")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Location> getAllLocations() {
        List<Location> locations = new ArrayList<>();
        String sql = "SELECT * FROM LOCATION ORDER BY location_id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                locations.add(new Location(
                        rs.getInt("location_id"),
                        rs.getString("street_address"),
                        rs.getString("city"),
                        rs.getString("zip_code")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return locations;
    }


}
