package gui;

import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import database.UserDAO;

public class RegisterView {
    private Stage stage;
    private UserDAO dao = new UserDAO();

    public RegisterView(Stage s) {
        stage = s;
    }

    public Scene createScene() {
        VBox v = new VBox(10);
        v.setAlignment(Pos.CENTER);
        v.setPadding(new Insets(20));

        Label title = new Label("Create Account");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        TextField user = new TextField();
        user.setPromptText("Username");
        user.setMaxWidth(300);

        PasswordField pass = new PasswordField();
        pass.setPromptText("Password");
        pass.setMaxWidth(300);

        PasswordField conf = new PasswordField();
        conf.setPromptText("Confirm Password");
        conf.setMaxWidth(300);

        TextField email = new TextField();
        email.setPromptText("Email");
        email.setMaxWidth(300);

        TextField name = new TextField();
        name.setPromptText("Full Name");
        name.setMaxWidth(300);

        ComboBox<String> curr = new ComboBox<>();
        curr.getItems().addAll("USD", "EUR", "GBP", "JPY", "CAD", "AUD", "EGP");
        curr.setValue("USD");
        curr.setMaxWidth(300);

        Label msg = new Label();

        Button reg = new Button("Register");
        reg.setPrefWidth(145);
        reg.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        reg.setOnAction(e -> {
            if (user.getText().isEmpty() || pass.getText().isEmpty() || email.getText().isEmpty() || name.getText().isEmpty()) {
                msg.setText("Fill all fields");
                msg.setStyle("-fx-text-fill: red;");
                return;
            }
            if (!pass.getText().equals(conf.getText())) {
                msg.setText("Passwords don't match");
                msg.setStyle("-fx-text-fill: red;");
                return;
            }
            if (dao.createUser(user.getText(), pass.getText(), email.getText(), name.getText(), curr.getValue())) {
                msg.setText("Success! Please login");
                msg.setStyle("-fx-text-fill: green;");
                user.clear();
                pass.clear();
                conf.clear();
                email.clear();
                name.clear();
            } else {
                msg.setText("Failed");
                msg.setStyle("-fx-text-fill: red;");
            }
        });

        Button back = new Button("Back");
        back.setPrefWidth(145);
        back.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white;");
        back.setOnAction(e -> stage.setScene(new LoginView(stage).createScene()));

        HBox btns = new HBox(10, reg, back);
        btns.setAlignment(Pos.CENTER);

        v.getChildren().addAll(title, user, pass, conf, email, name, curr, btns, msg);
        return new Scene(v, 800, 600);
    }
}
