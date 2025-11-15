package DAO;

import model.*;
import app.*;
import controller.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MealDeliveryDAO
{
    private Connection c;

    public MealDeliveryDAO(Connection connection)
    {
        this.c = connection;
    }

    // Insert a new meal_delivery record into the table
    public boolean addMealDelivery(MealDelivery md) 
    {
        String sql = "INSERT INTO meal_delivery (meal_id, transaction_id, remarks) " +
                     "VALUES (?, ?, ?)";

        try (PreparedStatement stmt = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            stmt.setInt(1, md.getMealID());
            stmt.setInt(2, md.getTransactionID());
            stmt.setString(3, md.getRemarks());
            
            stmt.executeUpdate();
            return true;
        } 
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    // Delete a record from the meal_delivery table given two IDs
    public boolean deleteMealDelivery(int mealID, int transactionID)
    {
        String sql = "DELETE FROM meal_delivery " +
                     "WHERE meal_id = ? AND transaction_id = ?";

        try (PreparedStatement stmt = c.prepareStatement(sql))
        {
            stmt.setInt(1, mealID);
            stmt.setInt(2, transactionID);
            
            int deletedRows = stmt.executeUpdate();
            
            return deletedRows > 0;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return false;
    }

    // Update remarks of a meal_delivery record given two IDs
    public boolean updateRemarksMD(int mealID, int transactionID, String newValue)
    {
        String sql = "UPDATE meal_delivery SET remarks = ? " +
                     "WHERE meal_id = ? AND transaction_id = ?";

        try (PreparedStatement stmt = c.prepareStatement(sql))
        {
            stmt.setString(1, newValue);
            stmt.setInt(2, mealID);
            stmt.setInt(3, transactionID);
            
            int updatedRow = stmt.executeUpdate();
            
            return updatedRow > 0;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return false;
    }

    // Get number of deliveries a meal is included in
    public int getNumDeliveriesForMeal(int mealID)
    {
        String sql = "SELECT COUNT(transaction_id) AS numDeliveries " +
                     "FROM meal_delivery WHERE meal_id = ? " +
                     "GROUP BY meal_id";
                     
        try (PreparedStatement stmt = c.prepareStatement(sql))
        {
            stmt.setInt(1, mealID);

            try (ResultSet rs = stmt.executeQuery())
            {
                if(rs.next())
                    return rs.getInt("numDeliveries");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return 0;
    }

}
