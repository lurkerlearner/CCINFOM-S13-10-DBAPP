package controller;

import DAO.ClientDAO;
import model.Client;

import java.time.LocalDate;

import java.time.LocalDate;
import java.util.List;

public class ClientController {

    private ClientDAO clientDAO;

    public ClientController() {
        clientDAO = new ClientDAO();
    }

    public boolean addClient(String name, String contactNo, String password, int planId, int dietPreferenceId, int locationId) {
        Client client = new Client();
        client.setName(name);
        client.setContactNo(contactNo);
        client.setDateCreated(LocalDate.now());
        client.setPlanID(planId);
        client.setDietPreferenceID(dietPreferenceId);
        client.setLocationID(locationId);
        client.setPassword(password);

        return clientDAO.addClient(client) > 0;
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
}

