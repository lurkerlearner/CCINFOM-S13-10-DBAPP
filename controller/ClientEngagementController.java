package controller;

import DAO.*;

import java.util.List;

public class ClientEngagementController {
    private ClientEngagementDAO dao = new ClientEngagementDAO();

    public List<Object[]> getMonthlyReport(int month, int year) {
        return dao.loadMonthlyEngagement(month, year);
    }

    public List<Object[]> getAnnualReport(int year) {
        return dao.loadAnnualEngagement(year);
    }
}
