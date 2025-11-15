package controller;

import model.*;
import DAO.*;
import app.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class DeliveryController
{
    private final DeliveryDAO deliveryDAO;

    public DeliveryController(DeliveryDAO dao)
    {
        this.deliveryDAO = dao;
    }

    public void addDelivery(Date orderDate, Time timeOrdered, Time timeDelivered,
                            PaymentMode paymentMode, PaymentStatus paymentStatus,
                            DeliveryMethod deliveryMethod, DeliveryStatus deliveryStatus,
                            int clientId, int mealId, int riderId) 
                            {
                                Delivery delivery = new Delivery();
                                delivery.setOrderDate(orderDate);
                                delivery.setTimeOrdered(timeOrdered);
                                delivery.setTimeDelivered(timeDelivered);
                                delivery.setPaymentMode(paymentMode);
                                delivery.setPaymentStatus(paymentStatus);
                                delivery.setDeliveryMethod(deliveryMethod);
                                delivery.setDeliveryStatus(deliveryStatus);
                                delivery.setClientID(clientId);
                                delivery.setMealID(mealId);
                                delivery.setRiderID(riderId);
                                deliveryDAO.addDelivery(delivery);
                            }

    public ArrayList<Delivery> getAllDeliveries() 
    {
        return deliveryDAO.getAllDeliveries();
    }

    public Delivery getDeliveryByID(int transactionID) 
    {
        return deliveryDAO.getDeliveryByKey(transactionID);
    }

    public boolean deleteDelivery(int transactionID) 
    {
        boolean deleted = deliveryDAO.deleteDelivery(transactionID);

        if(deleted)
            System.out.println("Delivery deleted successfully!");
        else
            System.out.println("Delivery not found!");

        return deleted;
    }

    public boolean updatePaymentMode(int transactionID, PaymentMode newMode) 
    {
        return deliveryDAO.updatePaymentMode(transactionID, newMode);
    }

    public boolean updatePaymentStatus(int transactionID, PaymentStatus newStatus) 
    {
        return deliveryDAO.updatePaymentStatus(transactionID, newStatus);
    }

    public boolean updateDeliveryStatus(int transactionID, DeliveryStatus newStatus) 
    {
        return deliveryDAO.updateDeliveryStatus(transactionID, newStatus);
    }

    public boolean updateDeliveryMethod(int transactionID, DeliveryMethod newMethod) 
    {
        return deliveryDAO.updateDeliveryMethod(transactionID, newMethod);
    }

    public boolean changeDeliveryColumn(int transactionID, String columnName, Object newValue) 
    {
        return deliveryDAO.changeDeliveryColumn(transactionID, columnName, newValue);
    }

    public ArrayList<Delivery> getDeliveriesByClient(int clientID) 
    {
        return deliveryDAO.getDeliveriesByClient(clientID);
    }

    public ArrayList<Delivery> getDeliveriesByMeal(int mealID)
    {
        return deliveryDAO.getDeliveriesByMeal(mealID);
    }

    public ArrayList<Delivery> getDeliveriesWithinDateRange(Date startDate, Date endDate) 
    {
        return deliveryDAO.getDeliveriesWithinDateRange(startDate, endDate);
    }

    public ArrayList<Delivery> getDeliveriesByPaymentStatus(PaymentStatus ps)
    {
        return deliveryDAO.getDeliveriesByPaymentStatus(ps);
    }

    public ArrayList<Delivery> getDeliveriesByDeliveryStatus(DeliveryStatus ds)
    {
        return deliveryDAO.getDeliveriesByDeliveryStatus(ds);
    }

    public ArrayList<Object[]> getMostDeliveredMeal()
    {
        return deliveryDAO.getMostDeliveredMeal();
    }

    public ArrayList<Object[]> getDeliveriesPerLocationWithFloodData()
    {
        return deliveryDAO.getDeliveriesPerLocationWithFloodData();
    }

    // TRANSACTION: ORDER PLACEMENT

    /* 
     * Confirm client's selected meal by validating if the Meal ID exists and
     * is available. Record the meal as an order in the Delivery table by
     * creating a new delivery entry. Alert client about computed flood risk in
     * their area, possible delivery distruptions, and ask for confirmation.
     * 
     * TABLES INVOLVED
     * - Delivery
     * - Meal
     * - Client
     * - MealDelivery
     * - Flood Data
    */
    public boolean startOrderTransaction(int mealID, PaymentMode pm, int clientID) 
    {
        Connection c = DBConnection.getConnection();

        try 
        {
            // since confirmation is needed before proceeding,
            c.setAutoCommit(false);

            String floodRisk = computeFloodRiskForClient(clientID);
            
            // inform client of risks and ask for confirmation
            int option = JOptionPane.showConfirmDialog(
                null,
                "Flood risk for your area: " + floodRisk + 
                "\nNote that the delivery may take longer or " +
                "use alternative delivery methods/packaging " +
                "if road conditions are not fully accessible." +
                "\n\nDo you want to proceed with the order?",
                "Confirm Order",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

            if (option != JOptionPane.YES_OPTION) 
            {
                c.rollback();
                JOptionPane.showMessageDialog(null, 
                "Order cancelled by user.");
                return false;
            }
            else
            {
                boolean success = placeOrderAfterConfirmation(mealID, pm, clientID);
                if (!success) 
                {
                    c.rollback(); // undo any changes if order placing failed
                    JOptionPane.showMessageDialog(null, 
                    "Order placing failed!");
                    return false;
                }
                else
                {
                    c.commit();
                    JOptionPane.showMessageDialog(null, 
                    "Order placed successfully!");
                    return true;
                }
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
            try 
            {
                c.rollback(); // undo any changes to database if error is encountered
            } 
            catch (SQLException ex) 
            {
                ex.printStackTrace();
            }
            return false;
        } 
        finally 
        {
            try 
            {
                c.setAutoCommit(true); // restore default
            } 
            catch (SQLException e) 
            {
                e.printStackTrace();
            }
        }
    }


    // HELPER METHODS for a complete order transaction

    // obtaining flood risk before client continues ordering
    public String computeFloodRiskForClient(int clientID) 
    {
        ClientDAO clientDAO = new ClientDAO();
        FloodDataDAO floodDAO = new FloodDataDAO(DBConnection.getConnection());

        Client client = clientDAO.getClientById(clientID);

        if (client == null)
            return "NO CLIENT";

        int locationID = client.getLocationID();

        return floodDAO.getRiskByFloodFactor(locationID);
    }

    // place order after client confirmation (actual adding to tables)
    public boolean placeOrderAfterConfirmation(int mealID, PaymentMode pm, int clientID)
    {
        Connection c = DBConnection.getConnection();

        MealDAO mealDAO = new MealDAO();
        ClientDAO clientDAO = new ClientDAO();
        RiderDAO riderDAO = new RiderDAO(c);
        MealDeliveryDAO mealdelDAO = new MealDeliveryDAO(c);

        Meal order = mealDAO.getMealById(mealID);
        Client client = clientDAO.getClientById(clientID);

        if (order == null || client == null)
            return false;

        // prepare delivery object
        Delivery d = new Delivery();
        Date currDate = new java.sql.Date(System.currentTimeMillis());
        Time currTime = new java.sql.Time(System.currentTimeMillis());

        d.setOrderDate(currDate);
        d.setTimeOrdered(currTime);
        d.setTimeDelivered(null);
        d.setPaymentMode(pm);
        d.setPaymentStatus(PaymentStatus.PENDING);
        d.setDeliveryMethod(DeliveryMethod.MOTORCYCLE);
        d.setDeliveryStatus(null);
        d.setClientID(clientID);
        d.setMealID(mealID);
        d.setRiderID(riderDAO.getRandomRiderID());

        // insert delivery first to generate transaction ID
        boolean addSuccess = deliveryDAO.addDelivery(d);

        if (!addSuccess)
            return false;

        // insert meal_delivery using generated transaction_id
        MealDelivery md = new MealDelivery(mealID, d.getTransactionID(), "");
        mealdelDAO.addMealDelivery(md);

        return true;
    }


    /* 
    compute flood risk first and ask for confirmation if client
    wants to push through with ordering para general structure would be

    order transaction
    boolean b;
    computeFloodRisk
    ask confirmation
        if yes
            placeOrder
        if no
            cancel order transaction
    else
        placeOrder

    */
}


// junction tables pa


