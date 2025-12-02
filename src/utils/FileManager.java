package utils;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import models.*;

public class FileManager {

    // Create reports folder when program starts
    static {
        new File("reports").mkdirs();
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

        List<String> rows = new java.util.ArrayList<>();
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

        List<String> rows = new java.util.ArrayList<>();
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

        List<String> rows = new java.util.ArrayList<>();
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

    // Open file in default program
    public static boolean openFile(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                System.out.println("File not found: " + filePath);
                return false;
            }

            java.awt.Desktop.getDesktop().open(file);
            return true;

        } catch (Exception e) {
            System.out.println("Could not open file: " + e.getMessage());
            return false;
        }
    }
}
