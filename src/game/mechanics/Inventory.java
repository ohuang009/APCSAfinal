package game.mechanics;

import game.item.Item;
import java.util.ArrayList;

public class Inventory {

    private ArrayList<Item> items;
    private int maxCapacity;

    public Inventory() {
        this(30);
    }

    public Inventory(int maxCapacity) {
        this.items = new ArrayList<>();
        this.maxCapacity = maxCapacity;
    }

    public boolean addItem(Item item) {
        if (items.size() >= maxCapacity) {
            System.out.println("Inventory full! Cannot add: " + item.getName());
            return false;
        }
        items.add(item);
        return true;
    }

    public boolean removeItem(Item item) {
        return items.remove(item);
    }

    public boolean removeItemByName(String name) {
        Item found = getItemByName(name);
        if (found != null) {
            items.remove(found);
            return true;
        }
        return false;
    }

    public Item getItemByName(String name) {
        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(name)) return item;
        }
        return null;
    }

    public boolean hasItem(String name) {
        return getItemByName(name) != null;
    }

    public int countItem(String name) {
        int count = 0;
        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(name)) count++;
        }
        return count;
    }

    public void displayInventory() {
        if (items.isEmpty()) {
            System.out.println("Your inventory is empty.");
            return;
        }
        System.out.println("=== INVENTORY (" + items.size() + "/" + maxCapacity + ") ===");
        for (int i = 0; i < items.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + items.get(i).getDisplayInfo());
        }
    }

    public ArrayList<Item> getItems() { return items; }
    public int getSize() { return items.size(); }
    public int getMaxCapacity() { return maxCapacity; }
    public boolean isFull() { return items.size() >= maxCapacity; }
    public boolean isEmpty() { return items.isEmpty(); }
}
