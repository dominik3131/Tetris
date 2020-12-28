package tetris;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class TLayoutParts {
    private final int blockSize = 32;

    public Scene setScene(BorderPane root) {
        root.setId("pane");
        Scene scene = new Scene(root, 600, 640);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        return scene;
    }

    public void drawBackground(Group backgroundGroup, int x, int y, int width, int height) {
        Rectangle r = new Rectangle(x, y, width, height);
        r.setFill(Color.BEIGE);
        backgroundGroup.getChildren().add(r);
    }

    public void drawBlock(Group termino, int col, int row, int color) {
        Rectangle r = new Rectangle(col * blockSize, row * blockSize, blockSize, blockSize);
        r.setFill(chooseColor(color));
        r.setStroke(Color.CORNSILK);
        r.setStrokeWidth(2);
        termino.getChildren().add(r);
    }

    public TextField drawTextField(Group textFieldGroup, int x, int y) {
        TextField textField = new TextField();
        textField.setLayoutX(x);
        textField.setLayoutY(y);
        textField.setPrefWidth(200);
        textFieldGroup.getChildren().add(textField);
        return textField;
    }

    public Slider drawSlider(Group sliderGroup, int x, int y, int min, int max) {
        Slider slider = new Slider();
        slider.setMin(min);
        slider.setMax(max);
        slider.setValue(1);
        slider.setShowTickLabels(false);
        slider.setLayoutX(x);
        slider.setLayoutY(y);
        slider.setMinorTickCount(1);
        slider.setMajorTickUnit(1);
        slider.setPrefSize(200, 10);
        slider.setBlockIncrement(1);
        slider.valueProperty().addListener((obs, oldval, newVal) ->
                slider.setValue(newVal.intValue()));
        sliderGroup.getChildren().add(slider);
        return slider;
    }

    public Button drawButton(Group buttonGroup, int x, int y, String buttonText) {
        Button button = new Button();
        button.setText(buttonText);
        button.setLayoutX(x);
        button.setLayoutY(y);
        button.setMinSize(200, 30);
        buttonGroup.getChildren().add(button);
        return button;
    }

    public Text setSmallLabel(Group labels, int x, int y, String phrase) {
        Text label = new Text();
        label.setX(x);
        label.setY(y);
        label.setText(phrase);

        labels.getChildren().add(label);
        return label;
    }

    public Text setBigLabel(Group labels, int x, int y, String phrase) {
        Text label = new Text();
        label.setX(x);
        label.setY(y);
        label.setText(phrase);
        label.setStrokeWidth(1);
        label.setStroke(Color.RED);
        label.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 30));
        labels.getChildren().add(label);
        return label;
    }


    public Color chooseColor(int colorNumber) {

        switch (colorNumber) {
            case 1:
                return Color.CYAN;
            case 2:
                return Color.BLUE;
            case 3:
                return Color.ORANGE;
            case 4:
                return Color.YELLOW;
            case 5:
                return Color.LIMEGREEN;
            case 6:
                return Color.PURPLE;
            case 7:
                return Color.RED;
            default:
                return Color.WHITE;
        }
    }
}