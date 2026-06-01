package game.mechanics;

import game.entity.Boss;
import game.entity.Enemy;
import game.entity.Player;
import game.item.Consumable;
import game.item.Item;
import java.util.Random;
import java.util.Scanner;

public class Battle {

    private Player  player;
    private Enemy   enemy;
    private Scanner scanner;
    private int     turnCount;
    private boolean playerFled;

    public Battle(Player player, Enemy enemy, Scanner scanner) {
        this.player     = player;
        this.enemy      = enemy;
        this.scanner    = scanner;
        this.turnCount  = 0;
        this.playerFled = false;
    }

    // ------------------------------------------------------------------ //
    //  Main battle loop                                                    //
    // ------------------------------------------------------------------ //

    public boolean start() {
        System.out.println("\n=== BATTLE: " + enemy.getName() + " ===");
        System.out.println(enemy.getStatusDisplay());

        while (player.isAlive() && enemy.isAlive() && !playerFled) {
            turnCount++;
            System.out.println("\n--- Turn " + turnCount + " ---");
            System.out.println("You : " + player.getStatusDisplay());
            System.out.println("Foe : " + enemy.getStatusDisplay());

            // boss phase-transition check
            if (enemy instanceof Boss) {
                Boss boss = (Boss) enemy;
                if (boss.shouldTransitionPhase()) {
                    boss.enterNextPhase();
                }
            }

            playerTurn();
            if (!enemy.isAlive()) break;

            enemyTurn();

            if (enemy instanceof game.entity.Troll) {
                ((game.entity.Troll) enemy).regenerate();
            }
        }

        return resolveOutcome();
    }

    private void playerTurn() {
        System.out.println("\nYour turn:");
        System.out.println("  [1] Attack   [2] Use Item   [3] Flee");
        System.out.print("  > ");
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                int raw    = player.getTotalAttack();
                int dealt  = enemy.takeDamage(raw);
                System.out.println("You attack " + enemy.getName()
                        + " for " + dealt + " damage!");
                break;
            case "2":
                System.out.print("  Item name: ");
                useItem(scanner.nextLine().trim());
                break;
            case "3":
                attemptFlee();
                break;
            default:
                System.out.println("Invalid input - you hesitate and lose your turn.");
        }
    }

    private void enemyTurn() {
        if (enemy instanceof game.entity.Bandit) {
            ((game.entity.Bandit) enemy).attemptPickpocket(player);
        }

        if (enemy instanceof game.entity.AncientGolem) {
            ((game.entity.AncientGolem) enemy).setBattleContext(player);
        }

        int baseDmg   = enemy.performAttack();
        int curseDmg  = (enemy instanceof game.entity.DemonLord)
                        ? ((game.entity.DemonLord) enemy).getCurseDamage() : 0;

        int totalDealt = player.takeDamage(baseDmg + curseDmg);
        System.out.println(enemy.getName() + " " + enemy.getAttackDescription()
                + " for " + totalDealt + " damage!");
    }

    private void useItem(String itemName) {
        Item item = player.getInventory().getItemByName(itemName);
        if (item == null) {
            System.out.println("Item not found in inventory.");
            return;
        }
        if (item instanceof Consumable) {
            Consumable c = (Consumable) item;
            if (c.getHealthRestore() > 0) {
                player.heal(c.getHealthRestore());
                System.out.println("Restored " + c.getHealthRestore() + " HP.");
            }
            if (c.getManaRestore() > 0 && player instanceof game.entity.Mage) {
                ((game.entity.Mage) player).regenMana(c.getManaRestore());
                System.out.println("Restored " + c.getManaRestore() + " MP.");
            }
            player.getInventory().removeItem(item);
        } else {
            System.out.println("That item cannot be used in battle.");
        }
    }

    private void attemptFlee() {
        int chance = 40 + player.getSpeed() - enemy.getSpeed();
        if (new Random().nextInt(100) < chance) {
            System.out.println("You successfully flee the battle!");
            playerFled = true;
        } else {
            System.out.println("Failed to flee!");
        }
    }

    private boolean resolveOutcome() {
        if (playerFled) return false;

        if (enemy instanceof game.entity.AncientGolem) {
            ((game.entity.AncientGolem) enemy).restorePlayerDebuffs();
        }

        if (player.isAlive()) {
            int  xp    = enemy.getXPReward();
            int  coins = enemy.rollCoinDrop();
            Item drop  = enemy.rollItemDrop();

            System.out.println("\n=== VICTORY! ===");
            System.out.println("  Defeated: " + enemy.getName());
            System.out.println("  XP gained   : " + xp);
            System.out.println("  Coins found : " + coins);

            player.gainXP(xp);
            player.gainCoins(coins);

            if (drop != null) {
                player.getInventory().addItem(drop);
                System.out.println("  Item drop   : " + drop.getName());
            }
            return true;
        } else {
            System.out.println("\n=== DEFEATED! ===");
            System.out.println("  " + enemy.getName() + " has bested you...");
            return false;
        }
    }

    public int     getTurnCount()   { return turnCount; }
    public boolean didPlayerFlee()  { return playerFled; }
}
