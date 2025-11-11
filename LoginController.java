import java.time.LocalDate;

public class LoginController {

    private ClientDAO clientDAO;

    public LoginController() {
        this.clientDAO = new ClientDAO();
    }

    public boolean register(String fullname,
                            String contactNo,
                            String password,
                            String unitDetails,
                            int locationId) {


        if (clientDAO.isContactExists(contactNo)) {
            return false; //DUPLICATE TRACKING
        }

        // default plan/diet for new users
        int defaultPlanId = 1;
        int defaultDietId = 1;

        Client c = new Client(
                fullname,
                contactNo,
                password,
                unitDetails,
                LocalDate.now(),
                locationId,
                defaultPlanId,
                defaultDietId
        );

        int generatedId = clientDAO.addClient(c);
        return generatedId > 0;
    }

    public Client login(String contact, String password) {
        return clientDAO.login(contact.trim(), password.trim());
    }

    public ClientDAO getClientDAO() {
        return clientDAO;
    }
}
