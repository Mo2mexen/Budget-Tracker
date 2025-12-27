package gui.views;

import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class RegisterView {
    private Stage stage;
    private TextField usernameField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private TextField emailField;
    private TextField fullNameField;
    private ComboBox<String> currencyBox;
    private Label messageLabel;
    private Button registerButton;
    private Button backButton;

    public RegisterView(Stage stage) {
        this.stage = stage;
    }

    public Scene createScene() {
        VBox mainContainer = new VBox(10);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setPadding(new Insets(20));

        // Title
        Label titleLabel = new Label("Create Account");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Input fields
        usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(300);

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(300);

        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm Password");
        confirmPasswordField.setMaxWidth(300);

        emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setMaxWidth(300);

        fullNameField = new TextField();
        fullNameField.setPromptText("Full Name");
        fullNameField.setMaxWidth(300);

        currencyBox = new ComboBox<>();
        currencyBox.getItems().addAll("USD", "EUR", "EGP");
        currencyBox.setValue("USD");
        currencyBox.setMaxWidth(300);

        // Message label
        messageLabel = new Label();

        // Register button
        registerButton = new Button("Register");
        registerButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        registerButton.setPrefWidth(145);

        // Back button
        backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white;");
        backButton.setPrefWidth(145);

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

    // Getters for controller
    public Stage getStage() {
        return stage;
    }

    public TextField getUsernameField() {
        return usernameField;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public PasswordField getConfirmPasswordField() {
        return confirmPasswordField;
    }

    public TextField getEmailField() {
        return emailField;
    }

    public TextField getFullNameField() {
        return fullNameField;
    }

    public ComboBox<String> getCurrencyBox() {
        return currencyBox;
    }

    public Label getMessageLabel() {
        return messageLabel;
    }

    public Button getRegisterButton() {
        return registerButton;
    }

    public Button getBackButton() {
        return backButton;
    }
}
