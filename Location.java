public class Location {
    private int locationId;
    private String street;
    private String city;
    private String zip;

    public Location() {}

    public Location(String street, String city, String zip) {
        this.street = street;
        this.city = city;
        this.zip = zip;
    }

    public Location(int locationId, String street, String city, String zip) {
        this.locationId = locationId;
        this.street = street;
        this.city = city;
        this.zip = zip;
    }

    public int getLocationId() { return locationId; }
    public String getStreet() { return street; }
    public String getCity() { return city; }
    public String getZip() { return zip; }
}

