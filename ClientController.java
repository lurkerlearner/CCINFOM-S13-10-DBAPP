import java.util.Date;

public class ClientController {
    private ClientDAO clientDAO;

    public ClientController(){
        clientDAO = new ClientDAO();
    }

    public boolean addClient(String name, String contactNo, int planId, int dietPreferenceId, int locationId){
        Client client = new Client();
        client.setName(name);
        client.setContactNo(contactNo);
        client.setDateCreated(new Date());
        client.setPlanId(planId);
        client.setDietPreferenceId(dietPreferenceId);
        client.setLocationId(locationId);

        clientDAO.addClient(client);

        return true;
    }



}
