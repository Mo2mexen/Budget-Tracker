package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import database.UserDAO;
import models.User;

public class LoginView {
    private Stage stage;
    private UserDAO userDAO = new UserDAO();

    public LoginView(Stage stage) {
        this.stage = stage;
    }

    public Scene createScene() {
        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        Label title = new Label("Budget Tracker");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(300);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(300);

        Label msgLabel = new Label();

        Button loginBtn = new Button("Login");
        loginBtn.setPrefWidth(145);
        loginBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        loginBtn.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText();
            if (username.isEmpty() || password.isEmpty()) {
                msgLabel.setText("Fill all fields");
                msgLabel.setStyle("-fx-text-fill: red;");
                return;
            }
            User user = userDAO.login(username, password);
            if (user != null) {
                stage.setScene(new DashboardView(stage, user).createScene());
            } else {
                msgLabel.setText("Invalid credentials");
                msgLabel.setStyle("-fx-text-fill: red;");
            }
        });

        Button registerBtn = new Button("Register");
        registerBtn.setPrefWidth(145);
        registerBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        registerBtn.setOnAction(e -> stage.setScene(new RegisterView(stage).createScene()));

        HBox btnBox = new HBox(10, loginBtn, registerBtn);
        btnBox.setAlignment(Pos.CENTER);

        passwordField.setOnAction(e -> loginBtn.fire());

        root.getChildren().addAll(title, usernameField, passwordField, btnBox, msgLabel);
        return new Scene(root, 800, 600);
    }
}
