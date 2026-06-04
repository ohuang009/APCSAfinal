package game;

import game.entity.*;
import game.item.*;
import game.mechanics.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    private static Player player;
    private static GameMap gameMap;
    private static Shop mainShop;
    private static ArrayList<Recipe> knownRecipes;
    private static ArrayList<DungeonStage> dungeonStages;
    private static ArrayList<Enchantment> enchantments;

    public static void main(String[] args) {
        printBanner();
        setupGame();
        gameLoop();
        scanner.close();
    }

    private static void setupGame() {
        System.out.print("Enter your character name: ");
        String name = scanner.nextLine().trim();

        System.out.println("\nChoose your class:");
        System.out.println("  [1] Warrior  - High health and combat power.");
        System.out.println("  [2] Mage     - High intelligence and mana-based spells.");
        System.out.println("  [3] Banker   - High wealth and intelligence; better shop deals.");
        System.out.print("  > ");
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "2":  player = new Mage(name);   break;
            case "3":  player = new Banker(name); break;
            default:   player = new Warrior(name); break;
        }

        System.out.println("\n" + player.getClassDescription());
        System.out.println("Welcome, " + player.getName()
                + " the " + player.getPlayerClass() + "!\n");

        gameMap      = new GameMap(player);
        mainShop     = buildMainShop();
        knownRecipes  = buildRecipes();
        dungeonStages = buildDungeonStages();
        enchantments  = buildEnchantments();
    }

    private static Shop buildMainShop() {
        Shop shop = new Shop("Hearthstone General Store");
        shop.addItem(new Weapon("Iron Sword", "A sturdy iron blade.", 50, "Common", "Sword", 8, 0));
        shop.addItem(new Weapon("Oak Staff", "Channels magical energy.", 60, "Common", "Staff", 4, 6));
        shop.addItem(new Weapon("Hunter's Bow", "A reliable short bow.", 70, "Common", "Bow", 7, 0));
        shop.addItem(new Armor("Leather Armor", "Basic chest protection.", 40, "Common", "Chestplate", 5, 10));
        shop.addItem(new Armor("Iron Shield", "Deflects incoming blows.", 55, "Common", "Shield", 8, 0));
        shop.addItem(new Consumable("Health Potion", "Restores 40 HP.", 20, "Common", 40, 0, 0, 0, 0));
        shop.addItem(new Consumable("Mana Potion", "Restores 30 MP.", 25, "Common", 0, 30, 0, 0, 0));
        shop.addItem(new Consumable("Strength Brew", "ATK +5 for 3 turns.", 35, "Uncommon", 0, 0, 5, 0, 3));
        return shop;
    }

    private static ArrayList<Recipe> buildRecipes() {
        ArrayList<Recipe> recipes = new ArrayList<>();

        Recipe woodBow = new Recipe("Wooden Bow",
                new Weapon("Wooden Bow", "A simple self-made bow.", 30, "Common", "Bow", 5, 0),
                1, "Craft a basic bow from lumber.");
        woodBow.addIngredient("Oak Wood", 3);
        recipes.add(woodBow);

        Recipe grilledFish = new Recipe("Grilled Fish",
                new Consumable("Grilled Fish", "A hearty meal. Restores 30 HP.", 12, "Common",
                               30, 0, 0, 0, 0),
                1, "Cook your caught fish for a health boost.");
        grilledFish.addIngredient("Bass",   1);
        recipes.add(grilledFish);

        Recipe reinforcedShield = new Recipe("Reinforced Shield",
                new Armor("Reinforced Shield", "A sturdy wooden-iron shield.", 90, "Uncommon",
                          "Shield", 12, 0),
                5, "Combine wood and iron for solid defence.");
        reinforcedShield.addIngredient("Oak Wood", 2);
        reinforcedShield.addIngredient("Iron Ore", 1);
        recipes.add(reinforcedShield);

        return recipes;
    }

    private static ArrayList<DungeonStage> buildDungeonStages() {
        ArrayList<DungeonStage> stages = new ArrayList<>();

        DungeonStage stage1 = new DungeonStage(1, "Dragon's Keep",
                "Ignarath the Flame Dragon awaits in a throne of scorched bone.",
                new DragonBoss(), 10);
        stage1.addReward(new Weapon("Dragon Fang Sword",
                "Forged from a dragon's tooth.", 300, "Rare", "Sword", 22, 0));
        stages.add(stage1);

        DungeonStage stage2 = new DungeonStage(2, "Demon Realm",
                "Malachar the Demon Lord holds dominion over this hellish plane.",
                new DemonLord(), 20);
        stage2.addReward(new Armor("Demonhide Chestplate",
                "Infused with demonic essence.", 450, "Rare", "Chestplate", 20, 30));
        stages.add(stage2);

        DungeonStage stage3 = new DungeonStage(3, "Ancient Sanctum",
                "Zytheron the Ancient Golem stirs - its core burns with an ancient rage.",
                new AncientGolem(), 30);
        stage3.addReward(new Weapon("Golem Core Staff",
                "Pulses with immense arcane energy.", 800, "Legendary", "Staff", 18, 25));
        stages.add(stage3);

        return stages;
    }

    private static ArrayList<Enchantment> buildEnchantments() {
        ArrayList<Enchantment> list = new ArrayList<>();
        list.add(new Enchantment("Sharpening", "attack", 3, 50, 5,
                "Hones the weapon's edge for increased attack."));
        list.add(new Enchantment("Fortification", "defense", 3, 50, 5,
                "Reinforces the armour's structure for increased defence."));
        list.add(new Enchantment("Arcane Infusion", "intelligence", 4, 80, 5,
                "Imbues the item with arcane energy."));
        return list;
    }

    private static void gameLoop() {
        System.out.println("Type 'help' for a list of commands.\n");
        while (true) {
            System.out.print(player.getName() + " > ");
            String cmd = scanner.nextLine().trim().toLowerCase();
            if (cmd.equals("quit") || cmd.equals("exit")) {
                System.out.println("Farewell, " + player.getName() + ". Until next time.");
                break;
            }
            handleCommand(cmd);
        }
    }

    private static void handleCommand(String command) {
        switch (command) {
            case "hunt":
                handleHunt();
                break;
            case "adventure":
                handleAdventure();
                break;
            case "shop":
                handleShop();
                break;
            case "buy":
                handleBuy();
                break;
            case "sell":
                handleSell();
                break;
            case "dungeon":
                handleDungeon();
                break;
            case "inventory":
                handleInventory();
                break;
            case "profile":
                handleProfile();
                break;
            case "chop":
                handleChop();
                break;
            case "fish":
                handleFish();
                break;
            case "craft":
                handleCraft();
                break;
            case "recipes":
                handleRecipes();
                break;
            case "enchant":
                handleEnchant();
                break;
            case "map":
                gameMap.displayMap();
                break;
            case "travel":
                handleTravel();
                break;
            case "help":
                printHelp();
                break;
            case "equip":
                handleEquip();
                break;
            case "debug_golem":
                handleDebugGolem();
                break;
            default:
                System.out.println("Unknown command. Type 'help' for options.");
        }
    }

    private static void handleEquip() {
        player.getInventory().displayInventory();
        System.out.print("Item to equip: ");
        String itemName = scanner.nextLine().trim();
        Item item = player.getInventory().getItemByName(itemName);
        if (item == null) {
            System.out.println("Item not found in inventory.");
            return;
        }
        if (item instanceof Weapon) {
            Weapon w = (Weapon) item;
            if (player.getEquippedWeapon() != null) {
                player.getInventory().addItem(player.getEquippedWeapon());
            }
            player.getInventory().removeItem(w);
            player.equipWeapon(w);
        } else if (item instanceof Armor) {
            Armor a = (Armor) item;
            if (player.getEquippedArmor() != null) {
                player.getInventory().addItem(player.getEquippedArmor());
            }
            player.getInventory().removeItem(a);
            player.equipArmor(a);
        } else {
            System.out.println("That item cannot be equipped.");
        }
    }

    private static void handleHunt() {
        String type = gameMap.getRandomEncounterType();
        Enemy enemy = spawnEnemy(type, player.getLevel());
        Battle battle = new Battle(player, enemy, scanner);
        boolean won = battle.start();

        if (!player.isAlive()) {
            System.out.println("You have been defeated. Game over.");
            System.exit(0);
        }
    }

    private static void handleAdventure() {
        System.out.println("You venture out into the unknown...");
        int roll = new Random().nextInt(3);
        switch (roll) {
            case 0:
                System.out.println("You stumble upon an enemy!");
                handleHunt();
                break;
            case 1:
                int coins = 10 + new Random().nextInt(41);
                player.gainCoins(coins);
                System.out.println("You find a pouch of " + coins + " coins on the ground!");
                break;
            case 2:
                System.out.println("A wandering merchant appears and sets up a small stall.");
                mainShop.displayShop();
                break;
        }
    }

    private static void handleShop() {
        mainShop.displayShop();
    }

    private static void handleBuy() {
        mainShop.displayShop();
        System.out.print("Item to buy: ");
        String itemName = scanner.nextLine().trim();
        if (player instanceof Banker) {
            double discount = ((Banker) player).getShopDiscount();
            mainShop.setBuyMarkup(1.2 * (1.0 - discount));
        }
        mainShop.buyItem(player, itemName);
        mainShop.setBuyMarkup(1.2);
    }

    private static void handleSell() {
        player.getInventory().displayInventory();
        System.out.print("Item to sell: ");
        String itemName = scanner.nextLine().trim();
        mainShop.sellItem(player, itemName);
    }

    private static void handleDebugGolem() {
        System.out.println("[DEBUG] Overriding stats to Lv.30 Warrior baseline...");
        player.setLevel(30);
        player.setMaxHealth(730);
        player.setAttack(165);
        player.setDefense(102);
        System.out.println("[DEBUG] HP: 730 | ATK: 165 | DEF: 102 | Level: 30");
        System.out.println("[DEBUG] Spawning Zytheron the Ancient Golem...");
        AncientGolem golem = new AncientGolem();
        Battle battle = new Battle(player, golem, scanner);
        battle.start();
        if (!player.isAlive()) {
            System.out.println("You have been defeated. Game over.");
            System.exit(0);
        }
    }

    private static void handleDungeon() {
        System.out.println("=== DUNGEON STAGES ===");
        for (DungeonStage stage : dungeonStages) {
            String status = stage.isCompleted() ? "[CLEARED]" : "[Lv." + stage.getRequiredPlayerLevel() + "+]";
            System.out.println("  Stage " + stage.getStageNumber()
                    + ": " + stage.getStageName() + "  " + status);
        }
        System.out.print("Enter stage number (or 0 to cancel): ");
        String input = scanner.nextLine().trim();
        try {
            int stageNum = Integer.parseInt(input);
            if (stageNum < 1 || stageNum > dungeonStages.size()) return;
            dungeonStages.get(stageNum - 1).run(player, scanner);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    private static void handleInventory() {
        player.getInventory().displayInventory();
    }

    private static void handleProfile() {
        System.out.println("\n=== PROFILE ===");
        System.out.println(player.getStatusDisplay());
        System.out.println("Class Description: " + player.getClassDescription());
        if (player.getEquippedWeapon() != null)
            System.out.println("Weapon : " + player.getEquippedWeapon().getDisplayInfo());
        else
            System.out.println("Weapon : (none)");
        if (player.getEquippedArmor() != null)
            System.out.println("Armor  : " + player.getEquippedArmor().getDisplayInfo());
        else
            System.out.println("Armor  : (none)");
        System.out.println("Dungeon Progress: Stage " + player.getDungeonStage());
        System.out.println("Location: " + gameMap.getCurrentLocationName());
    }

    private static void handleChop() {
        System.out.println("You head to the forest and chop wood...");
        int amount = 1 + new Random().nextInt(3);
        for (int i = 0; i < amount; i++) {
            player.getInventory().addItem(
                    new Material("Oak Wood", "A sturdy piece of lumber.", 5, "Common", "Wood"));
        }
        System.out.println("Gathered " + amount + " Oak Wood.");
    }

    private static void handleFish() {
        System.out.println("You cast your line into the water...");
        Random rand = new Random();
        if (rand.nextDouble() < 0.70) {
            String[] types = {"Bass", "Trout", "Salmon", "Carp"};
            String   fish  = types[rand.nextInt(types.length)];
            player.getInventory().addItem(
                    new Material(fish, "A freshly caught " + fish + ".", 8, "Common", "Fish"));
            System.out.println("You caught a " + fish + "!");
        } else {
            System.out.println("The fish aren't biting today...");
        }
    }

    private static void handleCraft() {
        handleRecipes();
        System.out.print("Recipe to craft (name): ");
        String name = scanner.nextLine().trim();
        for (Recipe recipe : knownRecipes) {
            if (recipe.getRecipeName().equalsIgnoreCase(name)) {
                if (recipe.canCraft(player)) {
                    recipe.craft(player);
                } else {
                    System.out.println("You don't meet the requirements for: "
                            + recipe.getRecipeName());
                    recipe.displayRecipe();
                }
                return;
            }
        }
        System.out.println("No recipe found for '" + name + "'.");
    }

    private static void handleRecipes() {
        System.out.println("=== KNOWN RECIPES ===");
        for (Recipe recipe : knownRecipes) {
            recipe.displayRecipe();
            System.out.println();
        }
    }

    private static void handleEnchant() {
        System.out.println("=== ENCHANTMENTS AVAILABLE ===");
        for (int i = 0; i < enchantments.size(); i++) {
            Enchantment e = enchantments.get(i);
            System.out.println("  [" + (i + 1) + "] " + e.getEnchantName()
                    + " | Cost: " + e.getCost() + " coins");
        }
        System.out.print("Choose enchantment number (or 0 to cancel): ");
        try {
            int eIdx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (eIdx < 0 || eIdx >= enchantments.size()) return;
            Enchantment chosen = enchantments.get(eIdx);

            player.getInventory().displayInventory();
            System.out.print("Item to enchant: ");
            String itemName = scanner.nextLine().trim();
            game.item.Item target = player.getInventory().getItemByName(itemName);

            if (target instanceof game.item.Weapon) {
                chosen.enchantWeapon(player, (game.item.Weapon) target);
            } else if (target instanceof game.item.Armor) {
                chosen.enchantArmor(player, (game.item.Armor) target);
            } else {
                System.out.println("That item cannot be enchanted.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    private static void handleTravel() {
        gameMap.displayMap();
        System.out.print("Travel to: ");
        String dest = scanner.nextLine().trim();
        gameMap.travel(dest);
    }

    private static Enemy spawnEnemy(String type, int levelScale) {
        switch (type) {
            case "Wolf":
                return new Wolf(levelScale);
            case "Bandit":
                return new Bandit(levelScale);
            case "Skeleton":
                return new Skeleton(levelScale);
            case "Troll":
                return new Troll(levelScale);
            default:
                return new Goblin(levelScale);
        }
    }

    private static void printBanner() {
        System.out.println("--------------------------------------------");
        System.out.println("|      TEXT-BASED AI ADVENTURE RPG         |");
        System.out.println("--------------------------------------------");
        System.out.println();
    }

    private static void printHelp() {
        System.out.println("=== COMMANDS ===");
        System.out.println("  hunt       - Battle a random enemy for XP and loot.");
        System.out.println("  adventure  - Explore and trigger a random world event.");
        System.out.println("  shop       - Browse items available for purchase.");
        System.out.println("  buy        - Purchase an item from the shop.");
        System.out.println("  sell       - Sell an item from your inventory.");
        System.out.println("  equip      - Equip a weapon or armor from your inventory.");
        System.out.println("  dungeon    - Face a dungeon boss for big rewards.");
        System.out.println("  inventory  - View your carried items.");
        System.out.println("  profile    - View your character stats and equipment.");
        System.out.println("  chop       - Gather wood from the forest.");
        System.out.println("  fish       - Fish at a lake for food and materials.");
        System.out.println("  craft      - Craft items using materials.");
        System.out.println("  recipes    - View all craftable item recipes.");
        System.out.println("  enchant    - Enchant a weapon or armor piece.");
        System.out.println("  map        - View the world map.");
        System.out.println("  travel     - Move to a connected location.");
        System.out.println("  quit       - Exit the game.");
    }
}
