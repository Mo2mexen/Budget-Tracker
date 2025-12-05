package models;

public class Account extends BaseEntity implements Displayable {
    private String accountName;
    private AccountType accountType;
    private double balance;
    private String currency;

    public enum AccountType {
        CHECKING, SAVINGS, CASH, CREDIT_CARD
    }

    public Account(int id, int userId, String accountName, AccountType accountType, double balance, String currency) {
        super(id, userId);
        this.accountName = accountName;
        this.accountType = accountType;
        this.balance = balance;
        this.currency = currency;
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

    @Override
    public String getDisplayInfo() {
        return accountName + " (" + accountType + "): " + getFormattedBalance();
    }

    @Override
    public String getFormattedDisplay() {
        return getDisplayInfo();
    }

    @Override
    public void printDetails() {
        System.out.println("Account Details:");
        System.out.println("Name: " + accountName);
        System.out.println("Type: " + accountType);
        System.out.println("Balance: " + getFormattedBalance());
        System.out.println("Created: " + getCreatedDate());
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

    @Override
    public String toString() {
        return accountName + " (" + accountType + "): " + getFormattedBalance();
    }
}