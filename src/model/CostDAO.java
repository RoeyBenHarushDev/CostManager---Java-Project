package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CostDAO {
    private Connection connection;

    public CostDAO() {
        try {
            // Connect to the embedded Derby database
            String dbUrl = "jdbc:derby:CostManagerDB;create=true";
            connection = DriverManager.getConnection(dbUrl);

            // Create the COSTS table if it doesn't exist
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS COSTS (ID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
                    "CATEGORY VARCHAR(255), SUM DOUBLE, CURRENCY VARCHAR(255), DESCRIPTION VARCHAR(255), " +
                    "DATE VARCHAR(255))");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Cost> getAllCosts() {
        List<Cost> costs = new ArrayList<>();
        try {
            // Execute a SELECT statement to get all costs from the COSTS table
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM COSTS");

            // Loop through the results and create a new Cost object for each row
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String category = resultSet.getString("CATEGORY");
                double sum = resultSet.getDouble("SUM");
                String currency = resultSet.getString("CURRENCY");
                String description = resultSet.getString("DESCRIPTION");
                String date = resultSet.getString("DATE");
                Cost cost = new Cost(id, category, sum, currency, description, date);
                costs.add(cost);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return costs;
    }

    public List<String> getCategories() {
        List<String> categories = new ArrayList<>();
        try {
            // Execute a SELECT DISTINCT statement to get all unique categories from the COSTS table
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT DISTINCT CATEGORY FROM COSTS");

            // Loop through the results and add each category to the list
            while (resultSet.next()) {
                String category = resultSet.getString("CATEGORY");
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    public void addCost(Cost cost) {
        try {
            // Execute an INSERT statement to add the cost to the COSTS table
            PreparedStatement statement = connection.prepareStatement("INSERT INTO COSTS (CATEGORY, SUM, CURRENCY, " +
                    "DESCRIPTION, DATE) VALUES (?, ?, ?, ?, ?)");
            statement.setString(1, cost.getCategory());
            statement.setDouble(2, cost.getSum());
            statement.setString(3, cost.getCurrency());
            statement.setString(4, cost.getDescription());
            statement.setString(5, cost.getDate());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            // Close the database connection
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Cost> getCostsByDate(String date, boolean isMonthly) throws SQLException {
        List<Cost> costs = new ArrayList<>();
        String sql;
        if (isMonthly) {
            sql = "SELECT * FROM costs WHERE date LIKE ?";
            date += "-%";
        } else {
            sql = "SELECT * FROM costs WHERE date = ?";
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, date);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String category = rs.getString("category");
                    double sum = rs.getDouble("sum");
                    String currency = rs.getString("currency");
                    String description = rs.getString("description");
                    String costDate = rs.getString("date");
                    Cost cost = new Cost(id, category, sum, currency, description, costDate);
                    costs.add(cost);
                }
            }
        }
        return costs;
    }

}
