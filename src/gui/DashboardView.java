package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import models.User;
import database.TransactionDAO;

public class DashboardView {
    private Stage s;
    private User u;
    private BorderPane root;

    public DashboardView(Stage s, User u) {
        this.s = s;
        this.u = u;
    }

    public Scene createScene() {
        root = new BorderPane();

        VBox menu = new VBox(10);
        menu.setPadding(new Insets(20));
        menu.setStyle("-fx-background-color: #2c3e50;");
        menu.setPrefWidth(200);

        Label t = new Label("Menu");
        t.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");
        menu.getChildren().addAll(t, new Separator());

        Button b1 = new Button("Dashboard");
        b1.setPrefWidth(180);
        b1.setAlignment(Pos.CENTER_LEFT);
        b1.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
        b1.setOnMouseEntered(e -> b1.setStyle("-fx-background-color: #34495e; -fx-text-fill: white;"));
        b1.setOnMouseExited(e -> b1.setStyle("-fx-background-color: transparent; -fx-text-fill: white;"));
        b1.setOnAction(e -> root.setCenter(getDashboard()));

        Button b2 = new Button("Accounts");
        b2.setPrefWidth(180);
        b2.setAlignment(Pos.CENTER_LEFT);
        b2.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
        b2.setOnMouseEntered(e -> b2.setStyle("-fx-background-color: #34495e; -fx-text-fill: white;"));
        b2.setOnMouseExited(e -> b2.setStyle("-fx-background-color: transparent; -fx-text-fill: white;"));
        b2.setOnAction(e -> root.setCenter(new AccountsView(u).getView()));

        Button b3 = new Button("Categories");
        b3.setPrefWidth(180);
        b3.setAlignment(Pos.CENTER_LEFT);
        b3.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
        b3.setOnMouseEntered(e -> b3.setStyle("-fx-background-color: #34495e; -fx-text-fill: white;"));
        b3.setOnMouseExited(e -> b3.setStyle("-fx-background-color: transparent; -fx-text-fill: white;"));
        b3.setOnAction(e -> root.setCenter(new CategoriesView(u).getView()));

        Button b4 = new Button("Transactions");
        b4.setPrefWidth(180);
        b4.setAlignment(Pos.CENTER_LEFT);
        b4.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
        b4.setOnMouseEntered(e -> b4.setStyle("-fx-background-color: #34495e; -fx-text-fill: white;"));
        b4.setOnMouseExited(e -> b4.setStyle("-fx-background-color: transparent; -fx-text-fill: white;"));
        b4.setOnAction(e -> root.setCenter(new TransactionsView(u).getView()));

        Button b5 = new Button("Budgets");
        b5.setPrefWidth(180);
        b5.setAlignment(Pos.CENTER_LEFT);
        b5.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
        b5.setOnMouseEntered(e -> b5.setStyle("-fx-background-color: #34495e; -fx-text-fill: white;"));
        b5.setOnMouseExited(e -> b5.setStyle("-fx-background-color: transparent; -fx-text-fill: white;"));
        b5.setOnAction(e -> root.setCenter(new BudgetsView(u).getView()));

        Button b6 = new Button("Goals");
        b6.setPrefWidth(180);
        b6.setAlignment(Pos.CENTER_LEFT);
        b6.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
        b6.setOnMouseEntered(e -> b6.setStyle("-fx-background-color: #34495e; -fx-text-fill: white;"));
        b6.setOnMouseExited(e -> b6.setStyle("-fx-background-color: transparent; -fx-text-fill: white;"));
        b6.setOnAction(e -> root.setCenter(new GoalsView(u).getView()));

        Button b7 = new Button("Reports");
        b7.setPrefWidth(180);
        b7.setAlignment(Pos.CENTER_LEFT);
        b7.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
        b7.setOnMouseEntered(e -> b7.setStyle("-fx-background-color: #34495e; -fx-text-fill: white;"));
        b7.setOnMouseExited(e -> b7.setStyle("-fx-background-color: transparent; -fx-text-fill: white;"));
        b7.setOnAction(e -> root.setCenter(new ReportsView(u).getView()));

        Button b8 = new Button("Profile");
        b8.setPrefWidth(180);
        b8.setAlignment(Pos.CENTER_LEFT);
        b8.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
        b8.setOnMouseEntered(e -> b8.setStyle("-fx-background-color: #34495e; -fx-text-fill: white;"));
        b8.setOnMouseExited(e -> b8.setStyle("-fx-background-color: transparent; -fx-text-fill: white;"));
        b8.setOnAction(e -> root.setCenter(new ProfileView(u).getView()));

        Region sp = new Region();
        VBox.setVgrow(sp, Priority.ALWAYS);

        Button logout = new Button("Logout");
        logout.setPrefWidth(180);
        logout.setAlignment(Pos.CENTER_LEFT);
        logout.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        logout.setOnAction(e -> {
            Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Logout?", ButtonType.OK, ButtonType.CANCEL);
            if (a.showAndWait().get() == ButtonType.OK) {
                s.setScene(new LoginView(s).createScene());
            }
        });

        menu.getChildren().addAll(b1, b2, b3, b4, b5, b6, b7, b8, sp, logout);

        HBox top = new HBox(20);
        top.setPadding(new Insets(15));
        top.setStyle("-fx-background-color: #ecf0f1;");
        top.setAlignment(Pos.CENTER_RIGHT);
        Label w = new Label("Welcome, " + u.getFullName());
        w.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        Label c = new Label("Currency: " + u.getCurrency());
        top.getChildren().addAll(w, new Separator(), c);

        root.setLeft(menu);
        root.setTop(top);
        root.setCenter(getDashboard());
        return new Scene(root, 1000, 700);
    }

