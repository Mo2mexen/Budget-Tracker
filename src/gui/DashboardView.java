package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import models.User;

public class DashboardView {
    private Stage stage;
    private User user;
    private BorderPane rootPane;

    public DashboardView(Stage stage, User user) {
        this.stage = stage;
        this.user = user;
    }

    public Scene createScene() {
        rootPane = new BorderPane();

        // Left menu
        VBox menuContainer = createMenuPanel();

        // Top header
        HBox topPanel = createTopPanel();

        rootPane.setLeft(menuContainer);
        rootPane.setTop(topPanel);
        rootPane.setCenter(createMainContent());

        return new Scene(rootPane, 1000, 700);
    }

    private VBox createMenuPanel() {
        VBox menuContainer = new VBox(10);
        menuContainer.setPadding(new Insets(20));
        menuContainer.setPrefWidth(200);

        Label menuTitleLabel = new Label("Menu");
        menuTitleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        menuContainer.getChildren().addAll(menuTitleLabel, new Separator());

        // Dashboard button
        Button dashboardButton = new Button("Dashboard");
        dashboardButton.setPrefWidth(180);
        dashboardButton.setAlignment(Pos.CENTER_LEFT);
        dashboardButton.setOnAction(event -> rootPane.setCenter(createMainContent()));

        // Accounts button
        Button accountsButton = new Button("Accounts");
        accountsButton.setPrefWidth(180);
        accountsButton.setAlignment(Pos.CENTER_LEFT);
        accountsButton.setOnAction(event -> rootPane.setCenter(new AccountsView(user).getView()));

        // Categories button
        Button categoriesButton = new Button("Categories");
        categoriesButton.setPrefWidth(180);
        categoriesButton.setAlignment(Pos.CENTER_LEFT);
        categoriesButton.setOnAction(event -> rootPane.setCenter(new CategoriesView(user).getView()));

        // Transactions button
        Button transactionsButton = new Button("Transactions");
        transactionsButton.setPrefWidth(180);
        transactionsButton.setAlignment(Pos.CENTER_LEFT);
        transactionsButton.setOnAction(event -> rootPane.setCenter(new TransactionsView(user).getView()));

        // Budgets button
        Button budgetsButton = new Button("Budgets");
        budgetsButton.setPrefWidth(180);
        budgetsButton.setAlignment(Pos.CENTER_LEFT);
        budgetsButton.setOnAction(event -> rootPane.setCenter(new BudgetsView(user).getView()));

        // Goals button
        Button goalsButton = new Button("Goals");
        goalsButton.setPrefWidth(180);
        goalsButton.setAlignment(Pos.CENTER_LEFT);
        goalsButton.setOnAction(event -> rootPane.setCenter(new GoalsView(user).getView()));

        // Reports button
        Button reportsButton = new Button("Reports");
        reportsButton.setPrefWidth(180);
        reportsButton.setAlignment(Pos.CENTER_LEFT);
        reportsButton.setOnAction(event -> rootPane.setCenter(new ReportsView(user).getView()));

        // Profile button
        Button profileButton = new Button("Profile");
        profileButton.setPrefWidth(180);
        profileButton.setAlignment(Pos.CENTER_LEFT);
        profileButton.setOnAction(event -> rootPane.setCenter(new ProfileView(user).getView()));

        // Spacer
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        // Logout button
        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        logoutButton.setPrefWidth(180);
        logoutButton.setAlignment(Pos.CENTER_LEFT);
        logoutButton.setOnAction(event -> handleLogout());

        menuContainer.getChildren().addAll(
            dashboardButton,
            accountsButton,
            categoriesButton,
            transactionsButton,
            budgetsButton,
            goalsButton,
            reportsButton,
            profileButton,
            spacer,
            logoutButton
        );

        return menuContainer;
    }

    private HBox createTopPanel() {
        HBox topPanel = new HBox(20);
        topPanel.setPadding(new Insets(15));
        topPanel.setAlignment(Pos.CENTER_RIGHT);

        Label welcomeLabel = new Label("Welcome, " + user.getFullName());
        Label currencyLabel = new Label("Currency: " + user.getCurrency());

        topPanel.getChildren().addAll(welcomeLabel, new Separator(), currencyLabel);

        return topPanel;
    }

    private VBox createMainContent() {
        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(20));

        Label titleLabel = new Label("Dashboard");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label simpleWelcomeLabel = new Label("Welcome to your Budget Tracker");

        mainContent.getChildren().addAll(titleLabel, simpleWelcomeLabel);

        return mainContent;
    }

    private void handleLogout() {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION, "Logout?", ButtonType.OK, ButtonType.CANCEL);
        if (confirmDialog.showAndWait().get() == ButtonType.OK) {
            stage.setScene(new LoginView(stage).createScene());
        }
    }
}
