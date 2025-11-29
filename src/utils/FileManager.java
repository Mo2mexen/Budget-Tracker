package utils;

import models.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FileManager {

    // Create reports folder when program starts
    static {
        new File("reports").mkdirs();
    }

    // Get today's date in format: 29_11_2025
    private static String getDate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd_MM_yyyy");
        return now.format(format);
    }

    // Fix text for CSV (wrap if it has comma or quote)
    private static String fix(String text) {
        if (text == null) {
            return "";
        }
        if (text.contains(",") || text.contains("\"")) {
            return "\"" + text.replace("\"", "\"\"") + "\"";
        }
        return text;
    }

    // Export transactions to CSV file
    public static String exportTransactionsCSV(List<Transaction> list) {
        String fileName = "reports/transactions_" + getDate() + ".csv";

        try {
            PrintWriter writer = new PrintWriter(new FileWriter(fileName));

            // Write header row
            writer.println("Date,Description,Amount,Type,Notes");

            // Write each transaction as a row
            for (int i = 0; i < list.size(); i++) {
                Transaction t = list.get(i);
                String row = fix(t.getTransactionDate().toString()) + "," +
                            fix(t.getDescription()) + "," +
                            fix(String.valueOf(t.getAmount())) + "," +
                            fix(t.getType().toString()) + "," +
                            fix(t.getNotes());
                writer.println(row);
            }

            writer.close();
            return fileName;

        } catch (IOException e) {
            System.out.println("Error: Could not save transactions file");
            return null;
        }
    }

    // Export budgets to CSV file
    public static String exportBudgetsCSV(List<Budget> list) {
        String fileName = "reports/budgets_" + getDate() + ".csv";

        try {
            PrintWriter writer = new PrintWriter(new FileWriter(fileName));

            // Write header row
            writer.println("Month,Year,Limit,Spent,Status");

            // Write each budget as a row
            for (int i = 0; i < list.size(); i++) {
                Budget b = list.get(i);
                String row = fix(String.valueOf(b.getMonth())) + "," +
                            fix(String.valueOf(b.getYear())) + "," +
                            fix(String.valueOf(b.getMonthlyLimit())) + "," +
                            fix(String.valueOf(b.getCurrentSpent())) + "," +
                            fix(b.getStatus());
                writer.println(row);
            }

            writer.close();
            return fileName;

        } catch (IOException e) {
            System.out.println("Error: Could not save budgets file");
            return null;
        }
    }

    // Export goals to CSV file
    public static String exportGoalsCSV(List<Goal> list) {
        String fileName = "reports/goals_" + getDate() + ".csv";

        try {
            PrintWriter writer = new PrintWriter(new FileWriter(fileName));

            // Write header row
            writer.println("Name,Target,Current,Progress,Deadline");

            // Write each goal as a row
            for (int i = 0; i < list.size(); i++) {
                Goal g = list.get(i);
                String row = fix(g.getGoalName()) + "," +
                            fix(String.valueOf(g.getTargetAmount())) + "," +
                            fix(String.valueOf(g.getCurrentAmount())) + "," +
                            fix(g.getProgress() + "%") + "," +
                            fix(g.getDeadline().toString());
                writer.println(row);
            }

            writer.close();
            return fileName;

        } catch (IOException e) {
            System.out.println("Error: Could not save goals file");
            return null;
        }
    }

    // Export all data to one big CSV file
    public static String exportAllDataCSV(User user, List<Account> accounts,
                                         List<Transaction> transactions,
                                         List<Budget> budgets,
                                         List<Goal> goals) {
        String fileName = "reports/complete_report_" + getDate() + ".csv";

        try {
            PrintWriter writer = new PrintWriter(new FileWriter(fileName));

            // === USER INFO ===
            writer.println("=== USER INFORMATION ===");
            writer.println("ID,Username,Email,Created Date");
            String userRow = fix(String.valueOf(user.getId())) + "," +
                            fix(user.getUsername()) + "," +
                            fix(user.getEmail()) + "," +
                            fix(user.getCreatedDate().toString());
            writer.println(userRow);
            writer.println();

            // === ACCOUNTS ===
            writer.println("=== ACCOUNTS ===");
            writer.println("Account ID,Name,Type,Balance");
            for (int i = 0; i < accounts.size(); i++) {
                Account a = accounts.get(i);
                String row = fix(String.valueOf(a.getId())) + "," +
                            fix(a.getAccountName()) + "," +
                            fix(a.getAccountType().toString()) + "," +
                            fix(String.valueOf(a.getBalance()));
                writer.println(row);
            }
            writer.println();

            // === TRANSACTIONS ===
            writer.println("=== TRANSACTIONS ===");
            writer.println("Date,Description,Amount,Type,Notes");
            for (int i = 0; i < transactions.size(); i++) {
                Transaction t = transactions.get(i);
                String row = fix(t.getTransactionDate().toString()) + "," +
                            fix(t.getDescription()) + "," +
                            fix(String.valueOf(t.getAmount())) + "," +
                            fix(t.getType().toString()) + "," +
                            fix(t.getNotes());
                writer.println(row);
            }
            writer.println();

            // === BUDGETS ===
            writer.println("=== BUDGETS ===");
            writer.println("Month,Year,Limit,Spent,Status");
            for (int i = 0; i < budgets.size(); i++) {
                Budget b = budgets.get(i);
                String row = fix(String.valueOf(b.getMonth())) + "," +
                            fix(String.valueOf(b.getYear())) + "," +
                            fix(String.valueOf(b.getMonthlyLimit())) + "," +
                            fix(String.valueOf(b.getCurrentSpent())) + "," +
                            fix(b.getStatus());
                writer.println(row);
            }
            writer.println();

            // === GOALS ===
            writer.println("=== GOALS ===");
            writer.println("Name,Target,Current,Progress,Deadline");
            for (int i = 0; i < goals.size(); i++) {
                Goal g = goals.get(i);
                String row = fix(g.getGoalName()) + "," +
                            fix(String.valueOf(g.getTargetAmount())) + "," +
                            fix(String.valueOf(g.getCurrentAmount())) + "," +
                            fix(g.getProgress() + "%") + "," +
                            fix(g.getDeadline().toString());
                writer.println(row);
            }

            writer.close();
            return fileName;

        } catch (IOException e) {
            System.out.println("Error: Could not save complete report file");
            return null;
        }
    }

    // Open the CSV file in Excel (or default program)
    public static boolean openFile(String path) {
        try {
            File file = new File(path);

            // Check if file exists
            if (!file.exists()) {
                System.out.println("Error: File does not exist");
                return false;
            }

            // Try to open file
            if (java.awt.Desktop.isDesktopSupported()) {
                java.awt.Desktop.getDesktop().open(file);
                return true;
            }

            System.out.println("Error: Cannot open file on this system");
            return false;

        } catch (Exception e) {
            System.out.println("Error: Could not open file");
            return false;
        }
    }
}
