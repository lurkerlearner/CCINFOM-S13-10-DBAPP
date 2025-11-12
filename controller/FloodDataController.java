package controller;

import model.*;
import DAO.*;

import java.util.ArrayList;

public class FloodDataController
{
    private final FloodDataDAO flooddataDAO;

    public FloodDataController(FloodDataDAO dao)
    {
        this.flooddataDAO = dao;
    }

    public void addFloodData(FloodFactor floodFactor, float avgWaterLevel,
                             int affectedHouseholds, RoadCondition roadCondition,
                             boolean specialPackaging, AltDeliveryMethod altDeliveryMethod,
                             int locationID) 
                             {
                                FloodData fd = new FloodData();
                                fd.setFloodFactor(floodFactor);
                                fd.setAvgWaterLevel(avgWaterLevel);
                                fd.setAffectedHouseholds(affectedHouseholds);
                                fd.setRoadCondition(roadCondition);
                                fd.setSpecialPackaging(specialPackaging);
                                fd.setAltDeliveryMethod(altDeliveryMethod);
                                fd.setLocationID(locationID);

                                flooddataDAO.addFloodData(fd);
                             }
    
    public ArrayList<FloodData> getAllFloodData() 
    {
        return flooddataDAO.getAllFloodData();
    }

    public FloodData getFloodDataById(int floodID) 
    {
        return flooddataDAO.getFloodDataByKey(floodID);
    }

    public boolean deleteFloodData(int floodID) 
    {
        boolean deleted = flooddataDAO.deleteFloodData(floodID);

        if (deleted)
            System.out.println("Flood data deleted successfully!");
        else
            System.out.println("Flood data not found!");

        return deleted;
    }

    public boolean updateFloodFactor(int floodID, FloodFactor newValue) 
    {
        return flooddataDAO.updateFloodFactor(floodID, newValue);
    }

    public boolean updateRoadCondition(int floodID, RoadCondition newValue) 
    {
        return flooddataDAO.updateRoadCondition(floodID, newValue);
    }

    public boolean updateAltDeliveryMethod(int floodID, AltDeliveryMethod newValue) 
    {
        return flooddataDAO.updateAltDeliveryMethod(floodID, newValue);
    }

    public boolean changeFloodDataColumn(int floodID, String columnName, Object newValue) 
    {
        return flooddataDAO.changeFloodDataColumn(floodID, columnName, newValue);
    }

    public ArrayList<FloodData> getByFloodFactor(FloodFactor floodFactor) 
    {
        return flooddataDAO.getByFloodFactor(floodFactor);
    }

    public ArrayList<FloodData> getByHousesAffected(int minHouses) 
    {
        return flooddataDAO.getByHousesAffected(minHouses);
    }

    public ArrayList<FloodData> getByRoadCondition(RoadCondition roadCondition) 
    {
        return flooddataDAO.getByRoadCondition(roadCondition);
    }

    public ArrayList<FloodData> getByLocation(int locationID) 
    {
        return flooddataDAO.getByLocation(locationID);
    }

    public ArrayList<Object[]> getFloodFrequencyPerArea() 
    {
        return flooddataDAO.getFloodFrequencyPerArea();
    }

    public ArrayList<Object[]> getNumClientsAffectedPerFlood() 
    {
        return flooddataDAO.getNumClientsAffectedPerFlood();
    }

    public ArrayList<Object[]> getDeliveriesPerFloodFactor() 
    {
        return flooddataDAO.getDeliveriesPerFloodFactor();
    }

    public ArrayList<Object[]> getRoadRestrictionFrequency() 
    {
        return flooddataDAO.getRoadRestrictionFrequency();
    }
}