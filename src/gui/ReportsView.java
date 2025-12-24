package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import models.User;
import database.*;
import utils.FileManager;

public class ReportsView {
    private User u;
    private TransactionDAO txDao = new TransactionDAO();
    private BudgetDAO budDao = new BudgetDAO();
    private GoalDAO goalDao = new GoalDAO();
    private FileManager fm = new FileManager();
    private VBox view;

    public ReportsView(User u) {
        this.u = u;
        this.view = create();
    }

    private VBox create() {
        VBox v = new VBox(20);
        v.setPadding(new Insets(20));
        v.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Export Reports");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        VBox box = new VBox(15);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(30));
        box.setMaxWidth(600);
        box.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7; -fx-border-width: 2;");

        HBox r1 = new HBox(15);
        r1.setAlignment(Pos.CENTER_LEFT);
        r1.setPadding(new Insets(10));
        Label l1 = new Label("Export Transactions");
        l1.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        HBox.setHgrow(l1, Priority.ALWAYS);
        Button b1 = new Button("Export");
        b1.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-padding: 10 20;");
        b1.setOnAction(e -> exportTx());
        r1.getChildren().addAll(l1, b1);

        HBox r2 = new HBox(15);
        r2.setAlignment(Pos.CENTER_LEFT);
        r2.setPadding(new Insets(10));
        Label l2 = new Label("Export Budgets");
        l2.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        HBox.setHgrow(l2, Priority.ALWAYS);
        Button b2 = new Button("Export");
        b2.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-padding: 10 20;");
        b2.setOnAction(e -> exportBud());
        r2.getChildren().addAll(l2, b2);

        HBox r3 = new HBox(15);
        r3.setAlignment(Pos.CENTER_LEFT);
        r3.setPadding(new Insets(10));
        Label l3 = new Label("Export Goals");
        l3.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        HBox.setHgrow(l3, Priority.ALWAYS);
        Button b3 = new Button("Export");
        b3.setStyle("-fx-background-color: #9b59b6; -fx-text-fill: white; -fx-padding: 10 20;");
        b3.setOnAction(e -> exportGoal());
        r3.getChildren().addAll(l3, b3);

        HBox r4 = new HBox(15);
        r4.setAlignment(Pos.CENTER_LEFT);
        r4.setPadding(new Insets(10));
        Label l4 = new Label("Export All Data");
        l4.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        HBox.setHgrow(l4, Priority.ALWAYS);
        Button b4 = new Button("Export");
        b4.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-padding: 10 20;");
        b4.setOnAction(e -> exportAll());
        r4.getChildren().addAll(l4, b4);

        box.getChildren().addAll(r1, new Separator(), r2, new Separator(), r3, new Separator(), r4);
        v.getChildren().addAll(title, box);
        return v;
    }

    private void exportTx() {
        var txs = txDao.getUserTransactions(u.getId());
        if (txs.isEmpty()) {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("No Data");
            a.setHeaderText(null);
            a.setContentText("No transactions");
            a.showAndWait();
            return;
        }
        try {
            String path = fm.exportTransactions(txs);
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Success");
            a.setHeaderText(null);
            a.setContentText("Exported to:\n" + path);
            a.showAndWait();
        } catch (Exception ex) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.setHeaderText(null);
            a.setContentText(ex.getMessage());
            a.showAndWait();
        }
    }

    private void exportBud() {
        var buds = budDao.getUserBudgets(u.getId());
        if (buds.isEmpty()) {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("No Data");
            a.setHeaderText(null);
            a.setContentText("No budgets");
            a.showAndWait();
            return;
        }
        try {
            String path = fm.exportBudgets(buds);
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Success");
            a.setHeaderText(null);
            a.setContentText("Exported to:\n" + path);
            a.showAndWait();
        } catch (Exception ex) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.setHeaderText(null);
            a.setContentText(ex.getMessage());
            a.showAndWait();
        }
    }

    private void exportGoal() {
        var goals = goalDao.getUserGoals(u.getId());
        if (goals.isEmpty()) {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("No Data");
            a.setHeaderText(null);
            a.setContentText("No goals");
            a.showAndWait();
            return;
        }
        try {
            String path = fm.exportGoals(goals);
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Success");
            a.setHeaderText(null);
            a.setContentText("Exported to:\n" + path);
            a.showAndWait();
        } catch (Exception ex) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.setHeaderText(null);
            a.setContentText(ex.getMessage());
            a.showAndWait();
        }
    }

    private void exportAll() {
        var txs = txDao.getUserTransactions(u.getId());
        var buds = budDao.getUserBudgets(u.getId());
        var goals = goalDao.getUserGoals(u.getId());
        if (txs.isEmpty() && buds.isEmpty() && goals.isEmpty()) {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("No Data");
            a.setHeaderText(null);
            a.setContentText("No data to export");
            a.showAndWait();
            return;
        }
        try {
            String path = fm.exportAllData(txs, buds, goals);
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Success");
            a.setHeaderText(null);
            a.setContentText("Exported to:\n" + path);
            a.showAndWait();
        } catch (Exception ex) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.setHeaderText(null);
            a.setContentText(ex.getMessage());
            a.showAndWait();
        }
    }

    public VBox getView() {
        return view;
    }
}
