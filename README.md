# tetris
Tetris game written in JAVA and JAVAFX

# Tetris-project
Project of Tetris game in JAVA  

List of classes with short description  

TScore            - class that calculates and stores points of actual game  
THighScore        - class storing best 10 scores and best score of every individual player using lists and serialization  
TBoard            - class with logical representation of blocks that already landed on the board and methods for clearing lines  
TBlock            - class that is logical representation of block  
TBlocksShapes     - class with representation of  I O T J L Z S tetrominoes, and their rotations  
TBlockRandomizer  - class with an algorithm TGM1 which is choosing next block  
TGameController   - connects all parts of the game and controls behavior of the game

List of tetrominoes  
1 deep blue - I tetromino  
2 red       - O tetromino  
3 yellow    - T tetromino  
4 green     - J tetromino  
5 purple    - L tetromino  
6 orange    - Z tetromino  
7 blue      - S tetromino  
