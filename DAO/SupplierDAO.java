package DAO;

import model.*;
import app.*;

import java.sql.*;
import java.util.ArrayList;

public class SupplierDAO {

    public boolean addSupplier(Supplier supplier) {
        // model.Restock_status restock_status = model.Restock_status.calculateStatus(ingredient.getStock_quantity());
        String sqlQuery = "INSERT INTO SUPPLIER (supplier_name, contact_no, alt_contact_no, location_id) " +
        "VALUES (?, ?, ?, ?)";

        // RETURN_GENERATED_KEYS to get the auto generated supplier_id from the database
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) 
        {
            stmt.setString(1, supplier.getSupplier_name());
            stmt.setString(2, supplier.getContact_no());
            stmt.setString(3, supplier.getAlt_contact_no());
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

    public boolean updateSupplierAll(Supplier supplier) {
        // Check if supplier has a valid ID (should exist in database to update)
        if (supplier.getSupplier_id() <= 0) 
        {
            System.err.println("Error: model.Supplier ID is invalid for update");
            return false;
        }

        String sqlQuery = "UPDATE SUPPLIER SET supplier_name = ?, contact_no = ?, " +
        "alt_contact_no = ?, location_id = ? WHERE supplier_id = ?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sqlQuery)) 
        {
            stmt.setString(1, supplier.getSupplier_name());
            stmt.setString(2, supplier.getContact_no());
            stmt.setString(3, supplier.getAlt_contact_no());
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

    public boolean updateSupplierName(int supplier_id, String newName) {

        String sqlQuery = "UPDATE SUPPLIER SET supplier_name = ? WHERE supplier_id = ?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sqlQuery)) 
        {
            stmt.setString(1, newName);
            stmt.setInt(2, supplier_id); // WHERE clause

            int affectedRows = stmt.executeUpdate();
            
            return affectedRows > 0;
        }
        catch (SQLException e) {
            System.err.println("Error updating name: " + e.getMessage());
            return false;
        }
    }

    public boolean updateContactNo(int supplier_id, String newContact) {

        String sqlQuery = "UPDATE SUPPLIER SET contact_no = ? WHERE supplier_id = ?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sqlQuery)) 
        {
            stmt.setString(1, newContact);
            stmt.setInt(2, supplier_id); // WHERE clause

            int affectedRows = stmt.executeUpdate();
            
            return affectedRows > 0;
        }
        catch (SQLException e) {
            System.err.println("Error updating contact number: " + e.getMessage());
            return false;
        }
    }

    public boolean updateAltContactNo(int supplier_id, String newAltContact) {

        String sqlQuery = "UPDATE SUPPLIER SET alt_contact_no = ? WHERE supplier_id = ?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sqlQuery)) 
        {
            stmt.setString(1, newAltContact);
            stmt.setInt(2, supplier_id); // WHERE clause

            int affectedRows = stmt.executeUpdate();
            
            return affectedRows > 0;
        }
        catch (SQLException e) {
            System.err.println("Error updating alt contact number: " + e.getMessage());
            return false;
        }
    }
    
    public boolean updateLocationID(int supplier_id, int location_id) {

        String sqlQuery = "UPDATE SUPPLIER SET location_id = ? WHERE supplier_id = ?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sqlQuery)) 
        {
            stmt.setInt(1, location_id);
            stmt.setInt(2, supplier_id); // WHERE clause

            int affectedRows = stmt.executeUpdate();
            
            return affectedRows > 0;
        }
        catch (SQLException e) {
            System.err.println("Error updating location ID: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteSupplier(int supplier_id) {
        // Check if supplier exists
        if (supplier_id <= 0) {
            System.err.println("Error: model.Supplier ID is invalid for deletion");
            return false;
        }

        String sqlQuery = "DELETE FROM SUPPLIER WHERE supplier_id = ?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sqlQuery)) 
        {
            stmt.setInt(1, supplier_id);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) 
            {
                System.out.println("model.Supplier ID " + supplier_id + " successfully deleted!");
                return true;
            } 
            else 
            {
                System.out.println("No supplier found with ID: " + supplier_id);
                return false;
            }
        }
        catch (SQLException e) {
            System.err.println("Error deleting supplier: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<Supplier> getAllSuppliers() {
        ArrayList<Supplier> suppliers = new ArrayList<>();
        String sqlQuery = "SELECT * FROM SUPPLIER";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlQuery)) 
        {
            try (ResultSet rs = stmt.executeQuery()) 
            {
                while (rs.next()) 
                {
                    Supplier supplier = new Supplier(
                        rs.getInt("supplier_id"),
                        rs.getString("supplier_name"),
                        rs.getString("contact_no"),
                        rs.getString("alt_contact_no"),
                        rs.getInt("location_id")
                    );
                    suppliers.add(supplier);
                }
            } 
        }
        catch (SQLException e) 
        {
            System.err.println("Error retrieving suppliers: " + e.getMessage());
        }

        return suppliers;
    }

    public ArrayList<Supplier> getSuppliersByLocation(int location_id) {
        ArrayList<Supplier> suppliers = new ArrayList<>();
        String sqlQuery = "SELECT * FROM SUPPLIER WHERE location_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlQuery)) 
        {
            stmt.setInt(1, location_id);

            try (ResultSet rs = stmt.executeQuery()) 
            {
                while (rs.next()) 
                {
                    Supplier supplier = new Supplier(
                        rs.getInt("supplier_id"),
                        rs.getString("supplier_name"),
                        rs.getString("contact_no"),
                        rs.getString("alt_contact_no"),
                        rs.getInt("location_id")
                    );
                    suppliers.add(supplier);
                }
            } 
        } 
        catch (SQLException e) 
        {
            System.err.println("Error retrieving suppliers by location: " + e.getMessage());
        }

        return suppliers;
    }

    public Supplier getSupplierById(int supplier_id) {
        String sqlQuery = "SELECT * FROM SUPPLIER WHERE supplier_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlQuery)) 
        {
            stmt.setInt(1, supplier_id);

            try (ResultSet rs = stmt.executeQuery()) 
            {
                if (rs.next()) 
                {
                    return new Supplier(
                        rs.getInt("supplier_id"),
                        rs.getString("supplier_name"),
                        rs.getString("contact_no"),
                        rs.getString("alt_contact_no"),
                        rs.getInt("location_id")
                    );
                }
            } 
        } 
        catch (SQLException e) 
        {
            System.err.println("Error retrieving supplier by ID: " + e.getMessage());
        }

        return null; // Return null if supplier not found
    }

}
