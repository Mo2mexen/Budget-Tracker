package models;

public class Budget {
    private int id;
    private int userId;
    private int categoryId;
    private double monthlyLimit;
    private int month;
    private int year;
    private double currentSpent;

    public Budget(int id, int userId, int categoryId, double monthlyLimit, int month, int year) {
        this.id = id;
        this.userId = userId;
        this.categoryId = categoryId;
        this.monthlyLimit = monthlyLimit;
        this.month = month;
        this.year = year;
        this.currentSpent = 0.0;
    }

    public double getRemainingAmount() {
        return monthlyLimit - currentSpent;
    }

    public double getSpentPercentage() {
        if (monthlyLimit == 0) return 0;
        return (currentSpent / monthlyLimit) * 100;
    }

    public boolean isOverBudget() {
        return currentSpent > monthlyLimit;
    }

    public boolean isNearLimit() {
        return getSpentPercentage() >= 80;
    }

    public void addExpense(double amount) {
        currentSpent += amount;
    }

    public String getStatus() {
        if (isOverBudget()) return "OVER";
        if (isNearLimit()) return "WARNING";
        return "GOOD";
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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public double getMonthlyLimit() {
        return monthlyLimit;
    }

    public void setMonthlyLimit(double monthlyLimit) {
        this.monthlyLimit = monthlyLimit;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getCurrentSpent() {
        return currentSpent;
    }

    public void setCurrentSpent(double currentSpent) {
        this.currentSpent = currentSpent;
    }

    @Override
    public String toString() {
        return String.format("Budget %d/%d: $%.2f / $%.2f (%s)", 
                           month, year, currentSpent, monthlyLimit, getStatus());
    }
}