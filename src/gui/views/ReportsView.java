package gui.views;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import models.User;

public class ReportsView {
    private User currentUser;
    private VBox mainContainer;
    private Button transactionsButton;
    private Button budgetsButton;
    private Button goalsButton;
    private Button allDataButton;

    public ReportsView(User currentUser) {
        this.currentUser = currentUser;
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
        transactionsButton = new Button("Export");
        transactionsButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        transactionsRow.getChildren().addAll(transactionsLabel, transactionsButton);

        // Budgets export row
        HBox budgetsRow = new HBox(10);
        Label budgetsLabel = new Label("Export Budgets");
        budgetsButton = new Button("Export");
        budgetsButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        budgetsRow.getChildren().addAll(budgetsLabel, budgetsButton);

        // Goals export row
        HBox goalsRow = new HBox(10);
        Label goalsLabel = new Label("Export Goals");
        goalsButton = new Button("Export");
        goalsButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        goalsRow.getChildren().addAll(goalsLabel, goalsButton);

        // All data export row
        HBox allDataRow = new HBox(10);
        Label allDataLabel = new Label("Export All Data");
        allDataButton = new Button("Export");
        allDataButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        allDataRow.getChildren().addAll(allDataLabel, allDataButton);

        container.getChildren().addAll(titleLabel, transactionsRow, budgetsRow, goalsRow, allDataRow);
        return container;
    }

    // Getters for controller
    public User getCurrentUser() {
        return currentUser;
    }

    public VBox getView() {
        return mainContainer;
    }

    public Button getTransactionsButton() {
        return transactionsButton;
    }

    public Button getBudgetsButton() {
        return budgetsButton;
    }

    public Button getGoalsButton() {
        return goalsButton;
    }

    public Button getAllDataButton() {
        return allDataButton;
    }
}
