package utils;

import models.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.Desktop;

public class FileManager {

    private static Gson jsonConverter = new GsonBuilder().setPrettyPrinting().create();
    
    static {
        File reportsFolder = new File("reports");
        reportsFolder.mkdirs();
    }
    
    private static String getTodayDate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd_MM_yyyy");
        return now.format(formatter);
    }
    
    public static String exportTransactionsCSV(List<Transaction> transactionList) {
        String fileName = "reports/transactions_" + getTodayDate() + ".csv";
        
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            CSVPrinter csvWriter = new CSVPrinter(fileWriter, 
                CSVFormat.DEFAULT.withHeader("Date", "Description", "Amount", "Type", "Notes"));
            
            for (Transaction transaction : transactionList) {
                csvWriter.printRecord(
                    transaction.getTransactionDate(), 
                    transaction.getDescription(), 
                    transaction.getAmount(), 
                    transaction.getType(), 
                    transaction.getNotes()
                );
            }
            
            csvWriter.close();
            return fileName;
        } catch (Exception error) {
            return null;
        }
    }
    
    public static String exportBudgetsCSV(List<Budget> budgetList) {
        String fileName = "reports/budgets_" + getTodayDate() + ".csv";
        
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            CSVPrinter csvWriter = new CSVPrinter(fileWriter, 
                CSVFormat.DEFAULT.withHeader("Month", "Year", "Limit", "Spent", "Status"));
            
            for (Budget budget : budgetList) {
                csvWriter.printRecord(
                    budget.getMonth(), 
                    budget.getYear(), 
                    budget.getMonthlyLimit(), 
                    budget.getCurrentSpent(), 
                    budget.getStatus()
                );
            }
            
            csvWriter.close();
            return fileName;
        } catch (Exception error) {
            return null;
        }
    }
    
    public static String exportGoalsCSV(List<Goal> goalList) {
        String fileName = "reports/goals_" + getTodayDate() + ".csv";
        
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            CSVPrinter csvWriter = new CSVPrinter(fileWriter, 
                CSVFormat.DEFAULT.withHeader("Name", "Target", "Current", "Progress", "Deadline"));
            
            for (Goal goal : goalList) {
                csvWriter.printRecord(
                    goal.getGoalName(), 
                    goal.getTargetAmount(), 
                    goal.getCurrentAmount(), 
                    goal.getProgress() + "%", 
                    goal.getDeadline()
                );
            }
            
            csvWriter.close();
            return fileName;
        } catch (Exception error) {
            return null;
        }
    }
    
    public static String exportBackupJSON(User user, List<Account> accountList, 
                                         List<Transaction> transactionList, 
                                         List<Budget> budgetList, 
                                         List<Goal> goalList) {
        String fileName = "reports/backup_" + getTodayDate() + ".json";
        
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            
            Map<String, Object> allData = new HashMap<>();
            allData.put("user", user);
            allData.put("accounts", accountList);
            allData.put("transactions", transactionList);
            allData.put("budgets", budgetList);
            allData.put("goals", goalList);
            
            jsonConverter.toJson(allData, fileWriter);
            fileWriter.close();
            return fileName;
        } catch (Exception error) {
            return null;
        }
    }
    
    public static boolean openFile(String filePath) {
        try {
            File file = new File(filePath);
            Desktop.getDesktop().open(file);
            return true;
        } catch (Exception error) {
            return false;
        }
    }
}