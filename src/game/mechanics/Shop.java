package game.mechanics;

import game.entity.Player;
import game.item.Item;
import java.util.ArrayList;

public class Shop {

    private String shopName;
    private ArrayList<Item> stock;
    private double buyMarkup;
    private double sellMarkdown;

    public Shop(String shopName) {
        this.shopName = shopName;
        this.stock = new ArrayList<>();
        this.buyMarkup = 1.2;
        this.sellMarkdown = 0.5;
    }

    public void addItem(Item item)    { stock.add(item); }
    public void removeItem(Item item) { stock.remove(item); }

    public void displayShop() {
        System.out.println("=== " + shopName.toUpperCase() + " ===");
        if (stock.isEmpty()) {
            System.out.println("  (No items in stock.)");
            return;
        }
        for (int i = 0; i < stock.size(); i++) {
            Item item = stock.get(i);
            int price = getBuyPrice(item);
            System.out.println("  " + (i + 1) + ". " + item.getDisplayInfo()
                    + "  |  Buy: " + price + " coins");
        }
    }

    public boolean buyItem(Player player, String itemName) {
        Item item = getItemByName(itemName);
        if (item == null) {
            System.out.println("'" + itemName + "' is not available in this shop.");
            return false;
        }
        int price = getBuyPrice(item);
        if (player.getCoins() < price) {
            System.out.println("Not enough coins. Need " + price
                    + ", you have " + player.getCoins() + ".");
            return false;
        }
        player.spendCoins(price);
        player.getInventory().addItem(item);
        stock.remove(item);
        System.out.println("Purchased " + item.getName() + " for " + price + " coins.");
        return true;
    }

    public boolean sellItem(Player player, String itemName) {
        Item item = player.getInventory().getItemByName(itemName);
        if (item == null) {
            System.out.println("You don't have '" + itemName + "' in your inventory.");
            return false;
        }
        int sellPrice = getSellPrice(item);
        player.getInventory().removeItem(item);
        player.gainCoins(sellPrice);
        stock.add(item);
        System.out.println("Sold " + item.getName() + " for " + sellPrice + " coins.");
        return true;
    }

    public int getBuyPrice(Item item)  { return (int) (item.getValue() * buyMarkup); }
    public int getSellPrice(Item item) { return (int) (item.getValue() * sellMarkdown); }

    public Item getItemByName(String name) {
        for (Item item : stock) {
            if (item.getName().equalsIgnoreCase(name)) return item;
        }
        return null;
    }

    public String getShopName() { return shopName; }
    public ArrayList<Item> getStock() { return stock; }
    public void setBuyMarkup(double m) { this.buyMarkup = m; }
    public void setSellMarkdown(double m) { this.sellMarkdown = m; }
}
