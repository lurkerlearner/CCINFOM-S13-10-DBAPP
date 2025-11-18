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

    // Determine the flood risk for an area based on flood factor
    public String getRiskByFloodFactor(int locationID) 
    {
        String risk = "NO DATA";

        try 
        {
            String sql = """
                SELECT AVG(
                    CASE 
                        WHEN flood_factor = 'LOW' THEN 1
                        WHEN flood_factor = 'MODERATE' THEN 2
                        WHEN flood_factor = 'HIGH' THEN 3
                        WHEN flood_factor = 'SEVERE' THEN 4
                        ELSE NULL
                    END
                ) AS avgFactor
                FROM flood_data
                WHERE location_id = ?
            """;

            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setInt(1, locationID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) 
            {
                double avgFactor = rs.getDouble("avgFactor");

                if (rs.wasNull()) 
                    risk = "NO DATA";
                else if (avgFactor < 1.5)
                    risk = "LOW";
                else if (avgFactor < 2.5)
                    risk = "MODERATE";
                else if (avgFactor < 3.5)
                    risk = "HIGH";
                else
                    risk = "SEVERE";
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }

        return risk;
    }

    // Determine the flood risk for an area based on average water level
    public float getRiskByAverageWaterLevel(int locationID)
    {
        float risk = 0.0f;

        try 
        {
            String sql = "SELECT AVG(avg_water_level) AS location_avg " +
                         "FROM flood_data WHERE location_id = ?";

            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setInt(1, locationID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) 
            {
                risk = rs.getFloat("location_avg");
                if (rs.wasNull())
                    risk = 0.0f;
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }

        return risk;
    }

    // REPORT: FLOOD IMPACT 

    /*
     * Show which clients are affected by a higher risk of flood
     * disruptions in terms of severity and water level based off
     * historical flooding records. Summarize how sales are affected
     * in their areas for the year and quarter needed.
     * 
     * RECORDS/TABLES INVOLVED
     * - Flood Data
     * - Client
     * - Delivery
     * - Location
     */

    // quarterly report
    public ArrayList<FloodImpactReport> generateFloodImpactReportQTR(int year, int quarter)
    {
        java.sql.Date startDate = null;
        java.sql.Date endDate = null;

        switch (quarter)
        {
            case 1 -> 
            {
                startDate = java.sql.Date.valueOf(year + "-01-01");
                endDate   = java.sql.Date.valueOf(year + "-03-31");
            }
            case 2 -> 
            {
                startDate = java.sql.Date.valueOf(year + "-04-01");
                endDate   = java.sql.Date.valueOf(year + "-06-30");
            }
            case 3 -> 
            {
                startDate = java.sql.Date.valueOf(year + "-07-01");
                endDate   = java.sql.Date.valueOf(year + "-09-30");
            }
            case 4 -> 
            {
                startDate = java.sql.Date.valueOf(year + "-10-01");
                endDate   = java.sql.Date.valueOf(year + "-12-31");
            }
        }

        ArrayList<FloodImpactReport> report = new ArrayList<>();

        String sql = """
                    SELECT 
                        c.client_id AS "Client ID",
                        c.name AS Name,
                        l.location_id,
                        l.street_address AS Street,
                        l.city AS City,
                        COUNT(d.transaction_id) AS Sales
                    FROM client c
                    JOIN location l ON c.location_id = l.location_id
                    LEFT JOIN delivery d ON c.client_id = d.client_id
                              AND d.order_date BETWEEN ? AND ?
                    GROUP BY c.client_id, c.name, l.location_id, l.street_address, l.city;
                """;
        
        try (PreparedStatement ps = c.prepareStatement(sql)) 
        {
            ps.setDate(1, startDate);
            ps.setDate(2, endDate);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) 
            {
                int cid = rs.getInt("Client ID");
                String n = rs.getString("Name");
                String st = rs.getString("Street");
                String ct = rs.getString("City");

                int locID = rs.getInt("location_id");

                // risk calculations
                String rbff = getRiskByFloodFactor(locID);
                double rbawl = getRiskByAverageWaterLevel(locID);

                int sl = rs.getInt("Sales");

                report.add(new FloodImpactReport(cid, n, st, ct, rbff, rbawl, sl));
            }

            // reorder so that highest overall risk is at the top
            report.sort((r1, r2) -> 
            {
                int severity1 = getFloodSeverityRank(r1.getRiskByFactor());
                int severity2 = getFloodSeverityRank(r2.getRiskByFactor());

                // flood factor descending
                int cmp = Integer.compare(severity2, severity1);
                if (cmp != 0) return cmp;

                // average water level descending
                return Double.compare(r2.getRiskByAWL(), r1.getRiskByAWL());
            });
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }

        return report;
    }

    // yearly report
    public ArrayList<FloodImpactReport> generateFloodImpactReportYEAR(int year)
    {
        ArrayList<FloodImpactReport> report = new ArrayList<>();

        String sql = """
                    SELECT 
                        c.client_id AS "Client ID",
                        c.name AS Name,
                        l.location_id,
                        l.street_address AS Street,
                        l.city AS City,
                        COUNT(d.transaction_id) AS Sales
                    FROM client c
                    JOIN location l ON c.location_id = l.location_id
                    LEFT JOIN delivery d ON c.client_id = d.client_id
                              AND YEAR(d.order_date) = ?
                    GROUP BY c.client_id, c.name, l.location_id, l.street_address, l.city;
                """;
        
        try (PreparedStatement ps = c.prepareStatement(sql)) 
        {
            ps.setInt(1, year);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) 
            {
                int cid = rs.getInt("Client ID");
                String n = rs.getString("Name");
                String st = rs.getString("Street");
                String ct = rs.getString("City");

                int locID = rs.getInt("location_id");

                // risk calculations
                String rbff = getRiskByFloodFactor(locID);
                double rbawl = getRiskByAverageWaterLevel(locID);

                int sl = rs.getInt("Sales");

                report.add(new FloodImpactReport(cid, n, st, ct, rbff, rbawl, sl));
            }

            // reorder so that highest overall risk is at the top
            report.sort((r1, r2) -> 
            {
                int severity1 = getFloodSeverityRank(r1.getRiskByFactor());
                int severity2 = getFloodSeverityRank(r2.getRiskByFactor());

                // flood factor descending
                int cmp = Integer.compare(severity2, severity1);
                if (cmp != 0) return cmp;

                // average water level descending
                return Double.compare(r2.getRiskByAWL(), r1.getRiskByAWL());
            });
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }

        return report;
    }

    // HELPER METHODS for generating Flood Impact Report

    // ordering via flood risk severity based on quarterly flood factor
    public int getFloodSeverityRank(String risk) 
    {
        int rank;
        switch (risk) 
        {
            case "LOW":
                rank = 1;
            case "MODERATE":
                rank = 2;
            case "HIGH":
                rank = 3;
            case "SEVERE":
                rank = 4;
            default:
                rank = 0;
        };
        return rank;
    }  
}


