package controller;

import model.*;
import DAO.*;

import java.sql.Date;
import java.util.ArrayList;

public class RiderController
{
    private RiderDAO riderDAO;

    public RiderController(RiderDAO dao)
    {
        this.riderDAO = dao;
    }

    public void addRider(String riderName, Date hireDate, String contactNo)
    {
        Rider rider = new Rider();
        rider.setRiderName(riderName);
        rider.setHireDate(hireDate);
        rider.setContactNo(contactNo);

        riderDAO.addRider(rider);
    }

    public ArrayList<Rider> getAllRiders()
    {
        return riderDAO.getAllRiders();
    }

    public Rider getRiderById(int riderID)
    {
        return riderDAO.getRiderByKey(riderID);
    }

    public boolean deleteRider(int riderID)
    {
        boolean deleted = riderDAO.deleteRider(riderID);

        if (deleted)
            System.out.println("Rider deleted successfully!");
        else
            System.out.println("Rider not found!");

        return deleted;
    }

    public boolean modifyRiderColumn(int riderID, String columnName, Object newValue)
    {
        return riderDAO.modifyRiderColumn(riderID, columnName, newValue);
    }
}