package gui;

import javafx.application.Application;
import javafx.stage.Stage;
import gui.views.LoginView;
import gui.controllers.LoginController;

public class BudgetTrackerApp extends Application {
    @Override
    public void start(Stage stage) {
        stage.setTitle("Budget Tracker");
        LoginView loginView = new LoginView(stage);
        stage.setScene(loginView.createScene());
        new LoginController(loginView);
        stage.setMinWidth(900);
        stage.setMinHeight(650);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
