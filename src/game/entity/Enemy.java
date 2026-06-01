package game.entity;

import game.item.Item;
import java.util.ArrayList;
import java.util.Random;

public abstract class Enemy extends NPC {

    protected int xpReward;
    protected int minCoinDrop;
    protected int maxCoinDrop;
    protected double dropChance;
    protected ArrayList<Item> possibleDrops;
    protected String attackDescription;

    public Enemy(String name, int maxHealth, int attack, int defense,
                 int intelligence, int speed, int level,
                 int xpReward, int minCoinDrop, int maxCoinDrop,
                 double dropChance, String attackDescription) {
        super(name, "Enemy", "...", maxHealth, attack, defense, intelligence, speed, level);
        this.xpReward          = xpReward;
        this.minCoinDrop       = minCoinDrop;
        this.maxCoinDrop       = maxCoinDrop;
        this.dropChance        = dropChance;
        this.possibleDrops     = new ArrayList<>();
        this.attackDescription = attackDescription;
    }

    public abstract int performAttack();
    public abstract String getSpecialAbilityDescription();

    @Override
    public void interact(Player player) {
        System.out.println(name + " snarls and readies for combat!");
    }

    @Override
    public String getNPCType() { return "Enemy"; }

    public int rollCoinDrop() {
        return minCoinDrop + new Random().nextInt(maxCoinDrop - minCoinDrop + 1);
    }

    public Item rollItemDrop() {
        if (possibleDrops.isEmpty()) return null;
        Random rand = new Random();
        if (rand.nextDouble() <= dropChance) {
            return possibleDrops.get(rand.nextInt(possibleDrops.size()));
        }
        return null;
    }

    public void addPossibleDrop(Item item) { possibleDrops.add(item); }

    public int             getXPReward()          { return xpReward; }
    public int             getMinCoinDrop()       { return minCoinDrop; }
    public int             getMaxCoinDrop()       { return maxCoinDrop; }
    public double          getDropChance()        { return dropChance; }
    public ArrayList<Item> getPossibleDrops()     { return possibleDrops; }
    public String          getAttackDescription() { return attackDescription; }
}