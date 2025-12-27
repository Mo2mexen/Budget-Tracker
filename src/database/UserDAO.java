package database;

import java.sql.*;
import java.time.LocalDate;
import models.User;

public class UserDAO {

    // In production, passwords should be hashed before storage
    public static boolean createUser(String username, String password, String email,
                                    String fullName, String currency) {
        String sql = "INSERT INTO users (username, password, email, full_name, currency, created_date) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);  // Currently storing plain text
            stmt.setString(3, email);
            stmt.setString(4, fullName);
            stmt.setString(5, currency);
            stmt.setDate(6, Date.valueOf(LocalDate.now()));
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public static User login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("email"),
                    rs.getString("full_name"),
                    rs.getString("currency")
                );
                user.setCreatedDate(rs.getDate("created_date").toLocalDate());
                return user;
            }
            return null;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public static User getUserById(int userId) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("email"),
                    rs.getString("full_name"),
                    rs.getString("currency")
                );
                user.setCreatedDate(rs.getDate("created_date").toLocalDate());
                return user;
            }
            return null;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public static boolean updateUser(User user) {
        String sql = "UPDATE users SET email = ?, full_name = ?, currency = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getFullName());
            stmt.setString(3, user.getCurrency());
            stmt.setInt(4, user.getId());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public static boolean changePassword(int userId, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newPassword);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteUser(int userId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            deleteUserTransactions(conn, userId);
            deleteUserBudgets(conn, userId);
            deleteUserGoals(conn, userId);
            deleteUserAccounts(conn, userId);
            deleteUserCategories(conn, userId);
            deleteUserRecord(conn, userId);
            return true;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    private static void deleteUserTransactions(Connection conn, int userId) throws SQLException {
        String sql = "DELETE FROM transactions WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }

    private static void deleteUserBudgets(Connection conn, int userId) throws SQLException {
        String sql = "DELETE FROM budgets WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }

    private static void deleteUserGoals(Connection conn, int userId) throws SQLException {
        String sql = "DELETE FROM goals WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }

    private static void deleteUserAccounts(Connection conn, int userId) throws SQLException {
        String sql = "DELETE FROM accounts WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }

    private static void deleteUserCategories(Connection conn, int userId) throws SQLException {
        String sql = "DELETE FROM categories WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }

    private static void deleteUserRecord(Connection conn, int userId) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }
}
