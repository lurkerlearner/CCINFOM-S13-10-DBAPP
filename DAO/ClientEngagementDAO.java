package DAO;

import java.sql.*;
import java.util.*;
import app.*;

public class ClientEngagementDAO {

    public List<Object[]> loadMonthlyEngagement(int month, int year) {
        List<Object[]> tableData = new ArrayList<>();

            // maps clientId -> row template
            Map<Integer, Object[]> rowMap = new HashMap<>();
            // maps clientId -> int[5] for weekly counts (index 0 => week1)
            Map<Integer, int[]> weeklyMap = new HashMap<>();
            // maps clientId -> most frequent meal name
            Map<Integer, String> mealMap = new HashMap<>();

            String weeklySql =
                    "SELECT c.client_id, c.name, " +
                            "WEEK(d.order_date, 1) - WEEK(DATE_SUB(d.order_date, INTERVAL DAY(d.order_date)-1 DAY), 1) + 1 AS weekNumber, " +
                            "COUNT(*) AS orderCount " +
                            "FROM DELIVERY d " +
                            "JOIN CLIENT c ON d.client_id = c.client_id " +
                            "WHERE MONTH(d.order_date) = ? AND YEAR(d.order_date) = ? " +
                            "GROUP BY c.client_id, weekNumber";

            // note: order by client_id, freq DESC ensures the first meal returned per client is the most frequent
            String mealSql =
                    "SELECT d.client_id, m.meal_name, COUNT(*) AS freq " +
                            "FROM DELIVERY d " +
                            "JOIN MEAL m ON d.meal_id = m.meal_id " +
                            "WHERE MONTH(d.order_date) = ? AND YEAR(d.order_date) = ? " +
                            "GROUP BY d.client_id, m.meal_id " +
                            "ORDER BY d.client_id, freq DESC";

            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement psWeek = conn.prepareStatement(weeklySql);
                 PreparedStatement psMeal = conn.prepareStatement(mealSql)) {

                // weekly
                psWeek.setInt(1, month);
                psWeek.setInt(2, year);
                try (ResultSet rs = psWeek.executeQuery()) {
                    while (rs.next()) {
                        int clientId = rs.getInt("client_id");
                        String name = rs.getString("name");
                        int week = rs.getInt("weekNumber");        // 1..5
                        int orders = rs.getInt("orderCount");

                        // ensure weeks array
                        weeklyMap.putIfAbsent(clientId, new int[5]);
                        if (week >= 1 && week <= 5) {
                            weeklyMap.get(clientId)[week - 1] = orders;
                        }

                        // create base row if not present: clientId, name, w1..w5, mostMeal(null)
                        rowMap.putIfAbsent(clientId, new Object[]{
                                clientId, name, 0, 0, 0, 0, 0, null
                        });
                    }
                }

                psMeal.setInt(1, month);
                psMeal.setInt(2, year);
                try (ResultSet rs = psMeal.executeQuery()) {
                    while (rs.next()) {
                        int clientId = rs.getInt("client_id");
                        String mealName = rs.getString("meal_name");


                        mealMap.putIfAbsent(clientId, mealName);
                    }
                }

                for (Integer clientId : rowMap.keySet()) {
                    Object[] row = rowMap.get(clientId);
                    int[] weeks = weeklyMap.getOrDefault(clientId, new int[5]);

                    row[2] = weeks.length > 0 ? weeks[0] : 0;
                    row[3] = weeks.length > 1 ? weeks[1] : 0;
                    row[4] = weeks.length > 2 ? weeks[2] : 0;
                    row[5] = weeks.length > 3 ? weeks[3] : 0;
                    row[6] = weeks.length > 4 ? weeks[4] : 0;
                    row[7] = mealMap.get(clientId); // may be null

                    tableData.add(row);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            // optional: sort by client id ascending for deterministic table ordering
            tableData.sort(Comparator.comparingInt(o -> (Integer) o[0]));
            return tableData;
        }

        public List<Object[]> loadAnnualEngagement(int year) {
            List<Object[]> tableData = new ArrayList<>();
            Map<Integer, Object[]> rowMap = new HashMap<>();
            Map<Integer, String> mealMap = new HashMap<>();

            String annualSql =
                    "SELECT c.client_id, c.name, COUNT(*) AS totalOrders " +
                            "FROM DELIVERY d " +
                            "JOIN CLIENT c ON d.client_id = c.client_id " +
                            "WHERE YEAR(d.order_date) = ? " +
                            "GROUP BY c.client_id";

            String mealSql =
                    "SELECT d.client_id, m.meal_name, COUNT(*) AS freq " +
                            "FROM DELIVERY d " +
                            "JOIN MEAL m ON d.meal_id = m.meal_id " +
                            "WHERE YEAR(d.order_date) = ? " +
                            "GROUP BY d.client_id, m.meal_id " +
                            "ORDER BY d.client_id, freq DESC";

            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement psAnnual = conn.prepareStatement(annualSql);
                 PreparedStatement psMeal = conn.prepareStatement(mealSql)) {

                // totals
                psAnnual.setInt(1, year);
                try (ResultSet rs = psAnnual.executeQuery()) {
                    while (rs.next()) {
                        int clientId = rs.getInt("client_id");
                        String name = rs.getString("name");
                        int totalOrders = rs.getInt("totalOrders");
                        rowMap.put(clientId, new Object[]{clientId, name, totalOrders, null});
                    }
                }

                // most frequent meal per client for the year
                psMeal.setInt(1, year);
                try (ResultSet rs = psMeal.executeQuery()) {
                    while (rs.next()) {
                        int clientId = rs.getInt("client_id");
                        String mealName = rs.getString("meal_name");

                        mealMap.putIfAbsent(clientId, mealName);
                    }
                }


                for (Map.Entry<Integer, Object[]> e : rowMap.entrySet()) {
                    Integer clientId = e.getKey();
                    Object[] row = e.getValue();
                    row[3] = mealMap.get(clientId); // may be null
                    tableData.add(row);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            tableData.sort(Comparator.comparingInt(o -> (Integer) o[0]));
            return tableData;
        }
}

