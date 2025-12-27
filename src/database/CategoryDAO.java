package database;

import models.Category;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    public static boolean createCategory(int userId, String name, String type) {
        String sql = "INSERT INTO categories (user_id, category_name, type, is_default) " +
                    "VALUES (?, ?, ?, false)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, name);
            stmt.setString(3, type);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public static List<Category> getUserCategories(int userId) {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM categories WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Category c = new Category(
                    rs.getInt("id"), rs.getInt("user_id"), rs.getString("category_name"),
                    Category.CategoryType.valueOf(rs.getString("type")),
                    rs.getBoolean("is_default")
                );
                list.add(c);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return list;
    }

    public static List<Category> getCategoriesByType(int userId, String type) {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM categories WHERE user_id = ? AND type = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, type);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Category c = new Category(
                    rs.getInt("id"), rs.getInt("user_id"), rs.getString("category_name"),
                    Category.CategoryType.valueOf(rs.getString("type")),
                    rs.getBoolean("is_default")
                );
                list.add(c);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return list;
    }

    public static Category getCategoryById(int categoryId) {
        String sql = "SELECT * FROM categories WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, categoryId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Category c = new Category(
                    rs.getInt("id"), rs.getInt("user_id"), rs.getString("category_name"),
                    Category.CategoryType.valueOf(rs.getString("type")),
                    rs.getBoolean("is_default")
                );
                return c;
            }
            return null;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public static boolean updateCategory(int categoryId, String name) {
        String sql = "UPDATE categories SET category_name = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setInt(2, categoryId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteCategory(int categoryId) {
        String sql = "DELETE FROM categories WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, categoryId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }
}
