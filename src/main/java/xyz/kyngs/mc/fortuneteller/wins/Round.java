package xyz.kyngs.mc.fortuneteller.wins;

import java.util.Set;

public class Round {

    private final int pixelPrice;
    private final int countInRow;
    private final int pixelWinChance;
    private final int pixelCount;
    private final Set<RoundSpecificWinnableItem> specificItems;

    public Round(int pixelPrice, int countInRow, int pixelWinChance, int pixelCount, Set<RoundSpecificWinnableItem> specificItems) {
        this.pixelPrice = pixelPrice;
        this.countInRow = countInRow;
        this.pixelWinChance = pixelWinChance;
        this.pixelCount = pixelCount;
        this.specificItems = specificItems;
    }

    public int getPixelPrice() {
        return pixelPrice;
    }

    public int getCountInRow() {
        return countInRow;
    }

    public int getPixelWinChance() {
        return pixelWinChance;
    }

    public int getPixelCount() {
        return pixelCount;
    }

    public Set<RoundSpecificWinnableItem> getSpecificItems() {
        return specificItems;
    }
}
