package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import models.User;
import database.UserDAO;

public class ProfileView {
    private User user;
    private UserDAO userDAO;
    private VBox mainView;

    public ProfileView(User user) {
        this.user = user;
        this.userDAO = new UserDAO();
        this.mainView = createView();
    }

    private VBox createView() {
        VBox container = new VBox(20);
        container.setPadding(new Insets(20));
        container.setAlignment(Pos.TOP_CENTER);

        Label titleLabel = new Label("Profile");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        GridPane profileGrid = new GridPane();
        profileGrid.setHgap(15);
        profileGrid.setVgap(15);
        profileGrid.setAlignment(Pos.CENTER);

        // ID row
        Label idLabel = new Label("ID:");
        Label idValueLabel = new Label(String.valueOf(user.getId()));
        profileGrid.add(idLabel, 0, 0);
        profileGrid.add(idValueLabel, 1, 0);

        // Username row
        Label usernameLabel = new Label("Username:");
        Label usernameValueLabel = new Label(user.getUsername());
        profileGrid.add(usernameLabel, 0, 1);
        profileGrid.add(usernameValueLabel, 1, 1);

        // Full Name row
        Label fullNameLabel = new Label("Name:");
        Label fullNameValueLabel = new Label(user.getFullName());
        profileGrid.add(fullNameLabel, 0, 2);
        profileGrid.add(fullNameValueLabel, 1, 2);

        // Email row
        Label emailLabel = new Label("Email:");
        Label emailValueLabel = new Label(user.getEmail());
        profileGrid.add(emailLabel, 0, 3);
        profileGrid.add(emailValueLabel, 1, 3);

        // Currency row
        Label currencyLabel = new Label("Currency:");
        Label currencyValueLabel = new Label(user.getCurrency());
        profileGrid.add(currencyLabel, 0, 4);
        profileGrid.add(currencyValueLabel, 1, 4);

        // Created date row
        Label createdLabel = new Label("Created:");
        Label createdValueLabel = new Label(user.getCreatedDate().toString());
        profileGrid.add(createdLabel, 0, 5);
        profileGrid.add(createdValueLabel, 1, 5);

        // Edit button
        Button editProfileButton = new Button("Edit Profile");
        editProfileButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        editProfileButton.setPrefWidth(180);
        editProfileButton.setOnAction(event -> openEditDialog());

        // Change password button
        Button changePasswordButton = new Button("Change Password");
        changePasswordButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        changePasswordButton.setPrefWidth(180);
        changePasswordButton.setOnAction(event -> openPasswordDialog());

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(editProfileButton, changePasswordButton);

        container.getChildren().addAll(titleLabel, profileGrid, buttonBox);
        return container;
    }

    private void openEditDialog() {
        Dialog<ButtonType> editDialog = new Dialog<>();
        editDialog.setTitle("Edit Profile");
        editDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane dialogGrid = new GridPane();
        dialogGrid.setHgap(10);
        dialogGrid.setVgap(10);
        dialogGrid.setPadding(new Insets(20));

        TextField usernameField = new TextField(user.getUsername());
        TextField emailField = new TextField(user.getEmail());
        TextField fullNameField = new TextField(user.getFullName());
        ComboBox<String> currencyCombo = new ComboBox<>();
        currencyCombo.getItems().addAll("USD", "EUR", "EGP");
        currencyCombo.setValue(user.getCurrency());

        dialogGrid.add(new Label("Username:"), 0, 0);
        dialogGrid.add(usernameField, 1, 0);
        dialogGrid.add(new Label("Email:"), 0, 1);
        dialogGrid.add(emailField, 1, 1);
        dialogGrid.add(new Label("Name:"), 0, 2);
        dialogGrid.add(fullNameField, 1, 2);
        dialogGrid.add(new Label("Currency:"), 0, 3);
        dialogGrid.add(currencyCombo, 1, 3);

        editDialog.getDialogPane().setContent(dialogGrid);

        editDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (usernameField.getText().trim().isEmpty() ||
                    emailField.getText().trim().isEmpty() ||
                    fullNameField.getText().trim().isEmpty()) {
                    showMessage("Error", "All fields required");
                    return;
                }

                user.setUsername(usernameField.getText().trim());
                user.setEmail(emailField.getText().trim());
                user.setFullName(fullNameField.getText().trim());
                user.setCurrency(currencyCombo.getValue());

                if (userDAO.updateUser(user)) {
                    showMessage("Success", "Profile updated");
                    this.mainView = createView();
                } else {
                    showMessage("Error", "Failed to update profile");
                }
            }
        });
    }

    private void openPasswordDialog() {
        Dialog<ButtonType> passwordDialog = new Dialog<>();
        passwordDialog.setTitle("Change Password");
        passwordDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane dialogGrid = new GridPane();
        dialogGrid.setHgap(10);
        dialogGrid.setVgap(10);
        dialogGrid.setPadding(new Insets(20));

        PasswordField currentPasswordField = new PasswordField();
        PasswordField newPasswordField = new PasswordField();
        PasswordField confirmPasswordField = new PasswordField();

        dialogGrid.add(new Label("Current:"), 0, 0);
        dialogGrid.add(currentPasswordField, 1, 0);
        dialogGrid.add(new Label("New:"), 0, 1);
        dialogGrid.add(newPasswordField, 1, 1);
        dialogGrid.add(new Label("Confirm:"), 0, 2);
        dialogGrid.add(confirmPasswordField, 1, 2);

        passwordDialog.getDialogPane().setContent(dialogGrid);

        passwordDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (!user.validatePassword(currentPasswordField.getText())) {
                    showMessage("Error", "Wrong password");
                    return;
                }

                if (newPasswordField.getText().length() < 6) {
                    showMessage("Error", "Password must be 6+ chars");
                    return;
                }

                if (!newPasswordField.getText().equals(confirmPasswordField.getText())) {
                    showMessage("Error", "Passwords don't match");
                    return;
                }

                if (userDAO.changePassword(user.getId(), newPasswordField.getText())) {
                    user.changePassword(newPasswordField.getText());
                    showMessage("Success", "Password changed");
                } else {
                    showMessage("Error", "Failed to change password");
                }
            }
        });
    }

    private void showMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public VBox getView() {
        return mainView;
    }
}
