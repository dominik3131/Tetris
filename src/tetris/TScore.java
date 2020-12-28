package tetris;

import java.io.Serializable;

class TScore implements Serializable, Comparable<TScore> {
    private static final long serialVersionUID = 1L;
    public int difficultyLevel;
    public int totalLinesCleared;
    public int score;
    public String playerName;

    public TScore(int level) {
        if (level < 1 || level > 10)
            difficultyLevel = 1;
        else
            difficultyLevel = level;

        totalLinesCleared = 0;
        score = 0;
    }

    public void addLinesClearScore(int numberOfLinesCleared) {
        switch (numberOfLinesCleared) {
            case 1:
                score = 40 * difficultyLevel;
                break;
            case 2:
                score = 100 * difficultyLevel;
                break;
            case 3:
                score = 300 * difficultyLevel;
                break;
            case 4:
                score = 1200 * difficultyLevel;
                break;
            default:
                System.out.println("ERROR OF LINES CLEARED");
                break;
        }

        totalLinesCleared += numberOfLinesCleared;
        if (difficultyLevel < 10) {
            if (totalLinesCleared / difficultyLevel > 10)
                difficultyLevel++;
        }
    }

    public void addSoftDropScore() {
        //numbers of fields block moved down when down arrow key was pressed
    }

    public int getScore() {
        return this.score;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int compareTo(TScore playerScore) {
        return playerScore.getScore() - this.score;
    }
}
