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
        topScores = new ArrayList<>(numberOfHighScores);
        playerBestScores = new LinkedList<>();
        readSavedScores();

    }

    public void addScore(TScore score) {
        addToHighScores(score);
        addToBestScoreOfPlayer(score);
    }

    @SuppressWarnings("unchecked")
    private void readSavedScores() {
        try {
            File listFile = new File(topScoresFile);
            if (!listFile.exists()) {
                listFile.createNewFile();

            } else {
                FileInputStream file = new FileInputStream(topScoresFile);
                ObjectInputStream inputStream = new ObjectInputStream(file);
                topScores = (List<TScore>) inputStream.readObject();
                file.close();
                inputStream.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR TOP10");
        }
        try {
            File listFile = new File(bestScoreOfPlayerFile);
            if (!listFile.exists()) {
                listFile.createNewFile();

            } else {
                FileInputStream file = new FileInputStream(bestScoreOfPlayerFile);
                ObjectInputStream inputStream = new ObjectInputStream(file);
                playerBestScores = (List<TScore>) inputStream.readObject();
                file.close();
                inputStream.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR TOP10");
        }
    }

    private void saveScores(String fileName, List<TScore> scoreList) {
        try {
            FileOutputStream file = new FileOutputStream(fileName);
            ObjectOutputStream outputStream = new ObjectOutputStream(file);
            outputStream.writeObject(scoreList);
            file.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error saving file");
        }
    }

    private void addToHighScores(TScore score) {
        if (topScores.size() == numberOfHighScores) {
            int lowestScore = this.topScores.get(this.numberOfHighScores - 1).getScore();
            if (lowestScore < score.getScore()) {
                topScores.remove(numberOfHighScores - 1);
                topScores.add(score);
                Collections.sort(topScores);
            }

        } else {
            topScores.add(score);
            Collections.sort(topScores);
        }

        saveScores(topScoresFile, topScores);
    }

    private void addToBestScoreOfPlayer(TScore score) {
        TScore tempScore;

        if (playerBestScores.contains(score)) {
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

        } else {
            playerBestScores.add(score);
        }

        saveScores(bestScoreOfPlayerFile, playerBestScores);
    }

    public String topScoresToString() {
        StringBuilder scores;
        if (topScores.size() == 0) {
            scores = new StringBuilder("There are no saved scores yet");
        } else {
            scores = new StringBuilder("1." + topScores.get(0) + "\n");
            for (int i = 1; i < topScores.size(); i++) {
                scores.append(i).append(1).append(".").append(topScores.get(i)).append("\n");
            }
        }
        return scores.toString();
    }

    public String bestScoreOfPlayerToString() {
        StringBuilder scores;
        if (playerBestScores.size() == 0) {
            scores = new StringBuilder("There are no saved scores yet");

        } else {
            scores = new StringBuilder(playerBestScores.get(0) + "\n");
            for (int i = 1; i < playerBestScores.size(); i++) {
                scores.append(playerBestScores.get(i)).append("\n");
            }
        }
        return scores.toString();
    }
}