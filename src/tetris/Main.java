package tetris;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            primaryStage.setTitle("Tetris");
            BorderPane root = new BorderPane();
            Scene scene = new Scene(root, 500, 640);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        TBoard a = new TBoard();
        a.fillForTest();
        a.showBoard();
        a.clearLine(6);
        a.showBoard();
    }
}