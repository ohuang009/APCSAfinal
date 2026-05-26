/**
 * An AI-controlled non-player character (enemy or ally).
 * Carries loot that is awarded to the player upon defeat.
 */
public class NPC extends Entity {
    private Inventory loot;
    private int goldReward;
    private String description;

    public NPC(String name, String description, int hp, int attack, int defense, int goldReward) {
        super(name, hp, attack, defense);
        this.description = description;
        this.goldReward = goldReward;
        this.loot = new Inventory();
    }

    public String getDescription() {
        return description;
    }

    public int getGoldReward() {
        return goldReward;
    }

    public Inventory getLoot() {
        return loot;
    }

    /** Adds an item to the NPC's loot table. */
    public void addLoot(Item item) {
        loot.addItem(item);
    }

    @Override
    public String toString() {
        return super.toString() + " | " + description;
    }
}
