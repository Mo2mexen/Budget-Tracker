package gui.controllers;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import gui.views.*;
import java.util.Optional;

public class MainAppController {
    private MainAppView view;

    public MainAppController(MainAppView view) {
        this.view = view;
        attachEventHandlers();
        initializeDefaultView();
    }

    private void initializeDefaultView() {
        ProfileView profileView = new ProfileView(view.getUser());
        view.getRootPane().setCenter(profileView.getView());
        new ProfileController(profileView);

        view.getProfileButton().requestFocus();
        view.getLogoutButton().setFocusTraversable(false);
    }

    private void attachEventHandlers() {
        view.getAccountsButton().setOnAction(event -> showAccounts());
        view.getCategoriesButton().setOnAction(event -> showCategories());
        view.getTransactionsButton().setOnAction(event -> showTransactions());
        view.getBudgetsButton().setOnAction(event -> showBudgets());
        view.getGoalsButton().setOnAction(event -> showGoals());
        view.getReportsButton().setOnAction(event -> showReports());
        view.getProfileButton().setOnAction(event -> showProfile());
        view.getLogoutButton().setOnAction(event -> handleLogout());
    }

    private void showAccounts() {
        AccountsView accountsView = new AccountsView(view.getUser());
        view.getRootPane().setCenter(accountsView.getView());
        new AccountsController(accountsView);
    }

    private void showCategories() {
        CategoriesView categoriesView = new CategoriesView(view.getUser());
        view.getRootPane().setCenter(categoriesView.getView());
        new CategoriesController(categoriesView);
    }

    private void showTransactions() {
        TransactionsView transactionsView = new TransactionsView(view.getUser());
        view.getRootPane().setCenter(transactionsView.getView());
        new TransactionsController(transactionsView);
    }

    private void showBudgets() {
        BudgetsView budgetsView = new BudgetsView(view.getUser());
        view.getRootPane().setCenter(budgetsView.getView());
        new BudgetsController(budgetsView);
    }

    private void showGoals() {
        GoalsView goalsView = new GoalsView(view.getUser());
        view.getRootPane().setCenter(goalsView.getView());
        new GoalsController(goalsView);
    }

    private void showReports() {
        ReportsView reportsView = new ReportsView(view.getUser());
        view.getRootPane().setCenter(reportsView.getView());
        new ReportsController(reportsView);
    }

    private void showProfile() {
        ProfileView profileView = new ProfileView(view.getUser());
        view.getRootPane().setCenter(profileView.getView());
        new ProfileController(profileView);
    }

    private void handleLogout() {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION, "Logout?", ButtonType.OK, ButtonType.CANCEL);
        Optional<ButtonType> result = confirmDialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            LoginView loginView = new LoginView(view.getStage());
            view.getStage().setScene(loginView.createScene());
            new LoginController(loginView);
        }
    }
}
