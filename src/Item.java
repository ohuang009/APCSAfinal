/**
 * Base class for all items in the game.
 * Every item has a name, description, and a price used by shops.
 */
public class Item {
    private String name;
    private String description;
    private int price;

    public Item(String name, String description, int price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return name + " - " + description + " (worth " + price + " gold)";
    }
}
