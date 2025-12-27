package gui.views;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import models.User;
import models.Budget;

public class BudgetsView {
    private User user;
    private TableView<Budget> budgetsTable;
    private VBox mainContainer;
    private Button addButton;
    private Button editButton;
    private Button deleteButton;
    private Button refreshButton;

    public BudgetsView(User user) {
        this.user = user;
        this.budgetsTable = new TableView<>();
        this.mainContainer = createView();
    }

    private VBox createView() {
        VBox container = new VBox(10);
        container.setPadding(new Insets(10));

        // Title
        Label titleLabel = new Label("Budgets");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Create buttons
        addButton = new Button("Add");
        addButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        editButton = new Button("Edit");
        editButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        deleteButton = new Button("Delete");
        deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        refreshButton = new Button("Refresh");
        refreshButton.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white;");

        HBox buttonBox = new HBox(5);
        buttonBox.getChildren().addAll(addButton, editButton, deleteButton, refreshButton);

        // Create table columns
        TableColumn<Budget, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Budget, Integer> monthColumn = new TableColumn<>("Month");
        monthColumn.setCellValueFactory(new PropertyValueFactory<>("month"));

        TableColumn<Budget, Integer> yearColumn = new TableColumn<>("Year");
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));

        TableColumn<Budget, Double> limitColumn = new TableColumn<>("Limit");
        limitColumn.setCellValueFactory(new PropertyValueFactory<>("monthlyLimit"));

        TableColumn<Budget, Double> spentColumn = new TableColumn<>("Spent");
        spentColumn.setCellValueFactory(new PropertyValueFactory<>("currentSpent"));

        budgetsTable.getColumns().addAll(idColumn, monthColumn, yearColumn, limitColumn, spentColumn);

        container.getChildren().addAll(titleLabel, buttonBox, budgetsTable);
        return container;
    }

    // Getters for controller
    public User getUser() {
        return user;
    }

    public VBox getView() {
        return mainContainer;
    }

    public TableView<Budget> getBudgetsTable() {
        return budgetsTable;
    }

    public Button getAddButton() {
        return addButton;
    }

    public Button getEditButton() {
        return editButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public Button getRefreshButton() {
        return refreshButton;
    }

    public void refreshView() {
        this.mainContainer = createView();
    }
}
