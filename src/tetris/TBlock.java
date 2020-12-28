package tetris;

public class TBlock {
    public int topLeftRow;
    public int topLeftColumn;
    public int[][][] shapes;
    public int actualShape;
    public int potentialShape;

    public TBlock(int blockType) {
        actualShape = 0;
        potentialShape = 1;
        topLeftRow = 0;
        topLeftColumn = 0;
        switch (blockType) {
            case 1:
                shapes = TBlocksShapes.ITetrominoRotations;
                break;
            case 2:
                shapes = TBlocksShapes.OTetrominoRotations;
                break;
            case 3:
                shapes = TBlocksShapes.TTetrominoRotations;
                break;
            case 4:
                shapes = TBlocksShapes.JTetrominoRotations;
                break;
            case 5:
                shapes = TBlocksShapes.LTetrominoRotations;
                break;
            case 6:
                shapes = TBlocksShapes.ZTetrominoRotations;
                break;
            case 7:
                shapes = TBlocksShapes.STetrominoRotations;
                break;
        }
    }

    public void rotateBlock() {
        actualShape = potentialShape;
        potentialShape++;
        if (potentialShape == 4) potentialShape = 0;
    }
}