package utils;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import models.*;

public class FileManager {

    // Ensure reports folder exists
    private static void ensureReportsFolder() {
        File reportsDir = new File("reports");
        if (!reportsDir.exists()) {
            reportsDir.mkdirs();
        }
    }

    // Get today's date
    private static String getDate() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd_MM_yyyy"));
    }

    // Fix text for CSV 
    private static String fixText(String text) {
        if (text == null) return "";
        if (text.contains(",") || text.contains("\"")) {
            return "\"" + text.replace("\"", "\"\"") + "\"";
        }
        return text;
    }

    // Write data to CSV file
    private static String writeCSV(String fileName, String header, List<String> rows) {
        try {
            ensureReportsFolder();

            PrintWriter writer = new PrintWriter(new FileWriter(fileName));
            writer.println(header);
            for (String row : rows) {
                writer.println(row);
            }
            writer.close();
            return fileName;
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
            return null;
        }
    }

    // Export transactions to CSV
    public static String exportTransactions(List<Transaction> list) {
        String fileName = "reports/transactions_" + getDate() + ".csv";
        String header = "Date,Description,Amount,Type,Notes";

        List<String> rows = new ArrayList<>();
        for (Transaction t : list) {
            String row = fixText(t.getTransactionDate().toString()) + "," +
                        fixText(t.getDescription()) + "," +
                        fixText(String.valueOf(t.getAmount())) + "," +
                        fixText(t.getType().toString()) + "," +
                        fixText(t.getNotes());
            rows.add(row);
        }

        return writeCSV(fileName, header, rows);
    }

    // Export budgets to CSV
    public static String exportBudgets(List<Budget> list) {
        String fileName = "reports/budgets_" + getDate() + ".csv";
        String header = "Month,Year,Limit,Spent,Status";

        List<String> rows = new ArrayList<>();
        for (Budget b : list) {
            String row = b.getMonth() + "," +
                        b.getYear() + "," +
                        b.getMonthlyLimit() + "," +
                        b.getCurrentSpent() + "," +
                        fixText(b.getStatus());
            rows.add(row);
        }

        return writeCSV(fileName, header, rows);
    }

    // Export goals to CSV
    public static String exportGoals(List<Goal> list) {
        String fileName = "reports/goals_" + getDate() + ".csv";
        String header = "Name,Target,Current,Progress,Deadline";

        List<String> rows = new ArrayList<>();
        for (Goal g : list) {
            String row = fixText(g.getGoalName()) + "," +
                        g.getTargetAmount() + "," +
                        g.getCurrentAmount() + "," +
                        g.getProgress() + "%," +
                        g.getDeadline();
            rows.add(row);
        }

        return writeCSV(fileName, header, rows);
    }

    // Export all data (transactions, budgets, goals) to a single CSV file
    public static String exportAllData(List<Transaction> transactions, List<Budget> budgets, List<Goal> goals) {
        String fileName = "reports/complete_report_" + getDate() + ".csv";

        try {
            ensureReportsFolder();

            PrintWriter writer = new PrintWriter(new FileWriter(fileName));
            
            // Write transactions section
            if (!transactions.isEmpty()) {
                writer.println("=== TRANSACTIONS ===");
                writer.println("Date,Description,Amount,Type,Notes");
                for (Transaction t : transactions) {
                    String row = fixText(t.getTransactionDate().toString()) + "," +
                                fixText(t.getDescription()) + "," +
                                fixText(String.valueOf(t.getAmount())) + "," +
                                fixText(t.getType().toString()) + "," +
                                fixText(t.getNotes());
                    writer.println(row);
                }
                writer.println(); // Empty line separator
            }
            
            // Write budgets section
            if (!budgets.isEmpty()) {
                writer.println("=== BUDGETS ===");
                writer.println("Month,Year,Limit,Spent,Status");
                for (Budget b : budgets) {
                    String row = b.getMonth() + "," +
                                b.getYear() + "," +
                                b.getMonthlyLimit() + "," +
                                b.getCurrentSpent() + "," +
                                fixText(b.getStatus());
                    writer.println(row);
                }
                writer.println(); // Empty line separator
            }
            
            // Write goals section
            if (!goals.isEmpty()) {
                writer.println("=== GOALS ===");
                writer.println("Name,Target,Current,Progress,Deadline");
                for (Goal g : goals) {
                    String row = fixText(g.getGoalName()) + "," +
                                g.getTargetAmount() + "," +
                                g.getCurrentAmount() + "," +
                                g.getProgress() + "%," +
                                g.getDeadline();
                    writer.println(row);
                }
            }
            
            writer.close();
            return fileName;
            
        } catch (IOException e) {
            System.out.println("Error saving complete report: " + e.getMessage());
            return null;
        }
    }
}
