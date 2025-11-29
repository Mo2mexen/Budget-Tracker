package models;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Goal {
    private int id;
    private int userId;
    private String goalName;
    private double targetAmount;
    private double currentAmount;
    private LocalDate deadline;
    private GoalStatus status;
    private LocalDate createdDate;

    public enum GoalStatus {
        ACTIVE, COMPLETED, CANCELLED
    }

    public Goal(int id, int userId, String goalName, double targetAmount, double currentAmount, LocalDate deadline) {
        this.id = id;
        this.userId = userId;
        this.goalName = goalName;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
        this.deadline = deadline;
        this.status = GoalStatus.ACTIVE;
        this.createdDate = LocalDate.now();
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
        if (targetAmount == 0) return 0;
        return (currentAmount / targetAmount) * 100;
    }

    public double getRemainingAmount() {
        return targetAmount - currentAmount;
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

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return String.format("%s: $%.2f / $%.2f (%.1f%% - %s)", 
                           goalName, currentAmount, targetAmount, getProgress(), status);
    }
}