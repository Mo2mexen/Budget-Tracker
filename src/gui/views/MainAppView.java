package gui.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import models.User;

public class MainAppView {
    private Stage stage;
    private User user;
    private BorderPane rootPane;
    private Button accountsButton;
    private Button categoriesButton;
    private Button transactionsButton;
    private Button budgetsButton;
    private Button goalsButton;
    private Button reportsButton;
    private Button profileButton;
    private Button logoutButton;

    public MainAppView(Stage stage, User user) {
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
        // Default view will be set by controller

        return new Scene(rootPane, 1000, 700);
    }

    private VBox createMenuPanel() {
        VBox menuContainer = new VBox(10);
        menuContainer.setPadding(new Insets(20));
        menuContainer.setPrefWidth(200);

        Label menuTitleLabel = new Label("Menu");
        menuTitleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        menuContainer.getChildren().addAll(menuTitleLabel, new Separator());

        // Accounts button
        accountsButton = new Button("Accounts");
        accountsButton.setPrefWidth(180);
        accountsButton.setAlignment(Pos.CENTER_LEFT);

        // Categories button
        categoriesButton = new Button("Categories");
        categoriesButton.setPrefWidth(180);
        categoriesButton.setAlignment(Pos.CENTER_LEFT);

        // Transactions button
        transactionsButton = new Button("Transactions");
        transactionsButton.setPrefWidth(180);
        transactionsButton.setAlignment(Pos.CENTER_LEFT);

        // Budgets button
        budgetsButton = new Button("Budgets");
        budgetsButton.setPrefWidth(180);
        budgetsButton.setAlignment(Pos.CENTER_LEFT);

        // Goals button
        goalsButton = new Button("Goals");
        goalsButton.setPrefWidth(180);
        goalsButton.setAlignment(Pos.CENTER_LEFT);

        // Reports button
        reportsButton = new Button("Reports");
        reportsButton.setPrefWidth(180);
        reportsButton.setAlignment(Pos.CENTER_LEFT);

        // Profile button
        profileButton = new Button("Profile");
        profileButton.setPrefWidth(180);
        profileButton.setAlignment(Pos.CENTER_LEFT);

        // Spacer
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        // Logout button
        logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        logoutButton.setPrefWidth(180);
        logoutButton.setAlignment(Pos.CENTER_LEFT);

        menuContainer.getChildren().addAll(
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

    // Getters for controller
    public Stage getStage() {
        return stage;
    }

    public User getUser() {
        return user;
    }

    public BorderPane getRootPane() {
        return rootPane;
    }

    public Button getAccountsButton() {
        return accountsButton;
    }

    public Button getCategoriesButton() {
        return categoriesButton;
    }

    public Button getTransactionsButton() {
        return transactionsButton;
    }

    public Button getBudgetsButton() {
        return budgetsButton;
    }

    public Button getGoalsButton() {
        return goalsButton;
    }

    public Button getReportsButton() {
        return reportsButton;
    }

    public Button getProfileButton() {
        return profileButton;
    }

    public Button getLogoutButton() {
        return logoutButton;
    }
}
