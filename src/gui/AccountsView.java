package gui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import models.User;
import models.Account;
import models.Account.AccountType;
import database.AccountDAO;

public class AccountsView {
    private User user;
    private AccountDAO accountDAO;
    private TableView<Account> accountsTable;
    private VBox mainContainer;

    public AccountsView(User user) {
        this.user = user;
        this.accountDAO = new AccountDAO();
        this.accountsTable = new TableView<>();
        this.mainContainer = createView();
    }

    private VBox createView() {
        VBox container = new VBox(10);
        container.setPadding(new Insets(10));

        // Title
        Label titleLabel = new Label("Accounts");
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

        addButton.setOnAction(event -> addAccount());
        editButton.setOnAction(event -> editAccount());
        deleteButton.setOnAction(event -> deleteAccount());
        refreshButton.setOnAction(event -> loadAccounts());

        HBox buttonBox = new HBox(5);
        buttonBox.getChildren().addAll(addButton, editButton, deleteButton, refreshButton);

        // Create table columns
        TableColumn<Account, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Account, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("accountName"));

        TableColumn<Account, AccountType> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("accountType"));

        TableColumn<Account, Double> balanceColumn = new TableColumn<>("Balance");
        balanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));

        TableColumn<Account, String> currencyColumn = new TableColumn<>("Currency");
        currencyColumn.setCellValueFactory(new PropertyValueFactory<>("currency"));

        accountsTable.getColumns().addAll(idColumn, nameColumn, typeColumn, balanceColumn, currencyColumn);
        loadAccounts();

        container.getChildren().addAll(titleLabel, buttonBox, accountsTable);
        return container;
    }

    private void loadAccounts() {
        accountsTable.getItems().clear();
        accountsTable.getItems().addAll(accountDAO.getUserAccounts(user.getId()));
    }

    private void addAccount() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Account");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        VBox dialogBox = new VBox(5);
        dialogBox.setPadding(new Insets(10));

        TextField nameField = new TextField();

        ComboBox<AccountType> typeBox = new ComboBox<>();
        typeBox.getItems().addAll(AccountType.values());
        typeBox.setValue(AccountType.CHECKING);

        TextField balanceField = new TextField("0.0");

        ComboBox<String> currencyBox = new ComboBox<>();
        currencyBox.getItems().addAll("USD", "EUR", "EGP");
        currencyBox.setValue(user.getCurrency());

        dialogBox.getChildren().addAll(
            new Label("Name:"), nameField,
            new Label("Type:"), typeBox,
            new Label("Balance:"), balanceField,
            new Label("Currency:"), currencyBox
        );

        dialog.getDialogPane().setContent(dialogBox);

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String accountName = nameField.getText().trim();

                if (accountName.isEmpty()) {
                    showMessage("Error", "Name is required");
                    return;
                }

                try {
                    double balance = Double.parseDouble(balanceField.getText().trim());
                    String type = typeBox.getValue().name();
                    String currency = currencyBox.getValue();

                    boolean success = accountDAO.createAccount(user.getId(), accountName, type, balance, currency);

                    if (success) {
                        showMessage("Success", "Account created");
                        loadAccounts();
                    } else {
                        showMessage("Error", "Failed to create account");
                    }
                } catch (Exception exception) {
                    showMessage("Error", "Invalid input");
                }
            }
        });
    }

    private void editAccount() {
        Account selectedAccount = accountsTable.getSelectionModel().getSelectedItem();

        if (selectedAccount == null) {
            showMessage("Warning", "Please select an account");
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Account");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        VBox dialogBox = new VBox(5);
        dialogBox.setPadding(new Insets(10));

        TextField nameField = new TextField(selectedAccount.getAccountName());

        ComboBox<AccountType> typeBox = new ComboBox<>();
        typeBox.getItems().addAll(AccountType.values());
        typeBox.setValue(selectedAccount.getAccountType());

        TextField balanceField = new TextField(String.valueOf(selectedAccount.getBalance()));

        ComboBox<String> currencyBox = new ComboBox<>();
        currencyBox.getItems().addAll("USD", "EUR", "EGP");
        currencyBox.setValue(selectedAccount.getCurrency());

        dialogBox.getChildren().addAll(
            new Label("Name:"), nameField,
            new Label("Type:"), typeBox,
            new Label("Balance:"), balanceField,
            new Label("Currency:"), currencyBox
        );

        dialog.getDialogPane().setContent(dialogBox);

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String accountName = nameField.getText().trim();

                if (accountName.isEmpty()) {
                    showMessage("Error", "Name is required");
                    return;
                }

                try {
                    double balance = Double.parseDouble(balanceField.getText().trim());
                    String type = typeBox.getValue().name();
                    String currency = currencyBox.getValue();

                    boolean success = accountDAO.updateAccount(selectedAccount.getId(), accountName, type, currency);

                    if (success) {
                        accountDAO.updateBalance(selectedAccount.getId(), balance);
                        showMessage("Success", "Account updated");
                        loadAccounts();
                    } else {
                        showMessage("Error", "Failed to update account");
                    }
                } catch (Exception exception) {
                    showMessage("Error", "Invalid input");
                }
            }
        });
    }

    private void deleteAccount() {
        Account selectedAccount = accountsTable.getSelectionModel().getSelectedItem();

        if (selectedAccount == null) {
            showMessage("Warning", "Please select an account");
            return;
        }

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm");
        confirmDialog.setHeaderText(null);
        confirmDialog.setContentText("Delete account: " + selectedAccount.getAccountName() + "?");

        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean success = accountDAO.deleteAccount(selectedAccount.getId());

                if (success) {
                    showMessage("Success", "Account deleted");
                    loadAccounts();
                } else {
                    showMessage("Error", "Failed to delete account");
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
