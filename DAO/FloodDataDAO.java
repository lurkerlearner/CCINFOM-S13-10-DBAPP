package DAO;

import model.*;
import controller.*;
import java.sql.*;
import java.util.ArrayList;

public class FloodDataDAO 
{
    private Connection c;

    public FloodDataDAO(Connection connection)
    {
        this.c = connection;
    }

    // MAIN OPERATIONS (create, select by pk, select all, delete)

    // Insert a new flood data record into the table
    public void addFloodData(FloodData fd) 
    {
        String sql = "INSERT INTO flood_data (flood_factor, avg_water_level, " +
                     "affected_households, road_condition, special_packaging, " +
                     "alt_delivery_method, location_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            stmt.setString(1, fd.getFloodFactor().name());
            stmt.setFloat(2, fd.getAvgWaterLevel());
            stmt.setInt(3, fd.getAffectedHouseholds());
            stmt.setString(4, fd.getRoadCondition().getDbValue());
            stmt.setBoolean(5, fd.getSpecialPackaging());
            stmt.setString(6, fd.getAltDeliveryMethod().getDbValue());
            stmt.setInt(7, fd.getLocationID());
            
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) 
            {
                int generatedID = rs.getInt(1);
                fd.setFloodID(generatedID);
            }
        } 
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    // HELPER METHOD (for building flood data in methods)
    public FloodData copyFloodData(ResultSet rs) throws SQLException
    {
        FloodData fd = new FloodData();

        fd.setFloodID(rs.getInt("flood_id"));
        fd.setFloodFactor(FloodFactor.valueOf(rs.getString("flood_factor")));
        fd.setAvgWaterLevel(rs.getFloat("avg_water_level"));
        fd.setAffectedHouseholds(rs.getInt("affected_households"));
        fd.setRoadCondition(RoadCondition.fromDbValue(rs.getString("road_condition")));
        fd.setSpecialPackaging(rs.getBoolean("special_packaging"));
        fd.setAltDeliveryMethod(AltDeliveryMethod.fromDbValue(rs.getString("alt_delivery_method")));
        fd.setLocationID(rs.getInt("location_id"));

        return fd;
    }

    // Select a flood data record from the table by its primary key
    public FloodData getFloodDataByKey(int key)
    {
        String sql = "SELECT * FROM flood_data WHERE flood_id = ?";

        try (PreparedStatement stmt = c.prepareStatement(sql))
        {
            stmt.setInt(1, key);

            ResultSet rs = stmt.executeQuery();

            if(rs.next())
            {
                return copyFloodData(rs);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    // Select all existing records in the flood data table
    public ArrayList<FloodData> getAllFloodData()
    {
        String sql = "SELECT * FROM flood_data";
        ArrayList<FloodData> floody_areas = new ArrayList<>();

        try (PreparedStatement stmt = c.prepareStatement(sql))
        {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) 
            {
                floody_areas.add(copyFloodData(rs));  
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return floody_areas;
    }

    // Delete a record from the flood data table given a primary key
    public boolean deleteFloodData(int key)
    {
        String sql = "DELETE FROM flood_data WHERE flood_id = ?";

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

    // Change a flood data record's non-enum data 
    public boolean changeFloodDataColumn(int key, String col, Object newValue)
    {
        String sql = "UPDATE flood_data SET " + col + " = ? WHERE flood_id = ?";

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
    public boolean executeFloodDataUpdate(String sql, String newValue, int flood_id)
    {
        try (PreparedStatement stmt = c.prepareStatement(sql)) 
        {
            stmt.setString(1, newValue); 
            stmt.setInt(2, flood_id);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } 
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    // Update a record's flood factor
    public boolean updateFloodFactor(int key, FloodFactor newValue)
    {
        String sql = "UPDATE flood_data SET flood_factor = ? WHERE flood_id = ?";
        return executeFloodDataUpdate(sql, newValue.name(), key);
    }

    // Update a record's road condition
    public boolean updateRoadCondition(int key, RoadCondition newValue)
    {
        String sql = "UPDATE flood_data SET road_condition = ? WHERE flood_id = ?";
        return executeFloodDataUpdate(sql, newValue.getDbValue(), key);
    }

    // Update a record's alt deliver method
    public boolean updateAltDeliveryMethod(int key, AltDeliveryMethod newValue)
    {
        String sql = "UPDATE flood_data SET alt_delivery_method = ? WHERE flood_id = ?";
        return executeFloodDataUpdate(sql, newValue.getDbValue(), key);
    }

    // SIMPLE FILTERS (single field)

    // Select records with a specific flood factor
    public ArrayList<FloodData> getByFloodFactor(FloodFactor ff)
    {
        String sql = "SELECT * FROM flood_data WHERE flood_factor = ?";
        ArrayList<FloodData> floody_areas = new ArrayList<>();

        try (PreparedStatement stmt = c.prepareStatement(sql))
        {
            stmt.setString(1, ff.name());

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) 
            {
                floody_areas.add(copyFloodData(rs)); 
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return floody_areas;
    }

    // Select records with an average water level matching or greater than specified
    public ArrayList<FloodData> getGreaterThanAvgWaterLevel(float avg)
    {
        String sql = "SELECT * FROM flood_data WHERE avg_water_level >= ?";
        ArrayList<FloodData> floody_areas = new ArrayList<>();

        try (PreparedStatement stmt = c.prepareStatement(sql))
        {
            stmt.setFloat(1, avg);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) 
            {
                floody_areas.add(copyFloodData(rs));  
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return floody_areas;
    }

    // Select records whose number of houses affected is at least what is given
    public ArrayList<FloodData> getByHousesAffected(int h)
    {
        String sql = "SELECT * FROM flood_data WHERE affected_households >= ?";
        ArrayList<FloodData> floody_areas = new ArrayList<>();

        try (PreparedStatement stmt = c.prepareStatement(sql))
        {
            stmt.setInt(1, h);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) 
            {
                floody_areas.add(copyFloodData(rs));  
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return floody_areas;
    }

    // Select records with a specific road condition
    public ArrayList<FloodData> getByRoadCondition(RoadCondition rd)
    {
        String sql = "SELECT * FROM flood_data WHERE road_condition = ?";
        ArrayList<FloodData> floody_areas = new ArrayList<>();

        try (PreparedStatement stmt = c.prepareStatement(sql))
        {
            stmt.setString(1, rd.getDbValue());

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) 
            {
                floody_areas.add(copyFloodData(rs));  
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return floody_areas;
    }

    // Select records with/without special packaging
    public ArrayList<FloodData> getBySpecialPackaging(boolean sp)
    {
        String sql = "SELECT * FROM flood_data WHERE special_packaging = ?";
        ArrayList<FloodData> floody_areas = new ArrayList<>();

        try (PreparedStatement stmt = c.prepareStatement(sql))
        {
            stmt.setBoolean(1, sp);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) 
            {
                floody_areas.add(copyFloodData(rs)); 
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return floody_areas;
    }

    // Select records with a specific alternative delivery method
    public ArrayList<FloodData> getByAltDeliveryMethod(AltDeliveryMethod adm)
    {
        String sql = "SELECT * FROM flood_data WHERE alt_delivery_method = ?";
        ArrayList<FloodData> floody_areas = new ArrayList<>();

        try (PreparedStatement stmt = c.prepareStatement(sql))
        {
            stmt.setString(1, adm.getDbValue());

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) 
            {
                floody_areas.add(copyFloodData(rs));  
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return floody_areas;
    }

    // Select records with a specific location id
    public ArrayList<FloodData> getByLocation(int location)
    {
        String sql = "SELECT * FROM flood_data WHERE location_id = ?";
        ArrayList<FloodData> floody_areas = new ArrayList<>();

        try (PreparedStatement stmt = c.prepareStatement(sql))
        {
            stmt.setInt(1, location);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) 
            {
                floody_areas.add(copyFloodData(rs)); 
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return floody_areas;
    }

    // MORE ADVANCED FILTERS (joins, aggregates, sorts)

    // Get the frequency of flooding per area. Show the city name and sort by 
    // location with most to least occurences of flood.
    public ArrayList<Object[]> getFloodFrequencyPerArea()
    {
        String sql = "SELECT l.city, COUNT(f.flood_id) AS flood_occurences " +
                     "FROM flood_data AS f " +
                     "JOIN location AS l ON f.location_id = l.location_id " +
                     "GROUP BY l.city " +
                     "ORDER BY flood_occurences DESC, l.city ASC";

        ArrayList<Object[]> result = new ArrayList<>();
        try (PreparedStatement stmt = c.prepareStatement(sql)) 
        {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) 
            {
                Object[] row = 
                {
                    rs.getString("city"),
                    rs.getInt("flood_occurences")
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

    // Count the number of clients affected when flooding occurs. Show the city where the flood
    // occured and the number of affected households and sort by most clients affected to least.
    public ArrayList<Object[]> getNumClientsAffectedPerFlood()
    {
        String sql = "SELECT f.flood_id, l.city, f.affected_households, COUNT(c.client_id) AS affected_clients " +
                     "FROM flood_data AS f " +
                     "JOIN location AS l ON f.location_id = l.location_id " +
                     "JOIN client AS c ON l.location_id = c.location_id " +
                     "GROUP BY f.flood_id, l.city, f.affected_households " + 
                     "ORDER BY affected_clients DESC, l.city ASC";

        ArrayList<Object[]> result = new ArrayList<>();
        try (PreparedStatement stmt = c.prepareStatement(sql)) 
        {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) 
            {
                Object[] row = 
                {
                    rs.getInt("flood_id"),
                    rs.getString("city"),
                    rs.getInt("affected_households"),
                    rs.getInt("affected_clients")
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

    // Count the total number of deliveries made for each flood severity. 
    public ArrayList<Object[]> getDeliveriesPerFloodFactor()
    {
        String sql = "SELECT f.flood_factor, COUNT(d.transaction_id) AS deliveries " +
                     "FROM flood_data AS f " +
                     "JOIN client AS c ON f.location_id = c.location_id " +
                     "JOIN delivery AS d ON c.client_id = d.client_id " +
                     "GROUP BY f.flood_factor";

        ArrayList<Object[]> result = new ArrayList<>();
        try (PreparedStatement stmt = c.prepareStatement(sql)) 
        {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) 
            {
                Object[] row = 
                {
                    rs.getString("flood_factor"),
                    rs.getInt("deliveries")
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

    // Count the number of times access to roads during flooding became restricted (partially or fully).
    public ArrayList<Object[]> getRoadRestrictionFrequency()
    {
        String sql = "SELECT road_condition, COUNT(flood_id) AS frequency " +
                     "FROM flood_data AS f " +
                     "WHERE road_condition IN ('Partially Flooded', 'Not Accessible') " +
                     "GROUP BY road_condition ";

        ArrayList<Object[]> result = new ArrayList<>();
        try (PreparedStatement stmt = c.prepareStatement(sql)) 
        {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) 
            {
                Object[] row = 
                {
                    rs.getString("road_condition").toUpperCase().replace(" ", "_"),
                    rs.getInt("frequency")
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