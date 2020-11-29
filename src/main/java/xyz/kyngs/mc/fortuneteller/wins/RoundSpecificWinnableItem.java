package xyz.kyngs.mc.fortuneteller.wins;

public class RoundSpecificWinnableItem {

    private final WinnableItem winnableItem;
    private final int count;
    private final int chance;

    public RoundSpecificWinnableItem(WinnableItem winnableItem, int count, int chance) {
        this.winnableItem = winnableItem;
        this.count = count;
        this.chance = chance;
    }

    public WinnableItem getWinnableItem() {
        return winnableItem;
    }

    public int getCount() {
        return count;
    }

    public int getChance() {
        return chance;
    }
}
