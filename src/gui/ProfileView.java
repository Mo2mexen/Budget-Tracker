package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import models.User;
import database.UserDAO;

public class ProfileView {
    private User u;
    private UserDAO dao = new UserDAO();
    private VBox view;

    public ProfileView(User u) {
        this.u = u;
        this.view = create();
    }

    private VBox create() {
        VBox v = new VBox(20);
        v.setPadding(new Insets(20));
        v.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Profile");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        VBox card = new VBox(15);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(30));
        card.setMaxWidth(600);
        card.setStyle("-fx-background-color: white; -fx-border-color: #3498db; -fx-border-width: 2;");

        GridPane g = new GridPane();
        g.setHgap(15);
        g.setVgap(15);
        g.setAlignment(Pos.CENTER);

        Label l1 = new Label("ID:");
        l1.setStyle("-fx-font-weight: bold;");
        Label v1 = new Label(String.valueOf(u.getId()));
        g.add(l1, 0, 0);
        g.add(v1, 1, 0);

        Label l2 = new Label("Username:");
        l2.setStyle("-fx-font-weight: bold;");
        Label v2 = new Label(u.getUsername());
        g.add(l2, 0, 1);
        g.add(v2, 1, 1);

        Label l3 = new Label("Name:");
        l3.setStyle("-fx-font-weight: bold;");
        Label v3 = new Label(u.getFullName());
        g.add(l3, 0, 2);
        g.add(v3, 1, 2);

        Label l4 = new Label("Email:");
        l4.setStyle("-fx-font-weight: bold;");
        Label v4 = new Label(u.getEmail());
        g.add(l4, 0, 3);
        g.add(v4, 1, 3);

        Label l5 = new Label("Currency:");
        l5.setStyle("-fx-font-weight: bold;");
        Label v5 = new Label(u.getCurrency());
        g.add(l5, 0, 4);
        g.add(v5, 1, 4);

        Label l6 = new Label("Created:");
        l6.setStyle("-fx-font-weight: bold;");
        Label v6 = new Label(u.getCreatedDate().toString());
        g.add(l6, 0, 5);
        g.add(v6, 1, 5);

        card.getChildren().addAll(new Label("Account Info"), new Separator(), g);

        Button b1 = new Button("Edit Profile");
        b1.setPrefWidth(180);
        b1.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-padding: 10 20;");
        b1.setOnAction(e -> editDialog());

        Button b2 = new Button("Change Password");
        b2.setPrefWidth(180);
        b2.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-padding: 10 20;");
        b2.setOnAction(e -> passwordDialog());

        HBox btns = new HBox(15);
        btns.setAlignment(Pos.CENTER);
        btns.getChildren().addAll(b1, b2);

        v.getChildren().addAll(title, card, btns);
        return v;
    }

    private void editDialog() {
        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Edit Profile");
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane g = new GridPane();
        g.setHgap(10);
        g.setVgap(10);
        g.setPadding(new Insets(20));

        TextField user = new TextField(u.getUsername());
        TextField email = new TextField(u.getEmail());
        TextField name = new TextField(u.getFullName());
        ComboBox<String> curr = new ComboBox<>();
        curr.getItems().addAll("USD", "EUR", "GBP", "JPY", "CAD", "AUD", "EGP", "SAR", "AED");
        curr.setValue(u.getCurrency());

        g.add(new Label("Username:"), 0, 0);
        g.add(user, 1, 0);
        g.add(new Label("Email:"), 0, 1);
        g.add(email, 1, 1);
        g.add(new Label("Name:"), 0, 2);
        g.add(name, 1, 2);
        g.add(new Label("Currency:"), 0, 3);
        g.add(curr, 1, 3);

        dlg.getDialogPane().setContent(g);

        dlg.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                if (user.getText().trim().isEmpty() || email.getText().trim().isEmpty() || name.getText().trim().isEmpty()) {
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Error");
                    a.setHeaderText(null);
                    a.setContentText("All fields required");
                    a.showAndWait();
                    return;
                }
                u.setUsername(user.getText().trim());
                u.setEmail(email.getText().trim());
                u.setFullName(name.getText().trim());
                u.setCurrency(curr.getValue());
                if (dao.updateUser(u)) {
                    Alert a = new Alert(Alert.AlertType.INFORMATION);
                    a.setTitle("Success");
                    a.setHeaderText(null);
                    a.setContentText("Profile updated");
                    a.showAndWait();
                    this.view = create();
                } else {
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Error");
                    a.setHeaderText(null);
                    a.setContentText("Failed");
                    a.showAndWait();
                }
            }
        });
    }

    private void passwordDialog() {
        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Change Password");
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane g = new GridPane();
        g.setHgap(10);
        g.setVgap(10);
        g.setPadding(new Insets(20));

        PasswordField cur = new PasswordField();
        PasswordField nw = new PasswordField();
        PasswordField conf = new PasswordField();

        g.add(new Label("Current:"), 0, 0);
        g.add(cur, 1, 0);
        g.add(new Label("New:"), 0, 1);
        g.add(nw, 1, 1);
        g.add(new Label("Confirm:"), 0, 2);
        g.add(conf, 1, 2);

        dlg.getDialogPane().setContent(g);

        dlg.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                if (!u.validatePassword(cur.getText())) {
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Error");
                    a.setHeaderText(null);
                    a.setContentText("Wrong password");
                    a.showAndWait();
                    return;
                }
                if (nw.getText().length() < 6) {
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Error");
                    a.setHeaderText(null);
                    a.setContentText("Password must be 6+ chars");
                    a.showAndWait();
                    return;
                }
                if (!nw.getText().equals(conf.getText())) {
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Error");
                    a.setHeaderText(null);
                    a.setContentText("Passwords don't match");
                    a.showAndWait();
                    return;
                }
                if (dao.changePassword(u.getId(), nw.getText())) {
                    u.changePassword(nw.getText());
                    Alert a = new Alert(Alert.AlertType.INFORMATION);
                    a.setTitle("Success");
                    a.setHeaderText(null);
                    a.setContentText("Password changed");
                    a.showAndWait();
                } else {
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Error");
                    a.setHeaderText(null);
                    a.setContentText("Failed");
                    a.showAndWait();
                }
            }
        });
    }

    public VBox getView() {
        return view;
    }
}
