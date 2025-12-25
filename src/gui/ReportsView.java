package gui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import models.User;
import database.*;
import utils.FileManager;

public class ReportsView {
    private User currentUser;
    private TransactionDAO transactionDAO;
    private BudgetDAO budgetDAO;
    private GoalDAO goalDAO;
    private FileManager fileManager;
    private VBox mainContainer;

    public ReportsView(User currentUser) {
        this.currentUser = currentUser;
        this.transactionDAO = new TransactionDAO();
        this.budgetDAO = new BudgetDAO();
        this.goalDAO = new GoalDAO();
        this.fileManager = new FileManager();
        this.mainContainer = createView();
    }

    private VBox createView() {
        VBox container = new VBox(10);
        container.setPadding(new Insets(10));

        // Title
        Label titleLabel = new Label("Export Reports");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Transactions export row
        HBox transactionsRow = new HBox(10);
        Label transactionsLabel = new Label("Export Transactions");
        Button transactionsButton = new Button("Export");
        transactionsButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        transactionsButton.setOnAction(event -> exportTransactions());
        transactionsRow.getChildren().addAll(transactionsLabel, transactionsButton);

        // Budgets export row
        HBox budgetsRow = new HBox(10);
        Label budgetsLabel = new Label("Export Budgets");
        Button budgetsButton = new Button("Export");
        budgetsButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        budgetsButton.setOnAction(event -> exportBudgets());
        budgetsRow.getChildren().addAll(budgetsLabel, budgetsButton);

        // Goals export row
        HBox goalsRow = new HBox(10);
        Label goalsLabel = new Label("Export Goals");
        Button goalsButton = new Button("Export");
        goalsButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        goalsButton.setOnAction(event -> exportGoals());
        goalsRow.getChildren().addAll(goalsLabel, goalsButton);

        // All data export row
        HBox allDataRow = new HBox(10);
        Label allDataLabel = new Label("Export All Data");
        Button allDataButton = new Button("Export");
        allDataButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        allDataButton.setOnAction(event -> exportAllData());
        allDataRow.getChildren().addAll(allDataLabel, allDataButton);

        container.getChildren().addAll(titleLabel, transactionsRow, budgetsRow, goalsRow, allDataRow);
        return container;
    }

    private void exportTransactions() {
        var transactions = transactionDAO.getUserTransactions(currentUser.getId());
        if (transactions.isEmpty()) {
            showMessage("No Data", "No transactions to export");
            return;
        }
        try {
            String path = fileManager.exportTransactions(transactions);
            showMessage("Success", "Exported to:\n" + path);
        } catch (Exception exception) {
            showMessage("Error", exception.getMessage());
        }
    }

    private void exportBudgets() {
        var budgets = budgetDAO.getUserBudgets(currentUser.getId());
        if (budgets.isEmpty()) {
            showMessage("No Data", "No budgets to export");
            return;
        }
        try {
            String path = fileManager.exportBudgets(budgets);
            showMessage("Success", "Exported to:\n" + path);
        } catch (Exception exception) {
            showMessage("Error", exception.getMessage());
        }
    }

    private void exportGoals() {
        var goals = goalDAO.getUserGoals(currentUser.getId());
        if (goals.isEmpty()) {
            showMessage("No Data", "No goals to export");
            return;
        }
        try {
            String path = fileManager.exportGoals(goals);
            showMessage("Success", "Exported to:\n" + path);
        } catch (Exception exception) {
            showMessage("Error", exception.getMessage());
        }
    }

    private void exportAllData() {
        var transactions = transactionDAO.getUserTransactions(currentUser.getId());
        var budgets = budgetDAO.getUserBudgets(currentUser.getId());
        var goals = goalDAO.getUserGoals(currentUser.getId());
        if (transactions.isEmpty() && budgets.isEmpty() && goals.isEmpty()) {
            showMessage("No Data", "No data to export");
            return;
        }
        try {
            String path = fileManager.exportAllData(transactions, budgets, goals);
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

    public VBox getView() {
        return mainContainer;
    }
}
