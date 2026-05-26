import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * A shop where the player can buy items with gold or sell items for half price.
 */
public class Shop {
    private String name;
    private Inventory stock;

    public Shop(String name) {
        this.name = name;
        this.stock = new Inventory();
    }

    public String getName() {
        return name;
    }

    /** Adds an item to the shop's stock. */
    public void addItem(Item item) {
        stock.addItem(item);
    }

    /**
     * Opens an interactive shop menu for the player.
     */
    public void open(Player player, Scanner scanner) {
        System.out.println("\n=== Welcome to " + name + " ===");

        boolean inShop = true;
        while (inShop) {
            System.out.println("\nYour gold: " + player.getGold());
            System.out.println("What would you like to do?");
            System.out.println("  [1] Buy");
            System.out.println("  [2] Sell");
            System.out.println("  [3] Leave");
            System.out.print("> ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    buyMenu(player, scanner);
                    break;
                case "2":
                    sellMenu(player, scanner);
                    break;
                case "3":
                    inShop = false;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private void buyMenu(Player player, Scanner scanner) {
        if (stock.isEmpty()) {
            System.out.println("Nothing for sale right now.");
            return;
        }

        System.out.println("\n--- Items for sale ---");
        stock.display();
        System.out.print("Enter item name to buy (or blank to cancel): ");
        String itemName = scanner.nextLine().trim();

        if (itemName.isEmpty()) return;

        Item item = stock.getItem(itemName);
        if (item == null) {
            System.out.println("Item not found.");
            return;
        }

        if (player.spendGold(item.getPrice())) {
            stock.removeItem(item.getName());
            player.getInventory().addItem(item);
            System.out.println("You purchased " + item.getName() + ".");
        }
    }

    private void sellMenu(Player player, Scanner scanner) {
        if (player.getInventory().isEmpty()) {
            System.out.println("You have nothing to sell.");
            return;
        }

        System.out.println("\n--- Your inventory ---");
        player.getInventory().display();
        System.out.print("Enter item name to sell (or blank to cancel): ");
        String itemName = scanner.nextLine().trim();

        if (itemName.isEmpty()) return;

        Item item = player.getInventory().removeItem(itemName);
        if (item == null) {
            System.out.println("Item not found in your inventory.");
            return;
        }

        int sellPrice = Math.max(1, item.getPrice() / 2);
        player.addGold(sellPrice);
        System.out.println("You sold " + item.getName() + " for " + sellPrice + " gold.");
    }
}
