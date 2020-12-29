package tetris;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;

//TGM1 randomize algorithm which makes less possibility for successive identical blocks
public class TBlockRandomizer {
    private final List<Integer> lastFourBlocks;

    public TBlockRandomizer() {
        lastFourBlocks = new ArrayList<>(4);
    }

    public int chooseBlock() {
        Random randomizer = new Random();
        int chosenBlock = randomizer.nextInt(7) + 1;
        if (lastFourBlocks.size() != 0) {
            for (int i = 0; i < 4; i++) {
                chosenBlock = randomizer.nextInt(7) + 1;
                if (!lastFourBlocks.contains(chosenBlock)) {
                    if (lastFourBlocks.size() == 4) {
                        lastFourBlocks.remove(3);
                    }
                    lastFourBlocks.add(0, chosenBlock);
                    break;
                }
            }
        }

        return chosenBlock;
    }

}