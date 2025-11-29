package models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private int id;
    private int userId;
    private int accountId;
    private int categoryId;
    private double amount;
    private String description;
    private LocalDate transactionDate;
    private TransactionType type;
    private LocalDateTime createdAt;
    private String notes;

    public enum TransactionType {
        INCOME, EXPENSE, TRANSFER
    }

    public Transaction(int id, int userId, int accountId, int categoryId, double amount, 
                      String description, LocalDate transactionDate, TransactionType type, String notes) {
        this.id = id;
        this.userId = userId;
        this.accountId = accountId;
        this.categoryId = categoryId;
        this.amount = amount;
        this.description = description;
        this.transactionDate = transactionDate;
        this.type = type;
        this.notes = notes;
        this.createdAt = LocalDateTime.now();
    }

    public boolean isIncome() {
        return type == TransactionType.INCOME;
    }

    public boolean isExpense() {
        return type == TransactionType.EXPENSE;
    }

    public String getFormattedAmount() {
        String sign = isIncome() ? "+" : "-";
        return sign + " $" + String.format("%.2f", amount);
    }

    public String getMonthYear() {
        return transactionDate.format(DateTimeFormatter.ofPattern("MM/yyyy"));
    }

    public boolean isInMonth(int month, int year) {
        return transactionDate.getMonthValue() == month && transactionDate.getYear() == year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return transactionDate + " - " + description + ": " + getFormattedAmount();
    }
}