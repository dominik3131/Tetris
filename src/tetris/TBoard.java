package tetris;

public class TBoard {
    final int widthOfBoard = 10;
    final int heightOfBoard = 20;
    public int[][] landedBlocks = new int[heightOfBoard][widthOfBoard];

    public int findAndClearFullLines() {
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

    private void clearLine(int numberOfLine) {
        if (numberOfLine < heightOfBoard && numberOfLine >= 0) {
            for (int i = numberOfLine; i > 0; i--) {
                System.arraycopy(landedBlocks[i - 1], 0, landedBlocks[i], 0, widthOfBoard);
            }
        }
    }

    public void fillForTest(int n) {
        for (int i = 0; i < heightOfBoard; i++) {
            for (int j = 0; j < widthOfBoard; j++) {
                landedBlocks[i][j] = i % n;
            }
        }
    }

}
