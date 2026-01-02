package utils;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class ValidationHelper {

    // Amount validation constants
    public static final double MIN_AMOUNT = 0.01;
    public static final double MAX_AMOUNT = 1000000000.0;

    // String length constants
    public static final int MIN_USERNAME_LENGTH = 3;
    public static final int MAX_USERNAME_LENGTH = 20;
    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final int MIN_NAME_LENGTH = 2;
    public static final int MAX_NAME_LENGTH = 100;
    public static final int MIN_DESCRIPTION_LENGTH = 3;
    public static final int MAX_DESCRIPTION_LENGTH = 200;
    public static final int MIN_ACCOUNT_NAME_LENGTH = 2;
    public static final int MAX_ACCOUNT_NAME_LENGTH = 50;

    // Date validation constants
    public static final int MIN_YEAR = 2000;
    public static final int MAX_YEAR = 2100;
    public static final int MAX_FUTURE_DAYS = 365;

    // Email validation pattern
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    // Valid currencies
    private static final String[] VALID_CURRENCIES = {"USD", "EUR", "EGP"};

    /**
     * Validates if amount is positive and within acceptable range
     */
    public static boolean isValidAmount(double amount) {
        return amount >= MIN_AMOUNT && amount <= MAX_AMOUNT;
    }

    /**
     * Validates if amount is positive, within range, with custom min/max
     */
    public static boolean isValidAmount(double amount, double min, double max) {
        return amount >= min && amount <= max;
    }

    /**
     * Validates string is not empty and within length limits
     */
    public static boolean isValidString(String value, int minLength, int maxLength) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }
        int length = value.trim().length();
        return length >= minLength && length <= maxLength;
    }

    /**
     * Validates email format (basic check)
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Validates month is between 1 and 12
     */
    public static boolean isValidMonth(int month) {
        return month >= 1 && month <= 12;
    }

    /**
     * Validates year is within acceptable range
     */
    public static boolean isValidYear(int year) {
        return year >= MIN_YEAR && year <= MAX_YEAR;
    }

    /**
     * Validates date is not null
     */
    public static boolean isValidDate(LocalDate date) {
        return date != null;
    }

    /**
     * Validates date is not too far in the future
     */
    public static boolean isNotTooFarInFuture(LocalDate date) {
        if (date == null) {
            return false;
        }
        LocalDate maxFutureDate = LocalDate.now().plusDays(MAX_FUTURE_DAYS);
        return !date.isAfter(maxFutureDate);
    }

    /**
     * Validates date is not in the past
     */
    public static boolean isNotInPast(LocalDate date) {
        if (date == null) {
            return false;
        }
        return !date.isBefore(LocalDate.now());
    }

    /**
     * Validates transaction type is INCOME or EXPENSE
     */
    public static boolean isValidTransactionType(String type) {
        if (type == null || type.trim().isEmpty()) {
            return false;
        }
        String upperType = type.trim().toUpperCase();
        return upperType.equals("INCOME") || upperType.equals("EXPENSE");
    }

    /**
     * Validates password meets minimum length requirement
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        return password.length() >= MIN_PASSWORD_LENGTH;
    }

    /**
     * Validates username meets requirements
     */
    public static boolean isValidUsername(String username) {
        return isValidString(username, MIN_USERNAME_LENGTH, MAX_USERNAME_LENGTH);
    }

    /**
     * Validates currency code
     */
    public static boolean isValidCurrency(String currency) {
        if (currency == null || currency.trim().isEmpty()) {
            return false;
        }
        for (String validCurrency : VALID_CURRENCIES) {
            if (validCurrency.equalsIgnoreCase(currency.trim())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Validates account balance based on account type
     * Credit cards can have negative balance, others cannot
     */
    public static boolean isValidAccountBalance(double balance, String accountType) {
        // Credit cards can have negative balance (debt)
        if (accountType != null && accountType.equalsIgnoreCase("CREDIT_CARD")) {
            return balance >= -MAX_AMOUNT && balance <= MAX_AMOUNT;
        }
        // Other accounts must have non-negative balance
        return balance >= 0 && balance <= MAX_AMOUNT;
    }

    /**
     * Gets error message for invalid amount
     */
    public static String getAmountErrorMessage() {
        return "Amount must be between $" + MIN_AMOUNT + " and $" + MAX_AMOUNT;
    }

    /**
     * Gets error message for invalid string length
     */
    public static String getStringLengthErrorMessage(String fieldName, int minLength, int maxLength) {
        return fieldName + " must be between " + minLength + " and " + maxLength + " characters";
    }

    /**
     * Gets error message for invalid email
     */
    public static String getEmailErrorMessage() {
        return "Invalid email format. Example: user@example.com";
    }

    /**
     * Gets error message for invalid month
     */
    public static String getMonthErrorMessage() {
        return "Month must be between 1 and 12";
    }

    /**
     * Gets error message for invalid year
     */
    public static String getYearErrorMessage() {
        return "Year must be between " + MIN_YEAR + " and " + MAX_YEAR;
    }

    /**
     * Gets error message for invalid password
     */
    public static String getPasswordErrorMessage() {
        return "Password must be at least " + MIN_PASSWORD_LENGTH + " characters";
    }

    /**
     * Gets error message for invalid date in past
     */
    public static String getPastDateErrorMessage() {
        return "Date cannot be in the past";
    }

    /**
     * Gets error message for date too far in future
     */
    public static String getFutureDateErrorMessage() {
        return "Date cannot be more than " + MAX_FUTURE_DAYS + " days in the future";
    }
}
