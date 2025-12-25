package gui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import models.User;
import models.Budget;
import models.Category;
import database.BudgetDAO;
import database.CategoryDAO;
import java.time.LocalDate;

public class BudgetsView {
    private User user;
    private BudgetDAO budgetDAO;
    private CategoryDAO categoryDAO;
    private TableView<Budget> budgetsTable;
    private VBox mainContainer;

    public BudgetsView(User user) {
        this.user = user;
        this.budgetDAO = new BudgetDAO();
        this.categoryDAO = new CategoryDAO();
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
        Button addButton = new Button("Add");
        addButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        Button editButton = new Button("Edit");
        editButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        Button deleteButton = new Button("Delete");
        deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        Button refreshButton = new Button("Refresh");
        refreshButton.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white;");

        addButton.setOnAction(event -> addBudget());
        editButton.setOnAction(event -> editBudget());
        deleteButton.setOnAction(event -> deleteBudget());
        refreshButton.setOnAction(event -> loadBudgets());

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
        loadBudgets();

        container.getChildren().addAll(titleLabel, buttonBox, budgetsTable);
        return container;
    }

    private void loadBudgets() {
        budgetsTable.getItems().clear();
        budgetsTable.getItems().addAll(budgetDAO.getUserBudgets(user.getId()));
    }

    private void addBudget() {
        java.util.List<Category> categoryList = categoryDAO.getUserCategories(user.getId());

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Budget");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        VBox dialogBox = new VBox(5);
        dialogBox.setPadding(new Insets(10));

        ComboBox<Category> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll(categoryList);
        if (!categoryList.isEmpty()) {
            categoryBox.setValue(categoryList.get(0));
        }

        TextField limitField = new TextField();

        TextField monthField = new TextField(String.valueOf(LocalDate.now().getMonthValue()));
        TextField yearField = new TextField(String.valueOf(LocalDate.now().getYear()));

        dialogBox.getChildren().addAll(
            new Label("Category:"), categoryBox,
            new Label("Limit:"), limitField,
            new Label("Month:"), monthField,
            new Label("Year:"), yearField
        );

        dialog.getDialogPane().setContent(dialogBox);

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (categoryBox.getValue() == null) {
                    showMessage("Error", "Please select a category");
                    return;
                }

                try {
                    double limit = Double.parseDouble(limitField.getText().trim());
                    int month = Integer.parseInt(monthField.getText().trim());
                    int year = Integer.parseInt(yearField.getText().trim());

                    boolean success = budgetDAO.createBudget(user.getId(), categoryBox.getValue().getId(), limit, month, year);

                    if (success) {
                        showMessage("Success", "Budget created");
                        loadBudgets();
                    } else {
                        showMessage("Error", "Failed to create budget");
                    }
                } catch (Exception exception) {
                    showMessage("Error", "Invalid input");
                }
            }
        });
    }

    private void editBudget() {
        Budget selectedBudget = budgetsTable.getSelectionModel().getSelectedItem();

        if (selectedBudget == null) {
            showMessage("Warning", "Please select a budget");
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Budget");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        VBox dialogBox = new VBox(5);
        dialogBox.setPadding(new Insets(10));

        TextField limitField = new TextField(String.valueOf(selectedBudget.getMonthlyLimit()));

        dialogBox.getChildren().addAll(
            new Label("Limit:"), limitField
        );

        dialog.getDialogPane().setContent(dialogBox);

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    double limit = Double.parseDouble(limitField.getText().trim());

                    boolean success = budgetDAO.updateBudget(selectedBudget.getId(), limit);

                    if (success) {
                        showMessage("Success", "Budget updated");
                        loadBudgets();
                    } else {
                        showMessage("Error", "Failed to update budget");
                    }
                } catch (Exception exception) {
                    showMessage("Error", "Invalid input");
                }
            }
        });
    }

    private void deleteBudget() {
        Budget selectedBudget = budgetsTable.getSelectionModel().getSelectedItem();

        if (selectedBudget == null) {
            showMessage("Warning", "Please select a budget");
            return;
        }

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm");
        confirmDialog.setHeaderText(null);
        confirmDialog.setContentText("Delete this budget?");

        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean success = budgetDAO.deleteBudget(selectedBudget.getId());

                if (success) {
                    showMessage("Success", "Budget deleted");
                    loadBudgets();
                } else {
                    showMessage("Error", "Failed to delete budget");
                }
            }
        });
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
