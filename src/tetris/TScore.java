package tetris;

import java.io.Serializable;

class TScore implements Serializable, Comparable<TScore> {
    private static final long serialVersionUID = 1L;
    private Integer difficultyLevel;
    private int linesClearedAtActualDifficulty;
    public Integer score;
    public String playerName;

    public TScore() {
    }

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
                linesClearedAtActualDifficulty = linesClearedAtActualDifficulty % 10;
            }
        }
    }

    public void addSoftDropScore() {
        score += 1;
    }

    public void addHardDropScore() {
        score += 2;
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

    @Override
    public String toString() {
        return playerName + " " + score;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((playerName == null) ? 0 : playerName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TScore other = (TScore) obj;
        if (playerName == null) {
            return other.playerName == null;

        } else
            return playerName.equals(other.playerName);
    }
}