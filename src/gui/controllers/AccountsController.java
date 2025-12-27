package gui.controllers;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import models.Account;
import models.Account.AccountType;
import database.AccountDAO;
import gui.views.AccountsView;
import java.util.Optional;

public class AccountsController {
    private AccountsView view;
    private AccountDAO accountDAO;

    public AccountsController(AccountsView view) {
        this.view = view;
        this.accountDAO = new AccountDAO();
        attachEventHandlers();
        loadAccounts();
    }

    private void attachEventHandlers() {
        view.getAddButton().setOnAction(event -> addAccount());
        view.getEditButton().setOnAction(event -> editAccount());
        view.getDeleteButton().setOnAction(event -> deleteAccount());
        view.getRefreshButton().setOnAction(event -> loadAccounts());
    }

    private void loadAccounts() {
        view.getAccountsTable().getItems().clear();
        view.getAccountsTable().getItems().addAll(accountDAO.getUserAccounts(view.getUser().getId()));
    }

    private void addAccount() {
        Dialog<ButtonType> dialog = createAccountDialog("Add Account", null);

        Optional<ButtonType> result = dialog.showAndWait();
        if (!result.isPresent() || result.get() != ButtonType.OK) {
            return;
        }

        VBox content = (VBox) dialog.getDialogPane().getContent();
        TextField nameField = (TextField) content.getChildren().get(1);
        String accountName = nameField.getText().trim();

        if (accountName.isEmpty()) {
            showMessage("Error", "Name is required");
            return;
        }

        try {
            ComboBox<AccountType> typeBox = (ComboBox<AccountType>) content.getChildren().get(3);
            TextField balanceField = (TextField) content.getChildren().get(5);
            ComboBox<String> currencyBox = (ComboBox<String>) content.getChildren().get(7);

            double balance = Double.parseDouble(balanceField.getText().trim());
            boolean success = accountDAO.createAccount(view.getUser().getId(), accountName, typeBox.getValue().name(), balance, currencyBox.getValue());

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

    private void editAccount() {
        Account selectedAccount = view.getAccountsTable().getSelectionModel().getSelectedItem();
        if (selectedAccount == null) {
            showMessage("Warning", "Please select an account");
            return;
        }

        Dialog<ButtonType> dialog = createAccountDialog("Edit Account", selectedAccount);

        Optional<ButtonType> result = dialog.showAndWait();
        if (!result.isPresent() || result.get() != ButtonType.OK) {
            return;
        }

        VBox content = (VBox) dialog.getDialogPane().getContent();
        TextField nameField = (TextField) content.getChildren().get(1);
        String accountName = nameField.getText().trim();

        if (accountName.isEmpty()) {
            showMessage("Error", "Name is required");
            return;
        }

        try {
            ComboBox<AccountType> typeBox = (ComboBox<AccountType>) content.getChildren().get(3);
            TextField balanceField = (TextField) content.getChildren().get(5);
            ComboBox<String> currencyBox = (ComboBox<String>) content.getChildren().get(7);

            double balance = Double.parseDouble(balanceField.getText().trim());
            boolean success = accountDAO.updateAccount(selectedAccount.getId(), accountName, typeBox.getValue().name(), currencyBox.getValue());

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

    private Dialog<ButtonType> createAccountDialog(String title, Account account) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        VBox dialogBox = new VBox(5);
        dialogBox.setPadding(new Insets(10));

        TextField nameField = new TextField(account != null ? account.getAccountName() : "");

        ComboBox<AccountType> typeBox = new ComboBox<>();
        typeBox.getItems().addAll(AccountType.values());
        typeBox.setValue(account != null ? account.getAccountType() : AccountType.CHECKING);

        TextField balanceField = new TextField(account != null ? String.valueOf(account.getBalance()) : "0.0");

        ComboBox<String> currencyBox = new ComboBox<>();
        currencyBox.getItems().addAll("USD", "EUR", "EGP");
        currencyBox.setValue(account != null ? account.getCurrency() : view.getUser().getCurrency());

        dialogBox.getChildren().addAll(
            new Label("Name:"), nameField,
            new Label("Type:"), typeBox,
            new Label("Balance:"), balanceField,
            new Label("Currency:"), currencyBox
        );

        dialog.getDialogPane().setContent(dialogBox);
        return dialog;
    }

    private void deleteAccount() {
        Account selectedAccount = view.getAccountsTable().getSelectionModel().getSelectedItem();
        if (selectedAccount == null) {
            showMessage("Warning", "Please select an account");
            return;
        }

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm");
        confirmDialog.setHeaderText(null);
        confirmDialog.setContentText("Delete account: " + selectedAccount.getAccountName() + "?");

        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (!result.isPresent() || result.get() != ButtonType.OK) {
            return;
        }

        boolean success = accountDAO.deleteAccount(selectedAccount.getId());
        if (success) {
            showMessage("Success", "Account deleted");
            loadAccounts();
        } else {
            showMessage("Error", "Failed to delete account");
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
