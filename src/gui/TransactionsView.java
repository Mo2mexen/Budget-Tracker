package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import models.*;
import models.Transaction.TransactionType;
import database.*;
import java.time.LocalDate;

public class TransactionsView {
    private User user;
    private TransactionDAO dao = new TransactionDAO();
    private AccountDAO accDao = new AccountDAO();
    private CategoryDAO catDao = new CategoryDAO();
    private TableView<Transaction> table = new TableView<>();
    private VBox view;

    public TransactionsView(User user) {
        this.user = user;
        this.view = create();
    }

    private VBox create() {
        VBox v = new VBox(15);
        v.setPadding(new Insets(20));

        Label title = new Label("Transactions");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        HBox btns = new HBox(10);
        btns.setAlignment(Pos.CENTER_LEFT);
        btns.getChildren().addAll(
            createBtn("Add", "#27ae60", e -> addDialog()),
            createBtn("Edit", "#3498db", e -> editDialog()),
            createBtn("Delete", "#e74c3c", e -> deleteDialog()),
            createBtn("Refresh", "#95a5a6", e -> refresh())
        );

        TableColumn<Transaction, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(50);

        TableColumn<Transaction, LocalDate> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
        dateCol.setPrefWidth(100);

        TableColumn<Transaction, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        descCol.setPrefWidth(200);

        TableColumn<Transaction, Double> amtCol = new TableColumn<>("Amount");
        amtCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        amtCol.setPrefWidth(100);

        TableColumn<Transaction, TransactionType> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeCol.setPrefWidth(100);

        table.getColumns().addAll(idCol, dateCol, descCol, amtCol, typeCol);
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
        table.getItems().addAll(dao.getUserTransactions(user.getId()));
    }

    private void addDialog() {
        var accounts = accDao.getUserAccounts(user.getId());
        var categories = catDao.getUserCategories(user.getId());

        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Add Transaction");
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        ComboBox<Account> accCombo = new ComboBox<>();
        accCombo.getItems().addAll(accounts);
        if (!accounts.isEmpty()) accCombo.setValue(accounts.get(0));

        ComboBox<Category> catCombo = new ComboBox<>();
        catCombo.getItems().addAll(categories);
        if (!categories.isEmpty()) catCombo.setValue(categories.get(0));

        TextField amtField = new TextField();
        TextField descField = new TextField();
        DatePicker datePicker = new DatePicker(LocalDate.now());
        ComboBox<TransactionType> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll(TransactionType.values());
        typeCombo.setValue(TransactionType.EXPENSE);
        TextArea notesArea = new TextArea();
        notesArea.setPrefRowCount(2);

        grid.add(new Label("Account:"), 0, 0);
        grid.add(accCombo, 1, 0);
        grid.add(new Label("Category:"), 0, 1);
        grid.add(catCombo, 1, 1);
        grid.add(new Label("Amount:"), 0, 2);
        grid.add(amtField, 1, 2);
        grid.add(new Label("Description:"), 0, 3);
        grid.add(descField, 1, 3);
        grid.add(new Label("Date:"), 0, 4);
        grid.add(datePicker, 1, 4);
        grid.add(new Label("Type:"), 0, 5);
        grid.add(typeCombo, 1, 5);
        grid.add(new Label("Notes:"), 0, 6);
        grid.add(notesArea, 1, 6);

        dlg.getDialogPane().setContent(grid);

        dlg.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                try {
                    if (accCombo.getValue() == null || catCombo.getValue() == null) {
                        alert(Alert.AlertType.ERROR, "Error", "Select account and category");
                        return;
                    }
                    String desc = descField.getText().trim();
                    if (desc.isEmpty()) throw new Exception("Description required");
                    double amt = Double.parseDouble(amtField.getText().trim());
                    if (dao.addTransaction(user.getId(), accCombo.getValue().getId(),
                        catCombo.getValue().getId(), amt, desc, datePicker.getValue(),
                        typeCombo.getValue().name(), notesArea.getText().trim())) {
                        alert(Alert.AlertType.INFORMATION, "Success", "Transaction added");
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
        Transaction tx = table.getSelectionModel().getSelectedItem();
        if (tx == null) {
            alert(Alert.AlertType.WARNING, "Warning", "Select transaction");
            return;
        }

        var accounts = accDao.getUserAccounts(user.getId());
        var categories = catDao.getUserCategories(user.getId());

        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Edit Transaction");
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        ComboBox<Account> accCombo = new ComboBox<>();
        accCombo.getItems().addAll(accounts);
        accCombo.setValue(accDao.getAccountById(tx.getAccountId()));

        ComboBox<Category> catCombo = new ComboBox<>();
        catCombo.getItems().addAll(categories);
        catCombo.setValue(catDao.getCategoryById(tx.getCategoryId()));

        TextField amtField = new TextField(String.valueOf(tx.getAmount()));
        TextField descField = new TextField(tx.getDescription());
        DatePicker datePicker = new DatePicker(tx.getTransactionDate());
        ComboBox<TransactionType> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll(TransactionType.values());
        typeCombo.setValue(tx.getType());
        TextArea notesArea = new TextArea(tx.getNotes());
        notesArea.setPrefRowCount(2);

        grid.add(new Label("Account:"), 0, 0);
        grid.add(accCombo, 1, 0);
        grid.add(new Label("Category:"), 0, 1);
        grid.add(catCombo, 1, 1);
        grid.add(new Label("Amount:"), 0, 2);
        grid.add(amtField, 1, 2);
        grid.add(new Label("Description:"), 0, 3);
        grid.add(descField, 1, 3);
        grid.add(new Label("Date:"), 0, 4);
        grid.add(datePicker, 1, 4);
        grid.add(new Label("Type:"), 0, 5);
        grid.add(typeCombo, 1, 5);
        grid.add(new Label("Notes:"), 0, 6);
        grid.add(notesArea, 1, 6);

        dlg.getDialogPane().setContent(grid);

        dlg.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                try {
                    String desc = descField.getText().trim();
                    if (desc.isEmpty()) throw new Exception("Description required");
                    double amt = Double.parseDouble(amtField.getText().trim());
                    if (dao.updateTransaction(tx.getId(), accCombo.getValue().getId(),
                        catCombo.getValue().getId(), amt, desc, datePicker.getValue(),
                        typeCombo.getValue().name(), notesArea.getText().trim())) {
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
        Transaction tx = table.getSelectionModel().getSelectedItem();
        if (tx == null) {
            alert(Alert.AlertType.WARNING, "Warning", "Select transaction");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + tx.getDescription() + "?",
            ButtonType.OK, ButtonType.CANCEL);
        if (confirm.showAndWait().get() == ButtonType.OK) {
            if (dao.deleteTransaction(tx.getId())) {
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
