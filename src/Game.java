import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Main game loop for the Text-Based Turn-Based AI Adventure RPG.
 *
 * Commands available while exploring:
 *   look              – describe the current location
 *   go <direction>    – move to an adjacent location
 *   fight             – engage the first NPC at this location
 *   shop              – open the shop at this location (if any)
 *   inventory         – display your items
 *   status            – show your stats
 *   equip <weapon>    – equip a weapon from your inventory
 *   use potion        – drink a potion outside of battle
 *   help              – show the command list
 *   quit              – exit the game
 */
public class Game {
    private Player player;
    private GameMap map;
    private Location currentLocation;
    private Scanner scanner;
    private boolean running;

    public Game() {
        scanner = new Scanner(System.in);
        running = false;
    }

    /** Initialises the game world and starts the main loop. */
    public void start() {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║   TEXT-BASED TURN-BASED AI RPG       ║");
        System.out.println("╚══════════════════════════════════════╝");

        setupPlayer();

        map = GameMap.buildDefaultMap();
        currentLocation = map.getStartLocation();

        System.out.println("\nWelcome, " + player.getName() + "! Your adventure begins.\n");
        currentLocation.describe();

        running = true;
        gameLoop();
    }

    // ── Setup ─────────────────────────────────────────────────────────────────

    private void setupPlayer() {
        System.out.print("Enter your hero's name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) name = "Hero";
        // Starting stats: 100 HP, 12 ATK, 3 DEF, 50 gold.
        player = new Player(name, 100, 12, 3, 50);
        System.out.println("Created character: " + player);
    }

    // ── Main loop ─────────────────────────────────────────────────────────────

    private void gameLoop() {
        while (running) {
            System.out.print("\n> ");
            String input = scanner.nextLine().trim().toLowerCase();
            handleCommand(input);

            if (!player.isAlive()) {
                System.out.println("\nGame Over. " + player.getName() + " has fallen.");
                running = false;
            }
        }
    }

    // ── Command dispatch ──────────────────────────────────────────────────────

    private void handleCommand(String input) {
        if (input.startsWith("go ")) {
            movePlayer(input.substring(3).trim());
        } else if (input.equals("look")) {
            currentLocation.describe();
        } else if (input.equals("fight")) {
            fightNpc();
        } else if (input.equals("shop")) {
            openShop();
        } else if (input.equals("inventory") || input.equals("inv")) {
            showInventory();
        } else if (input.equals("status") || input.equals("stats")) {
            System.out.println(player);
        } else if (input.startsWith("equip ")) {
            player.equipWeaponFromInventory(input.substring(6).trim());
        } else if (input.equals("use potion")) {
            player.usePotion();
        } else if (input.equals("help")) {
            printHelp();
        } else if (input.equals("quit") || input.equals("exit")) {
            System.out.println("Thanks for playing!");
            running = false;
        } else {
            System.out.println("Unknown command. Type 'help' for a list of commands.");
        }
    }

    // ── Command implementations ───────────────────────────────────────────────

    private void movePlayer(String direction) {
        Location next = currentLocation.getExit(direction);
        if (next == null) {
            System.out.println("You can't go that way.");
        } else {
            currentLocation = next;
            System.out.println("You travel " + direction + "...");
            currentLocation.describe();
        }
    }

    private void fightNpc() {
        List<NPC> npcs = currentLocation.getNpcs();
        if (npcs.isEmpty()) {
            System.out.println("There are no enemies here.");
            return;
        }

        NPC enemy = npcs.get(0);
        Battle battle = new Battle(player, enemy, scanner);
        boolean won = battle.start();

        if (won) {
            currentLocation.removeNpc(enemy);
        }
    }

    private void openShop() {
        if (!currentLocation.hasShop()) {
            System.out.println("There is no shop here.");
            return;
        }
        currentLocation.getShop().open(player, scanner);
    }

    private void showInventory() {
        System.out.println("\n--- " + player.getName() + "'s Inventory ---");
        player.getInventory().display();
    }

    private void printHelp() {
        System.out.println("\nAvailable commands:");
        System.out.println("  look              – describe the current location");
        System.out.println("  go <direction>    – move (e.g. go north)");
        System.out.println("  fight             – battle an enemy here");
        System.out.println("  shop              – open the shop here");
        System.out.println("  inventory         – list your items");
        System.out.println("  status            – show your stats");
        System.out.println("  equip <weapon>    – equip a weapon from inventory");
        System.out.println("  use potion        – drink a potion");
        System.out.println("  help              – show this list");
        System.out.println("  quit              – exit the game");
    }

    // ── Entry point ───────────────────────────────────────────────────────────

    public static void main(String[] args) {
        new Game().start();
    }
}
