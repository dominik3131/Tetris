package tetris;

public class TBoard {
    final int widthOfBoard = 10;
    final int heightOfBoard = 20;
    public int[][] landedBlocks = new int[heightOfBoard][widthOfBoard];

    public void showBoard() {
        System.out.println("\n---------------------------");
        for (int i = 0; i < heightOfBoard; i++) {
            System.out.println();
            for (int j = 0; j < widthOfBoard; j++) {
                System.out.format("%2d ", landedBlocks[i][j]);
            }
        }
    }

    public int checkFullLines() {
        int fullLines = 0;
        for (int i = heightOfBoard - 1; i >= 0; i--) {
            int lineCheckMarker = 1;
            for (int j = 0; j < widthOfBoard; j++) {
                lineCheckMarker *= landedBlocks[i][j];
            }

            if (lineCheckMarker != 0) {
                clearLine(i);
                fullLines++;
                i++;
            }
        }

        return fullLines;
    }

    public void clearLine(int numberOfLine) {
        if (numberOfLine < heightOfBoard && numberOfLine >= 0) {
            for (int i = numberOfLine; i > 0; i--) {
                for (int j = 0; j < widthOfBoard; j++) {
                    landedBlocks[i][j] = landedBlocks[i - 1][j];
                }
            }
        }
    }

    public void fillForTest() {
        for (int i = 0; i < heightOfBoard; i++) {
            for (int j = 0; j < widthOfBoard; j++) {
                landedBlocks[i][j] = i;
            }
        }
    }

}
