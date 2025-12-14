package database;

import models.Transaction;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    public static boolean addTransaction(int userId, int accountId, int categoryId,
                                        double amount, String description, LocalDate date,
                                        String type, String notes) {
        String sql = "INSERT INTO transactions (user_id, account_id, category_id, amount, " +
                    "description, transaction_date, type, notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, accountId);
            stmt.setInt(3, categoryId);
            stmt.setDouble(4, amount);
            stmt.setString(5, description);
            stmt.setDate(6, Date.valueOf(date));
            stmt.setString(7, type);
            stmt.setString(8, notes);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public static List<Transaction> getUserTransactions(int userId) {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE user_id = ? ORDER BY transaction_date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Transaction t = new Transaction(
                    rs.getInt("id"), rs.getInt("user_id"), rs.getInt("account_id"),
                    rs.getInt("category_id"), rs.getDouble("amount"), rs.getString("description"),
                    rs.getDate("transaction_date").toLocalDate(),
                    Transaction.TransactionType.valueOf(rs.getString("type")),
                    rs.getString("notes")
                );
                t.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                list.add(t);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return list;
    }

    public static List<Transaction> getTransactionsByMonth(int userId, int month, int year) {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE user_id = ? " +
                    "AND MONTH(transaction_date) = ? AND YEAR(transaction_date) = ? " +
                    "ORDER BY transaction_date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, month);
            stmt.setInt(3, year);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Transaction t = new Transaction(
                    rs.getInt("id"), rs.getInt("user_id"), rs.getInt("account_id"),
                    rs.getInt("category_id"), rs.getDouble("amount"), rs.getString("description"),
                    rs.getDate("transaction_date").toLocalDate(),
                    Transaction.TransactionType.valueOf(rs.getString("type")),
                    rs.getString("notes")
                );
                list.add(t);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return list;
    }

    public static Transaction getTransactionById(int transactionId) {
        String sql = "SELECT * FROM transactions WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, transactionId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Transaction t = new Transaction(
                    rs.getInt("id"), rs.getInt("user_id"), rs.getInt("account_id"),
                    rs.getInt("category_id"), rs.getDouble("amount"), rs.getString("description"),
                    rs.getDate("transaction_date").toLocalDate(),
                    Transaction.TransactionType.valueOf(rs.getString("type")),
                    rs.getString("notes")
                );
                t.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                return t;
            }
            return null;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public static boolean updateTransaction(int transactionId, int accountId, int categoryId,
                                           double amount, String description, LocalDate date,
                                           String type, String notes) {
        String sql = "UPDATE transactions SET account_id = ?, category_id = ?, amount = ?, " +
                    "description = ?, transaction_date = ?, type = ?, notes = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, accountId);
            stmt.setInt(2, categoryId);
            stmt.setDouble(3, amount);
            stmt.setString(4, description);
            stmt.setDate(5, Date.valueOf(date));
            stmt.setString(6, type);
            stmt.setString(7, notes);
            stmt.setInt(8, transactionId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteTransaction(int id) {
        String sql = "DELETE FROM transactions WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public static double getTotalIncome(int userId) {
        String sql = "SELECT SUM(amount) FROM transactions WHERE user_id = ? AND type = 'INCOME'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return 0.0;
    }

    public static double getTotalExpenses(int userId) {
        String sql = "SELECT SUM(amount) FROM transactions WHERE user_id = ? AND type = 'EXPENSE'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return 0.0;
    }
}
