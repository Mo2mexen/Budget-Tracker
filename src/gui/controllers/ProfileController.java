package gui.controllers;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import database.UserDAO;
import models.User;
import gui.views.ProfileView;

public class ProfileController {
    private ProfileView view;
    private UserDAO userDAO;

    public ProfileController(ProfileView view) {
        this.view = view;
        this.userDAO = new UserDAO();
        attachEventHandlers();
    }

    private void attachEventHandlers() {
        view.getEditProfileButton().setOnAction(event -> openEditDialog());
        view.getChangePasswordButton().setOnAction(event -> openPasswordDialog());
    }

    private void openEditDialog() {
        User user = view.getUser();

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
                    view.refreshView();
                } else {
                    showMessage("Error", "Failed to update profile");
                }
            }
        });
    }

    private void openPasswordDialog() {
        User user = view.getUser();

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
}
