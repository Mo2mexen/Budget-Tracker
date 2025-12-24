package gui;

import javafx.application.Application;
import javafx.stage.Stage;

public class BudgetTrackerApp extends Application {
    @Override
    public void start(Stage stage) {
        stage.setTitle("Budget Tracker");
        stage.setScene(new LoginView(stage).createScene());
        stage.setMinWidth(900);
        stage.setMinHeight(650);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
