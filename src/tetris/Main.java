package tetris;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @SuppressWarnings("unused")
    @Override
    public void start(Stage primaryStage) {
        try {
            TGameController controller = new TGameController(primaryStage);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}