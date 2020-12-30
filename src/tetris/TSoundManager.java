package tetris;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;


public class TSoundManager {

    private MediaPlayer gameOverPlayer;
    private MediaPlayer softDropPlayer;
    private MediaPlayer gamePlayer;
    private MediaPlayer buttonPlayer;
    private MediaPlayer lineClearPlayer;
    private MediaPlayer blockPutPlayer;
    private MediaPlayer rotatePlayer;


    public TSoundManager() {
        gameOverPlayer = new MediaPlayer(new Media(new File("sounds\\game_over.wav").toURI().toString()));
        softDropPlayer = new MediaPlayer(new Media(new File("sounds\\soft_drop.wav").toURI().toString()));
        gamePlayer = new MediaPlayer(new Media(new File("sounds\\game.mp3").toURI().toString()));
        buttonPlayer = new MediaPlayer(new Media(new File("sounds\\button_click.wav").toURI().toString()));
        blockPutPlayer = new MediaPlayer(new Media(new File("sounds\\block_put.wav").toURI().toString()));
        rotatePlayer = new MediaPlayer(new Media(new File("sounds\\rotate.wav").toURI().toString()));
        lineClearPlayer = new MediaPlayer(new Media(new File("sounds\\line_clear.wav").toURI().toString()));
    }

    public void playGameOverSound() {
        gamePlayer.stop();
        gameOverPlayer.seek(Duration.ZERO);
        gameOverPlayer.play();
    }

    public void playSoftDropSound() {
        softDropPlayer.setVolume(0.2);
        softDropPlayer.seek(Duration.ZERO);
        softDropPlayer.play();
    }

    public void playGameSound() {
        gamePlayer.seek(Duration.ZERO);
        gamePlayer.play();
        gamePlayer.setVolume(0.1);
    }

    public void playButtonClickSound() {
        softDropPlayer.setVolume(0.2);
        buttonPlayer.seek(Duration.ZERO);
        buttonPlayer.play();
    }

    public void playBlockPutSound() {
        blockPutPlayer.seek(Duration.ZERO);
        blockPutPlayer.play();
    }

    public void playRotateSound() {
        rotatePlayer.seek(Duration.ZERO);
        rotatePlayer.play();
    }

    public void playLineClearSound() {
        lineClearPlayer.seek(Duration.ZERO);
        lineClearPlayer.play();
    }
}
