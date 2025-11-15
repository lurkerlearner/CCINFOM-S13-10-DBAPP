package DAO;

import model.*;
import controller.*;
import java.sql.*;
import java.util.ArrayList;

public class DeliveryDAO 
{
    private Connection c;

    public DeliveryDAO(Connection connection)
    {
        this.c = connection;
    }

    // MAIN OPERATIONS (create, select by pk, select all, delete)

    // Insert a new delivery record into the table
    public boolean addDelivery(Delivery d)
    {
        String sql = "INSERT INTO delivery (order_date, time_ordered, time_delivered, " +
                     "payment_mode, payment_status, delivery_method, delivery_status, " +
                     "client_id, meal_id, rider_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            
            stmt.setDate(1, d.getOrderDate());

            if (d.getTimeOrdered() != null)
                stmt.setTime(2, d.getTimeOrdered());
            else
                stmt.setNull(2, Types.TIME);

            if (d.getTimeDelivered() != null)
                stmt.setTime(3, d.getTimeDelivered());
            else
                stmt.setNull(3, Types.TIME);

            stmt.setString(4, d.getPaymentMode().getDbValue());
            stmt.setString(5, d.getPaymentStatus().getDbValue());
            stmt.setString(6, d.getDeliveryMethod().getDbValue());
            stmt.setString(7, d.getDeliveryStatus().getDbValue());
            stmt.setInt(8, d.getClientID());
            stmt.setInt(9, d.getMealID());
            stmt.setInt(10, d.getRiderID());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) 
            {
                int generatedID = rs.getInt(1);
                d.setTransactionID(generatedID);
                return true;
            }
        } 
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }

        return false;
    }

    // HELPER METHOD (for building deliveries in methods)
    public Delivery copyDeliveryData(ResultSet rs) throws SQLException
    {
        Delivery d = new Delivery();

        d.setTransactionID(rs.getInt("transaction_id"));
        d.setOrderDate(rs.getDate("order_date"));
        
        Time timeOrdered = rs.getTime("time_ordered");
        d.setTimeOrdered(timeOrdered != null ? timeOrdered : null);

        Time timeDelivered = rs.getTime("time_delivered");
        d.setTimeDelivered(timeDelivered != null ? timeDelivered : null);
    
        d.setPaymentMode(PaymentMode.fromDbValue(rs.getString("payment_mode")));
        d.setPaymentStatus(PaymentStatus.fromDbValue(rs.getString("payment_status")));
        d.setDeliveryMethod(DeliveryMethod.fromDbValue(rs.getString("delivery_method")));
        d.setDeliveryStatus(DeliveryStatus.fromDbValue(rs.getString("delivery_status")));
        d.setClientID(rs.getInt("client_id"));
        d.setMealID(rs.getInt("meal_id"));
        d.setRiderID(rs.getInt("rider_id"));

        return d;
    }

    // Select a delivery record from the table by its primary key
    public Delivery getDeliveryByKey(int key)
    {
        String sql = "SELECT * FROM delivery WHERE transaction_id = ?";

        try (PreparedStatement stmt = c.prepareStatement(sql))
        {
            stmt.setInt(1, key);

            ResultSet rs = stmt.executeQuery();

            if(rs.next())
                return copyDeliveryData(rs);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    // Select all existing records in the delivery table
    public ArrayList<Delivery> getAllDeliveries()
    {
        String sql = "SELECT * FROM delivery";
        ArrayList<Delivery> deliveries = new ArrayList<>();

        try (PreparedStatement stmt = c.prepareStatement(sql))
        {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) 
            {
                deliveries.add(copyDeliveryData(rs)); 
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return deliveries;
    }

    // Delete a record from the delivery table given a primary key
    public boolean deleteDelivery(int key)
    {
        String sql = "DELETE FROM delivery WHERE transaction_id = ?";

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

    // METHODS FOR MODIFICATION 

    // Change a delivery record's non-enum data 
    public boolean changeDeliveryColumn(int key, String col, Object newValue)
    {
        String sql = "UPDATE delivery SET " + col + " = ? WHERE transaction_id = ?";

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

    // HELPER METHOD (for executing updates)
    public boolean executeDeliveryUpdate(String sql, String newValue, int transaction_id)
    {
        try (PreparedStatement stmt = c.prepareStatement(sql)) 
        {
            stmt.setString(1, newValue); 
            stmt.setInt(2, transaction_id);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } 
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    // Update a record's payment mode
    public boolean updatePaymentMode(int key, PaymentMode newValue)
    {
        String sql = "UPDATE delivery SET payment_mode = ? WHERE transaction_id = ?";
        return executeDeliveryUpdate(sql, newValue.getDbValue(), key);
    }

    // Update a record's payment status
    public boolean updatePaymentStatus(int key, PaymentStatus newValue)
    {
        String sql = "UPDATE delivery SET payment_status = ? WHERE transaction_id = ?";
        return executeDeliveryUpdate(sql, newValue.getDbValue(), key);
    }

    // Update a record's delivery status
    public boolean updateDeliveryStatus(int key, DeliveryStatus newValue)
    {
        String sql = "UPDATE delivery SET delivery_status = ? WHERE transaction_id = ?";
        return executeDeliveryUpdate(sql, newValue.getDbValue(), key);
    }

    // Update a record's delivery method
    public boolean updateDeliveryMethod(int key, DeliveryMethod newValue)
    {
        String sql = "UPDATE delivery SET delivery_method = ? WHERE transaction_id = ?";
        return executeDeliveryUpdate(sql, newValue.getDbValue(), key);
    }

    // SIMPLE FILTERS (single field)

    // Select all deliveries made by one client
    public ArrayList<Delivery> getDeliveriesByClient(int client)
    {
        String sql = "SELECT * FROM delivery WHERE client_id = ?";
        ArrayList<Delivery> deliveries = new ArrayList<>();

        try (PreparedStatement stmt = c.prepareStatement(sql))
        {
            stmt.setInt(1, client);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) 
            {
                deliveries.add(copyDeliveryData(rs)); 
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return deliveries;
    }

    // Select all deliveries with a specific meal ordered
    public ArrayList<Delivery> getDeliveriesByMeal(int meal)
    {
        String sql = "SELECT * FROM delivery WHERE meal_id = ?";
        ArrayList<Delivery> deliveries = new ArrayList<>();

        try (PreparedStatement stmt = c.prepareStatement(sql))
        {
            stmt.setInt(1, meal);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) 
            {
                deliveries.add(copyDeliveryData(rs)); 
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return deliveries;
    }

    // Select all deliveries made within a certain date range
    public ArrayList<Delivery> getDeliveriesWithinDateRange(Date d1, Date d2)
    {
        String sql = "SELECT * FROM delivery WHERE order_date >= ? AND order_date <= ?";
        ArrayList<Delivery> deliveries = new ArrayList<>();

        try (PreparedStatement stmt = c.prepareStatement(sql))
        {
            stmt.setDate(1, d1);
            stmt.setDate(2, d2);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) 
            {
                deliveries.add(copyDeliveryData(rs)); 
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return deliveries;
    }

    // Select all deliveries made before a date
    public ArrayList<Delivery> getDeliveriesBeforeDate(Date date)
    {
        String sql = "SELECT * FROM delivery WHERE order_date < ?";
        ArrayList<Delivery> deliveries = new ArrayList<>();

        try (PreparedStatement stmt = c.prepareStatement(sql))
        {
            stmt.setDate(1, date);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) 
            {
                deliveries.add(copyDeliveryData(rs));  
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return deliveries;
    }

    // Select all deliveries made after a date
    public ArrayList<Delivery> getDeliveriesAfterDate(Date date)
    {
        String sql = "SELECT * FROM delivery WHERE order_date > ?";
        ArrayList<Delivery> deliveries = new ArrayList<>();

        try (PreparedStatement stmt = c.prepareStatement(sql))
        {
            stmt.setDate(1, date);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) 
            {
                deliveries.add(copyDeliveryData(rs)); 
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return deliveries;
    }

    // Select all deliveries with a certain payment mode
    public ArrayList<Delivery> getDeliveriesByPaymentMode(PaymentMode pm)
    {
        String sql = "SELECT * FROM delivery WHERE payment_mode = ?";
        ArrayList<Delivery> deliveries = new ArrayList<>();

        try (PreparedStatement stmt = c.prepareStatement(sql))
        {
            stmt.setString(1, pm.getDbValue());

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) 
            {
                deliveries.add(copyDeliveryData(rs)); 
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return deliveries;
    }

    // Select all deliveries with a certain payment status
    public ArrayList<Delivery> getDeliveriesByPaymentStatus(PaymentStatus ps)
    {
        String sql = "SELECT * FROM delivery WHERE payment_status = ?";
        ArrayList<Delivery> deliveries = new ArrayList<>();

        try (PreparedStatement stmt = c.prepareStatement(sql))
        {
            stmt.setString(1, ps.getDbValue());

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) 
            {
                deliveries.add(copyDeliveryData(rs)); 
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return deliveries;
    }

    // Select all deliveries with a certain delivery status
    public ArrayList<Delivery> getDeliveriesByDeliveryStatus(DeliveryStatus ds)
    {
        String sql = "SELECT * FROM delivery WHERE delivery_status = ?";
        ArrayList<Delivery> deliveries = new ArrayList<>();

        try (PreparedStatement stmt = c.prepareStatement(sql))
        {
            stmt.setString(1, ds.getDbValue());

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) 
            {
                deliveries.add(copyDeliveryData(rs)); 
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return deliveries;
    }

    // Select all deliveries with a certain delivery method
    public ArrayList<Delivery> getDeliveriesByMethod(DeliveryMethod dm)
    {
        String sql = "SELECT * FROM delivery WHERE delivery_method = ?";
        ArrayList<Delivery> deliveries = new ArrayList<>();

        try (PreparedStatement stmt = c.prepareStatement(sql))
        {
            stmt.setString(1, dm.getDbValue());

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) 
            {
                deliveries.add(copyDeliveryData(rs)); 
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return deliveries;
    }

    // Select all deliveries handled by one rider
    public ArrayList<Delivery> getDeliveriesByRider(int rider)
    {
        String sql = "SELECT * FROM delivery WHERE rider_id = ?";
        ArrayList<Delivery> deliveries = new ArrayList<>();

        try (PreparedStatement stmt = c.prepareStatement(sql))
        {
            stmt.setInt(1, rider);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) 
            {
                deliveries.add(copyDeliveryData(rs));  
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return deliveries;
    }

    // MORE ADVANCED FILTERS (joins, aggregates, sorts)

    // Select all records that exist in the delivery table showing transaction id, 
    // name of client, and date of order, sorted from earliest to latest 
    public ArrayList<Object[]> getDeliveriesWithClientName()
    {
        String sql = "SELECT d.transaction_id, c.name AS client_name, d.order_date " +
                     "FROM delivery AS d " +
                     "JOIN client AS c ON d.client_id = c.client_id " +
                     "ORDER BY d.order_date ASC, d.time_ordered ASC";

        ArrayList<Object[]> result = new ArrayList<>();
        try (PreparedStatement stmt = c.prepareStatement(sql)) 
        {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) 
            {
                Object[] row = 
                {
                    rs.getInt("transaction_id"), 
                    rs.getString("client_name"),
                    rs.getDate("order_date")
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

    // Count the number of transactions each client has made and display with their
    // name. Results are displayed starting with the client with most deliveries.
    public ArrayList<Object[]> getDeliveryCountPerClient() 
    {
        String sql = "SELECT c.name AS client_name, COUNT(d.transaction_id) AS orders_made " +
                     "FROM delivery AS d JOIN client AS c ON d.client_id = c.client_id " +
                     "GROUP BY c.client_id, c.name " + 
                     "ORDER BY orders_made DESC, c.name ASC";

        ArrayList<Object[]> result = new ArrayList<>();
        try (PreparedStatement stmt = c.prepareStatement(sql)) 
        {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) 
            {
                Object[] row = 
                {
                    rs.getString("client_name"), 
                    rs.getInt("orders_made")
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

    // Count the number of times a meal was included in a delivery 
    // and sort by most to least ordered. 
    public ArrayList<Object[]> getMostDeliveredMeal()
    {
        String sql = "SELECT m.meal_name, COUNT(d.transaction_id) AS total_orders " +
                     "FROM delivery AS d JOIN meal AS m ON d.meal_id = m.meal_id " +
                     "GROUP BY m.meal_id, m.meal_name " + 
                     "ORDER BY total_orders DESC, m.meal_name ASC";

        ArrayList<Object[]> result = new ArrayList<>();
        try (PreparedStatement stmt = c.prepareStatement(sql)) 
        {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) 
            {
                Object[] row = 
                {
                    rs.getString("meal_name"), 
                    rs.getInt("total_orders")
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

    // Count the number of deliveries made to every location. Include the name of the
    // city, the severity of flooding in its area, and the road condition there. 
    public ArrayList<Object[]> getDeliveriesPerLocationWithFloodData()
    {
        String sql = "SELECT l.city, COUNT(d.transaction_id) AS deliveries, f.flood_factor, f.road_condition " +
                     "FROM delivery AS d " +
                     "JOIN client AS c ON d.client_id = c.client_id " +
                     "JOIN location AS l ON c.location_id = l.location_id " +
                     "JOIN flood_data AS f ON l.location_id = f.location_id  " +
                     "GROUP BY l.city, f.flood_factor, f.road_condition " + 
                     "ORDER BY deliveries DESC, l.city ASC";

        ArrayList<Object[]> result = new ArrayList<>();
        try (PreparedStatement stmt = c.prepareStatement(sql)) 
        {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) 
            {
                Object[] row = 
                {
                    rs.getString("city"), 
                    rs.getInt("deliveries"),
                    rs.getString("flood_factor"),
                    rs.getString("road_condition")
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
