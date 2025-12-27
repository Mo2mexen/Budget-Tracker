package gui.controllers;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import models.Transaction;
import models.Account;
import models.Category;
import models.Transaction.TransactionType;
import database.TransactionDAO;
import database.AccountDAO;
import database.CategoryDAO;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import gui.views.TransactionsView;

public class TransactionsController {
    private TransactionsView view;
    private TransactionDAO transactionDAO;
    private AccountDAO accountDAO;
    private CategoryDAO categoryDAO;

    public TransactionsController(TransactionsView view) {
        this.view = view;
        this.transactionDAO = new TransactionDAO();
        this.accountDAO = new AccountDAO();
        this.categoryDAO = new CategoryDAO();
        attachEventHandlers();
        loadTransactions();
    }

    private void attachEventHandlers() {
        view.getAddButton().setOnAction(event -> addTransaction());
        view.getEditButton().setOnAction(event -> editTransaction());
        view.getDeleteButton().setOnAction(event -> deleteTransaction());
        view.getRefreshButton().setOnAction(event -> loadTransactions());
    }

    private void loadTransactions() {
        view.getTransactionsTable().getItems().clear();
        view.getTransactionsTable().getItems().addAll(transactionDAO.getUserTransactions(view.getUser().getId()));
    }

    private void addTransaction() {
        List<Account> accountList = accountDAO.getUserAccounts(view.getUser().getId());
        List<Category> categoryList = categoryDAO.getUserCategories(view.getUser().getId());

        Dialog<ButtonType> dialog = createTransactionDialog("Add Transaction", null, accountList, categoryList);

        Optional<ButtonType> result = dialog.showAndWait();
        if (!result.isPresent() || result.get() != ButtonType.OK) {
            return;
        }

        VBox content = (VBox) dialog.getDialogPane().getContent();
        ComboBox<Account> accountBox = getComboBox(content, 1);
        ComboBox<Category> categoryBox = getComboBox(content, 3);

        if (accountBox.getValue() == null || categoryBox.getValue() == null) {
            showMessage("Error", "Please select account and category");
            return;
        }

        TextField descriptionField = getTextField(content, 7);
        String description = descriptionField.getText().trim();
        if (description.isEmpty()) {
            showMessage("Error", "Description is required");
            return;
        }

        try {
            TextField amountField = getTextField(content, 5);
            DatePicker datePicker = getDatePicker(content, 9);
            ComboBox<TransactionType> typeBox = getComboBox(content, 11);
            TextArea notesArea = getTextArea(content, 13);

            double amount = Double.parseDouble(amountField.getText().trim());
            boolean success = transactionDAO.addTransaction(
                view.getUser().getId(),
                accountBox.getValue().getId(),
                categoryBox.getValue().getId(),
                amount,
                description,
                datePicker.getValue(),
                typeBox.getValue().name(),
                notesArea.getText().trim()
            );

            if (success) {
                showMessage("Success", "Transaction created");
                loadTransactions();
            } else {
                showMessage("Error", "Failed to create transaction");
            }
        } catch (Exception exception) {
            showMessage("Error", "Invalid input");
        }
    }

    private void editTransaction() {
        Transaction selectedTransaction = view.getTransactionsTable().getSelectionModel().getSelectedItem();
        if (selectedTransaction == null) {
            showMessage("Warning", "Please select a transaction");
            return;
        }

        List<Account> accountList = accountDAO.getUserAccounts(view.getUser().getId());
        List<Category> categoryList = categoryDAO.getUserCategories(view.getUser().getId());

        Dialog<ButtonType> dialog = createTransactionDialog("Edit Transaction", selectedTransaction, accountList, categoryList);

        Optional<ButtonType> result = dialog.showAndWait();
        if (!result.isPresent() || result.get() != ButtonType.OK) {
            return;
        }

        VBox content = (VBox) dialog.getDialogPane().getContent();
        TextField descriptionField = getTextField(content, 7);
        String description = descriptionField.getText().trim();
        if (description.isEmpty()) {
            showMessage("Error", "Description is required");
            return;
        }

        try {
            ComboBox<Account> accountBox = getComboBox(content, 1);
            ComboBox<Category> categoryBox = getComboBox(content, 3);
            TextField amountField = getTextField(content, 5);
            DatePicker datePicker = getDatePicker(content, 9);
            ComboBox<TransactionType> typeBox = getComboBox(content, 11);
            TextArea notesArea = getTextArea(content, 13);

            double amount = Double.parseDouble(amountField.getText().trim());
            boolean success = transactionDAO.updateTransaction(
                selectedTransaction.getId(),
                accountBox.getValue().getId(),
                categoryBox.getValue().getId(),
                amount,
                description,
                datePicker.getValue(),
                typeBox.getValue().name(),
                notesArea.getText().trim()
            );

            if (success) {
                showMessage("Success", "Transaction updated");
                loadTransactions();
            } else {
                showMessage("Error", "Failed to update transaction");
            }
        } catch (Exception exception) {
            showMessage("Error", "Invalid input");
        }
    }

