import java.sql.*;

public class LocationDAO {

    public int addLocation(String street, String city, String zip) {
        String sql = "INSERT INTO location (street_address, city, zip_code) VALUES (?,?,?)";

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
}
