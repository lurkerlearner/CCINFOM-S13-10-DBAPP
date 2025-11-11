import java.sql.*;

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

}
