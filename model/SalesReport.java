package model;

/*
 * Represents a sales report with the number of meals sold/ meaning successfully ordered sha
 * (so get number of deliveries via transaction id).
 * must get the difference between the cost and price of the meals
 * sold in order to get the gross income (sum of all prices of the meals ordered)
 * and profit made (price - cost).
 * 
 * query draft:
 * SELECT YEAR(d.order_date) AS year, MONTH(d.order_date) AS month,
 * COUNT(d.transaction_id) AS salesMade, SUM(m.price) AS grossIncome,
 * SUM(m.price - m.cost) AS profitMade
 * FROM DELIVERY d  JOIN MEAL_DELIVERY md ON d.transaction_id = md.transaction_id
 *                  JOIN MEAL m ON md.meal_id = m.meal_id
 * WHERE d.delivery_status <> 'Cancelled'
 * GROUP BY YEAR(d.order_date), MONTH(d.order_date)
 * ORDER BY year, month;
 */
public class SalesReport {

    // no. of meals successfully delivered/placed
    private int year;
    private int month;
    private int sales_made;
    private double gross_income;
    private double net_profit;

    public SalesReport(int year, int month, int sales_made, double gross_income, double net_profit) {
        this.year = year;
        this.month = month;
        this.sales_made = sales_made;
        this.gross_income = gross_income;
        this.net_profit = net_profit;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getSales_made() {
        return sales_made;
    }

    public void setSales_made(int sales_made) {
        this.sales_made = sales_made;
    }

    public double getGross_income() {
        return gross_income;
    }

    public void setGross_income(double gross_income) {
        this.gross_income = gross_income;
    }

    public double getNet_profit() {
        return net_profit;
    }

    public void setNet_profit(double net_profit) {
        this.net_profit = net_profit;
    }
    
}
