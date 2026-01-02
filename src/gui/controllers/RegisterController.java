package gui.controllers;

import database.UserDAO;
import gui.views.RegisterView;
import gui.views.LoginView;
import utils.ValidationHelper;

public class RegisterController {
    private RegisterView view;
    private UserDAO userDAO;

    public RegisterController(RegisterView view) {
        this.view = view;
        this.userDAO = new UserDAO();
        attachEventHandlers();
    }

    private void attachEventHandlers() {
        // Register button click
        view.getRegisterButton().setOnAction(event -> handleRegister());

        // Back button click
        view.getBackButton().setOnAction(event -> handleBack());
    }

    private void handleRegister() {
        String username = view.getUsernameField().getText().trim();
        String password = view.getPasswordField().getText();
        String confirmPassword = view.getConfirmPasswordField().getText();
        String email = view.getEmailField().getText().trim();
        String fullName = view.getFullNameField().getText().trim();
        String currency = view.getCurrencyBox().getValue();

        // Validation - Check if fields are empty
        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || fullName.isEmpty()) {
            view.getMessageLabel().setText("Fill all fields");
            return;
        }

        // Validate username length
        if (!ValidationHelper.isValidUsername(username)) {
            view.getMessageLabel().setText(ValidationHelper.getStringLengthErrorMessage("Username", ValidationHelper.MIN_USERNAME_LENGTH, ValidationHelper.MAX_USERNAME_LENGTH));
            return;
        }

        // Validate password length
        if (!ValidationHelper.isValidPassword(password)) {
            view.getMessageLabel().setText(ValidationHelper.getPasswordErrorMessage());
            return;
        }

        // Validate password confirmation
        if (!password.equals(confirmPassword)) {
            view.getMessageLabel().setText("Passwords don't match");
            return;
        }

        // Validate email format
        if (!ValidationHelper.isValidEmail(email)) {
            view.getMessageLabel().setText(ValidationHelper.getEmailErrorMessage());
            return;
        }

        // Validate full name length
        if (!ValidationHelper.isValidString(fullName, ValidationHelper.MIN_NAME_LENGTH, ValidationHelper.MAX_NAME_LENGTH)) {
            view.getMessageLabel().setText(ValidationHelper.getStringLengthErrorMessage("Full name", ValidationHelper.MIN_NAME_LENGTH, ValidationHelper.MAX_NAME_LENGTH));
            return;
        }

        // Validate currency selection
        if (currency == null || !ValidationHelper.isValidCurrency(currency)) {
            view.getMessageLabel().setText("Please select a valid currency");
            return;
        }

        // Create user
        boolean success = userDAO.createUser(username, password, email, fullName, currency);

        if (success) {
            view.getMessageLabel().setText("Success! Please login");
            view.getUsernameField().clear();
            view.getPasswordField().clear();
            view.getConfirmPasswordField().clear();
            view.getEmailField().clear();
            view.getFullNameField().clear();
        } else {
            view.getMessageLabel().setText("Failed to create account");
        }
    }

    private void handleBack() {
        LoginView loginView = new LoginView(view.getStage());
        view.getStage().setScene(loginView.createScene());
        new LoginController(loginView);
    }
}
