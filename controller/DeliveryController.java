package controller;

import model.Delivery;
import DAO.DeliveryDAO;

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
}


// junction tables pa
