import database.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import models.*;
import utils.FileManager;
import utils.ValidationHelper;

public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static User currentUser = null;
    private static List<Budget> budgets = new ArrayList<>();
    private static List<Goal> goals = new ArrayList<>();

    // Default account and category for quick transactions
    private static final int DEFAULT_ACCOUNT_ID = 1;
    private static final int DEFAULT_CATEGORY_ID = 1;

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
            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    createAccount();
                    break;
                case 3:
                    System.out.println("Goodbye");
                    return;
                default:
                    System.out.println("Invalid choice");
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
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Full Name: ");
        String fullName = scanner.nextLine().trim();
        System.out.print("Currency (USD, EUR, EGP): ");
        String currency = scanner.nextLine().trim().toUpperCase();

        // Validate username
        if (!ValidationHelper.isValidUsername(username)) {
            System.out.println("Error: " + ValidationHelper.getStringLengthErrorMessage("Username", ValidationHelper.MIN_USERNAME_LENGTH, ValidationHelper.MAX_USERNAME_LENGTH));
            return;
        }

        // Validate password
        if (!ValidationHelper.isValidPassword(password)) {
            System.out.println("Error: " + ValidationHelper.getPasswordErrorMessage());
            return;
        }

        // Validate email
        if (!ValidationHelper.isValidEmail(email)) {
            System.out.println("Error: " + ValidationHelper.getEmailErrorMessage());
            return;
        }

        // Validate full name
        if (!ValidationHelper.isValidString(fullName, ValidationHelper.MIN_NAME_LENGTH, ValidationHelper.MAX_NAME_LENGTH)) {
            System.out.println("Error: " + ValidationHelper.getStringLengthErrorMessage("Full name", ValidationHelper.MIN_NAME_LENGTH, ValidationHelper.MAX_NAME_LENGTH));
            return;
        }

        // Validate currency
        if (!ValidationHelper.isValidCurrency(currency)) {
            System.out.println("Error: Invalid currency. Please use USD, EUR, or EGP");
            return;
        }

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

            switch (choice) {
                case 1:
                    addTransaction();
                    break;
                case 2:
                    viewTransactions();
                    break;
                case 3:
                    monthlyReport();
                    break;
                case 4:
                    viewBudgets();
                    break;
                case 5:
                    viewGoals();
                    break;
                case 6:
                    exportData();
                    break;
                case 7:
                    showProfile();
                    break;
                case 8:
                    System.out.println("Logged out");
                    currentUser = null;
                    return;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    private static void addTransaction() {
        System.out.println("\n--- ADD TRANSACTION ---");
        System.out.print("Description: ");
        String description = scanner.nextLine().trim();
        System.out.print("Amount: ");
        double amount = getDouble();
        System.out.print("Type (INCOME/EXPENSE): ");
        String type = scanner.nextLine().trim().toUpperCase();
        System.out.print("Notes: ");
        String notes = scanner.nextLine().trim();

        // Validate description
        if (!ValidationHelper.isValidString(description, ValidationHelper.MIN_DESCRIPTION_LENGTH, ValidationHelper.MAX_DESCRIPTION_LENGTH)) {
            System.out.println("Error: " + ValidationHelper.getStringLengthErrorMessage("Description", ValidationHelper.MIN_DESCRIPTION_LENGTH, ValidationHelper.MAX_DESCRIPTION_LENGTH));
            return;
        }

        // Validate amount
        if (!ValidationHelper.isValidAmount(amount)) {
            System.out.println("Error: " + ValidationHelper.getAmountErrorMessage());
            return;
        }

        // Validate transaction type
        if (!ValidationHelper.isValidTransactionType(type)) {
            System.out.println("Error: Type must be INCOME or EXPENSE");
            return;
        }

        boolean success = TransactionDAO.addTransaction(
            currentUser.getId(), DEFAULT_ACCOUNT_ID, DEFAULT_CATEGORY_ID, amount, description,
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
        while (!ValidationHelper.isValidMonth(month)) {
            System.out.println("Error: " + ValidationHelper.getMonthErrorMessage());
            System.out.print("Month (1-12): ");
            month = getInt();
        }
        System.out.print("Year: ");
        int year = getInt();
        while (!ValidationHelper.isValidYear(year)) {
            System.out.println("Error: " + ValidationHelper.getYearErrorMessage());
            System.out.print("Year: ");
            year = getInt();
        }

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

        switch (choice) {
            case 1:
                List<Transaction> transactions = TransactionDAO.getUserTransactions(currentUser.getId());
                if (transactions.isEmpty()) {
                    System.out.println("No transactions to export");
                    return;
                }
                String transFilePath = FileManager.exportTransactions(transactions);
                if (transFilePath != null) {
                    System.out.println("Exported transactions to: " + transFilePath);
                } else {
                    System.out.println("Failed to export transactions");
                }
                break;

            case 2:
                if (budgets.isEmpty()) {
                    System.out.println("No budgets to export");
                    return;
                }
                String budgetFilePath = FileManager.exportBudgets(budgets);
                if (budgetFilePath != null) {
                    System.out.println("Exported budgets to: " + budgetFilePath);
                } else {
                    System.out.println("Failed to export budgets");
                }
                break;

            case 3:
                if (goals.isEmpty()) {
                    System.out.println("No goals to export");
                    return;
                }
                String goalFilePath = FileManager.exportGoals(goals);
                if (goalFilePath != null) {
                    System.out.println("Exported goals to: " + goalFilePath);
                } else {
                    System.out.println("Failed to export goals");
                }
                break;

            case 4:
                List<Transaction> allTransactions = TransactionDAO.getUserTransactions(currentUser.getId());

                if (allTransactions.isEmpty() && budgets.isEmpty() && goals.isEmpty()) {
                    System.out.println("No data to export");
                    return;
                }

                String allFilePath = FileManager.exportAllData(allTransactions, budgets, goals);
                if (allFilePath != null) {
                    System.out.println("Exported all data to: " + allFilePath);
                } else {
                    System.out.println("Failed to export data");
                }
                break;

            case 5:
                return;

            default:
                System.out.println("Invalid choice");
        }
    }

    private static int getInt() {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    System.out.println("Invalid number! Please enter a valid integer.");
                    System.out.print("Try again: ");
                    continue;
                }
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number! Please enter a valid integer.");
                System.out.print("Try again: ");
            }
        }
    }

    private static double getDouble() {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    System.out.println("Invalid number! Please enter a valid number.");
                    System.out.print("Try again: ");
                    continue;
                }
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number! Please enter a valid number.");
                System.out.print("Try again: ");
            }
        }
    }
}
