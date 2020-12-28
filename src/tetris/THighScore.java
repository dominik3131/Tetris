package tetris;

import java.util.*;
import java.io.*;

class THighScore {
    private String topScoresFile = "topScores.ser";
    private String bestScoreOfPlayerFile = "bestScoreOfPlayer.ser";
    public List<TScore> topScores;
    public List<TScore> playerBestScores;
    final int numberOfHighScores = 10;

    public THighScore() {
        this.topScores = new ArrayList<>(numberOfHighScores);
        this.playerBestScores = new LinkedList<>();
        readSavedScores(topScoresFile, topScores);
        readSavedScores(bestScoreOfPlayerFile, playerBestScores);
    }

    @SuppressWarnings("unchecked")
    private void readSavedScores(String fileName, List<TScore> scoreList) {
        try {
            File listFile = new File(fileName);
            if (!listFile.exists()) {
                listFile.createNewFile();
            } else {
                ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName));
                scoreList = (List<TScore>) inputStream.readObject();
                inputStream.close();
            }
        } catch (Exception e) {
            System.out.println("ERROR WITH CREATING FILE");
        }


    }

    private void saveScores(String fileName, List<TScore> scoreList) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName))) {
            outputStream.writeObject(scoreList);
            outputStream.close();
        } catch (IOException e) {
            System.out.println("error saving file");
        }
    }

    public void addScore(TScore score) {
        addToHighScores(score);
        addToBestScoreOfPlayer(score);
    }

    public void addToHighScores(TScore score) {
        int lowestScore = this.topScores.get(this.numberOfHighScores - 1).getScore();
        if (lowestScore < score.getScore()) {
            this.topScores.remove(this.numberOfHighScores - 1);
            this.topScores.add(score);
            Collections.sort(this.topScores);
            saveScores(topScoresFile, topScores);
        }
    }

    public void addToBestScoreOfPlayer(TScore score) {
        TScore tempScore;
        for (int i = 0; i < playerBestScores.size(); i++) {
            tempScore = playerBestScores.get(i);
            if (tempScore.getPlayerName().equals(score.getPlayerName())) {
                if (tempScore.getScore() < score.getScore()) {
                    playerBestScores.remove(i);
                    playerBestScores.add(i, score);
                    break;
                }
            }
        }
    }

}