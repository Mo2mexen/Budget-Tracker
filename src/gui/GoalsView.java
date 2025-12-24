package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import models.User;
import models.Goal;
import models.Goal.GoalStatus;
import database.GoalDAO;
import java.time.LocalDate;

public class GoalsView {
    private User user;
    private GoalDAO dao = new GoalDAO();
    private TableView<Goal> table = new TableView<>();
    private VBox view;

    public GoalsView(User user) {
        this.user = user;
        this.view = create();
    }

    private VBox create() {
        VBox v = new VBox(15);
        v.setPadding(new Insets(20));

        Label title = new Label("Goals");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        HBox btns = new HBox(10);
        btns.setAlignment(Pos.CENTER_LEFT);
        btns.getChildren().addAll(
            createBtn("Add", "#27ae60", e -> addDialog()),
            createBtn("Edit", "#3498db", e -> editDialog()),
            createBtn("Contribute", "#9b59b6", e -> contributeDialog()),
            createBtn("Delete", "#e74c3c", e -> deleteDialog()),
            createBtn("Refresh", "#95a5a6", e -> refresh())
        );

        TableColumn<Goal, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(50);

        TableColumn<Goal, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("goalName"));
        nameCol.setPrefWidth(150);

        TableColumn<Goal, Double> targetCol = new TableColumn<>("Target");
        targetCol.setCellValueFactory(new PropertyValueFactory<>("targetAmount"));
        targetCol.setPrefWidth(100);

        TableColumn<Goal, Double> currentCol = new TableColumn<>("Current");
        currentCol.setCellValueFactory(new PropertyValueFactory<>("currentAmount"));
        currentCol.setPrefWidth(100);

        TableColumn<Goal, LocalDate> deadlineCol = new TableColumn<>("Deadline");
        deadlineCol.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        deadlineCol.setPrefWidth(100);

        TableColumn<Goal, GoalStatus> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(100);

        table.getColumns().addAll(idCol, nameCol, targetCol, currentCol, deadlineCol, statusCol);
        refresh();

        v.getChildren().addAll(title, btns, table);
        return v;
    }

    private Button createBtn(String text, String color, javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white;");
        btn.setOnAction(action);
        return btn;
    }

    private void refresh() {
        table.getItems().clear();
        table.getItems().addAll(dao.getUserGoals(user.getId()));
    }

    private void addDialog() {
        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Add Goal");
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField nameField = new TextField();
        TextField targetField = new TextField();
        DatePicker deadlinePicker = new DatePicker(LocalDate.now().plusMonths(6));

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Target:"), 0, 1);
        grid.add(targetField, 1, 1);
        grid.add(new Label("Deadline:"), 0, 2);
        grid.add(deadlinePicker, 1, 2);

        dlg.getDialogPane().setContent(grid);

        dlg.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                try {
                    String name = nameField.getText().trim();
                    if (name.isEmpty()) throw new Exception("Name required");
                    double target = Double.parseDouble(targetField.getText().trim());
                    if (target <= 0) throw new Exception("Target must be > 0");
                    if (dao.createGoal(user.getId(), name, target, deadlinePicker.getValue())) {
                        alert(Alert.AlertType.INFORMATION, "Success", "Goal created");
                        refresh();
                    } else {
                        alert(Alert.AlertType.ERROR, "Error", "Failed");
                    }
                } catch (Exception ex) {
                    alert(Alert.AlertType.ERROR, "Error", ex.getMessage());
                }
            }
        });
    }

    private void editDialog() {
        Goal goal = table.getSelectionModel().getSelectedItem();
        if (goal == null) {
            alert(Alert.AlertType.WARNING, "Warning", "Select goal");
            return;
        }

        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Edit Goal");
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField nameField = new TextField(goal.getGoalName());
        TextField targetField = new TextField(String.valueOf(goal.getTargetAmount()));
        DatePicker deadlinePicker = new DatePicker(goal.getDeadline());
        ComboBox<GoalStatus> statusCombo = new ComboBox<>();
        statusCombo.getItems().addAll(GoalStatus.values());
        statusCombo.setValue(goal.getStatus());

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Target:"), 0, 1);
        grid.add(targetField, 1, 1);
        grid.add(new Label("Deadline:"), 0, 2);
        grid.add(deadlinePicker, 1, 2);
        grid.add(new Label("Status:"), 0, 3);
        grid.add(statusCombo, 1, 3);

        dlg.getDialogPane().setContent(grid);

        dlg.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                try {
                    String name = nameField.getText().trim();
                    if (name.isEmpty()) throw new Exception("Name required");
                    double target = Double.parseDouble(targetField.getText().trim());
                    if (dao.updateGoal(goal.getId(), name, target, deadlinePicker.getValue())) {
                        dao.updateStatus(goal.getId(), statusCombo.getValue().name());
                        alert(Alert.AlertType.INFORMATION, "Success", "Updated");
                        refresh();
                    } else {
                        alert(Alert.AlertType.ERROR, "Error", "Failed");
                    }
                } catch (Exception ex) {
                    alert(Alert.AlertType.ERROR, "Error", ex.getMessage());
                }
            }
        });
    }

    private void contributeDialog() {
        Goal goal = table.getSelectionModel().getSelectedItem();
        if (goal == null) {
            alert(Alert.AlertType.WARNING, "Warning", "Select goal");
            return;
        }
        if (goal.isCompleted()) {
            alert(Alert.AlertType.INFORMATION, "Info", "Goal already completed");
            return;
        }

        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Add Contribution");
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        Label info = new Label("Current: " + goal.getCurrentAmount() + " / Target: " + goal.getTargetAmount());
        TextField amtField = new TextField();

        grid.add(info, 0, 0, 2, 1);
        grid.add(new Label("Amount:"), 0, 1);
        grid.add(amtField, 1, 1);

        dlg.getDialogPane().setContent(grid);

        dlg.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                try {
                    double amt = Double.parseDouble(amtField.getText().trim());
                    if (amt <= 0) throw new Exception("Amount must be > 0");
                    double newAmt = goal.getCurrentAmount() + amt;
                    if (dao.updateProgress(goal.getId(), newAmt)) {
                        if (newAmt >= goal.getTargetAmount()) {
                            dao.updateStatus(goal.getId(), GoalStatus.COMPLETED.name());
                            alert(Alert.AlertType.INFORMATION, "Success", "Goal completed!");
                        } else {
                            alert(Alert.AlertType.INFORMATION, "Success", "Contribution added");
                        }
                        refresh();
                    } else {
                        alert(Alert.AlertType.ERROR, "Error", "Failed");
                    }
                } catch (Exception ex) {
                    alert(Alert.AlertType.ERROR, "Error", ex.getMessage());
                }
            }
        });
    }

    private void deleteDialog() {
        Goal goal = table.getSelectionModel().getSelectedItem();
        if (goal == null) {
            alert(Alert.AlertType.WARNING, "Warning", "Select goal");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + goal.getGoalName() + "?",
            ButtonType.OK, ButtonType.CANCEL);
        if (confirm.showAndWait().get() == ButtonType.OK) {
            if (dao.deleteGoal(goal.getId())) {
                alert(Alert.AlertType.INFORMATION, "Success", "Deleted");
                refresh();
            } else {
                alert(Alert.AlertType.ERROR, "Error", "Failed");
            }
        }
    }

    private void alert(Alert.AlertType type, String title, String msg) {
        Alert a = new Alert(type);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    public VBox getView() {
        return view;
    }
}
