import java.sql.*;
import java.util.*;

public class SupplierDAO {

    public boolean addSupplier(Supplier supplier) {
        // Restock_status restock_status = Restock_status.calculateStatus(ingredient.getStock_quantity());
        String sqlQuery = "INSERT INTO SUPPLIER (supplier_name, contact_no, alt_contact_no, location_id) " +
        "VALUES (?, ?, ?, ?)";

        // RETURN_GENERATED_KEYS to get the auto generated supplier_id from the database
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) 
        {
            stmt.setString(1, supplier.getSupplier_name());
            stmt.setInt(2, supplier.getContact_no());
            stmt.setInt(3, supplier.getAlt_contact_no());
            stmt.setInt(4, supplier.getLocation_id()); // ensure that location table already has records

            int affectedRows = stmt.executeUpdate();
            
            // successfully added/inserted if affectedRows > 1
            if (affectedRows > 0) {
                // Retrieve the auto-generated supplier_id
                // go through the generated id for each supplier (in sql), and assign it to the supplier (in java)
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        supplier.setSupplier_id(generatedId);
                    }
                }
                return true;
            }
            return false;
        }

        catch (SQLException e) 
        {
            System.err.println("Error adding supplier: " + e.getMessage());
            return false;
        }
    } 

    public boolean updateSupplier(Supplier supplier) {
        // Check if supplier has a valid ID (should exist in database to update)
        if (supplier.getSupplier_id() <= 0) 
        {
            System.err.println("Error: Supplier ID is invalid for update");
            return false;
        }

        String sqlQuery = "UPDATE SUPPLIER SET supplier_name = ?, contact_no = ?, " +
        "alt_contact_no = ?, location_id = ? WHERE supplier_id = ?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sqlQuery)) 
        {
            stmt.setString(1, supplier.getSupplier_name());
            stmt.setInt(2, supplier.getContact_no());
            stmt.setInt(3, supplier.getAlt_contact_no());
            stmt.setInt(4, supplier.getLocation_id());
            stmt.setInt(5, supplier.getSupplier_id()); // WHERE clause

            int affectedRows = stmt.executeUpdate();
            
            return affectedRows > 0;
        }
        catch (SQLException e) {
            System.err.println("Error updating supplier: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteSupplier(int supplierId) {
        // Check if supplier exists
        if (supplierId <= 0) {
            System.err.println("Error: Supplier ID is invalid for deletion");
            return false;
        }

        String sqlQuery = "DELETE FROM SUPPLIER WHERE supplier_id = ?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sqlQuery)) 
        {
            stmt.setInt(1, supplierId);

            int affectedRows = stmt.executeUpdate();
            
            return affectedRows > 0;
        }
        catch (SQLException e) {
            System.err.println("Error deleting supplier: " + e.getMessage());
            return false;
        }
    }

}
