package controller;

import DAO.*;
import model.*;

import java.time.LocalDate;

import java.time.LocalDate;
import java.util.List;

public class ClientController {

    private ClientDAO clientDAO;
    private LocationDAO locationDAO;

    public ClientController() {
        clientDAO = new ClientDAO();
    }

    public boolean addClient(String name, String contact, String password, String unit,
                             int planID, List<Integer> dietIDs, int locationID) {
        Client c = new Client();
        c.setName(name);
        c.setContactNo(contact);
        c.setPassword(password);
        c.setUnitDetails(unit);
        c.setPlanID(planID);
        c.setDietPreferenceIDs(dietIDs);
        c.setLocationID(locationID);
        c.setDateCreated(LocalDate.now());

        int clientId = clientDAO.addClient(c);

        if (clientId > 0) {
            clientDAO.addClientDietPreferences(clientId, dietIDs);
            return true;
        }

        return false;
    }



    public boolean addClient(Client client) {
        if (client.getDateCreated() == null) {
            client.setDateCreated(LocalDate.now());
        }
        return clientDAO.addClient(client) > 0;
    }

    public List<Client> getAllClients() {
        return clientDAO.getAllClients();
    }

    public List<Client> searchClientsByName(String name) {
        return clientDAO.searchClients("name", name);
    }

    public List<Client> searchClientsById(String idStr) {
        try {
            int id = Integer.parseInt(idStr);
            return clientDAO.searchClients("id", id);
        } catch (NumberFormatException e) {
            return List.of();
        }
    }

    public List<Client> searchClientsByContact(String contactNo) {
        return clientDAO.searchClients("contact", contactNo);
    }

    public boolean deleteClient(int clientId) {
        return clientDAO.deleteClient(clientId);
    }

}

