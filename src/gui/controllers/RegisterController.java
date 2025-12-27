package gui.controllers;

import database.UserDAO;
import gui.views.RegisterView;
import gui.views.LoginView;

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
        String username = view.getUsernameField().getText();
        String password = view.getPasswordField().getText();
        String confirmPassword = view.getConfirmPasswordField().getText();
        String email = view.getEmailField().getText();
        String fullName = view.getFullNameField().getText();
        String currency = view.getCurrencyBox().getValue();

        // Validation
        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || fullName.isEmpty()) {
            view.getMessageLabel().setText("Fill all fields");
            return;
        }

        if (!password.equals(confirmPassword)) {
            view.getMessageLabel().setText("Passwords don't match");
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
