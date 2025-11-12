package model;

import java.sql.Date;
import java.sql.Time;

public class Delivery 
{
    private int transaction_id;
    private Date order_date;
    private Time time_ordered;
    private Time time_delivered;
    private PaymentMode payment_mode;
    private PaymentStatus payment_status;
    private DeliveryMethod delivery_method;
    private DeliveryStatus delivery_status;
    private int client_id;
    private int meal_id;
    private int rider_id;

    public Delivery(int transaction_id, Date order_date, Time time_ordered, Time time_delivered,
                    PaymentMode payment_mode, PaymentStatus payment_status, DeliveryMethod delivery_method, 
                    DeliveryStatus delivery_status, int client_id, int meal_id, int rider_id)
                    {
                        this.transaction_id = transaction_id;
                        this.order_date = order_date;
                        this.time_ordered = time_ordered;
                        this.time_delivered = time_delivered;
                        this.payment_mode = payment_mode;
                        this.payment_status = payment_status;
                        this.delivery_method = delivery_method;
                        this.delivery_status = delivery_status;
                        this.client_id = client_id;
                        this.meal_id = meal_id;
                        this.rider_id = rider_id;
                    }

    public Delivery() {}

    public int getTransactionID() 
    {
        return transaction_id;
    }

    public int getClientID()
    {
        return client_id;
    }

    public int getMealID()
    {
        return meal_id;
    }

    public Date getOrderDate()
    {
        return order_date;
    }

    public Time getTimeOrdered()
    {
        return time_ordered;
    }

    public Time getTimeDelivered()
    {
        return time_delivered;
    }

    public int getRiderID()
    {
        return rider_id;
    }

    public PaymentMode getPaymentMode()
    {
        return payment_mode;
    }

    public PaymentStatus getPaymentStatus()
    {
        return payment_status;
    }

    public DeliveryStatus getDeliveryStatus()
    {
        return delivery_status;
    }

    public DeliveryMethod getDeliveryMethod()
    {
        return delivery_method;
    }

    public void setTransactionID(int transaction_id)
    {
        this.transaction_id = transaction_id;
    }

    public void setClientID(int client_id)
    {
        this.client_id = client_id;
    }

    public void setMealID(int meal_id)
    {
        this.meal_id = meal_id;
    }

    public void setOrderDate(Date order_date)
    {
        this.order_date = order_date;
    }

    public void setTimeOrdered(Time time_ordered)
    {
        this.time_ordered = time_ordered;
    }

    public void setTimeDelivered(Time time_delivered)
    {
        this.time_delivered = time_delivered;
    }

    public void setRiderID(int rider_id)
    {
        this.rider_id = rider_id;
    }

    public void setPaymentMode(PaymentMode payment_mode)
    {
        this.payment_mode = payment_mode;
    }

    public void setPaymentStatus(PaymentStatus payment_status)
    {
        this.payment_status = payment_status;
    }

    public void setDeliveryStatus(DeliveryStatus delivery_status)
    {
        this.delivery_status = delivery_status;
    }

    public void setDeliveryMethod(DeliveryMethod delivery_method)
    {
        this.delivery_method = delivery_method;
    }

    @Override
    public String toString() 
    {
        return  "Transaction ID: " + transaction_id + "\n" +
                "Order Date: " + order_date + "\n" +
                "Time Ordered: " + time_ordered + "\n" +
                "Time Delivered: " + time_delivered + "\n" +
                "Payment Mode: " + payment_mode + "\n" +
                "Payment Status: " + payment_status + "\n" +
                "Delivery Method: " + delivery_method + "\n" +
                "Delivery Status: " + delivery_status + "\n" +
                "Client ID: " + client_id + "\n" +
                "Meal ID: " + meal_id + "\n" +
                "Rider ID: " + rider_id;
    }
}
