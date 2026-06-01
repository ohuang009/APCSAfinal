package game.item;

public class Armor extends Item {

    private int    defenseBonus;
    private int    healthBonus;
    private String armorType;
    private int    durability;
    private int    maxDurability;
    private int    enchantLevel;

    public Armor(String name, String description, int value, String rarity,
                 String armorType, int defenseBonus, int healthBonus) {
        super(name, description, value, rarity, "Armor");
        this.armorType     = armorType;
        this.defenseBonus  = defenseBonus;
        this.healthBonus   = healthBonus;
        this.maxDurability = 100;
        this.durability    = maxDurability;
        this.enchantLevel  = 0;
    }

    @Override
    public String getDisplayInfo() {
        return String.format(
            "[%s] %s | %s | DEF +%d | HP +%d | Enchant: +%d | Dur: %d/%d | %d coins",
            rarity, name, armorType,
            defenseBonus, healthBonus,
            enchantLevel, durability, maxDurability, value);
    }

    public int getTotalDefenseBonus() {
        return defenseBonus + (enchantLevel * 2);
    }

    public void applyEnchantment(int levels) {
        enchantLevel += levels;
    }

    public void reduceDurability(int amount) {
        durability = Math.max(0, durability - amount);
    }

    public boolean isBroken() { return durability <= 0; }

    public int    getDefenseBonus()      { return defenseBonus; }
    public int    getHealthBonus()       { return healthBonus; }
    public String getArmorType()         { return armorType; }
    public int    getDurability()        { return durability; }
    public int    getMaxDurability()     { return maxDurability; }
    public int    getEnchantLevel()      { return enchantLevel; }
    public void   setDefenseBonus(int b) { this.defenseBonus = b; }
}
