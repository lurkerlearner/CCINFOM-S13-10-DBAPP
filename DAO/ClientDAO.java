package DAO;

import java.sql.*;
import model.*;
import app.*;
import java.util.*;

public class ClientDAO {

    public int addClient(Client c) {
        String sql = "INSERT INTO client (name, contact_no, password, unit_details, date_created, " +
                "location_id, plan_id, diet_preference_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, c.getName());
            stmt.setString(2, c.getContactNo());
            stmt.setString(3, c.getPassword());
            stmt.setString(4, c.getUnitDetails());
            stmt.setDate(5, java.sql.Date.valueOf(c.getDateCreated()));
            stmt.setInt(6, c.getLocationID());
            stmt.setInt(7, c.getPlanID());
            stmt.setInt(8, c.getDietPreferenceID());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public Client getClientById(int id) {
        String sql = "SELECT * FROM CLIENT WHERE client_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            System.out.println("Looking for client ID: " + id); //debugging
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Client c = new Client(
                        rs.getInt("client_id"),
                        rs.getString("name"),
                        rs.getString("contact_no"),
                        rs.getString("password"),
                        rs.getString("unit_details"),
                        rs.getDate("date_created").toLocalDate(),
                        rs.getInt("location_id"),
                        rs.getInt("plan_id"),
                        rs.getInt("diet_preference_id")
                );
                return c;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public Client login(String contact, String password) {
        String sql = "SELECT * FROM client WHERE contact_no = ? AND password = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, contact);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Client c = new Client();
                c.setClientID(rs.getInt("client_id"));
                c.setName(rs.getString("name"));
                c.setContactNo(rs.getString("contact_no"));
                c.setPassword(rs.getString("password"));
                c.setUnitDetails(rs.getString("unit_details"));
                c.setDateCreated(rs.getDate("date_created").toLocalDate());
                c.setLocationID(rs.getInt("location_id"));
                c.setPlanID(rs.getInt("plan_id"));
                c.setDietPreferenceID(rs.getInt("diet_preference_id"));

                return c;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //FINDING DUPLICATES
    public boolean isContactExists(String contactNo) {
        String sql = "SELECT client_id FROM client WHERE contact_no = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, contactNo);
            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateClient(Client c) {
        String sql = "UPDATE CLIENT SET name = ?, contact_no = ?, unit_details = ?, location_id = ? WHERE client_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getName());
            ps.setString(2, c.getContactNo());
            ps.setString(3, c.getUnitDetails());
            ps.setInt(4, c.getLocationID());
            ps.setInt(5, c.getClientID());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<Client> getAllClients() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM client ORDER BY client_id";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                clients.add(mapResultSetToClient(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clients;
    }

    public List<Client> searchClients(String type, Object value) {
        List<Client> clients = new ArrayList<>();
        String sql = "";

        switch (type.toLowerCase()) {
            case "id":
                sql = "SELECT * FROM client WHERE client_id = ?";
                break;
            case "name":
                sql = "SELECT * FROM client WHERE name LIKE ?";
                break;
            case "contact":
                sql = "SELECT * FROM client WHERE contact_no LIKE ?";
                break;
            default:
                return clients;
        }

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            if (type.equalsIgnoreCase("name") || type.equalsIgnoreCase("contact")) {
                ps.setString(1, "%" + value + "%"); // partial match
            } else {
                ps.setInt(1, (int) value);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    clients.add(mapResultSetToClient(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clients;
    }

    private Client mapResultSetToClient(ResultSet rs) throws SQLException {
        Client c = new Client();
        c.setClientID(rs.getInt("client_id"));
        c.setName(rs.getString("name"));
        c.setContactNo(rs.getString("contact_no"));
        c.setPassword(rs.getString("password"));
        c.setUnitDetails(rs.getString("unit_details"));
        c.setDateCreated(rs.getDate("date_created").toLocalDate());
        c.setLocationID(rs.getInt("location_id"));
        c.setPlanID(rs.getInt("plan_id"));
        c.setDietPreferenceID(rs.getInt("diet_preference_id"));


        return c;
    }


}
