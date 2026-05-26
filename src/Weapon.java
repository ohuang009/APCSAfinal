/**
 * A weapon item that increases attack power.
 * Weapons can be equipped by entities to boost their attack stat.
 */
public class Weapon extends Item {
    private int attackBonus;

    public Weapon(String name, String description, int price, int attackBonus) {
        super(name, description, price);
        this.attackBonus = attackBonus;
    }

    public int getAttackBonus() {
        return attackBonus;
    }

    @Override
    public String toString() {
        return getName() + " [Weapon +ATK " + attackBonus + "] - " + getDescription()
                + " (worth " + getPrice() + " gold)";
    }
}
