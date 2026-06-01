package game.mechanics;

import game.entity.Boss;
import game.entity.Player;
import game.item.Item;
import java.util.ArrayList;
import java.util.Scanner;

public class DungeonStage {

    private int             stageNumber;
    private String          stageName;
    private String          stageDescription;
    private Boss            boss;
    private ArrayList<Item> stageRewards;
    private boolean         completed;
    private int             requiredPlayerLevel;

    public DungeonStage(int stageNumber, String stageName,
                        String stageDescription, Boss boss,
                        int requiredPlayerLevel) {
        this.stageNumber         = stageNumber;
        this.stageName           = stageName;
        this.stageDescription    = stageDescription;
        this.boss                = boss;
        this.requiredPlayerLevel = requiredPlayerLevel;
        this.stageRewards        = new ArrayList<>();
        this.completed           = false;
    }

    public boolean run(Player player, Scanner scanner) {
        if (player.getLevel() < requiredPlayerLevel) {
            System.out.println("You must be at least level " + requiredPlayerLevel
                    + " to enter " + stageName + ".");
            return false;
        }
        if (completed) {
            System.out.println(stageName + " has already been cleared.");
            return false;
        }

        System.out.println("\n=== DUNGEON STAGE " + stageNumber + ": "
                + stageName.toUpperCase() + " ===");
        System.out.println(stageDescription);
        System.out.println("Boss: " + boss.getName());
        System.out.println();

        Battle bossBattle = new Battle(player, boss, scanner);
        boolean victory   = bossBattle.start();

        if (victory) {
            completed = true;
            player.setDungeonStage(stageNumber);
            distributeRewards(player);
        }
        return victory;
    }

    private void distributeRewards(Player player) {
        System.out.println("\n=== DUNGEON REWARDS ===");
        for (Item reward : stageRewards) {
            player.getInventory().addItem(reward);
            System.out.println("  + " + reward.getName());
        }
        System.out.println("Dungeon Stage " + stageNumber
                + " cleared! New areas unlocked.");
    }

    public void addReward(Item item) { stageRewards.add(item); }

    public int             getStageNumber()         { return stageNumber; }
    public String          getStageName()           { return stageName; }
    public Boss            getBoss()                { return boss; }
    public boolean         isCompleted()            { return completed; }
    public int             getRequiredPlayerLevel() { return requiredPlayerLevel; }
    public ArrayList<Item> getStageRewards()        { return stageRewards; }
}
