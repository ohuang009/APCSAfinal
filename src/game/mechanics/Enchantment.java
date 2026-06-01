package game.mechanics;

import game.entity.Player;
import game.item.Armor;
import game.item.Weapon;
import java.util.Random;

public class Enchantment {

    private String enchantName;
    private String enchantType;
    private int    bonusPerLevel;
    private int    cost;
    private int    maxEnchantLevel;
    private String description;

    public Enchantment(String enchantName, String enchantType,
                       int bonusPerLevel, int cost,
                       int maxEnchantLevel, String description) {
        this.enchantName    = enchantName;
        this.enchantType    = enchantType;
        this.bonusPerLevel  = bonusPerLevel;
        this.cost           = cost;
        this.maxEnchantLevel = maxEnchantLevel;
        this.description    = description;
    }

    public boolean enchantWeapon(Player player, Weapon weapon) {
        if (!canAfford(player)) return false;
        if (weapon.getEnchantLevel() >= maxEnchantLevel) {
            System.out.println(weapon.getName() + " is already at max enchantment level ("
                    + maxEnchantLevel + ").");
            return false;
        }
        if (rollSuccess(player)) {
            player.spendCoins(cost);
            weapon.applyEnchantment(bonusPerLevel);
            System.out.println("Success! " + weapon.getName()
                    + " enchanted with " + enchantName + "!");
            return true;
        } else {
            player.spendCoins(cost / 2);
            System.out.println("Enchantment failed. Lost " + (cost / 2) + " coins.");
            return false;
        }
    }

    public boolean enchantArmor(Player player, Armor armor) {
        if (!canAfford(player)) return false;
        if (armor.getEnchantLevel() >= maxEnchantLevel) {
            System.out.println(armor.getName() + " is already at max enchantment level ("
                    + maxEnchantLevel + ").");
            return false;
        }
        if (rollSuccess(player)) {
            player.spendCoins(cost);
            armor.applyEnchantment(bonusPerLevel);
            System.out.println("Success! " + armor.getName()
                    + " enchanted with " + enchantName + "!");
            return true;
        } else {
            player.spendCoins(cost / 2);
            System.out.println("Enchantment failed. Lost " + (cost / 2) + " coins.");
            return false;
        }
    }

    private boolean canAfford(Player player) {
        if (player.getCoins() < cost) {
            System.out.println("Not enough coins to enchant. Need: " + cost
                    + ", have: " + player.getCoins());
            return false;
        }
        return true;
    }

    private boolean rollSuccess(Player player) {
        double chance = 0.80 + (player.getIntelligence() * 0.002);
        return new Random().nextDouble() < Math.min(chance, 0.95);
    }

    public String getEnchantName()    { return enchantName; }
    public String getEnchantType()    { return enchantType; }
    public int    getBonusPerLevel()  { return bonusPerLevel; }
    public int    getCost()           { return cost; }
    public int    getMaxEnchantLevel(){ return maxEnchantLevel; }
    public String getDescription()    { return description; }
}
