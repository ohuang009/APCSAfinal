package game.entity;

public class Warrior extends Player {

    private static final int    BASE_HEALTH       = 150;
    private static final int    BASE_ATTACK       = 20;
    private static final int    BASE_DEFENSE      = 15;
    private static final int    BASE_INTELLIGENCE = 5;
    private static final int    BASE_SPEED        = 8;
    private static final int    STARTING_COINS    = 50;

    public Warrior(String name) {
        super(name, "Warrior",
              BASE_HEALTH, BASE_ATTACK, BASE_DEFENSE,
              BASE_INTELLIGENCE, BASE_SPEED, STARTING_COINS);
    }

    @Override
    public void levelUp() {
        level++;
        maxHealth     += 20;
        currentHealth  = maxHealth;
        attack        += 5;
        defense       += 3;
        intelligence  += 1;
        xpToNextLevel  = (int) (xpToNextLevel * 1.5);
        System.out.println(name + " leveled up to Lv." + level + "! Strength surges through your veins.");
    }

    @Override
    public String getClassDescription() {
        return "Warrior: Unstoppable in combat. High health and attack make them the dominant "
             + "force on the battlefield, though they start light on coin.";
    }

    public int powerStrike() {
        return (int) (attack * 1.5);
    }

    public int getBerserkBonus() {
        return ((double) currentHealth / maxHealth < 0.25) ? attack / 2 : 0;
    }
}
