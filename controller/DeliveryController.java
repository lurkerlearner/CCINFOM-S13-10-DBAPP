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
     * creating a new delivery entry. Alert client if computed flood risk in
     * their area might cause delivery distruption and ask for confirmation.
     * 
     * TABLES INVOLVED
     * - Delivery
     * - Meal
     * - Client
     * - MealDelivery
     * - Flood Data
    */
    public boolean placeOrder(int mealID, PaymentMode pm, int clientID)
    {
        boolean orderPlaced = false;

        Connection c = DBConnection.getConnection();

        // DAOs for involved tables
        MealDAO mealDAO = new MealDAO();
        ClientDAO clientDAO = new ClientDAO();
        RiderDAO riderDAO = new RiderDAO(c);
        MealDeliveryDAO mealdelDAO = new MealDeliveryDAO(c);
        FloodDataDAO floodDataDAO = new FloodDataDAO(c);

        Meal order = mealDAO.getMealById(mealID);
        Client client = clientDAO.getClientById(clientID);

        // if meal does not exist
        if (order == null)
            return orderPlaced;
        else
        {
            Delivery d = new Delivery();
            Date currDate = new java.sql.Date(System.currentTimeMillis());
            Time currTime = new java.sql.Time(System.currentTimeMillis());

            // prepare delivery record (not added to table yet)
            d.setOrderDate(currDate);
            d.setTimeOrdered(currTime);
            d.setTimeDelivered(null);
            d.setPaymentMode(pm);
            d.setPaymentStatus(PaymentStatus.PENDING);
            d.setDeliveryMethod(DeliveryMethod.MOTORCYCLE); // default value
            d.setDeliveryStatus(null);
            d.setClientID(clientID);
            d.setMealID(mealID);
            d.setRiderID(riderDAO.getRandomRiderID()); // assign random rider

            // prepare meal_delivery record (not added to table yet)
            MealDelivery md = new MealDelivery(mealID, d.getTransactionID(), "");

            // compute flood risk for client's location
            int location = client.getLocationID();
            String riskFF = floodDataDAO.getRiskByFloodFactor(location);
            boolean proceed;

            // ask for client confirmation if needed
            if (riskFF.equalsIgnoreCase("MODERATE") ||
                riskFF.equalsIgnoreCase("HIGH") || 
                riskFF.equalsIgnoreCase("SEVERE"))
                {
                    // if yes, insert the created records into their tables
                    deliveryDAO.addDelivery(d);
                    mealdelDAO.addMealDelivery(md);

                    orderPlaced = true;


                    // if no, do nothing -- di maiinsert sa tables yung records
                }
            else // meaning no/low risk in client's location
            {
                // automatically insert the created records into their tables
                deliveryDAO.addDelivery(d);
                mealdelDAO.addMealDelivery(md);

                orderPlaced = true;
            }
        }
        return orderPlaced;
    }
}


// junction tables pa

