package model;

public class ClientDietPreference {
    private int clientId;
    private int dietPreferenceId;

    public ClientDietPreference() {
    }

    public ClientDietPreference(int clientId, int dietPreferenceId) {
        this.clientId = clientId;
        this.dietPreferenceId = dietPreferenceId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getDietPreferenceId() {
        return dietPreferenceId;
    }

    public void setDietPreferenceId(int dietPreferenceId) {
        this.dietPreferenceId = dietPreferenceId;
    }

}
