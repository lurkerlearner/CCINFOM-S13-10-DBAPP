package model;

public class ClientEngagementReport {
    //weekly order count

    /*
    * IDEA:
    * SELECT
    c.clientID,
    c.name AS clientName,
    WEEK(d.deliveryDate, 1) - WEEK(DATE_SUB(d.deliveryDate, INTERVAL DAY(d.deliveryDate)-1 DAY), 1) + 1 AS weekNumber,
    COUNT(*) AS orderCount
    * FROM delivery d
    JOIN client c ON d.clientID = c.clientID
    WHERE MONTH(d.deliveryDate) = ? AND YEAR(d.deliveryDate) = ?
    GROUP BY c.clientID, weekNumber;
    *
    * */

    //most frequently ordered meal
    /*
    * SELECT
    d.clientID,
    m.mealName,
    COUNT(*) AS freq
    FROM delivery_meal dm
    JOIN delivery d ON dm.deliveryID = d.deliveryID
    JOIN meal m ON dm.mealID = m.mealID
    WHERE MONTH(d.deliveryDate) = ? AND YEAR(d.deliveryDate) = ?
    GROUP BY d.clientID, m.mealID
    ORDER BY freq DESC;
*/


    /*Report

    private Connection conn;

    public List<ReportRow> getClientEngagementReport(int month, int year) {
        List<ReportRow> report = new ArrayList<>();

        Map<Integer, ReportRow> rowMap = new HashMap<>();

      
        String weeklySql = "SELECT c.clientID, c.name, "
                + "WEEK(d.deliveryDate, 1) - WEEK(DATE_SUB(d.deliveryDate, INTERVAL DAY(d.deliveryDate)-1 DAY), 1) + 1 AS weekNumber, "
                + "COUNT(*) AS orderCount "
                + "FROM delivery d "
                + "JOIN client c ON d.clientID = c.clientID "
                + "WHERE MONTH(d.deliveryDate) = ? AND YEAR(d.deliveryDate) = ? "
                + "GROUP BY c.clientID, weekNumber";

        try (PreparedStatement ps = conn.prepareStatement(weeklySql)) {
            ps.setInt(1, month);
            ps.setInt(2, year);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int clientId = rs.getInt("clientID");
                String name = rs.getString("name");
                int week = rs.getInt("weekNumber");
                int orders = rs.getInt("orderCount");

                rowMap.putIfAbsent(clientId, new ReportRow(clientId, name));

                ReportRow row = rowMap.get(clientId);

                switch(week) {
                    case 1: row.setWeek1Orders(orders); break;
                    case 2: row.setWeek2Orders(orders); break;
                    case 3: row.setWeek3Orders(orders); break;
                    case 4: row.setWeek4Orders(orders); break;
                    case 5: row.setWeek5Orders(orders); break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 2. Most Ordered Meal
        String mealSql = "SELECT d.clientID, m.mealName, COUNT(*) AS freq "
                + "FROM delivery_meal dm "
                + "JOIN delivery d ON dm.deliveryID = d.deliveryID "
                + "JOIN meal m ON dm.mealID = m.mealID "
                + "WHERE MONTH(d.deliveryDate) = ? AND YEAR(d.deliveryDate) = ? "
                + "GROUP BY d.clientID, m.mealID "
                + "ORDER BY freq DESC";

        try (PreparedStatement ps = conn.prepareStatement(mealSql)) {
            ps.setInt(1, month);
            ps.setInt(2, year);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int clientId = rs.getInt("clientID");
                String mealName = rs.getString("mealName");

                if (rowMap.containsKey(clientId) && rowMap.get(clientId).getMostOrderedMeal() == null) {
                    rowMap.get(clientId).setMostOrderedMeal(mealName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        report.addAll(rowMap.values());
        return report;
    }
}
    * */
}
