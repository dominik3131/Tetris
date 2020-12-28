package tetris;

import java.io.Serializable;

class TScore implements Serializable, Comparable<TScore> {
    private static final long serialVersionUID = 1L;
    private Integer difficultyLevel;
    private int linesClearedAtActualDifficulty;
    private Integer score;
    private String playerName;

    public TScore(int level, String name) {
        if (level < 1 || level > 10)
            difficultyLevel = 1;
        else
            difficultyLevel = level;
        playerName = name;
        linesClearedAtActualDifficulty = 0;
        score = 0;
    }

    public void addLinesClearScore(int numberOfLinesCleared) {
        switch (numberOfLinesCleared) {
            case 0:
                break;
            case 1:
                score += 40 * difficultyLevel;
                break;
            case 2:
                score += 100 * difficultyLevel;
                break;
            case 3:
                score += 300 * difficultyLevel;
                break;
            case 4:
                score += 1200 * difficultyLevel;
                break;
            default:
                System.out.println("ERROR OF LINES CLEARED");
                break;
        }

        linesClearedAtActualDifficulty += numberOfLinesCleared;
        if (difficultyLevel < 10) {
            if (linesClearedAtActualDifficulty >= 10) {
                difficultyLevel++;
                linesClearedAtActualDifficulty = 0;
            }
        }
    }

    public void addSoftDropScore() {
        score += 1;
    }

    public Integer getScore() {
        return this.score;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Integer getDifficulty() {
        return this.difficultyLevel;
    }

    public void setPlayerName(String name) {
        playerName = name;
    }

    public int compareTo(TScore playerScore) {
        return playerScore.getScore() - this.score;
    }
}