package tetris;


//based on Super Rotation System
public class TBlocksShapes {
    public static int[][][] ITetrominoRotations =
            {
                    {
                            {0, 0, 0, 0},
                            {1, 1, 1, 1},
                            {0, 0, 0, 0},
                            {0, 0, 0, 0},
                    },
                    {
                            {0, 1, 0, 0},
                            {0, 1, 0, 0},
                            {0, 1, 0, 0},
                            {0, 1, 0, 0},
                    },
                    {
                            {0, 0, 0, 0},
                            {0, 0, 0, 0},
                            {1, 1, 1, 1},
                            {0, 0, 0, 0},
                    },
                    {
                            {0, 0, 1, 0},
                            {0, 0, 1, 0},
                            {0, 0, 1, 0},
                            {0, 0, 1, 0},
                    },
            };
    public static int[][][] OTetrominoRotations =
            {
                    {
                            {0, 2, 2, 0},
                            {0, 2, 2, 0},
                            {0, 0, 0, 0},
                            {0, 0, 0, 0},
                    },
                    {
                            {0, 2, 2, 0},
                            {0, 2, 2, 0},
                            {0, 0, 0, 0},
                            {0, 0, 0, 0},
                    },
                    {
                            {0, 2, 2, 0},
                            {0, 2, 2, 0},
                            {0, 0, 0, 0},
                            {0, 0, 0, 0},
                    },
                    {
                            {0, 2, 2, 0},
                            {0, 2, 2, 0},
                            {0, 0, 0, 0},
                            {0, 0, 0, 0},
                    },
            };
    public static int[][][] TTetrominoRotations = {
            {
                    {0, 3, 0, 0},
                    {3, 3, 3, 0},
                    {0, 0, 0, 0},
                    {0, 0, 0, 0},
            },
            {
                    {0, 3, 0, 0},
                    {0, 3, 3, 0},
                    {0, 3, 0, 0},
                    {0, 0, 0, 0},
            },
            {
                    {0, 0, 0, 0},
                    {3, 3, 3, 0},
                    {0, 3, 0, 0},
                    {0, 0, 0, 0},
            },
            {
                    {0, 3, 0, 0},
                    {3, 3, 0, 0},
                    {0, 3, 0, 0},
                    {0, 0, 0, 0},
            },
    };

    public static int[][][] JTetrominoRotations = {
            {
                    {4, 0, 0, 0},
                    {4, 4, 4, 0},
                    {0, 0, 0, 0},
                    {0, 0, 0, 0},
            },
            {
                    {0, 4, 4, 0},
                    {0, 4, 0, 0},
                    {0, 4, 0, 0},
                    {0, 0, 0, 0},
            },
            {
                    {0, 0, 0, 0},
                    {4, 4, 4, 0},
                    {0, 0, 4, 0},
                    {0, 0, 0, 0},
            },
            {
                    {0, 4, 0, 0},
                    {0, 4, 0, 0},
                    {4, 4, 0, 0},
                    {0, 0, 0, 0},
            },
    };

    public static int[][][] LTetrominoRotations = {
            {
                    {0, 0, 5, 0},
                    {5, 5, 5, 0},
                    {0, 0, 0, 0},
                    {0, 0, 0, 0},
            },
            {
                    {0, 5, 0, 0},
                    {0, 5, 0, 0},
                    {0, 5, 5, 0},
                    {0, 0, 0, 0},
            },
            {
                    {0, 0, 0, 0},
                    {5, 5, 5, 0},
                    {5, 0, 0, 0},
                    {0, 0, 0, 0},
            },
            {
                    {5, 5, 0, 0},
                    {0, 5, 0, 0},
                    {0, 5, 0, 0},
                    {0, 0, 0, 0},
            },
    };
    public static int[][][] ZTetrominoRotations = {
            {
                    {6, 6, 0, 0},
                    {0, 6, 6, 0},
                    {0, 0, 0, 0},
                    {0, 0, 0, 0},
            },
            {
                    {0, 0, 6, 0},
                    {0, 6, 6, 0},
                    {0, 6, 0, 0},
                    {0, 0, 0, 0},
            },
            {
                    {0, 0, 0, 0},
                    {6, 6, 0, 0},
                    {0, 6, 6, 0},
                    {0, 0, 0, 0},
            },
            {
                    {0, 6, 0, 0},
                    {6, 6, 0, 0},
                    {6, 0, 0, 0},
                    {0, 0, 0, 0},
            },
    };
    public static int[][][] STetrominoRotations = {
            {
                    {0, 7, 7, 0},
                    {7, 7, 0, 0},
                    {0, 0, 0, 0},
                    {0, 0, 0, 0},
            },
            {
                    {0, 7, 0, 0},
                    {0, 7, 7, 0},
                    {0, 0, 7, 0},
                    {0, 0, 0, 0},
            },
            {
                    {0, 0, 0, 0},
                    {0, 7, 7, 0},
                    {7, 7, 0, 0},
                    {0, 0, 0, 0},
            },
            {
                    {7, 0, 0, 0},
                    {7, 7, 0, 0},
                    {0, 7, 0, 0},
                    {0, 0, 0, 0},
            },
    };
}