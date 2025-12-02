package models;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Goal extends BaseEntity implements Calculable, Displayable {
    private String goalName;
    private double targetAmount;
    private double currentAmount;
    private LocalDate deadline;
    private GoalStatus status;

    public enum GoalStatus {
        ACTIVE, COMPLETED, CANCELLED
    }

    public Goal(int id, int userId, String goalName, double targetAmount, double currentAmount, LocalDate deadline) {
        super(id, userId);
        this.goalName = goalName;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
        this.deadline = deadline;
        this.status = GoalStatus.ACTIVE;
    }

    @Override
    public double calculateTotal() {
        return targetAmount;
    }

    @Override
    public double calculateRemaining() {
        return targetAmount - currentAmount;
    }

    @Override
    public double calculatePercentage() {
        if (targetAmount == 0) return 0;
        return (currentAmount / targetAmount) * 100;
    }

    @Override
    public String getFormattedDisplay() {
        return String.format("%s: $%.2f / $%.2f (%.1f%% - %s)",
                           goalName, currentAmount, targetAmount, calculatePercentage(), status);
    }

    @Override
    public void printDetails() {
        System.out.println("Goal Details:");
        System.out.println("Name: " + goalName);
        System.out.println("Target: $" + targetAmount);
        System.out.println("Current: $" + currentAmount);
        System.out.println("Progress: " + calculatePercentage() + "%");
        System.out.println("Deadline: " + deadline);
        System.out.println("Status: " + status);
    }

    @Override
    public String getDisplayInfo() {
        return goalName + " - " + calculatePercentage() + "% complete";
    }

    public boolean addContribution(double amount) {
        if (amount > 0 && status == GoalStatus.ACTIVE) {
            currentAmount += amount;
            if (currentAmount >= targetAmount) {
                status = GoalStatus.COMPLETED;
            }
            return true;
        }
        return false;
    }

    public double getProgress() {
        return calculatePercentage();
    }

    public long getDaysRemaining() {
        return ChronoUnit.DAYS.between(LocalDate.now(), deadline);
    }

    public boolean isCompleted() {
        return status == GoalStatus.COMPLETED;
    }

    public boolean isOverdue() {
        return LocalDate.now().isAfter(deadline) && !isCompleted();
    }

    public String getGoalName() {
        return goalName;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    public double getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(double targetAmount) {
        this.targetAmount = targetAmount;
    }

    public double getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(double currentAmount) {
        this.currentAmount = currentAmount;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public GoalStatus getStatus() {
        return status;
    }

    public void setStatus(GoalStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return getFormattedDisplay();
    }
}