    private VBox getDashboard() {
        VBox v = new VBox(20);
        v.setPadding(new Insets(20));

        Label title = new Label("Dashboard");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        TransactionDAO dao = new TransactionDAO();
        double inc = dao.getTotalIncome(u.getId());
        double exp = dao.getTotalExpenses(u.getId());
        double bal = inc - exp;

        VBox c1 = new VBox(10);
        c1.setPadding(new Insets(20));
        c1.setAlignment(Pos.CENTER);
        c1.setStyle("-fx-background-color: white; -fx-border-color: #27ae60; -fx-border-width: 2; -fx-border-radius: 5;");
        c1.setPrefWidth(250);
        Label t1 = new Label("Income");
        t1.setStyle("-fx-font-size: 14px; -fx-text-fill: gray;");
        Label v1 = new Label(u.getFormattedCurrency(inc));
        v1.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #27ae60;");
        c1.getChildren().addAll(t1, v1);

        VBox c2 = new VBox(10);
        c2.setPadding(new Insets(20));
        c2.setAlignment(Pos.CENTER);
        c2.setStyle("-fx-background-color: white; -fx-border-color: #e74c3c; -fx-border-width: 2; -fx-border-radius: 5;");
        c2.setPrefWidth(250);
        Label t2 = new Label("Expenses");
        t2.setStyle("-fx-font-size: 14px; -fx-text-fill: gray;");
        Label v2 = new Label(u.getFormattedCurrency(exp));
        v2.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #e74c3c;");
        c2.getChildren().addAll(t2, v2);

        VBox c3 = new VBox(10);
        c3.setPadding(new Insets(20));
        c3.setAlignment(Pos.CENTER);
        c3.setStyle("-fx-background-color: white; -fx-border-color: #3498db; -fx-border-width: 2; -fx-border-radius: 5;");
        c3.setPrefWidth(250);
        Label t3 = new Label("Balance");
        t3.setStyle("-fx-font-size: 14px; -fx-text-fill: gray;");
        Label v3 = new Label(u.getFormattedCurrency(bal));
        v3.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #3498db;");
        c3.getChildren().addAll(t3, v3);

        HBox cards = new HBox(15);
        cards.setAlignment(Pos.CENTER);
        cards.getChildren().addAll(c1, c2, c3);

        v.getChildren().addAll(title, cards);
        return v;
    }
}
