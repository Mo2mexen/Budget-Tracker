package models;

import java.time.LocalDate;

public class Account {
    private int id;
    private int userId;
    private String accountName;
    private AccountType accountType;
    private double balance;
    private String currency;
    private LocalDate createdDate;

    public enum AccountType {
        CHECKING, SAVINGS, CASH, CREDIT_CARD
    }

    public Account(int id, int userId, String accountName, AccountType accountType, double balance, String currency) {
        this.id = id;
        this.userId = userId;
        this.accountName = accountName;
        this.accountType = accountType;
        this.balance = balance;
        this.currency = currency;
        this.createdDate = LocalDate.now();
    }

    public boolean deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            return true;
        }
        return false;
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public boolean transfer(Account toAccount, double amount) {
        if (this.withdraw(amount)) {
            toAccount.deposit(amount);
            return true;
        }
        return false;
    }

    public String getFormattedBalance() {
        return currency + " " + String.format("%.2f", balance);
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

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return accountName + " (" + accountType + "): " + getFormattedBalance();
    }
}