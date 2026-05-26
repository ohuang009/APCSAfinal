import java.util.Scanner;

/**
 * Manages a turn-based battle between the Player and an NPC.
 *
 * Turn order: Player acts first, then the NPC (via AIController).
 * The battle ends when either combatant runs out of HP.
 *
 * Player actions each turn:
 *   1. Attack  – deal damage equal to player's attack stat
 *   2. Use Potion – consume a potion from inventory
 *   3. Flee    – 50 % chance to escape the battle
 */
public class Battle {
    private Player player;
    private NPC enemy;
    private Scanner scanner;

    public Battle(Player player, NPC enemy, Scanner scanner) {
        this.player = player;
        this.enemy = enemy;
        this.scanner = scanner;
    }

    /**
     * Starts and runs the battle loop.
     *
     * @return true if the player won, false if the player fled or was defeated.
     */
    public boolean start() {
        System.out.println("\n=== BATTLE START ===");
        System.out.println("You encountered: " + enemy);
        System.out.println("====================\n");

        while (player.isAlive() && enemy.isAlive()) {
            printStatus();
            boolean playerActed = playerTurn();

            if (!playerActed) {
                // Player fled successfully.
                return false;
            }

            if (!enemy.isAlive()) {
                break;
            }

            // NPC's turn.
            System.out.println("\n--- " + enemy.getName() + "'s turn ---");
            AIController.takeTurn(enemy, player);
        }

        return resolveOutcome();
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private void printStatus() {
        System.out.println("\n--- Battle Status ---");
        System.out.println("  " + player);
        System.out.println("  " + enemy);
        System.out.println("---------------------");
    }

    /**
     * Prompts the player to choose an action.
     *
     * @return true if the turn was taken (fight or use item), false if the
     *         player successfully flees.
     */
    private boolean playerTurn() {
        System.out.println("\nYour turn! Choose an action:");
        System.out.println("  [1] Attack");
        System.out.println("  [2] Use Potion");
        System.out.println("  [3] Flee");
        System.out.print("> ");

        String input = scanner.nextLine().trim();

        switch (input) {
            case "1":
                System.out.println(player.getName() + " attacks " + enemy.getName() + "!");
                enemy.takeDamage(player.getAttack());
                return true;

            case "2":
                player.usePotion();
                return true;

            case "3":
                if (Math.random() < 0.5) {
                    System.out.println("You successfully fled!");
                    return false;
                } else {
                    System.out.println("Failed to flee!");
                    return true;
                }

            default:
                System.out.println("Invalid choice – you hesitate!");
                return true;
        }
    }

    /**
     * Determines and announces the battle outcome, awarding loot to the player.
     *
     * @return true if the player survived (won or the enemy died), false if the
     *         player is dead.
     */
    private boolean resolveOutcome() {
        if (!player.isAlive()) {
            System.out.println("\n*** YOU HAVE BEEN DEFEATED! ***");
            return false;
        }

        System.out.println("\n=== VICTORY! You defeated " + enemy.getName() + "! ===");
        player.addGold(enemy.getGoldReward());

        // Transfer loot items to the player's inventory.
        for (Item item : enemy.getLoot().getItems()) {
            System.out.println("  You obtained: " + item.getName());
            player.getInventory().addItem(item);
        }

        return true;
    }
}
