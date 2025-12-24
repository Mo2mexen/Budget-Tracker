package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import models.User;
import models.Account;
import models.Account.AccountType;
import database.AccountDAO;

public class AccountsView {
    private User user;
    private AccountDAO dao = new AccountDAO();
    private TableView<Account> table = new TableView<>();
    private VBox view;

    public AccountsView(User user) {
        this.user = user;
        this.view = create();
    }

    private VBox create() {
        VBox v = new VBox(15);
        v.setPadding(new Insets(20));

        Label title = new Label("Accounts");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        HBox btns = new HBox(10);
        btns.setAlignment(Pos.CENTER_LEFT);
        btns.getChildren().addAll(
            createBtn("Add", "#27ae60", e -> addDialog()),
            createBtn("Edit", "#3498db", e -> editDialog()),
            createBtn("Delete", "#e74c3c", e -> deleteDialog()),
            createBtn("Refresh", "#95a5a6", e -> refresh())
        );

        TableColumn<Account, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(50);

        TableColumn<Account, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("accountName"));
        nameCol.setPrefWidth(200);

        TableColumn<Account, AccountType> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("accountType"));
        typeCol.setPrefWidth(150);

        TableColumn<Account, Double> balCol = new TableColumn<>("Balance");
        balCol.setCellValueFactory(new PropertyValueFactory<>("balance"));
        balCol.setPrefWidth(150);

        TableColumn<Account, String> curCol = new TableColumn<>("Currency");
        curCol.setCellValueFactory(new PropertyValueFactory<>("currency"));
        curCol.setPrefWidth(100);

        table.getColumns().addAll(idCol, nameCol, typeCol, balCol, curCol);
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
        table.getItems().addAll(dao.getUserAccounts(user.getId()));
    }

    private void addDialog() {
        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Add Account");
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField nameField = new TextField();
        ComboBox<AccountType> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll(AccountType.values());
        typeCombo.setValue(AccountType.CHECKING);
        TextField balField = new TextField("0.0");
        ComboBox<String> curCombo = new ComboBox<>();
        curCombo.getItems().addAll("USD", "EUR", "GBP", "JPY", "CAD", "AUD", "EGP", "SAR", "AED");
        curCombo.setValue(user.getCurrency());

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Type:"), 0, 1);
        grid.add(typeCombo, 1, 1);
        grid.add(new Label("Balance:"), 0, 2);
        grid.add(balField, 1, 2);
        grid.add(new Label("Currency:"), 0, 3);
        grid.add(curCombo, 1, 3);

        dlg.getDialogPane().setContent(grid);

        dlg.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                try {
                    String name = nameField.getText().trim();
                    if (name.isEmpty()) throw new Exception("Name required");
                    double bal = Double.parseDouble(balField.getText().trim());
                    if (dao.createAccount(user.getId(), name, typeCombo.getValue().name(), bal, curCombo.getValue())) {
                        alert(Alert.AlertType.INFORMATION, "Success", "Account created");
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
        Account acc = table.getSelectionModel().getSelectedItem();
        if (acc == null) {
            alert(Alert.AlertType.WARNING, "Warning", "Select account");
            return;
        }

        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Edit Account");
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField nameField = new TextField(acc.getAccountName());
        ComboBox<AccountType> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll(AccountType.values());
        typeCombo.setValue(acc.getAccountType());
        TextField balField = new TextField(String.valueOf(acc.getBalance()));
        ComboBox<String> curCombo = new ComboBox<>();
        curCombo.getItems().addAll("USD", "EUR", "GBP", "JPY", "CAD", "AUD", "EGP", "SAR", "AED");
        curCombo.setValue(acc.getCurrency());

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Type:"), 0, 1);
        grid.add(typeCombo, 1, 1);
        grid.add(new Label("Balance:"), 0, 2);
        grid.add(balField, 1, 2);
        grid.add(new Label("Currency:"), 0, 3);
        grid.add(curCombo, 1, 3);

        dlg.getDialogPane().setContent(grid);

        dlg.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                try {
                    String name = nameField.getText().trim();
                    if (name.isEmpty()) throw new Exception("Name required");
                    double bal = Double.parseDouble(balField.getText().trim());
                    if (dao.updateAccount(acc.getId(), name, typeCombo.getValue().name(), curCombo.getValue())) {
                        dao.updateBalance(acc.getId(), bal);
                        alert(Alert.AlertType.INFORMATION, "Success", "Account updated");
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
        Account acc = table.getSelectionModel().getSelectedItem();
        if (acc == null) {
            alert(Alert.AlertType.WARNING, "Warning", "Select account");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + acc.getAccountName() + "?",
            ButtonType.OK, ButtonType.CANCEL);
        if (confirm.showAndWait().get() == ButtonType.OK) {
            if (dao.deleteAccount(acc.getId())) {
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
