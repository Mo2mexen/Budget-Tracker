package gui.controllers;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import models.Goal;
import models.Goal.GoalStatus;
import database.GoalDAO;
import java.time.LocalDate;
import java.util.Optional;
import gui.views.GoalsView;
import utils.ValidationHelper;

public class GoalsController {
    private GoalsView view;
    private GoalDAO goalDAO;

    public GoalsController(GoalsView view) {
        this.view = view;
        this.goalDAO = new GoalDAO();
        attachEventHandlers();
        loadGoals();
    }

    private void attachEventHandlers() {
        view.getAddButton().setOnAction(event -> addGoal());
        view.getEditButton().setOnAction(event -> editGoal());
        view.getDeleteButton().setOnAction(event -> deleteGoal());
        view.getRefreshButton().setOnAction(event -> loadGoals());
        view.getGoalsTable().setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                addContribution();
            }
        });
    }

    private void loadGoals() {
        view.getGoalsTable().getItems().clear();
        view.getGoalsTable().getItems().addAll(goalDAO.getUserGoals(view.getUser().getId()));
    }

    private void addGoal() {
        Dialog<ButtonType> dialog = createGoalDialog("Add Goal", null);

        Optional<ButtonType> result = dialog.showAndWait();
        if (!result.isPresent() || result.get() != ButtonType.OK) {
            return;
        }

        VBox content = (VBox) dialog.getDialogPane().getContent();
        TextField nameField = (TextField) content.getChildren().get(1);
        String goalName = nameField.getText().trim();

        if (goalName.isEmpty()) {
            showMessage("Error", "Name is required");
            return;
        }

        // Validate goal name length
        if (!ValidationHelper.isValidString(goalName, ValidationHelper.MIN_NAME_LENGTH, ValidationHelper.MAX_NAME_LENGTH)) {
            showMessage("Error", ValidationHelper.getStringLengthErrorMessage("Goal name", ValidationHelper.MIN_NAME_LENGTH, ValidationHelper.MAX_NAME_LENGTH));
            return;
        }

        try {
            TextField targetField = (TextField) content.getChildren().get(3);
            DatePicker deadlinePicker = (DatePicker) content.getChildren().get(5);

            double targetAmount = Double.parseDouble(targetField.getText().trim());

            // Validate target amount
            if (!ValidationHelper.isValidAmount(targetAmount)) {
                showMessage("Error", ValidationHelper.getAmountErrorMessage());
                return;
            }

            // Validate deadline is not null
            if (!ValidationHelper.isValidDate(deadlinePicker.getValue())) {
                showMessage("Error", "Please select a deadline date");
                return;
            }

            // Validate deadline is not in the past
            if (!ValidationHelper.isNotInPast(deadlinePicker.getValue())) {
                showMessage("Error", ValidationHelper.getPastDateErrorMessage());
                return;
            }

            boolean success = goalDAO.createGoal(view.getUser().getId(), goalName, targetAmount, deadlinePicker.getValue());

            if (success) {
                showMessage("Success", "Goal created");
                loadGoals();
            } else {
                showMessage("Error", "Failed to create goal");
            }
        } catch (Exception exception) {
            showMessage("Error", "Invalid input");
        }
    }

    private void editGoal() {
        Goal selectedGoal = view.getGoalsTable().getSelectionModel().getSelectedItem();
        if (selectedGoal == null) {
            showMessage("Warning", "Please select a goal");
            return;
        }

        Dialog<ButtonType> dialog = createGoalDialog("Edit Goal", selectedGoal);

        Optional<ButtonType> result = dialog.showAndWait();
        if (!result.isPresent() || result.get() != ButtonType.OK) {
            return;
        }

        VBox content = (VBox) dialog.getDialogPane().getContent();
        TextField nameField = (TextField) content.getChildren().get(1);
        String goalName = nameField.getText().trim();

        if (goalName.isEmpty()) {
            showMessage("Error", "Name is required");
            return;
        }

        // Validate goal name length
        if (!ValidationHelper.isValidString(goalName, ValidationHelper.MIN_NAME_LENGTH, ValidationHelper.MAX_NAME_LENGTH)) {
            showMessage("Error", ValidationHelper.getStringLengthErrorMessage("Goal name", ValidationHelper.MIN_NAME_LENGTH, ValidationHelper.MAX_NAME_LENGTH));
            return;
        }

        try {
            TextField targetField = (TextField) content.getChildren().get(3);
            DatePicker deadlinePicker = (DatePicker) content.getChildren().get(5);
            ComboBox<GoalStatus> statusBox = (ComboBox<GoalStatus>) content.getChildren().get(7);

            double targetAmount = Double.parseDouble(targetField.getText().trim());

            // Validate target amount
            if (!ValidationHelper.isValidAmount(targetAmount)) {
                showMessage("Error", ValidationHelper.getAmountErrorMessage());
                return;
            }

            // Validate deadline is not null
            if (!ValidationHelper.isValidDate(deadlinePicker.getValue())) {
                showMessage("Error", "Please select a deadline date");
                return;
            }

            boolean success = goalDAO.updateGoal(selectedGoal.getId(), goalName, targetAmount, deadlinePicker.getValue());

            if (success) {
                goalDAO.updateStatus(selectedGoal.getId(), statusBox.getValue().name());
                showMessage("Success", "Goal updated");
                loadGoals();
            } else {
                showMessage("Error", "Failed to update goal");
            }
        } catch (Exception exception) {
            showMessage("Error", "Invalid input");
        }
    }

    private void addContribution() {
        Goal selectedGoal = view.getGoalsTable().getSelectionModel().getSelectedItem();
        if (selectedGoal == null) {
            showMessage("Warning", "Please select a goal");
            return;
        }

        if (selectedGoal.getStatus() != GoalStatus.ACTIVE) {
            showMessage("Warning", "Cannot contribute to inactive goal");
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Contribution to: " + selectedGoal.getGoalName());
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        VBox dialogBox = new VBox(10);
        dialogBox.setPadding(new Insets(10));

        Label progressLabel = new Label(String.format("Current: $%.2f / $%.2f (%.1f%%)",
            selectedGoal.getCurrentAmount(),
            selectedGoal.getTargetAmount(),
            selectedGoal.calculatePercentage()));

        TextField amountField = new TextField();
        amountField.setPromptText("Enter contribution amount");

        dialogBox.getChildren().addAll(
            progressLabel,
            new Label("Contribution Amount:"),
            amountField
        );

        dialog.getDialogPane().setContent(dialogBox);

        Optional<ButtonType> result = dialog.showAndWait();
        if (!result.isPresent() || result.get() != ButtonType.OK) {
            return;
        }

        try {
            double contribution = Double.parseDouble(amountField.getText().trim());

            if (contribution <= 0) {
                showMessage("Error", "Amount must be greater than 0");
                return;
            }

            // Validate contribution amount
            if (!ValidationHelper.isValidAmount(contribution)) {
                showMessage("Error", ValidationHelper.getAmountErrorMessage());
                return;
            }

            double newAmount = selectedGoal.getCurrentAmount() + contribution;

            boolean success = goalDAO.updateProgress(selectedGoal.getId(), newAmount);

            if (success) {
                if (newAmount >= selectedGoal.getTargetAmount()) {
                    goalDAO.updateStatus(selectedGoal.getId(), "COMPLETED");
                    showMessage("Success", String.format("Goal completed! Added $%.2f", contribution));
                } else {
                    showMessage("Success", String.format("Added $%.2f. New total: $%.2f",
                        contribution, newAmount));
                }
                loadGoals();
            } else {
                showMessage("Error", "Failed to add contribution");
            }
        } catch (NumberFormatException e) {
            showMessage("Error", "Invalid amount");
        }
    }

    private Dialog<ButtonType> createGoalDialog(String title, Goal goal) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        VBox dialogBox = new VBox(5);
        dialogBox.setPadding(new Insets(10));

        TextField nameField = new TextField(goal != null ? goal.getGoalName() : "");
        TextField targetField = new TextField(goal != null ? String.valueOf(goal.getTargetAmount()) : "");
        DatePicker deadlinePicker = new DatePicker(goal != null ? goal.getDeadline() : LocalDate.now().plusMonths(6));

        dialogBox.getChildren().addAll(
            new Label("Name:"), nameField,
            new Label("Target:"), targetField,
            new Label("Deadline:"), deadlinePicker
        );

        if (goal != null) {
            ComboBox<GoalStatus> statusBox = new ComboBox<>();
            statusBox.getItems().addAll(GoalStatus.values());
            statusBox.setValue(goal.getStatus());
            dialogBox.getChildren().addAll(new Label("Status:"), statusBox);
        }

        dialog.getDialogPane().setContent(dialogBox);
        return dialog;
    }

    private void deleteGoal() {
        Goal selectedGoal = view.getGoalsTable().getSelectionModel().getSelectedItem();
        if (selectedGoal == null) {
            showMessage("Warning", "Please select a goal");
            return;
        }

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm");
        confirmDialog.setHeaderText(null);
        confirmDialog.setContentText("Delete goal: " + selectedGoal.getGoalName() + "?");

        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (!result.isPresent() || result.get() != ButtonType.OK) {
            return;
        }

        boolean success = goalDAO.deleteGoal(selectedGoal.getId());
        if (success) {
            showMessage("Success", "Goal deleted");
            loadGoals();
        } else {
            showMessage("Error", "Failed to delete goal");
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
