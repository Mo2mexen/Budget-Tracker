package gui.controllers;

import database.UserDAO;
import models.User;
import gui.views.LoginView;
import gui.views.MainAppView;
import gui.views.RegisterView;

public class LoginController {
    private LoginView view;
    private UserDAO userDAO;

    public LoginController(LoginView view) {
        this.view = view;
        this.userDAO = new UserDAO();
        attachEventHandlers();
    }

    private void attachEventHandlers() {
        // Login button click
        view.getLoginButton().setOnAction(event -> handleLogin());

        // Register button click
        view.getRegisterButton().setOnAction(event -> handleRegister());
    }

    private void handleLogin() {
        String username = view.getUsernameField().getText().trim();
        String password = view.getPasswordField().getText();

        if (username.isEmpty() || password.isEmpty()) {
            view.getMessageLabel().setText("Please fill all fields");
            return;
        }

        User user = userDAO.login(username, password);

        if (user != null) {
            MainAppView mainAppView = new MainAppView(view.getPrimaryStage(), user);
            view.getPrimaryStage().setScene(mainAppView.createScene());
            new MainAppController(mainAppView);
        } else {
            view.getMessageLabel().setText("Invalid username or password");
        }
    }

    private void handleRegister() {
        RegisterView registerView = new RegisterView(view.getPrimaryStage());
        view.getPrimaryStage().setScene(registerView.createScene());
        new RegisterController(registerView);
    }
}
