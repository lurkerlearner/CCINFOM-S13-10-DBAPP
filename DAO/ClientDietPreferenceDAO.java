package DAO;

import model.*;
import app.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDietPreferenceDAO {

    public boolean add(ClientDietPreference cdp) {

        String checkClient = "SELECT COUNT(*) FROM client WHERE client_id = ?";
        String checkDiet = "SELECT COUNT(*) FROM diet_preference WHERE diet_preference_id = ?";
        String checkDuplicate =
                "SELECT COUNT(*) FROM client_diet_preference WHERE client_id = ? AND diet_preference_id = ?";
        String insert =
                "INSERT INTO client_diet_preference (diet_preference_id, client_id) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection()) {

            try (PreparedStatement ps = conn.prepareStatement(checkClient)) {
                ps.setInt(1, cdp.getClientID());
                ResultSet rs = ps.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) {
                    return false; // client not found
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(checkDiet)) {
                ps.setInt(1, cdp.getDietPreferenceID());
                ResultSet rs = ps.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) {
                    return false; // diet preference not found
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(checkDuplicate)) {
                ps.setInt(1, cdp.getClientID());
                ps.setInt(2, cdp.getDietPreferenceID());
                ResultSet rs = ps.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    return false; // already exists
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(insert)) {
                ps.setInt(1, cdp.getDietPreferenceID());
                ps.setInt(2, cdp.getClientID());
                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public List<ClientDietPreference> getAll() {
        List<ClientDietPreference> list = new ArrayList<>();
        String sql = "SELECT * FROM client_diet_preference ORDER BY client_id";

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new ClientDietPreference(
                        rs.getInt("diet_preference_id"),
                        rs.getInt("client_id")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<ClientDietPreference> searchByDietPref(int dietPrefID) {
        List<ClientDietPreference> list = new ArrayList<>();
        String sql = "SELECT * FROM client_diet_preference WHERE diet_preference_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, dietPrefID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new ClientDietPreference(
                        rs.getInt("diet_preference_id"),
                        rs.getInt("client_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<ClientDietPreference> searchByClient(int clientID) {
        List<ClientDietPreference> list = new ArrayList<>();
        String sql = "SELECT * FROM client_diet_preference WHERE client_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, clientID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new ClientDietPreference(
                        rs.getInt("diet_preference_id"),
                        rs.getInt("client_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean updateClientDietPreference(int clientId, int newDietPrefId) {
        String deleteSql = "DELETE FROM CLIENT_DIET_PREFERENCE WHERE client_id = ?";
        String insertSql = "INSERT INTO CLIENT_DIET_PREFERENCE (diet_preference_id, client_id) VALUES (?, ?)";

        try {Connection conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // transaction start

            // Delete existing mapping(s)
            try (PreparedStatement psDel = conn.prepareStatement(deleteSql)) {
                psDel.setInt(1, clientId);
                psDel.executeUpdate();
            }

            // Insert new mapping
            try (PreparedStatement psIns = conn.prepareStatement(insertSql)) {
                psIns.setInt(1, newDietPrefId);
                psIns.setInt(2, clientId);
                psIns.executeUpdate();
            }

            conn.commit(); // transaction commit
            conn.setAutoCommit(true);
            return true;

        } catch (SQLException e) {
            try { Connection conn = DBConnection.getConnection();
                conn.rollback(); conn.setAutoCommit(true); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
            return false;
        }
    }

    public boolean addMapping(int dietPrefID, int clientID) {
        String sql = "INSERT INTO CLIENT_DIET_PREFERENCE (diet_preference_id, client_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, dietPrefID);
            ps.setInt(2, clientID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeMapping(int dietPrefID, int clientID) {
        String countSql = "SELECT COUNT(*) AS total FROM CLIENT_DIET_PREFERENCE WHERE client_id = ?";
        String deleteSql = "DELETE FROM CLIENT_DIET_PREFERENCE WHERE diet_preference_id = ? AND client_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement countPs = conn.prepareStatement(countSql)) {

            countPs.setInt(1, clientID);
            ResultSet rs = countPs.executeQuery();
            if (rs.next() && rs.getInt("total") <= 1) {
               //safe measure to ensure that they have at least one diet pref
                return false;
            }

            try (PreparedStatement deletePs = conn.prepareStatement(deleteSql)) {
                deletePs.setInt(1, dietPrefID);
                deletePs.setInt(2, clientID);
                return deletePs.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ClientDietPreference> getAllMappings() {
        List<ClientDietPreference> list = new ArrayList<>();
        String sql = "SELECT diet_preference_id, client_id FROM CLIENT_DIET_PREFERENCE";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new ClientDietPreference(
                        rs.getInt("diet_preference_id"),
                        rs.getInt("client_id")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean setClientDietPreferences(int clientId, List<Integer> dietPrefIds) {
        String deleteSql = "DELETE FROM client_diet_preference WHERE client_id = ?";
        String insertSql = "INSERT INTO client_diet_preference (diet_preference_id, client_id) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement psDel = conn.prepareStatement(deleteSql)) {
                psDel.setInt(1, clientId);
                psDel.executeUpdate();
            }

            try (PreparedStatement psIns = conn.prepareStatement(insertSql)) {
                for (Integer dietId : dietPrefIds) {
                    psIns.setInt(1, dietId);
                    psIns.setInt(2, clientId);
                    psIns.addBatch();
                }
                psIns.executeBatch();
            }

            conn.commit();
            conn.setAutoCommit(true);
            return true;
        } catch (SQLException e) {
            try {
                Connection conn = DBConnection.getConnection();
                conn.rollback();
                conn.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        }
    }



}
