/**
 * A consumable potion that restores HP when used.
 */
public class Potion extends Item {
    private int healAmount;

    public Potion(String name, String description, int price, int healAmount) {
        super(name, description, price);
        this.healAmount = healAmount;
    }

    public int getHealAmount() {
        return healAmount;
    }

    @Override
    public String toString() {
        return getName() + " [Potion +" + healAmount + " HP] - " + getDescription()
                + " (worth " + getPrice() + " gold)";
    }
}
