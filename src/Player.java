/**
 * The human-controlled player character.
 * Extends Entity with an inventory and a gold currency.
 */
public class Player extends Entity {
    private Inventory inventory;
    private int gold;

    public Player(String name, int hp, int attack, int defense, int startingGold) {
        super(name, hp, attack, defense);
        this.inventory = new Inventory();
        this.gold = startingGold;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public int getGold() {
        return gold;
    }

    public void addGold(int amount) {
        gold += amount;
        System.out.println(getName() + " gained " + amount + " gold. (Total: " + gold + ")");
    }

    public boolean spendGold(int amount) {
        if (gold < amount) {
            System.out.println("Not enough gold!");
            return false;
        }
        gold -= amount;
        return true;
    }

    /**
     * Uses the first available potion from the inventory to restore HP.
     * Returns true if a potion was used.
     */
    public boolean usePotion() {
        for (Item item : inventory.getItems()) {
            if (item instanceof Potion) {
                Potion potion = (Potion) item;
                inventory.removeItem(potion.getName());
                heal(potion.getHealAmount());
                System.out.println(getName() + " used " + potion.getName()
                        + " and restored " + potion.getHealAmount() + " HP. (HP: "
                        + getHp() + "/" + getMaxHp() + ")");
                return true;
            }
        }
        System.out.println("No potions available!");
        return false;
    }

    /** Equips a weapon from the inventory by name. */
    public boolean equipWeaponFromInventory(String weaponName) {
        Item item = inventory.getItem(weaponName);
        if (item instanceof Weapon) {
            equipWeapon((Weapon) item);
            return true;
        }
        System.out.println("Weapon '" + weaponName + "' not found in inventory.");
        return false;
    }

    @Override
    public String toString() {
        return super.toString() + " | Gold: " + gold;
    }
}
