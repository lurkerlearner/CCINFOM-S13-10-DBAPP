public class FloodData
{
    private int flood_id;
    private FloodFactor flood_factor;
    private float avg_water_level;
    private int affected_households;
    private RoadCondition road_condition;
    private boolean special_packaging;
    private AltDeliveryMethod alt_delivery_method;
    private int location_id;

    public FloodData(int flood_id, FloodFactor flood_factor, float avg_water_level, 
                     int affected_households, RoadCondition road_condition, boolean special_packaging,
                     AltDeliveryMethod alt_delivery_method, int location_id)
                     {
                        this.flood_id = flood_id;
                        this.flood_factor = flood_factor;
                        this.avg_water_level = avg_water_level;
                        this.affected_households = affected_households;
                        this.road_condition = road_condition;
                        this.special_packaging = special_packaging;
                        this.alt_delivery_method = alt_delivery_method;
                        this.location_id = location_id;
                     }

    public FloodData() {}

    public int getFloodID()
    {
        return flood_id;
    }

    public FloodFactor getFloodFactor()
    {
        return flood_factor;
    }

    public int getLocationID()
    {
        return location_id;
    }

    public AltDeliveryMethod getAltDeliveryMethod()
    {
        return alt_delivery_method;
    }

    public int getAffectedHouseholds()
    {
        return affected_households;
    }

    public float getAvgWaterLevel()
    {
        return avg_water_level;
    }

    public RoadCondition getRoadCondition()
    {
        return road_condition;
    }

    public boolean getSpecialPackaging()
    {
        return special_packaging;
    }
    
    public void setFloodID(int flood_id)
    {
        this.flood_id = flood_id;
    }

    public void setFloodFactor(FloodFactor flood_factor)
    {
        this.flood_factor = flood_factor;
    }

    public void setLocationID(int location_id)
    {
        this.location_id = location_id;
    }

    public void setAltDeliveryMethod(AltDeliveryMethod alt_delivery_method)
    {
        this.alt_delivery_method = alt_delivery_method;
    }

    public void setAffectedHouseholds(int affected_households)
    {
        this.affected_households = affected_households;
    }

    public void setAvgWaterLevel(float avg_water_level)
    {
        this.avg_water_level = avg_water_level;
    }

    public void setRoadCondition(RoadCondition road_condition)
    {
        this.road_condition = road_condition;
    }

    public void setSpecialPackaging(boolean special_packaging)
    {
        this.special_packaging = special_packaging;
    }
}
