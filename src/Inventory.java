import java.util.ArrayList;
import java.util.List;

/**
 * Manages a collection of items for an entity or shop.
 */
public class Inventory {
    private List<Item> items;

    public Inventory() {
        items = new ArrayList<>();
    }

    /** Adds an item to the inventory. */
    public void addItem(Item item) {
        items.add(item);
    }

    /**
     * Removes and returns the first item with the given name.
     * Returns null if no match is found.
     */
    public Item removeItem(String name) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getName().equalsIgnoreCase(name)) {
                return items.remove(i);
            }
        }
        return null;
    }

    /** Returns the first item with the given name without removing it, or null. */
    public Item getItem(String name) {
        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }

    /** Returns all potions in the inventory. */
    public List<Potion> getPotions() {
        List<Potion> potions = new ArrayList<>();
        for (Item item : items) {
            if (item instanceof Potion) {
                potions.add((Potion) item);
            }
        }
        return potions;
    }

    /** Returns whether the inventory has at least one item. */
    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int size() {
        return items.size();
    }

    public List<Item> getItems() {
        return items;
    }

    /** Prints all items in the inventory with their index. */
    public void display() {
        if (items.isEmpty()) {
            System.out.println("  (empty)");
        } else {
            for (int i = 0; i < items.size(); i++) {
                System.out.println("  [" + i + "] " + items.get(i));
            }
        }
    }
}
