package game.item;

public class Weapon extends Item {

    private int attackBonus;
    private int intelligenceBonus;
    private String weaponType;
    private int durability;
    private int maxDurability;
    private int enchantLevel;

    public Weapon(String name, String description, int value, String rarity,
                  String weaponType, int attackBonus, int intelligenceBonus) {
        super(name, description, value, rarity, "Weapon");
        this.weaponType = weaponType;
        this.attackBonus = attackBonus;
        this.intelligenceBonus = intelligenceBonus;
        this.maxDurability = 100;
        this.durability = maxDurability;
        this.enchantLevel = 0;
    }

    @Override
    public String getDisplayInfo() {
        return String.format(
            "[%s] %s | %s | ATK +%d | INT +%d | Enchant: +%d | Dur: %d/%d | %d coins",
            rarity, name, weaponType,
            attackBonus, intelligenceBonus,
            enchantLevel, durability, maxDurability, value);
    }

    public int getTotalAttackBonus() {
        return attackBonus + (enchantLevel * 2);
    }

    public void applyEnchantment(int levels) {
        enchantLevel += levels;
    }

    public void reduceDurability(int amount) {
        durability = Math.max(0, durability - amount);
    }

    public boolean isBroken() { return durability <= 0; }

    public int getAttackBonus() { return attackBonus; }
    public int getIntelligenceBonus() { return intelligenceBonus; }
    public String getWeaponType() { return weaponType; }
    public int getDurability() { return durability; }
    public int getMaxDurability() { return maxDurability; }
    public int getEnchantLevel() { return enchantLevel; }
    public void setAttackBonus(int b) { this.attackBonus = b; }
}
