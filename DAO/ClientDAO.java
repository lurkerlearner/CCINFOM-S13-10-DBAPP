package DAO;

import java.sql.*;
import model.*;
import app.*;
import java.util.*;

public class ClientDAO {

    public int addClient(Client c) {
        String sql = "INSERT INTO client (name, contact_no, password, unit_details, date_created, location_id, plan_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, c.getName());
            stmt.setString(2, c.getContactNo());
            stmt.setString(3, c.getPassword());
            stmt.setString(4, c.getUnitDetails());
            stmt.setDate(5, java.sql.Date.valueOf(c.getDateCreated()));
            stmt.setInt(6, c.getLocationID());
            stmt.setInt(7, c.getPlanID());

            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }


    public void addClientDietPreferences(int clientId, List<Integer> dietPrefIds) {
        String sql = "INSERT INTO client_diet_preference (client_id, diet_preference_id) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (Integer dietId : dietPrefIds) {
                ps.setInt(1, clientId);
                ps.setInt(2, dietId);
                ps.addBatch();
            }
            ps.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void loadDietPreferences(Client c, Connection conn) throws SQLException {
        String sql = "SELECT diet_preference_id FROM client_diet_preference WHERE client_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, c.getClientID());
            try (ResultSet rs = ps.executeQuery()) {
                List<Integer> dietIds = new ArrayList<>();
                while (rs.next()) {
                    dietIds.add(rs.getInt("diet_preference_id"));
                }
                c.setDietPreferenceIDs(dietIds);
            }
        }
    }


    public Client getClientById(int id) {
        String sql = "SELECT * FROM CLIENT WHERE client_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Client c = mapResultSetToClient(rs);
                loadDietPreferences(c, conn);
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

                loadDietPreferences(c,conn);

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

    public boolean deleteClient(int clientId) {
        String sql = "DELETE FROM CLIENT WHERE client_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, clientId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLIntegrityConstraintViolationException e) {
            //for FK stuff
            System.err.println("Cannot delete client due to related records: " + e.getMessage());
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Integer> getClientDietPreferences(int clientId) {
        List<Integer> dietIds = new ArrayList<>();
        String sql = "SELECT diet_preference_id FROM CLIENT_DIET_PREFERENCE WHERE client_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, clientId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                dietIds.add(rs.getInt("diet_preference_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dietIds;
    }


    public boolean updateClient(int clientId, String name, String contact, String password,
                                String unit, int planId, int locationId) {
        String sql = "UPDATE CLIENT SET name = ?, contact_no = ?, password = ?, unit_details = ?, plan_id = ?, location_id = ? WHERE client_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, contact);
            ps.setString(3, password);
            ps.setString(4, unit);
            ps.setInt(5, planId);
            ps.setInt(6, locationId);
            ps.setInt(7, clientId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void updateClientDietPreferences(int clientId, List<Integer> dietIds) throws SQLException {
        Connection conn = DBConnection.getConnection(); // single connection for the whole method
        try {
            conn.setAutoCommit(false); // start transaction

            String deleteSql = "DELETE FROM CLIENT_DIET_PREFERENCE WHERE client_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(deleteSql)) {
                ps.setInt(1, clientId);
                ps.executeUpdate();
            }

            String insertSql = "INSERT INTO CLIENT_DIET_PREFERENCE(diet_preference_id, client_id) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                for (Integer dietId : dietIds) {
                    ps.setInt(1, dietId);
                    ps.setInt(2, clientId);
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
        } finally {
            conn.setAutoCommit(true);
        }
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
        return c;
    }



}
