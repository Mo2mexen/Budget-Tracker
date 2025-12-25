package gui;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import models.User;
import models.Goal;
import models.Goal.GoalStatus;
import database.GoalDAO;
import java.time.LocalDate;

public class GoalsView {
    private User user;
    private GoalDAO goalDAO;
    private TableView<Goal> goalsTable;
    private VBox mainContainer;

    public GoalsView(User user) {
        this.user = user;
        this.goalDAO = new GoalDAO();
        this.goalsTable = new TableView<>();
        this.mainContainer = createView();
    }

    private VBox createView() {
        VBox container = new VBox(10);
        container.setPadding(new Insets(10));

        // Title
        Label titleLabel = new Label("Goals");
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

        addButton.setOnAction(event -> addGoal());
        editButton.setOnAction(event -> editGoal());
        deleteButton.setOnAction(event -> deleteGoal());
        refreshButton.setOnAction(event -> loadGoals());

        HBox buttonBox = new HBox(5);
        buttonBox.getChildren().addAll(addButton, editButton, deleteButton, refreshButton);

        // Create table columns
        TableColumn<Goal, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Goal, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("goalName"));

        TableColumn<Goal, Double> targetColumn = new TableColumn<>("Target");
        targetColumn.setCellValueFactory(new PropertyValueFactory<>("targetAmount"));

        TableColumn<Goal, Double> currentColumn = new TableColumn<>("Current");
        currentColumn.setCellValueFactory(new PropertyValueFactory<>("currentAmount"));

        TableColumn<Goal, LocalDate> deadlineColumn = new TableColumn<>("Deadline");
        deadlineColumn.setCellValueFactory(new PropertyValueFactory<>("deadline"));

        TableColumn<Goal, GoalStatus> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        goalsTable.getColumns().addAll(idColumn, nameColumn, targetColumn, currentColumn, deadlineColumn, statusColumn);
        loadGoals();

        container.getChildren().addAll(titleLabel, buttonBox, goalsTable);
        return container;
    }

    private void loadGoals() {
        goalsTable.getItems().clear();
        goalsTable.getItems().addAll(goalDAO.getUserGoals(user.getId()));
    }

    private void addGoal() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Goal");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        VBox dialogBox = new VBox(5);
        dialogBox.setPadding(new Insets(10));

        TextField nameField = new TextField();
        TextField targetField = new TextField();
        DatePicker deadlinePicker = new DatePicker(LocalDate.now().plusMonths(6));

        dialogBox.getChildren().addAll(
            new Label("Name:"), nameField,
            new Label("Target:"), targetField,
            new Label("Deadline:"), deadlinePicker
        );

        dialog.getDialogPane().setContent(dialogBox);

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String goalName = nameField.getText().trim();

                if (goalName.isEmpty()) {
                    showMessage("Error", "Name is required");
                    return;
                }

                try {
                    double targetAmount = Double.parseDouble(targetField.getText().trim());

                    if (targetAmount <= 0) {
                        showMessage("Error", "Target must be greater than 0");
                        return;
                    }

                    boolean success = goalDAO.createGoal(user.getId(), goalName, targetAmount, deadlinePicker.getValue());

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
        });
    }

    private void editGoal() {
        Goal selectedGoal = goalsTable.getSelectionModel().getSelectedItem();

        if (selectedGoal == null) {
            showMessage("Warning", "Please select a goal");
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Goal");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        VBox dialogBox = new VBox(5);
        dialogBox.setPadding(new Insets(10));

        TextField nameField = new TextField(selectedGoal.getGoalName());
        TextField targetField = new TextField(String.valueOf(selectedGoal.getTargetAmount()));
        DatePicker deadlinePicker = new DatePicker(selectedGoal.getDeadline());
        ComboBox<GoalStatus> statusBox = new ComboBox<>();
        statusBox.getItems().addAll(GoalStatus.values());
        statusBox.setValue(selectedGoal.getStatus());

        dialogBox.getChildren().addAll(
            new Label("Name:"), nameField,
            new Label("Target:"), targetField,
            new Label("Deadline:"), deadlinePicker,
            new Label("Status:"), statusBox
        );

        dialog.getDialogPane().setContent(dialogBox);

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String goalName = nameField.getText().trim();

                if (goalName.isEmpty()) {
                    showMessage("Error", "Name is required");
                    return;
                }

                try {
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
        });
    }

    private void deleteGoal() {
        Goal selectedGoal = goalsTable.getSelectionModel().getSelectedItem();

        if (selectedGoal == null) {
            showMessage("Warning", "Please select a goal");
            return;
        }

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm");
        confirmDialog.setHeaderText(null);
        confirmDialog.setContentText("Delete goal: " + selectedGoal.getGoalName() + "?");

        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean success = goalDAO.deleteGoal(selectedGoal.getId());

                if (success) {
                    showMessage("Success", "Goal deleted");
                    loadGoals();
                } else {
                    showMessage("Error", "Failed to delete goal");
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
