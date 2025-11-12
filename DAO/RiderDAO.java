package DAO;

import model.*;
import java.sql.*;
import java.util.ArrayList;

public class RiderDAO 
{
    private Connection c;

    public RiderDAO(Connection connection)
    {
        this.c = connection;
    }

    // MAIN OPERATIONS (create, select by pk, select all, delete)

    // Insert a new rider record into the table
    public void addRider(Rider r) 
    {
        String sql = "INSERT INTO rider (rider_name, hire_date, contact_no) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            stmt.setString(1, r.getRiderName());
            stmt.setDate(2, r.getHireDate());
            stmt.setString(3, r.getContactNo());
            
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) 
            {
                int generatedID = rs.getInt(1);
                r.setRiderID(generatedID);
            }
        } 
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    // HELPER METHOD (for building riders in methods)
    public Rider copyRiderData(ResultSet rs) throws SQLException
    {
        Rider r = new Rider();

        r.setRiderID(rs.getInt("rider_id"));
        r.setRiderName(rs.getString("rider_name"));
        r.setHireDate(rs.getDate("hire_date"));
        r.setContactNo(rs.getString("contact_no"));

        return r;
    }

    // Select a rider record from the table by its primary key
    public Rider getRiderByKey(int key)
    {
        String sql = "SELECT * FROM rider WHERE rider_id = ?";

        try (PreparedStatement stmt = c.prepareStatement(sql))
        {
            stmt.setInt(1, key);

            ResultSet rs = stmt.executeQuery();

            if(rs.next())
            {
                return copyRiderData(rs);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    // Select all existing records in the rider table
    public ArrayList<Rider> getAllRiders()
    {
        String sql = "SELECT * FROM rider";
        ArrayList<Rider> riders = new ArrayList<>();

        try (PreparedStatement stmt = c.prepareStatement(sql))
        {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) 
            {
                riders.add(copyRiderData(rs));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return riders;
    }

    // Delete a record from the rider table given a primary key
    public boolean deleteRider(int key)
    {
        String sql = "DELETE FROM rider WHERE rider_id = ?";

        try (PreparedStatement stmt = c.prepareStatement(sql))
        {
            stmt.setInt(1, key);
            
            int deletedRows = stmt.executeUpdate();
            
            return deletedRows > 0;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return false;
    }

    // METHOD FOR MODIFICATION 

    // Modify a rider record's data
    public boolean modifyRiderColumn(int key, String col, Object newValue)
    {
        String sql = "UPDATE rider SET " + col + " = ? WHERE rider_id = ?";

        try (PreparedStatement stmt = c.prepareStatement(sql)) 
        {
            stmt.setObject(1, newValue);
            stmt.setInt(2, key);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
            return false;
        }
    }

    // SIMPLE FILTERS (single field)

    // Select all riders hired before a date
    public ArrayList<Rider> getRidersHiredBefore(Date d)
    {
        String sql = "SELECT * FROM rider WHERE hire_date < ?";
        ArrayList<Rider> riders = new ArrayList<>();

        try (PreparedStatement stmt = c.prepareStatement(sql))
        {
            stmt.setDate(1, d);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) 
            {
                riders.add(copyRiderData(rs));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return riders;
    }

    // Select all riders hired after a date
    public ArrayList<Rider> getRidersHiredAfter(Date d)
    {
        String sql = "SELECT * FROM rider WHERE hire_date > ?";
        ArrayList<Rider> riders = new ArrayList<>();

        try (PreparedStatement stmt = c.prepareStatement(sql))
        {
            stmt.setDate(1, d);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) 
            {
                riders.add(copyRiderData(rs)); 
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return riders;
    }

    // MORE ADVANCED FILTERS (joins, aggregates, sorts)

    // Count the number of deliveries each rider has made. Sort by most deliveries to least.
    public ArrayList<Object[]> getDeliveryCountPerRider() 
    {
        String sql = "SELECT r.rider_name, COUNT(d.transaction_id) AS deliveries_made " +
                     "FROM rider AS r JOIN delivery AS d ON r.rider_id = d.rider_id " +
                     "GROUP BY r.rider_name, r.rider_id " + 
                     "ORDER BY deliveries_made DESC, r.rider_name ASC";

        ArrayList<Object[]> result = new ArrayList<>();
        try (PreparedStatement stmt = c.prepareStatement(sql)) 
        {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) 
            {
                Object[] row = 
                {
                    rs.getString("rider_name"), 
                    rs.getInt("deliveries_made")
                };
                result.add(row);
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
        return result;
    }
}
