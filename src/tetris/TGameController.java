package tetris;

import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class TGameController {
    private TScore actualScore;
    private TBoard actualBoard;
    private TBlock actualTetromino;
    private TBlock nextTetromino;
    private THighScore scores;
    private TBlockRandomizer blockChooser;
    private BorderPane root;
    private Group menu;
    private Group newGameLauncher;
    private Stage primaryStage;
    private Group boardBlocks;
    private Group actualTetrominoBlocks;
    private Group nextTetrominoBlocks;
    private Group labels;
    private Slider difficultySlider;
    private TextField playerNameInput;
    private Text difficultyChooseLabel;
    private Text scoreStatus;
    private Text difficultyStatus;
    private Timeline TetrominoFall;
    private boolean softDropStatus;
    private TLayoutParts layoutParts;

    public TGameController(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("Tetris");
        layoutParts = new TLayoutParts();
        root = new BorderPane();


        primaryStage.setResizable(false);
        root.setId("pane");


        Scene scene = layoutParts.setScene(root);
        primaryStage.setScene(scene);
        createMenu();
        createNewGameLauncher();
        showMenu();
        softDropStatus = true;

    }


    /*
     * --------------------------------------------------------------------------
     * 							Game Logic
     * --------------------------------------------------------------------------
     */
    public void setGame() {
        scores = new THighScore();
        actualBoard = new TBoard();
        blockChooser = new TBlockRandomizer();
        actualTetromino = new TBlock(blockChooser.chooseBlock());
        nextTetromino = new TBlock(blockChooser.chooseBlock());
        boardBlocks = new Group();
        actualTetrominoBlocks = new Group();
        nextTetrominoBlocks = new Group();
        labels = new Group();
        createGameView();

        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, controls);
        primaryStage.addEventHandler(KeyEvent.KEY_RELEASED, softDropResume);
    }

    public void playTheGame() {
        setGame();
        root.getChildren().addAll(boardBlocks, labels, actualTetrominoBlocks, nextTetrominoBlocks);
        refreshBoard();
        TetrominoFall = new Timeline(new KeyFrame(Duration.seconds(getFallingSpeed()), new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                moveTetrominoDown();
            }
        }));
        TetrominoFall.setCycleCount(Timeline.INDEFINITE);
        TetrominoFall.play();
    }

    private void endTheGame() {
        TetrominoFall.stop();
        primaryStage.removeEventHandler(KeyEvent.KEY_PRESSED, controls);

        System.out.println("koniec");
    }

    private double getFallingSpeed() {
        return (11 - (double) actualScore.getDifficulty()) / 10;
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
        softDropStatus = false;
    }
    /*
     * --------------------------------------------------------------------------
     * 							Collision Detecting
     * --------------------------------------------------------------------------
     */

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

    private boolean rotateCollisionDetection() {
        for (int row = 0; row < actualTetromino.shapes[actualTetromino.potentialShape].length; row++) {
            for (int col = 0; col < actualTetromino.shapes[actualTetromino.potentialShape][row].length; col++) {
                if (actualTetromino.shapes[actualTetromino.potentialShape][row][col] != 0) {
                    if (col + actualTetromino.topLeftColumn < 0) {
                        return true;
                    } else if (col + actualTetromino.topLeftColumn >= actualBoard.landedBlocks[row].length) {
                        return true;
                    } else if (actualBoard.landedBlocks[row + actualTetromino.topLeftRow][col + actualTetromino.topLeftColumn] != 0) {
                        return true;
                    } else if (row + actualTetromino.topLeftRow + 1 >= actualBoard.landedBlocks.length) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isSpaceForNextTetromino() {
        for (int row = 0; row < actualTetromino.shapes[actualTetromino.actualShape].length; row++) {
            for (int col = 0; col < actualTetromino.shapes[actualTetromino.actualShape][row].length; col++) {
                if (actualTetromino.shapes[actualTetromino.actualShape][row][col] != 0) {
                    if (actualBoard.landedBlocks[row + actualTetromino.topLeftRow][col + actualTetromino.topLeftColumn] != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /*
     * --------------------------------------------------------------------------
     * 							Moving and Rotating
     * --------------------------------------------------------------------------
     */

    void moveTetrominoDown() {
        if (dropCollisionDetection()) {
            addTetrominoToBoard();
        } else {
            actualTetromino.topLeftRow++;
            renderActualTetromino();
        }
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

    private void rotateTetromino() {
        if (!rotateCollisionDetection()) {
            actualTetromino.rotateBlock();
        }
        renderActualTetromino();
    }

    /*
     * --------------------------------------------------------------------------
     * 						Layout and Rendering Tetrominos
     * --------------------------------------------------------------------------
     */
    private void createMenu() {
        menu = new Group();
        layoutParts.drawBackground(menu, 50, 220, 500, 300);
        Button playButton = layoutParts.drawButton(menu, 200, 250, "PLAY");
        Button top10Button = layoutParts.drawButton(menu, 200, 310, "TOP 10 Scores");
        Button highscoresButton = layoutParts.drawButton(menu, 200, 370, "HIGHSCORE OF EACH PLAYER");
        Button exitButton = layoutParts.drawButton(menu, 200, 430, "EXIT");
        playButton.addEventHandler(ActionEvent.ACTION, playGameEvent);
        top10Button.addEventHandler(ActionEvent.ACTION, top10Event);
        highscoresButton.addEventHandler(ActionEvent.ACTION, highscoresEvent);
        exitButton.addEventHandler(ActionEvent.ACTION, exitEvent);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void createNewGameLauncher() {
        newGameLauncher = new Group();
        layoutParts.drawBackground(newGameLauncher, 50, 220, 500, 300);
        layoutParts.setSmallLabel(newGameLauncher, 200, 260, "YOUR NAME");
        playerNameInput = layoutParts.drawTextField(newGameLauncher, 200, 280);
        difficultySlider = layoutParts.drawSlider(newGameLauncher, 200, 350, 1, 10);
        layoutParts.setSmallLabel(newGameLauncher, 200, 380, "DIFFICULTY LEVEL:");
        difficultyChooseLabel = layoutParts.setSmallLabel(newGameLauncher, 320, 380, "1");
        difficultySlider.valueProperty().addListener(new ChangeListener() {
            public void changed(ObservableValue arg0, Object arg1, Object arg2) {
                difficultyChooseLabel.textProperty().setValue(
                        String.valueOf((int) difficultySlider.getValue()));

            }
        });
        Button playButton = layoutParts.drawButton(newGameLauncher, 200, 430, "PLAY");
        playButton.addEventHandler(ActionEvent.ACTION, launchGameEvent);
    }

    private void createGameView() {

        scoreStatus = layoutParts.setBigLabel(labels, 350, 130, "");
        difficultyStatus = layoutParts.setBigLabel(labels, 350, 290, "");
        layoutParts.setBigLabel(labels, 350, 50, actualScore.getPlayerName());
        layoutParts.setBigLabel(labels, 350, 100, "SCORE");
        layoutParts.setBigLabel(labels, 350, 210, "DIFFICULTY\nLEVEL");
        layoutParts.setBigLabel(labels, 350, 370, "NEXT\nTETROMINO");
    }

    private void showMenu() {
        root.getChildren().add(menu);
    }

    private void hideMenu() {
        root.getChildren().remove(menu);
    }

    private void showNewGameLauncher() {
        root.getChildren().add(newGameLauncher);
    }

    private void hideNewGameLauncher() {
        root.getChildren().remove(newGameLauncher);
    }

    private void refreshBoard() {
        actualScore.addLinesClearScore(actualBoard.findAndClearFullLines());
        refreshStatusTexts();
        renderBoard();
        nextTetromino();
        if (isSpaceForNextTetromino()) {
            renderActualTetromino();
            renderNextTetromino();
        } else {
            endTheGame();
        }
    }

    private void refreshStatusTexts() {
        scoreStatus.setText(actualScore.getScore().toString());
        difficultyStatus.setText(actualScore.getDifficulty().toString());
    }

    private void renderBoard() {
        boardBlocks.getChildren().clear();
        for (int row = 0; row < actualBoard.heightOfBoard; row++) {
            for (int col = 0; col < actualBoard.widthOfBoard; col++) {
                if (actualBoard.landedBlocks[row][col] != 0) {
                    layoutParts.drawBlock(boardBlocks, col, row, actualBoard.landedBlocks[row][col]);
                }
            }
        }
    }

    private void renderActualTetromino() {
        actualTetrominoBlocks.getChildren().clear();
        for (int row = 0; row < actualTetromino.shapes[actualTetromino.actualShape].length; row++) {
            for (int col = 0; col < actualTetromino.shapes[actualTetromino.actualShape][row].length; col++) {
                if (actualTetromino.shapes[actualTetromino.actualShape][row][col] != 0) {
                    layoutParts.drawBlock(actualTetrominoBlocks, col + actualTetromino.topLeftColumn,
                            row + actualTetromino.topLeftRow, actualTetromino.shapes[actualTetromino.actualShape][row][col]);
                }
            }
        }
    }

    private void renderNextTetromino() {
        nextTetrominoBlocks.getChildren().clear();
        for (int row = 0; row < nextTetromino.shapes[nextTetromino.actualShape].length; row++) {
            for (int col = 0; col < nextTetromino.shapes[nextTetromino.actualShape][row].length; col++) {
                if (nextTetromino.shapes[nextTetromino.actualShape][row][col] != 0) {
                    //Rectangle r = new Rectangle(col*blockSize+350,row*blockSize+400,blockSize,blockSize);
                    //r.setFill(layoutParts.chooseColor(nextTetromino.shapes[nextTetromino.actualShape][row][col]));
                    //nextTetrominoBlocks.getChildren().add(r);
                    layoutParts.drawBlock(nextTetrominoBlocks, col + 12, row + 14,
                            nextTetromino.shapes[nextTetromino.actualShape][row][col]);
                }
            }
        }
    }



    /*
     * --------------------------------------------------------------------------
     * 						Controls Events
     * --------------------------------------------------------------------------
     */

    EventHandler<ActionEvent> playGameEvent = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            System.out.println("play");
            hideMenu();
            showNewGameLauncher();
        }
    };
    EventHandler<ActionEvent> launchGameEvent = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            System.out.println("play");
            if (playerNameInput.getText().isEmpty()) {

            } else {
                actualScore = new TScore((int) difficultySlider.getValue(), playerNameInput.getText());
                hideNewGameLauncher();
                playTheGame();
            }

        }
    };
    EventHandler<ActionEvent> top10Event = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            System.out.println("top10");
            hideMenu();
            //showTop10();
        }
    };
    EventHandler<ActionEvent> highscoresEvent = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            System.out.println("highscore");
            hideMenu();
            // showHighscores();
        }
    };
    EventHandler<ActionEvent> exitEvent = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            System.exit(0);
        }
    };

    EventHandler<KeyEvent> softDropResume = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
            if (event.getCode() == KeyCode.DOWN)
                softDropStatus = true;
        }
    };

    private final EventHandler<KeyEvent> controls = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
            if (event.getCode() == KeyCode.RIGHT) {
                moveTetrominoToTheSide("right");
            } else if (event.getCode() == KeyCode.DOWN) {
                if (softDropStatus) {
                    moveTetrominoDown();
                    actualScore.addSoftDropScore();
                    refreshStatusTexts();
                }
            } else if (event.getCode() == KeyCode.LEFT) {
                moveTetrominoToTheSide("left");
            } else if (event.getCode() == KeyCode.UP) {
                rotateTetromino();
            }
        }
    };
}