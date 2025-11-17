package DAO;

import java.sql.*;
import java.util.*;
import app.*;

public class ClientEngagementDAO {

    public List<Object[]> loadMonthlyEngagement(int month, int year) {
        List<Object[]> tableData = new ArrayList<>();

        Map<Integer, Object[]> rowMap = new HashMap<>();
        Map<Integer, int[]> weeklyMap = new HashMap<>();
        Map<Integer, String> mealMap = new HashMap<>();

        try (Connection conn = DBConnection.getConnection()) {

            // Weekly order counts per client
            String weeklySql =
                    "SELECT c.client_id, c.name, " +
                            "WEEK(d.order_date, 1) - WEEK(DATE_SUB(d.order_date, INTERVAL DAY(d.order_date)-1 DAY), 1) + 1 AS weekNumber, " +
                            "COUNT(*) AS orderCount " +
                            "FROM DELIVERY d " +
                            "JOIN CLIENT c ON d.client_id = c.client_id " +
                            "WHERE MONTH(d.order_date) = ? AND YEAR(d.order_date) = ? " +
                            "GROUP BY c.client_id, weekNumber";

            PreparedStatement ps1 = conn.prepareStatement(weeklySql);
            ps1.setInt(1, month);
            ps1.setInt(2, year);
            ResultSet rs1 = ps1.executeQuery();

            while (rs1.next()) {
                int clientId = rs1.getInt("client_id");
                String name = rs1.getString("name");
                int week = rs1.getInt("weekNumber");
                int orders = rs1.getInt("orderCount");

                weeklyMap.putIfAbsent(clientId, new int[5]);
                weeklyMap.get(clientId)[week - 1] = orders;

                rowMap.putIfAbsent(clientId, new Object[]{
                        clientId, name, 0, 0, 0, 0, 0, null
                });
            }

            // Most frequently ordered meal per client
            String mealSql =
                    "SELECT d.client_id, m.meal_name, COUNT(*) AS freq " +
                            "FROM MEAL_DELIVERY md " +
                            "JOIN DELIVERY d ON md.transaction_id = d.transaction_id " +
                            "JOIN MEAL m ON md.meal_id = m.meal_id " +
                            "WHERE MONTH(d.order_date) = ? AND YEAR(d.order_date) = ? " +
                            "GROUP BY d.client_id, m.meal_id " +
                            "ORDER BY freq DESC";

            PreparedStatement ps2 = conn.prepareStatement(mealSql);
            ps2.setInt(1, month);
            ps2.setInt(2, year);
            ResultSet rs2 = ps2.executeQuery();

            while (rs2.next()) {
                int clientId = rs2.getInt("client_id");
                String mealName = rs2.getString("meal_name");

                mealMap.putIfAbsent(clientId, mealName);
            }

            // Merge weekly orders and most ordered meal
            for (Integer id : rowMap.keySet()) {
                Object[] row = rowMap.get(id);
                int[] weeks = weeklyMap.get(id);

                row[2] = weeks[0];
                row[3] = weeks[1];
                row[4] = weeks[2];
                row[5] = weeks[3];
                row[6] = weeks[4];
                row[7] = mealMap.get(id);

                tableData.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tableData;
    }

    public List<Object[]> loadAnnualEngagement(int year) {
        List<Object[]> tableData = new ArrayList<>();
        Map<Integer, Object[]> rowMap = new HashMap<>();
        Map<Integer, String> mealMap = new HashMap<>();

        try (Connection conn = DBConnection.getConnection()) {

            // Total orders per client in the year
            String annualSql =
                    "SELECT c.client_id, c.name, COUNT(*) AS totalOrders " +
                            "FROM DELIVERY d " +
                            "JOIN CLIENT c ON d.client_id = c.client_id " +
                            "WHERE YEAR(d.order_date) = ? " +
                            "GROUP BY c.client_id";

            PreparedStatement ps1 = conn.prepareStatement(annualSql);
            ps1.setInt(1, year);
            ResultSet rs1 = ps1.executeQuery();

            while (rs1.next()) {
                int clientId = rs1.getInt("client_id");
                String name = rs1.getString("name");
                int totalOrders = rs1.getInt("totalOrders");

                rowMap.put(clientId, new Object[]{clientId, name, totalOrders, null});
            }

            // Most frequently ordered meal per client in the year
            String mealSql =
                    "SELECT d.client_id, m.meal_name, COUNT(*) AS freq " +
                            "FROM MEAL_DELIVERY md " +
                            "JOIN DELIVERY d ON md.transaction_id = d.transaction_id " +
                            "JOIN MEAL m ON md.meal_id = m.meal_id " +
                            "WHERE YEAR(d.order_date) = ? " +
                            "GROUP BY d.client_id, m.meal_id " +
                            "ORDER BY freq DESC";

            PreparedStatement ps2 = conn.prepareStatement(mealSql);
            ps2.setInt(1, year);
            ResultSet rs2 = ps2.executeQuery();

            while (rs2.next()) {
                int clientId = rs2.getInt("client_id");
                String mealName = rs2.getString("meal_name");

                if (rowMap.containsKey(clientId) && rowMap.get(clientId)[3] == null) {
                    rowMap.get(clientId)[3] = mealName;
                }
            }

            tableData.addAll(rowMap.values());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tableData;
    }
}

