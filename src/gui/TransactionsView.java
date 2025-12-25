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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import models.User;
import models.Transaction;
import models.Account;
import models.Category;
import models.Transaction.TransactionType;
import database.TransactionDAO;
import database.AccountDAO;
import database.CategoryDAO;
import java.time.LocalDate;

public class TransactionsView {
    private User user;
    private TransactionDAO transactionDAO;
    private AccountDAO accountDAO;
    private CategoryDAO categoryDAO;
    private TableView<Transaction> transactionsTable;
    private VBox mainContainer;

    public TransactionsView(User user) {
        this.user = user;
        this.transactionDAO = new TransactionDAO();
        this.accountDAO = new AccountDAO();
        this.categoryDAO = new CategoryDAO();
        this.transactionsTable = new TableView<>();
        this.mainContainer = createView();
    }

    private VBox createView() {
        VBox container = new VBox(10);
        container.setPadding(new Insets(10));

        // Title
        Label titleLabel = new Label("Transactions");
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

        addButton.setOnAction(event -> addTransaction());
        editButton.setOnAction(event -> editTransaction());
        deleteButton.setOnAction(event -> deleteTransaction());
        refreshButton.setOnAction(event -> loadTransactions());

        HBox buttonBox = new HBox(5);
        buttonBox.getChildren().addAll(addButton, editButton, deleteButton, refreshButton);

        // Create table columns
        TableColumn<Transaction, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Transaction, LocalDate> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));

        TableColumn<Transaction, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<Transaction, Double> amountColumn = new TableColumn<>("Amount");
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableColumn<Transaction, TransactionType> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        transactionsTable.getColumns().addAll(idColumn, dateColumn, descriptionColumn, amountColumn, typeColumn);
        loadTransactions();

        container.getChildren().addAll(titleLabel, buttonBox, transactionsTable);
        return container;
    }

    private void loadTransactions() {
        transactionsTable.getItems().clear();
        transactionsTable.getItems().addAll(transactionDAO.getUserTransactions(user.getId()));
    }

    private void addTransaction() {
        java.util.List<Account> accountList = accountDAO.getUserAccounts(user.getId());
        java.util.List<Category> categoryList = categoryDAO.getUserCategories(user.getId());

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Transaction");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        VBox dialogBox = new VBox(5);
        dialogBox.setPadding(new Insets(10));

        ComboBox<Account> accountBox = new ComboBox<>();
        accountBox.getItems().addAll(accountList);
        if (!accountList.isEmpty()) {
            accountBox.setValue(accountList.get(0));
        }

        ComboBox<Category> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll(categoryList);
        if (!categoryList.isEmpty()) {
            categoryBox.setValue(categoryList.get(0));
        }

        TextField amountField = new TextField();
        TextField descriptionField = new TextField();
        DatePicker datePicker = new DatePicker(LocalDate.now());
        ComboBox<TransactionType> typeBox = new ComboBox<>();
        typeBox.getItems().addAll(TransactionType.values());
        typeBox.setValue(TransactionType.EXPENSE);
        TextArea notesArea = new TextArea();
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

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (accountBox.getValue() == null || categoryBox.getValue() == null) {
                    showMessage("Error", "Please select account and category");
                    return;
                }

                String description = descriptionField.getText().trim();

                if (description.isEmpty()) {
                    showMessage("Error", "Description is required");
                    return;
                }

                try {
                    double amount = Double.parseDouble(amountField.getText().trim());
                    String transactionType = typeBox.getValue().name();
                    String notes = notesArea.getText().trim();

                    boolean success = transactionDAO.addTransaction(
                        user.getId(),
                        accountBox.getValue().getId(),
                        categoryBox.getValue().getId(),
                        amount,
                        description,
                        datePicker.getValue(),
                        transactionType,
                        notes
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
        });
    }

    private void editTransaction() {
        Transaction selectedTransaction = transactionsTable.getSelectionModel().getSelectedItem();

        if (selectedTransaction == null) {
            showMessage("Warning", "Please select a transaction");
            return;
        }

        java.util.List<Account> accountList = accountDAO.getUserAccounts(user.getId());
        java.util.List<Category> categoryList = categoryDAO.getUserCategories(user.getId());

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Transaction");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        VBox dialogBox = new VBox(5);
        dialogBox.setPadding(new Insets(10));

        ComboBox<Account> accountBox = new ComboBox<>();
        accountBox.getItems().addAll(accountList);
        accountBox.setValue(accountDAO.getAccountById(selectedTransaction.getAccountId()));

        ComboBox<Category> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll(categoryList);
        categoryBox.setValue(categoryDAO.getCategoryById(selectedTransaction.getCategoryId()));

        TextField amountField = new TextField(String.valueOf(selectedTransaction.getAmount()));
        TextField descriptionField = new TextField(selectedTransaction.getDescription());
        DatePicker datePicker = new DatePicker(selectedTransaction.getTransactionDate());
        ComboBox<TransactionType> typeBox = new ComboBox<>();
        typeBox.getItems().addAll(TransactionType.values());
        typeBox.setValue(selectedTransaction.getType());
        TextArea notesArea = new TextArea(selectedTransaction.getNotes());
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

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String description = descriptionField.getText().trim();

                if (description.isEmpty()) {
                    showMessage("Error", "Description is required");
                    return;
                }

                try {
                    double amount = Double.parseDouble(amountField.getText().trim());
                    String transactionType = typeBox.getValue().name();
                    String notes = notesArea.getText().trim();

                    boolean success = transactionDAO.updateTransaction(
                        selectedTransaction.getId(),
                        accountBox.getValue().getId(),
                        categoryBox.getValue().getId(),
                        amount,
                        description,
                        datePicker.getValue(),
                        transactionType,
                        notes
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
        });
    }

    private void deleteTransaction() {
        Transaction selectedTransaction = transactionsTable.getSelectionModel().getSelectedItem();

        if (selectedTransaction == null) {
            showMessage("Warning", "Please select a transaction");
            return;
        }

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm");
        confirmDialog.setHeaderText(null);
        confirmDialog.setContentText("Delete transaction: " + selectedTransaction.getDescription() + "?");

        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean success = transactionDAO.deleteTransaction(selectedTransaction.getId());

                if (success) {
                    showMessage("Success", "Transaction deleted");
                    loadTransactions();
                } else {
                    showMessage("Error", "Failed to delete transaction");
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
