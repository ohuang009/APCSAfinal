package game.entity;

public class Mage extends Player {

    private static final int BASE_HEALTH = 100;
    private static final int BASE_ATTACK = 12;
    private static final int BASE_DEFENSE = 8;
    private static final int BASE_INTELLIGENCE = 20;
    private static final int BASE_SPEED = 12;
    private static final int STARTING_COINS = 100;

    private int mana;
    private int maxMana;

    public Mage(String name) {
        super(name, "Mage",
              BASE_HEALTH, BASE_ATTACK, BASE_DEFENSE,
              BASE_INTELLIGENCE, BASE_SPEED, STARTING_COINS);
        this.maxMana = 100;
        this.mana = maxMana;
    }

    @Override
    public void levelUp() {
        level++;
        maxHealth += 12;
        currentHealth = maxHealth;
        attack += 3;
        defense += 2;
        intelligence += 5;
        maxMana += 15;
        mana = maxMana;
        xpToNextLevel = (int) (xpToNextLevel * 1.5);
        System.out.println(name + " leveled up to Lv." + level + "! Arcane power flows freely.");
    }

    @Override
    public String getStatusDisplay() {
        return super.getStatusDisplay()
             + String.format(" | Mana: %d/%d", mana, maxMana);
    }

    @Override
    public String getClassDescription() {
        return "Mage: Wielders of arcane power. High intelligence and speed let them strike "
             + "first and unleash devastating spells, at the cost of lower raw defence.";
    }

    public int castSpell(int manaCost) {
        if (mana < manaCost) {
            System.out.println("Not enough mana!");
            return 0;
        }
        mana -= manaCost;
        return intelligence * 2;
    }

    public void regenMana(int amount) {
        mana = Math.min(mana + amount, maxMana);
    }

    public int getMana() { return mana; }
    public int getMaxMana() { return maxMana; }
    public void setMana(int mana) {
        this.mana = Math.max(0, Math.min(mana, maxMana));
    }
}
