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

        try {
            TextField targetField = (TextField) content.getChildren().get(3);
            DatePicker deadlinePicker = (DatePicker) content.getChildren().get(5);

            double targetAmount = Double.parseDouble(targetField.getText().trim());

            if (targetAmount <= 0) {
                showMessage("Error", "Target must be greater than 0");
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

        try {
            TextField targetField = (TextField) content.getChildren().get(3);
            DatePicker deadlinePicker = (DatePicker) content.getChildren().get(5);
            ComboBox<GoalStatus> statusBox = (ComboBox<GoalStatus>) content.getChildren().get(7);

            double targetAmount = Double.parseDouble(targetField.getText().trim());

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
