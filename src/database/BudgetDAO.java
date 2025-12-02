package database;

import models.Budget;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BudgetDAO {

    public static boolean createBudget(int userId, int categoryId, double limit, int month, int year) {
        String sql = "INSERT INTO budgets (user_id, category_id, monthly_limit, month, year, current_spent) " +
                    "VALUES (?, ?, ?, ?, ?, 0)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, categoryId);
            stmt.setDouble(3, limit);
            stmt.setInt(4, month);
            stmt.setInt(5, year);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public static List<Budget> getUserBudgets(int userId) {
        List<Budget> list = new ArrayList<>();
        String sql = "SELECT * FROM budgets WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Budget b = new Budget(
                    rs.getInt("id"), rs.getInt("user_id"), rs.getInt("category_id"),
                    rs.getDouble("monthly_limit"), rs.getInt("month"), rs.getInt("year")
                );
                b.setCurrentSpent(rs.getDouble("current_spent"));
                list.add(b);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return list;
    }

    public static boolean updateSpent(int budgetId, double spent) {
        String sql = "UPDATE budgets SET current_spent = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, spent);
            stmt.setInt(2, budgetId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteBudget(int budgetId) {
        String sql = "DELETE FROM budgets WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, budgetId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }
}
