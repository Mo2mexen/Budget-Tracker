package gui.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import models.User;

public class ProfileView {
    private User user;
    private VBox mainView;
    private Button editProfileButton;
    private Button changePasswordButton;

    public ProfileView(User user) {
        this.user = user;
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
        editProfileButton = new Button("Edit Profile");
        editProfileButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        editProfileButton.setPrefWidth(180);

        // Change password button
        changePasswordButton = new Button("Change Password");
        changePasswordButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        changePasswordButton.setPrefWidth(180);

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(editProfileButton, changePasswordButton);

        container.getChildren().addAll(titleLabel, profileGrid, buttonBox);
        return container;
    }

    // Getters for controller
    public User getUser() {
        return user;
    }

    public VBox getView() {
        return mainView;
    }

    public Button getEditProfileButton() {
        return editProfileButton;
    }

    public Button getChangePasswordButton() {
        return changePasswordButton;
    }

    public void refreshView() {
        this.mainView = createView();
    }
}
