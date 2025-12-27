package gui.controllers;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import models.Budget;
import models.Category;
import database.BudgetDAO;
import database.CategoryDAO;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import gui.views.BudgetsView;

public class BudgetsController {
    private BudgetsView view;
    private BudgetDAO budgetDAO;
    private CategoryDAO categoryDAO;

    public BudgetsController(BudgetsView view) {
        this.view = view;
        this.budgetDAO = new BudgetDAO();
        this.categoryDAO = new CategoryDAO();
        attachEventHandlers();
        loadBudgets();
    }

    private void attachEventHandlers() {
        view.getAddButton().setOnAction(event -> addBudget());
        view.getEditButton().setOnAction(event -> editBudget());
        view.getDeleteButton().setOnAction(event -> deleteBudget());
        view.getRefreshButton().setOnAction(event -> loadBudgets());
    }

    private void loadBudgets() {
        view.getBudgetsTable().getItems().clear();
        view.getBudgetsTable().getItems().addAll(budgetDAO.getUserBudgets(view.getUser().getId()));
    }

    private void addBudget() {
        List<Category> categoryList = categoryDAO.getUserCategories(view.getUser().getId());

        Dialog<ButtonType> dialog = createAddBudgetDialog(categoryList);

        Optional<ButtonType> result = dialog.showAndWait();
        if (!result.isPresent() || result.get() != ButtonType.OK) {
            return;
        }

        VBox content = (VBox) dialog.getDialogPane().getContent();
        ComboBox<Category> categoryBox = (ComboBox<Category>) content.getChildren().get(1);

        if (categoryBox.getValue() == null) {
            showMessage("Error", "Please select a category");
            return;
        }

        try {
            TextField limitField = (TextField) content.getChildren().get(3);
            TextField monthField = (TextField) content.getChildren().get(5);
            TextField yearField = (TextField) content.getChildren().get(7);

            double limit = Double.parseDouble(limitField.getText().trim());
            int month = Integer.parseInt(monthField.getText().trim());
            int year = Integer.parseInt(yearField.getText().trim());

            boolean success = budgetDAO.createBudget(view.getUser().getId(), categoryBox.getValue().getId(), limit, month, year);

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

    private void editBudget() {
        Budget selectedBudget = view.getBudgetsTable().getSelectionModel().getSelectedItem();
        if (selectedBudget == null) {
            showMessage("Warning", "Please select a budget");
            return;
        }

        Dialog<ButtonType> dialog = createEditBudgetDialog(selectedBudget);

        Optional<ButtonType> result = dialog.showAndWait();
        if (!result.isPresent() || result.get() != ButtonType.OK) {
            return;
        }

        try {
            VBox content = (VBox) dialog.getDialogPane().getContent();
            TextField limitField = (TextField) content.getChildren().get(1);
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

    private Dialog<ButtonType> createAddBudgetDialog(List<Category> categoryList) {
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
        return dialog;
    }

    private Dialog<ButtonType> createEditBudgetDialog(Budget budget) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Budget");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        VBox dialogBox = new VBox(5);
        dialogBox.setPadding(new Insets(10));

        TextField limitField = new TextField(String.valueOf(budget.getMonthlyLimit()));

        dialogBox.getChildren().addAll(
            new Label("Limit:"), limitField
        );

        dialog.getDialogPane().setContent(dialogBox);
        return dialog;
    }

    private void deleteBudget() {
        Budget selectedBudget = view.getBudgetsTable().getSelectionModel().getSelectedItem();
        if (selectedBudget == null) {
            showMessage("Warning", "Please select a budget");
            return;
        }

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm");
        confirmDialog.setHeaderText(null);
        confirmDialog.setContentText("Delete this budget?");

        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (!result.isPresent() || result.get() != ButtonType.OK) {
            return;
        }

        boolean success = budgetDAO.deleteBudget(selectedBudget.getId());
        if (success) {
            showMessage("Success", "Budget deleted");
            loadBudgets();
        } else {
            showMessage("Error", "Failed to delete budget");
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
