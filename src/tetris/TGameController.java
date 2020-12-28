package tetris;

import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.event.EventHandler;

public class TGameController {
    private final int blockSize = 32;
    TScore actualScore;
    TBoard actualBoard;
    TBlock actualTetromino;
    TBlock nextTetromino;
    THighScore scores;
    TBlockRandomizer blockChooser;
    Stage primaryStage;
    Group boardBlocks;
    Group tetrominoBlocks;

    public TGameController(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("Tetris");
        scores = new THighScore();
        actualScore = new TScore(1);
        actualBoard = new TBoard();
        blockChooser = new TBlockRandomizer();
        actualTetromino = new TBlock(blockChooser.chooseBlock());
        nextTetromino = new TBlock(blockChooser.chooseBlock());
        boardBlocks = new Group();
        tetrominoBlocks = new Group();

        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, controls);


        BorderPane root = new BorderPane();
        root.setId("pane");
        root.getChildren().add(boardBlocks);
        root.getChildren().add(tetrominoBlocks);

        renderActualTetromino();
        Scene scene = new Scene(root, 500, 640);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        primaryStage.setScene(scene);
    }

    public void playTheGame() {

    }

    private final EventHandler<KeyEvent> controls = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
            if (event.getCode() == KeyCode.RIGHT) {
                System.out.println("right");
                moveTetrominoToTheSide("right");
            } else if (event.getCode() == KeyCode.DOWN) {
                System.out.println("DOWN");
                moveTetrominoDown();
                actualScore.addSoftDropScore();
            } else if (event.getCode() == KeyCode.LEFT) {
                System.out.println("left");
                moveTetrominoToTheSide("left");
            } else if (event.getCode() == KeyCode.UP) {
                System.out.println("UP");
                actualTetromino.rotateBlock();
                renderActualTetromino();
            } else if (event.getCode() == KeyCode.N) {
                System.out.println("N");
                actualTetromino = nextTetromino;
                nextTetromino = new TBlock(blockChooser.chooseBlock());
                renderActualTetromino();
            } else if (event.getCode() == KeyCode.M) {
                System.out.println("M");
                System.out.println(actualScore.getScore());
            }
        }
    };

    private void renderBoard() {
        boardBlocks.getChildren().clear();
        for (int row = 0; row < actualBoard.heightOfBoard; row++) {
            for (int col = 0; col < actualBoard.widthOfBoard; col++) {
                if (actualBoard.landedBlocks[row][col] != 0) {
                    Rectangle r = new Rectangle(col * blockSize, row * blockSize, blockSize, blockSize);
                    r.setFill(chooseColor(actualBoard.landedBlocks[row][col]));
                    boardBlocks.getChildren().add(r);
                }
            }
        }
    }

    private void renderActualTetromino() {
        tetrominoBlocks.getChildren().clear();
        for (int row = 0; row < actualTetromino.shapes[actualTetromino.actualShape].length; row++) {
            for (int col = 0; col < actualTetromino.shapes[actualTetromino.actualShape][row].length; col++) {
                if (actualTetromino.shapes[actualTetromino.actualShape][row][col] != 0) {
                    Rectangle r = new Rectangle((col + actualTetromino.topLeftColumn) * blockSize, (row + actualTetromino.topLeftRow) * blockSize, blockSize, blockSize);
                    r.setFill(chooseColor(actualTetromino.shapes[actualTetromino.actualShape][row][col]));
                    tetrominoBlocks.getChildren().add(r);
                }
            }
        }
    }


    private void moveTetrominoDown() {
        if (dropCollisionDetection()) {
            addTetrominoToBoard();
        } else {
            actualTetromino.topLeftRow++;
            renderActualTetromino();
        }
    }

    private boolean dropCollisionDetection() {
        for (int row = 0; row < actualTetromino.shapes[actualTetromino.actualShape].length; row++) {
            for (int col = 0; col < actualTetromino.shapes[actualTetromino.actualShape][row].length; col++) {
                if (actualTetromino.shapes[actualTetromino.actualShape][row][col] != 0) {

                    if (row + actualTetromino.topLeftRow + 1 >= actualBoard.landedBlocks.length) {
                        return true;
                    } else if (actualBoard.landedBlocks[row + actualTetromino.topLeftRow + 1][col + actualTetromino.topLeftColumn] != 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    private void moveTetrominoToTheSide(String side) {
        int direction = 0;
        if (side.toLowerCase().equals("left")) {
            direction = -1;
        } else if (side.toLowerCase().equals("right")) {
            direction = 1;
        }
        if (!moveCollisionDetection(direction)) {
            actualTetromino.topLeftColumn += direction;
            renderActualTetromino();
        }
    }

    private boolean moveCollisionDetection(int direction) {
        for (int row = 0; row < actualTetromino.shapes[actualTetromino.actualShape].length; row++) {
            for (int col = 0; col < actualTetromino.shapes[actualTetromino.actualShape][row].length; col++) {
                if (actualTetromino.shapes[actualTetromino.actualShape][row][col] != 0) {
                    if (col + actualTetromino.topLeftColumn + direction < 0) {
                        return true;
                    } else if (col + actualTetromino.topLeftColumn + direction >= actualBoard.landedBlocks[row].length) {
                        return true;
                    } else if (actualBoard.landedBlocks[row + actualTetromino.topLeftRow][col + actualTetromino.topLeftColumn + direction] != 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void rotateTetromino() {
        if (!rotateCollisionDetection()) {
            actualTetromino.rotateBlock();
        }
        renderActualTetromino();
    }

    private boolean rotateCollisionDetection() {
        return false;
    }


    private void addTetrominoToBoard() {
        for (int row = 0; row < actualTetromino.shapes[actualTetromino.actualShape].length; row++) {
            for (int col = 0; col < actualTetromino.shapes[actualTetromino.actualShape][row].length; col++) {
                if (actualTetromino.shapes[actualTetromino.actualShape][row][col] != 0) {
                    actualBoard.landedBlocks[row + actualTetromino.topLeftRow][col + actualTetromino.topLeftColumn] = actualTetromino.shapes[actualTetromino.actualShape][row][col];
                }
            }
        }
        refreshBoard();
    }

    private void nextTetromino() {
        actualTetromino = nextTetromino;
        nextTetromino = new TBlock(blockChooser.chooseBlock());
    }

    private void refreshBoard() {
        actualScore.addLinesClearScore(actualBoard.findAndClearFullLines());
        renderBoard();
        nextTetromino();
        renderActualTetromino();
    }

    private Color chooseColor(int colorNumber) {

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