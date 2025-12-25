package gui;

import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import database.UserDAO;

public class RegisterView {
    private Stage stage;
    private UserDAO userDAO;

    public RegisterView(Stage stage) {
        this.stage = stage;
        this.userDAO = new UserDAO();
    }

    public Scene createScene() {
        VBox mainContainer = new VBox(10);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setPadding(new Insets(20));

        // Title
        Label titleLabel = new Label("Create Account");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Input fields
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(300);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(300);

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm Password");
        confirmPasswordField.setMaxWidth(300);

        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setMaxWidth(300);

        TextField fullNameField = new TextField();
        fullNameField.setPromptText("Full Name");
        fullNameField.setMaxWidth(300);

        ComboBox<String> currencyBox = new ComboBox<>();
        currencyBox.getItems().addAll("USD", "EUR", "EGP");
        currencyBox.setValue("USD");
        currencyBox.setMaxWidth(300);

        // Message label
        Label messageLabel = new Label();

        // Register button
        Button registerButton = new Button("Register");
        registerButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        registerButton.setPrefWidth(145);
        registerButton.setOnAction(event -> handleRegister(
            usernameField.getText(),
            passwordField.getText(),
            confirmPasswordField.getText(),
            emailField.getText(),
            fullNameField.getText(),
            currencyBox.getValue(),
            messageLabel,
            usernameField,
            passwordField,
            confirmPasswordField,
            emailField,
            fullNameField
        ));

        // Back button
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white;");
        backButton.setPrefWidth(145);
        backButton.setOnAction(event -> stage.setScene(new LoginView(stage).createScene()));

        HBox buttonBox = new HBox(10, registerButton, backButton);
        buttonBox.setAlignment(Pos.CENTER);

        mainContainer.getChildren().addAll(
            titleLabel,
            usernameField,
            passwordField,
            confirmPasswordField,
            emailField,
            fullNameField,
            currencyBox,
            buttonBox,
            messageLabel
        );

        return new Scene(mainContainer, 800, 600);
    }

    private void handleRegister(String username, String password, String confirmPassword,
                                String email, String fullName, String currency,
                                Label messageLabel, TextField usernameField,
                                PasswordField passwordField, PasswordField confirmPasswordField,
                                TextField emailField, TextField fullNameField) {
        // Validation
        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || fullName.isEmpty()) {
            messageLabel.setText("Fill all fields");
            return;
        }

        if (!password.equals(confirmPassword)) {
            messageLabel.setText("Passwords don't match");
            return;
        }

        // Create user
        boolean success = userDAO.createUser(username, password, email, fullName, currency);

        if (success) {
            messageLabel.setText("Success! Please login");
            usernameField.clear();
            passwordField.clear();
            confirmPasswordField.clear();
            emailField.clear();
            fullNameField.clear();
        } else {
            messageLabel.setText("Failed to create account");
        }
    }
}
