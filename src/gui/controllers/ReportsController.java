package gui.controllers;

import javafx.scene.control.Alert;
import database.TransactionDAO;
import database.BudgetDAO;
import database.GoalDAO;
import models.Transaction;
import models.Budget;
import models.Goal;
import utils.FileManager;
import gui.views.ReportsView;
import java.util.List;

public class ReportsController {
    private ReportsView view;
    private TransactionDAO transactionDAO;
    private BudgetDAO budgetDAO;
    private GoalDAO goalDAO;

    public ReportsController(ReportsView view) {
        this.view = view;
        this.transactionDAO = new TransactionDAO();
        this.budgetDAO = new BudgetDAO();
        this.goalDAO = new GoalDAO();
        attachEventHandlers();
    }

    private void attachEventHandlers() {
        view.getTransactionsButton().setOnAction(event -> exportTransactions());
        view.getBudgetsButton().setOnAction(event -> exportBudgets());
        view.getGoalsButton().setOnAction(event -> exportGoals());
        view.getAllDataButton().setOnAction(event -> exportAllData());
    }

    private void exportTransactions() {
        List<Transaction> transactions = transactionDAO.getUserTransactions(view.getCurrentUser().getId());
        if (transactions.isEmpty()) {
            showMessage("No Data", "No transactions to export");
            return;
        }
        try {
            String path = FileManager.exportTransactions(transactions);
            showMessage("Success", "Exported to:\n" + path);
        } catch (Exception exception) {
            showMessage("Error", exception.getMessage());
        }
    }

    private void exportBudgets() {
        List<Budget> budgets = budgetDAO.getUserBudgets(view.getCurrentUser().getId());
        if (budgets.isEmpty()) {
            showMessage("No Data", "No budgets to export");
            return;
        }
        try {
            String path = FileManager.exportBudgets(budgets);
            showMessage("Success", "Exported to:\n" + path);
        } catch (Exception exception) {
            showMessage("Error", exception.getMessage());
        }
    }

    private void exportGoals() {
        List<Goal> goals = goalDAO.getUserGoals(view.getCurrentUser().getId());
        if (goals.isEmpty()) {
            showMessage("No Data", "No goals to export");
            return;
        }
        try {
            String path = FileManager.exportGoals(goals);
            showMessage("Success", "Exported to:\n" + path);
        } catch (Exception exception) {
            showMessage("Error", exception.getMessage());
        }
    }

    private void exportAllData() {
        List<Transaction> transactions = transactionDAO.getUserTransactions(view.getCurrentUser().getId());
        List<Budget> budgets = budgetDAO.getUserBudgets(view.getCurrentUser().getId());
        List<Goal> goals = goalDAO.getUserGoals(view.getCurrentUser().getId());
        if (transactions.isEmpty() && budgets.isEmpty() && goals.isEmpty()) {
            showMessage("No Data", "No data to export");
            return;
        }
        try {
            String path = FileManager.exportAllData(transactions, budgets, goals);
            showMessage("Success", "Exported to:\n" + path);
        } catch (Exception exception) {
            showMessage("Error", exception.getMessage());
        }
    }

    private void showMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
