package gui.views;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import models.User;
import models.Goal;
import models.Goal.GoalStatus;
import java.time.LocalDate;

public class GoalsView {
    private User user;
    private TableView<Goal> goalsTable;
    private VBox mainContainer;
    private Button addButton;
    private Button editButton;
    private Button deleteButton;
    private Button refreshButton;

    public GoalsView(User user) {
        this.user = user;
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

        container.getChildren().addAll(titleLabel, buttonBox, goalsTable);
        return container;
    }

    // Getters for controller
    public User getUser() {
        return user;
    }

    public VBox getView() {
        return mainContainer;
    }

    public TableView<Goal> getGoalsTable() {
        return goalsTable;
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
