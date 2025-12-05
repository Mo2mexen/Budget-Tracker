import database.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import models.*;
import utils.FileManager;

public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static User currentUser = null;
    private static List<Budget> budgets = new ArrayList<>();
    private static List<Goal> goals = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("BUDGET TRACKER");
        System.out.println();
        showMainMenu();
        scanner.close();
    }

    private static void showMainMenu() {
        while (true) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Login");
            System.out.println("2. Create Account");
            System.out.println("3. Exit");
            System.out.print("Choice: ");

            int choice = getInt();
            if (choice == 1) login();
            else if (choice == 2) createAccount();
            else if (choice == 3) {
                System.out.println("Goodbye");
                return;
            }
        }
    }

    private static void login() {
        System.out.println("\n--- LOGIN ---");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        currentUser = UserDAO.login(username, password);
        if (currentUser != null) {
            System.out.println("Login successful");
            loadUserData();
            showDashboard();
        } else {
            System.out.println("Invalid credentials");
        }
    }

    private static void createAccount() {
        System.out.println("\n--- CREATE ACCOUNT ---");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Full Name: ");
        String fullName = scanner.nextLine();
        System.out.print("Currency (USD, EUR, etc.): ");
        String currency = scanner.nextLine();

        if (UserDAO.createUser(username, password, email, fullName, currency)) {
            System.out.println("Account created successfully");
        } else {
            System.out.println("Failed to create account");
        }
    }

    private static void loadUserData() {
        budgets = BudgetDAO.getUserBudgets(currentUser.getId());
        goals = GoalDAO.getUserGoals(currentUser.getId());
    }

    private static void showDashboard() {
        while (true) {
            System.out.println("\n--- DASHBOARD ---");
            System.out.println("Welcome, " + currentUser.getFullName());

            double income = TransactionDAO.getTotalIncome(currentUser.getId());
            double expenses = TransactionDAO.getTotalExpenses(currentUser.getId());
            double balance = income - expenses;

            System.out.println("Income:   " + currentUser.getFormattedCurrency(income));
            System.out.println("Expenses: " + currentUser.getFormattedCurrency(expenses));
            System.out.println("Balance:  " + currentUser.getFormattedCurrency(balance));
            System.out.println();

            System.out.println("1. Add Transaction");
            System.out.println("2. View Transactions");
            System.out.println("3. Monthly Report");
            System.out.println("4. View Budgets");
            System.out.println("5. View Goals");
            System.out.println("6. Export Data");
            System.out.println("7. Profile");
            System.out.println("8. Logout");
            System.out.print("Choice: ");

            int choice = getInt();
            if (choice == 1) addTransaction();
            else if (choice == 2) viewTransactions();
            else if (choice == 3) monthlyReport();
            else if (choice == 4) viewBudgets();
            else if (choice == 5) viewGoals();
            else if (choice == 6) exportData();
            else if (choice == 7) showProfile();
            else if (choice == 8) {
                System.out.println("Logged out");
                currentUser = null;
                return;
            }
        }
    }

    private static void addTransaction() {
        System.out.println("\n--- ADD TRANSACTION ---");
        System.out.print("Description: ");
        String description = scanner.nextLine();
        System.out.print("Amount: ");
        double amount = getDouble();
        System.out.print("Type (INCOME/EXPENSE): ");
        String type = scanner.nextLine().toUpperCase();
        System.out.print("Notes: ");
        String notes = scanner.nextLine();

        boolean success = TransactionDAO.addTransaction(
            currentUser.getId(), 1, 1, amount, description,
            LocalDate.now(), type, notes
        );

        if (success) System.out.println("Transaction added");
        else System.out.println("Failed to add transaction");
    }

    private static void viewTransactions() {
        System.out.println("\n--- TRANSACTIONS ---");
        List<Transaction> transactions = TransactionDAO.getUserTransactions(currentUser.getId());

        if (transactions.isEmpty()) {
            System.out.println("No transactions found");
            return;
        }

        System.out.println("Total: " + transactions.size());
        for (Transaction t : transactions) {
            System.out.println(t.toString());
        }
    }

    private static void monthlyReport() {
        System.out.println("\n--- MONTHLY REPORT ---");
        System.out.print("Month (1-12): ");
        int month = getInt();
        System.out.print("Year: ");
        int year = getInt();

        List<Transaction> transactions = TransactionDAO.getTransactionsByMonth(
            currentUser.getId(), month, year
        );

        if (transactions.isEmpty()) {
            System.out.println("No transactions for " + month + "/" + year);
            return;
        }

        double income = 0, expenses = 0;
        for (Transaction t : transactions) {
            if (t.isIncome()) income += t.getAmount();
            else if (t.isExpense()) expenses += t.getAmount();
        }

        System.out.println("\nReport for " + month + "/" + year);
        System.out.println("Transactions: " + transactions.size());
        System.out.println("Income:  " + currentUser.getFormattedCurrency(income));
        System.out.println("Expenses: " + currentUser.getFormattedCurrency(expenses));
        System.out.println("Net:      " + currentUser.getFormattedCurrency(income - expenses));
    }

    private static void viewBudgets() {
        System.out.println("\n--- BUDGETS ---");

        if (budgets.isEmpty()) {
            System.out.println("No budgets found");
            return;
        }

        for (Budget b : budgets) {
            System.out.println(b.getDisplayInfo());
        }

        System.out.println("\nDetailed view:");
        for (Budget b : budgets) {
            System.out.println("Progress: " + b.calculatePercentage() + "%");
            System.out.println("Remaining: $" + b.calculateRemaining());
            b.printDetails();
            System.out.println();
        }
    }

    private static void viewGoals() {
        System.out.println("\n--- GOALS ---");

        if (goals.isEmpty()) {
            System.out.println("No goals found");
            return;
        }

        for (Goal g : goals) {
            System.out.println(g.getDisplayInfo());
        }

        System.out.println("\nDetailed view:");
        for (Goal g : goals) {
            System.out.println("Progress: " + g.calculatePercentage() + "%");
            System.out.println("Remaining: $" + g.calculateRemaining());
            g.printDetails();
            System.out.println();
        }
    }

    private static void showProfile() {
        System.out.println("\n--- PROFILE ---");
        System.out.println("Username: " + currentUser.getUsername());
        System.out.println("Email:    " + currentUser.getEmail());
        System.out.println("Name:     " + currentUser.getFullName());
        System.out.println("Currency: " + currentUser.getCurrency());
        System.out.println("Joined:   " + currentUser.getCreatedDate());
    }

    private static void exportData() {
        System.out.println("\n--- EXPORT DATA ---");
        System.out.println("1. Export Transactions");
        System.out.println("2. Export Budgets");
        System.out.println("3. Export Goals");
        System.out.println("4. Export All");
        System.out.println("5. Back");
        System.out.print("Choice: ");

        int choice = getInt();

        if (choice == 1) {
            List<Transaction> transactions = TransactionDAO.getUserTransactions(currentUser.getId());
            if (transactions.isEmpty()) {
                System.out.println("No transactions to export");
                return;
            }
            String filePath = FileManager.exportTransactions(transactions);
            if (filePath != null) {
                System.out.println("Exported " + transactions.size() + " transactions to: " + filePath);
                System.out.print("Open file? (y/n): ");
                String open = scanner.nextLine();
                if (open.equalsIgnoreCase("y")) {
                    FileManager.openFile(filePath);
                }
            }
        } else if (choice == 2) {
            if (budgets.isEmpty()) {
                System.out.println("No budgets to export");
                return;
            }
            String filePath = FileManager.exportBudgets(budgets);
            if (filePath != null) {
                System.out.println("Exported " + budgets.size() + " budgets to: " + filePath);
                System.out.print("Open file? (y/n): ");
                String open = scanner.nextLine();
                if (open.equalsIgnoreCase("y")) {
                    FileManager.openFile(filePath);
                }
            }
        } else if (choice == 3) {
            if (goals.isEmpty()) {
                System.out.println("No goals to export");
                return;
            }
            String filePath = FileManager.exportGoals(goals);
            if (filePath != null) {
                System.out.println("Exported " + goals.size() + " goals to: " + filePath);
                System.out.print("Open file? (y/n): ");
                String open = scanner.nextLine();
                if (open.equalsIgnoreCase("y")) {
                    FileManager.openFile(filePath);
                }
            }
        } else if (choice == 4) {
            int exported = 0;
            List<Transaction> transactions = TransactionDAO.getUserTransactions(currentUser.getId());
            if (!transactions.isEmpty()) {
                String filePath = FileManager.exportTransactions(transactions);
                if (filePath != null) {
                    System.out.println("Exported transactions to: " + filePath);
                    exported++;
                }
            }
            if (!budgets.isEmpty()) {
                String filePath = FileManager.exportBudgets(budgets);
                if (filePath != null) {
                    System.out.println("Exported budgets to: " + filePath);
                    exported++;
                }
            }
            if (!goals.isEmpty()) {
                String filePath = FileManager.exportGoals(goals);
                if (filePath != null) {
                    System.out.println("Exported goals to: " + filePath);
                    exported++;
                }
            }
            if (exported > 0) {
                System.out.println("\nExported " + exported + " file(s) to reports/ folder");
            } else {
                System.out.println("No data to export");
            }
        }
    }

    private static int getInt() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            return -1;
        }
    }

    private static double getDouble() {
        try {
            return Double.parseDouble(scanner.nextLine());
        } catch (Exception e) {
            return 0.0;
        }
    }
}
