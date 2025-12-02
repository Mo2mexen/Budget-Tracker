package database;

import models.Goal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GoalDAO {

    public static boolean createGoal(int userId, String name, double target, LocalDate deadline) {
        String sql = "INSERT INTO goals (user_id, goal_name, target_amount, current_amount, deadline, status, created_date) " +
                    "VALUES (?, ?, ?, 0, ?, 'ACTIVE', ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, name);
            stmt.setDouble(3, target);
            stmt.setDate(4, Date.valueOf(deadline));
            stmt.setDate(5, Date.valueOf(LocalDate.now()));
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public static List<Goal> getUserGoals(int userId) {
        List<Goal> list = new ArrayList<>();
        String sql = "SELECT * FROM goals WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Goal g = new Goal(
                    rs.getInt("id"), rs.getInt("user_id"), rs.getString("goal_name"),
                    rs.getDouble("target_amount"), rs.getDouble("current_amount"),
                    rs.getDate("deadline").toLocalDate()
                );
                g.setStatus(Goal.GoalStatus.valueOf(rs.getString("status")));
                g.setCreatedDate(rs.getDate("created_date").toLocalDate());
                list.add(g);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return list;
    }

    public static boolean updateProgress(int goalId, double current) {
        String sql = "UPDATE goals SET current_amount = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, current);
            stmt.setInt(2, goalId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public static boolean updateStatus(int goalId, String status) {
        String sql = "UPDATE goals SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, goalId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteGoal(int goalId) {
        String sql = "DELETE FROM goals WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, goalId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }
}
