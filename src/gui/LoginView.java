package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import database.UserDAO;
import models.User;

public class LoginView {
    private Stage primaryStage;
    private UserDAO userDAO;

    public LoginView(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.userDAO = new UserDAO();
    }

    public Scene createScene() {
        VBox mainContainer = new VBox(15);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setPadding(new Insets(20));

        Label titleLabel = new Label("Budget Tracker");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(300);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(300);

        Label messageLabel = new Label();

        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        loginButton.setPrefWidth(145);
        loginButton.setOnAction(event -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Please fill all fields");
                return;
            }

            User user = userDAO.login(username, password);

            if (user != null) {
                primaryStage.setScene(new DashboardView(primaryStage, user).createScene());
            } else {
                messageLabel.setText("Invalid username or password");
            }
        });

        Button registerButton = new Button("Register");
        registerButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        registerButton.setPrefWidth(145);
        registerButton.setOnAction(event -> {
            primaryStage.setScene(new RegisterView(primaryStage).createScene());
        });

        HBox buttonContainer = new HBox(10, loginButton, registerButton);
        buttonContainer.setAlignment(Pos.CENTER);

        // Allow pressing Enter to login
        passwordField.setOnAction(event -> loginButton.fire());

        mainContainer.getChildren().addAll(titleLabel, usernameField, passwordField, buttonContainer, messageLabel);

        return new Scene(mainContainer, 800, 600);
    }
}
