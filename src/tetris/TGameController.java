package tetris;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TGameController {
    private TScore actualScore;
    private TBoard actualBoard;
    private TBlock actualTetromino;
    private TBlock nextTetromino;
    private final THighScore scores;
    private TBlockRandomizer blockChooser;
    private final BorderPane root;
    private Group menu;
    private Group newGameLauncher;
    private final Group topTenScoresScreen;
    private final Group bestScoreOfPlayerScreen;
    private final Group endGameScreen;
    private final Stage primaryStage;
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
    private boolean dropStatus;
    private final TLayoutParts layoutParts;

    public TGameController(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("Tetris");
        layoutParts = new TLayoutParts();
        root = new BorderPane();
        scores = new THighScore();
        topTenScoresScreen = new Group();
        bestScoreOfPlayerScreen = new Group();
        endGameScreen = new Group();
        primaryStage.setResizable(false);
        root.setId("pane");

        Scene scene = layoutParts.setScene(root);
        primaryStage.setScene(scene);
        createMenu();
        createNewGameLauncher();

        showMenu();
    }


    /*
     * --------------------------------------------------------------------------
     * 							Game Logic
     * --------------------------------------------------------------------------
     */
    public void setGame() {
        clearStateOfTheGame();

        actualBoard = new TBoard();
        blockChooser = new TBlockRandomizer();
        actualTetromino = new TBlock(blockChooser.chooseBlock());
        nextTetromino = new TBlock(blockChooser.chooseBlock());
        boardBlocks = new Group();
        actualTetrominoBlocks = new Group();
        nextTetrominoBlocks = new Group();
        labels = new Group();
        createGameScreen();

        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, controls);
        primaryStage.addEventHandler(KeyEvent.KEY_RELEASED, softDropResume);
    }

    public void playTheGame() {
        setGame();

        root.getChildren().addAll(boardBlocks, labels, actualTetrominoBlocks, nextTetrominoBlocks);

        TetrominoFall = new Timeline(new KeyFrame(Duration.seconds(getFallingSpeed()), event -> moveTetrominoDown()));
        TetrominoFall.setCycleCount(Timeline.INDEFINITE);
        refreshBoard();
        TetrominoFall.play();
        dropStatus = true;
    }

    private void refreshTetrominoFalling() {
        TetrominoFall.stop();
        TetrominoFall = new Timeline(new KeyFrame(Duration.seconds(getFallingSpeed()), event -> moveTetrominoDown()));
        TetrominoFall.setCycleCount(Timeline.INDEFINITE);
        TetrominoFall.play();
    }

    private void endTheGame() {
        TetrominoFall.stop();
        primaryStage.removeEventHandler(KeyEvent.KEY_PRESSED, controls);
        showMenu();
        createEndGameScreen();
        showEndGameScreen();
        scores.addScore(actualScore);
    }

    private void clearStateOfTheGame() {
        root.getChildren().removeAll(boardBlocks, labels, actualTetrominoBlocks, nextTetrominoBlocks);
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
        dropStatus = false;
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
    void hardDrop() {
        while (moveTetrominoDown()) {
            actualScore.addHardDropScore();
        }
    }

    boolean moveTetrominoDown() {
        if (dropCollisionDetection()) {
            addTetrominoToBoard();
            return false;
        } else {
            actualTetromino.topLeftRow++;
            renderActualTetromino();
            return true;
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
     * 						Layout and Rendering Tetrominoes
     * --------------------------------------------------------------------------
     */
    private void createMenu() {
        menu = new Group();
        layoutParts.drawBackground(menu, 50, 220, 500, 300);
        Button playButton = layoutParts.drawButton(menu, 200, 250, "PLAY");
        Button top10Button = layoutParts.drawButton(menu, 200, 310, "TOP 10 Scores");
        Button highScoresButton = layoutParts.drawButton(menu, 200, 370, "HIGHSCORE OF EACH PLAYER");
        Button exitButton = layoutParts.drawButton(menu, 200, 430, "EXIT");
        playButton.addEventHandler(ActionEvent.ACTION, playGameEvent);
        top10Button.addEventHandler(ActionEvent.ACTION, top10Event);
        highScoresButton.addEventHandler(ActionEvent.ACTION, highScoresEvent);
        exitButton.addEventHandler(ActionEvent.ACTION, exitEvent);
    }

    private void createEndGameScreen() {
        endGameScreen.getChildren().clear();
        layoutParts.drawBackground(endGameScreen, 50, 10, 500, 200);
        layoutParts.setBigLabel(endGameScreen, 210, 50, "GAME OVER");
        layoutParts.setBigLabel(endGameScreen, 100, 100, "NO PLACE FOR NEXT \nTETROMINO");
        layoutParts.setBigLabel(endGameScreen, 100, 200, "YOUR SCORE: " + actualScore.getScore().toString());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void createNewGameLauncher() {
        newGameLauncher = new Group();
        layoutParts.drawBackground(newGameLauncher, 50, 220, 500, 400);
        layoutParts.setSmallLabel(newGameLauncher, 200, 260, "YOUR NAME");
        playerNameInput = layoutParts.drawTextField(newGameLauncher, 200, 280);
        difficultySlider = layoutParts.drawSlider(newGameLauncher, 200, 350, 1, 10);
        layoutParts.setSmallLabel(newGameLauncher, 200, 380, "DIFFICULTY LEVEL:");
        difficultyChooseLabel = layoutParts.setSmallLabel(newGameLauncher, 320, 380, "1");

        difficultySlider.valueProperty().addListener((ChangeListener) (arg0, arg1, arg2) -> difficultyChooseLabel.textProperty().setValue(
                String.valueOf((int) difficultySlider.getValue())));

        Button playButton = layoutParts.drawButton(newGameLauncher, 200, 410, "PLAY");
        playButton.addEventHandler(ActionEvent.ACTION, launchGameEvent);
        Button backButton = layoutParts.drawButton(newGameLauncher, 200, 460, "BACK TO MENU");
        backButton.addEventHandler(ActionEvent.ACTION, backToMenuEvent);
        layoutParts.setSmallLabel(newGameLauncher, 200, 520, "CONTROLS\nUP-ROTATE\nLEFT/RIGHT-MOVE TO SIDE\nDOWN-SOFTDROP\nSPACE-HARDDROP");
    }

    private void createGameScreen() {
        scoreStatus = layoutParts.setBigLabel(labels, 350, 130, "");
        difficultyStatus = layoutParts.setBigLabel(labels, 350, 290, "");
        layoutParts.setBigLabel(labels, 350, 50, actualScore.getPlayerName());
        layoutParts.setBigLabel(labels, 350, 100, "SCORE");
        layoutParts.setBigLabel(labels, 350, 210, "DIFFICULTY\nLEVEL");
        layoutParts.setBigLabel(labels, 350, 370, "NEXT\nTETROMINO");
    }

    private void createTopTenScoresScreen() {
        topTenScoresScreen.getChildren().clear();
        layoutParts.drawBackground(topTenScoresScreen, 50, 220, 500, 300);
        layoutParts.setBigLabel(topTenScoresScreen, 180, 260, "TOP 10 SCORES!!!");
        layoutParts.setSmallLabel(topTenScoresScreen, 200, 280, scores.topScoresToString());
        Button backButton = layoutParts.drawButton(topTenScoresScreen, 200, 440, "BACK TO MENU");
        backButton.addEventHandler(ActionEvent.ACTION, backToMenuEvent);
    }

    private void createBestScoreOfPlayerScreen() {
        bestScoreOfPlayerScreen.getChildren().clear();
        layoutParts.drawBackground(bestScoreOfPlayerScreen, 50, 80, 500, 500);
        layoutParts.setBigLabel(bestScoreOfPlayerScreen, 130, 120, "BEST SCORE OF PLAYERS");
        Text scoresText = layoutParts.setSmallLabel(bestScoreOfPlayerScreen, 200, 140, scores.bestScoreOfPlayerToString());
        bestScoreOfPlayerScreen.getChildren().remove(scoresText);
        ScrollPane list = new ScrollPane(scoresText);
        list.setLayoutX(150);
        list.setLayoutY(150);
        list.setPrefSize(300, 300);
        bestScoreOfPlayerScreen.getChildren().add(list);
        Button backButton = layoutParts.drawButton(bestScoreOfPlayerScreen, 200, 500, "BACK TO MENU");
        backButton.addEventHandler(ActionEvent.ACTION, backToMenuEvent);
    }

    private void showTopTenScoresScreen() {
        createTopTenScoresScreen();
        root.getChildren().add(topTenScoresScreen);
    }

    private void showBestScoreOfPlayerScreen() {
        createBestScoreOfPlayerScreen();
        root.getChildren().add(bestScoreOfPlayerScreen);
    }

    private void hideTopTenScoresScreen() {
        root.getChildren().remove(topTenScoresScreen);
    }

    private void hideBestScoreOfPlayerScreen() {
        root.getChildren().remove(bestScoreOfPlayerScreen);
    }

    private void showMenu() {
        root.getChildren().add(menu);
    }

    private void hideMenu() {
        root.getChildren().remove(menu);
        hideEndGameScreen();
    }

    private void showNewGameLauncher() {
        root.getChildren().add(newGameLauncher);
    }

    private void hideNewGameLauncher() {
        root.getChildren().remove(newGameLauncher);
    }

    private void showEndGameScreen() {
        root.getChildren().add(endGameScreen);
    }

    private void hideEndGameScreen() {
        root.getChildren().remove(endGameScreen);
    }

    private void refreshBoard() {
        actualScore.addLinesClearScore(actualBoard.findAndClearFullLines());
        refreshStatusTexts();
        renderBoard();
        refreshTetrominoFalling();
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

    EventHandler<ActionEvent> playGameEvent = event -> {
        hideMenu();
        showNewGameLauncher();
    };

    EventHandler<ActionEvent> launchGameEvent = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            if (playerNameInput.getText().trim().length() == 0) {
                layoutParts.setSmallLabel(newGameLauncher, 200, 320, "Your name can't be empty");

            } else {
                actualScore = new TScore((int) difficultySlider.getValue(), playerNameInput.getText().trim());
                hideNewGameLauncher();
                playTheGame();
            }
        }
    };

    EventHandler<ActionEvent> backToMenuEvent = event -> {
        hideNewGameLauncher();
        hideTopTenScoresScreen();
        hideBestScoreOfPlayerScreen();
        showMenu();
    };

    EventHandler<ActionEvent> top10Event = event -> {
        hideMenu();
        showTopTenScoresScreen();
    };

    EventHandler<ActionEvent> highScoresEvent = event -> {
        hideMenu();
        showBestScoreOfPlayerScreen();
    };

    EventHandler<ActionEvent> exitEvent = event -> System.exit(0);

    EventHandler<KeyEvent> softDropResume = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
            if (event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.SPACE)
                dropStatus = true;
        }
    };

    private final EventHandler<KeyEvent> controls = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
            if (event.getCode() == KeyCode.RIGHT) {
                moveTetrominoToTheSide("right");

            } else if (event.getCode() == KeyCode.DOWN) {
                if (dropStatus) {
                    moveTetrominoDown();
                    actualScore.addSoftDropScore();
                    refreshStatusTexts();
                }

            } else if (event.getCode() == KeyCode.LEFT) {
                moveTetrominoToTheSide("left");

            } else if (event.getCode() == KeyCode.UP) {
                rotateTetromino();

            } else if (event.getCode() == KeyCode.SPACE) {
                if (dropStatus) {
                    hardDrop();
                }

            }
        }
    };
}