
import java.util.Date;

public class Client {
    private int clientID;
    private String name;
    private String contactNo;
    private Date dateCreated;
    private int planID;
    private int dietPreferenceID;
    private int locationID;

    public Client(){

    }

    public Client(int clientId, String name, String contactNo, Date dateCreated, int planId, int dietPreferenceId, int locationId) {
        this.clientID = clientId;
        this.name = name;
        this.contactNo = contactNo;
        this.dateCreated = dateCreated;
        this.planID = planId;
        this.dietPreferenceID = dietPreferenceId;
        this.locationID = locationId;
    }

    public int getClientId() { return clientID; }
    public void setClientId(int clientID) { this.clientID = clientID; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContactNo() { return contactNo; }
    public void setContactNo(String contactNo) { this.contactNo = contactNo; }

    public Date getDateCreated() { return dateCreated; }
    public void setDateCreated(Date dateCreated) { this.dateCreated = dateCreated; }

    public int getPlanId() { return planID; }
    public void setPlanId(int planID) { this.planID = planID; }

    public int getDietPreferenceID() { return dietPreferenceID; }
    public void setDietPreferenceId(int dietPreferenceID) { this.dietPreferenceID = dietPreferenceID; }

    public int getLocationId() { return locationID; }
    public void setLocationId(int locationID) { this.locationID = locationID; }



}
