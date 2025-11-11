import java.time.LocalDate;
import java.sql.Date;

public class ClientController {
    private ClientDAO clientDAO;

    public ClientController(){
        clientDAO = new ClientDAO();
    }

    public boolean addClient(String name, String contactNo, int planId, int dietPreferenceId, int locationId){
        Client client = new Client();
        client.setName(name);
        client.setContactNo(contactNo);

        client.setDateCreated(LocalDate.now());

        client.setPlanID(planId);
        client.setDietPreferenceID(dietPreferenceId);
        client.setLocationID(locationId);

        return clientDAO.addClient(client) > 0;
    }
}

