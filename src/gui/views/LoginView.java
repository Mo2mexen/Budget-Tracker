package gui.views;

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

public class LoginView {
    private Stage primaryStage;
    private TextField usernameField;
    private PasswordField passwordField;
    private Label messageLabel;
    private Button loginButton;
    private Button registerButton;

    public LoginView(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Scene createScene() {
        VBox mainContainer = new VBox(15);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setPadding(new Insets(20));

        Label titleLabel = new Label("Budget Tracker");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(300);

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(300);

        messageLabel = new Label();

        loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        loginButton.setPrefWidth(145);
        

        registerButton = new Button("Register");
        registerButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        registerButton.setPrefWidth(145);

        HBox buttonContainer = new HBox(10, loginButton, registerButton);
        buttonContainer.setAlignment(Pos.CENTER);

        // Allow pressing Enter to login
        passwordField.setOnAction(event -> loginButton.fire());

        mainContainer.getChildren().addAll(titleLabel, usernameField, passwordField, buttonContainer, messageLabel);

        return new Scene(mainContainer, 800, 600);
    }

    // Getters for controller
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public TextField getUsernameField() {
        return usernameField;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public Label getMessageLabel() {
        return messageLabel;
    }

    public Button getLoginButton() {
        return loginButton;
    }

    public Button getRegisterButton() {
        return registerButton;
    }
}