    private Dialog<ButtonType> createTransactionDialog(String title, Transaction transaction,
                                                       List<Account> accountList, List<Category> categoryList) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        VBox dialogBox = new VBox(5);
        dialogBox.setPadding(new Insets(10));

        ComboBox<Account> accountBox = new ComboBox<>();
        accountBox.getItems().addAll(accountList);
        if (transaction != null) {
            accountBox.setValue(accountDAO.getAccountById(transaction.getAccountId()));
        } else if (!accountList.isEmpty()) {
            accountBox.setValue(accountList.get(0));
        }

        ComboBox<Category> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll(categoryList);
        if (transaction != null) {
            categoryBox.setValue(categoryDAO.getCategoryById(transaction.getCategoryId()));
        } else if (!categoryList.isEmpty()) {
            categoryBox.setValue(categoryList.get(0));
        }

        TextField amountField = new TextField(transaction != null ? String.valueOf(transaction.getAmount()) : "");
        TextField descriptionField = new TextField(transaction != null ? transaction.getDescription() : "");
        DatePicker datePicker = new DatePicker(transaction != null ? transaction.getTransactionDate() : LocalDate.now());

        ComboBox<TransactionType> typeBox = new ComboBox<>();
        typeBox.getItems().addAll(TransactionType.values());
        typeBox.setValue(transaction != null ? transaction.getType() : TransactionType.EXPENSE);

        TextArea notesArea = new TextArea(transaction != null ? transaction.getNotes() : "");
        notesArea.setPrefRowCount(2);

        dialogBox.getChildren().addAll(
            new Label("Account:"), accountBox,
            new Label("Category:"), categoryBox,
            new Label("Amount:"), amountField,
            new Label("Description:"), descriptionField,
            new Label("Date:"), datePicker,
            new Label("Type:"), typeBox,
            new Label("Notes:"), notesArea
        );

        dialog.getDialogPane().setContent(dialogBox);
        return dialog;
    }

    private void deleteTransaction() {
        Transaction selectedTransaction = view.getTransactionsTable().getSelectionModel().getSelectedItem();
        if (selectedTransaction == null) {
            showMessage("Warning", "Please select a transaction");
            return;
        }

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm");
        confirmDialog.setHeaderText(null);
        confirmDialog.setContentText("Delete transaction: " + selectedTransaction.getDescription() + "?");

        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (!result.isPresent() || result.get() != ButtonType.OK) {
            return;
        }

        boolean success = transactionDAO.deleteTransaction(selectedTransaction.getId());
        if (success) {
            showMessage("Success", "Transaction deleted");
            loadTransactions();
        } else {
            showMessage("Error", "Failed to delete transaction");
        }
    }

    private ComboBox getComboBox(VBox box, int index) {
        return (ComboBox) box.getChildren().get(index);
    }

    private TextField getTextField(VBox box, int index) {
        return (TextField) box.getChildren().get(index);
    }

    private DatePicker getDatePicker(VBox box, int index) {
        return (DatePicker) box.getChildren().get(index);
    }

    private TextArea getTextArea(VBox box, int index) {
        return (TextArea) box.getChildren().get(index);
    }

    private void showMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
