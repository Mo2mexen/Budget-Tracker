package models;

public class Budget extends BaseEntity implements Calculable, Displayable {
    private static final double WARNING_THRESHOLD_PERCENTAGE = 80.0;

    private int categoryId;
    private double monthlyLimit;
    private int month;
    private int year;
    private double currentSpent;

    public Budget(int id, int userId, int categoryId, double monthlyLimit, int month, int year) {
        super(id, userId);
        this.categoryId = categoryId;
        this.monthlyLimit = monthlyLimit;
        this.month = month;
        this.year = year;
        this.currentSpent = 0.0;
    }

    @Override
    public double calculateTotal() {
        return monthlyLimit;
    }

    @Override
    public double calculateRemaining() {
        return monthlyLimit - currentSpent;
    }

    @Override
    public double calculatePercentage() {
        if (monthlyLimit == 0) return 0;
        return (currentSpent / monthlyLimit) * 100;
    }

    @Override
    public String getFormattedDisplay() {
        return String.format("Budget %d/%d: $%.2f / $%.2f (%s)",
                           month, year, currentSpent, monthlyLimit, getStatus());
    }

    @Override
    public void printDetails() {
        System.out.println("Budget Details:");
        System.out.println("Period: " + month + "/" + year);
        System.out.println("Limit: $" + monthlyLimit);
        System.out.println("Spent: $" + currentSpent);
        System.out.println("Status: " + getStatus());
    }

    @Override
    public String getDisplayInfo() {
        return "Budget for " + month + "/" + year + " - " + getStatus();
    }

    public boolean isOverBudget() {
        return currentSpent > monthlyLimit;
    }

    public boolean isNearLimit() {
        return calculatePercentage() >= WARNING_THRESHOLD_PERCENTAGE;
    }

    public void addExpense(double amount) {
        currentSpent += amount;
    }

    public String getStatus() {
        if (isOverBudget()) return "OVER";
        if (isNearLimit()) return "WARNING";
        return "GOOD";
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
        return getFormattedDisplay();
    }
}
