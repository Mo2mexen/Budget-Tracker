package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import models.User;
import models.Budget;
import models.Category;
import database.BudgetDAO;
import database.CategoryDAO;

public class BudgetsView {
    private User user;
    private BudgetDAO dao = new BudgetDAO();
    private CategoryDAO catDao = new CategoryDAO();
    private TableView<Budget> table = new TableView<>();
    private VBox view;

    public BudgetsView(User user) {
        this.user = user;
        this.view = create();
    }

    private VBox create() {
        VBox v = new VBox(15);
        v.setPadding(new Insets(20));

        Label title = new Label("Budgets");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        HBox btns = new HBox(10);
        btns.setAlignment(Pos.CENTER_LEFT);
        btns.getChildren().addAll(
            createBtn("Add", "#27ae60", e -> addDialog()),
            createBtn("Edit", "#3498db", e -> editDialog()),
            createBtn("Delete", "#e74c3c", e -> deleteDialog()),
            createBtn("Refresh", "#95a5a6", e -> refresh())
        );

        TableColumn<Budget, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(50);

        TableColumn<Budget, Integer> monthCol = new TableColumn<>("Month");
        monthCol.setCellValueFactory(new PropertyValueFactory<>("month"));
        monthCol.setPrefWidth(80);

        TableColumn<Budget, Integer> yearCol = new TableColumn<>("Year");
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
        yearCol.setPrefWidth(80);

        TableColumn<Budget, Double> limitCol = new TableColumn<>("Limit");
        limitCol.setCellValueFactory(new PropertyValueFactory<>("monthlyLimit"));
        limitCol.setPrefWidth(120);

        TableColumn<Budget, Double> spentCol = new TableColumn<>("Spent");
        spentCol.setCellValueFactory(new PropertyValueFactory<>("currentSpent"));
        spentCol.setPrefWidth(120);

        TableColumn<Budget, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(100);

        table.getColumns().addAll(idCol, monthCol, yearCol, limitCol, spentCol, statusCol);
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
        table.getItems().addAll(dao.getUserBudgets(user.getId()));
    }

    private void addDialog() {
        var categories = catDao.getUserCategories(user.getId());

        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Add Budget");
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        ComboBox<Category> catCombo = new ComboBox<>();
        catCombo.getItems().addAll(categories);
        if (!categories.isEmpty()) catCombo.setValue(categories.get(0));

        TextField limitField = new TextField();
        Spinner<Integer> monthSpinner = new Spinner<>(1, 12, java.time.LocalDate.now().getMonthValue());
        Spinner<Integer> yearSpinner = new Spinner<>(2020, 2100, java.time.LocalDate.now().getYear());

        grid.add(new Label("Category:"), 0, 0);
        grid.add(catCombo, 1, 0);
        grid.add(new Label("Limit:"), 0, 1);
        grid.add(limitField, 1, 1);
        grid.add(new Label("Month:"), 0, 2);
        grid.add(monthSpinner, 1, 2);
        grid.add(new Label("Year:"), 0, 3);
        grid.add(yearSpinner, 1, 3);

        dlg.getDialogPane().setContent(grid);

        dlg.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                try {
                    if (catCombo.getValue() == null) {
                        alert(Alert.AlertType.ERROR, "Error", "Select category");
                        return;
                    }
                    double limit = Double.parseDouble(limitField.getText().trim());
                    if (limit <= 0) throw new Exception("Limit must be > 0");
                    if (dao.createBudget(user.getId(), catCombo.getValue().getId(), limit,
                        monthSpinner.getValue(), yearSpinner.getValue())) {
                        alert(Alert.AlertType.INFORMATION, "Success", "Budget created");
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
        Budget bud = table.getSelectionModel().getSelectedItem();
        if (bud == null) {
            alert(Alert.AlertType.WARNING, "Warning", "Select budget");
            return;
        }

        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Edit Budget");
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField limitField = new TextField(String.valueOf(bud.getMonthlyLimit()));

        grid.add(new Label("Limit:"), 0, 0);
        grid.add(limitField, 1, 0);

        dlg.getDialogPane().setContent(grid);

        dlg.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                try {
                    double limit = Double.parseDouble(limitField.getText().trim());
                    if (limit <= 0) throw new Exception("Limit must be > 0");
                    if (dao.updateBudget(bud.getId(), limit)) {
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

    private void deleteDialog() {
        Budget bud = table.getSelectionModel().getSelectedItem();
        if (bud == null) {
            alert(Alert.AlertType.WARNING, "Warning", "Select budget");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Delete budget?",
            ButtonType.OK, ButtonType.CANCEL);
        if (confirm.showAndWait().get() == ButtonType.OK) {
            if (dao.deleteBudget(bud.getId())) {
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
