package game.entity;

import game.item.Armor;
import game.item.Weapon;
import game.mechanics.Inventory;

public abstract class Player extends Entity {

    protected int xp;
    protected int xpToNextLevel;
    protected int coins;
    protected Inventory inventory;
    protected Weapon equippedWeapon;
    protected Armor equippedArmor;
    protected int dungeonStage;
    protected String playerClass;

    public Player(String name, String playerClass,
                  int maxHealth, int attack, int defense,
                  int intelligence, int speed, int coins) {
        super(name, maxHealth, attack, defense, intelligence, speed, 1);
        this.playerClass = playerClass;
        this.xp = 0;
        this.xpToNextLevel = 100;
        this.coins = coins;
        this.inventory = new Inventory();
        this.dungeonStage = 0;
    }

    public abstract void levelUp();

    public abstract String getClassDescription();

    @Override
    public int takeDamage(int incomingDamage) {
        int armorBonus = (equippedArmor != null) ? equippedArmor.getDefenseBonus() : 0;
        int damage = Math.max(1, incomingDamage - (defense + armorBonus));
        currentHealth = Math.max(0, currentHealth - damage);
        return damage;
    }

    @Override
    public String getStatusDisplay() {
        return String.format(
            "[%s | %s | Lv.%d | HP: %d/%d | ATK: %d | DEF: %d | INT: %d | Coins: %d | XP: %d/%d]",
            name, playerClass, level, currentHealth, maxHealth,
            getTotalAttack(), getTotalDefense(), intelligence, coins, xp, xpToNextLevel);
    }

    public int getTotalAttack() {
        return attack + ((equippedWeapon != null) ? equippedWeapon.getAttackBonus() : 0);
    }

    public int getTotalDefense() {
        return defense + ((equippedArmor != null) ? equippedArmor.getDefenseBonus() : 0);
    }

    public void gainXP(int amount) {
        xp += amount;
        while (xp >= xpToNextLevel) {
            xp -= xpToNextLevel;
            levelUp();
        }
    }

    public void gainCoins(int amount) { coins += amount; }

    public boolean spendCoins(int amount) {
        if (coins >= amount) {
            coins -= amount;
            return true;
        }
        return false;
    }

    public void equipWeapon(Weapon weapon) {
        this.equippedWeapon = weapon;
        System.out.println(name + " equipped " + weapon.getName() + ".");
    }

    public void equipArmor(Armor armor) {
        this.equippedArmor = armor;
        System.out.println(name + " equipped " + armor.getName() + ".");
    }

    public int getXP() { return xp; }
    public int getXPToNextLevel() { return xpToNextLevel; }
    public int getCoins() { return coins; }
    public Inventory getInventory() { return inventory; }
    public Weapon getEquippedWeapon() { return equippedWeapon; }
    public Armor getEquippedArmor() { return equippedArmor; }
    public int getDungeonStage() { return dungeonStage; }
    public String getPlayerClass() { return playerClass; }
    public void setDungeonStage(int stage) { this.dungeonStage = stage; }
}
