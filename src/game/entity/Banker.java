package game.entity;

public class Banker extends Player {

    private static final int    BASE_HEALTH       = 80;
    private static final int    BASE_ATTACK       = 8;
    private static final int    BASE_DEFENSE      = 8;
    private static final int    BASE_INTELLIGENCE = 18;
    private static final int    BASE_SPEED        = 10;
    private static final int    STARTING_COINS    = 250;

    public Banker(String name) {
        super(name, "Banker",
              BASE_HEALTH, BASE_ATTACK, BASE_DEFENSE,
              BASE_INTELLIGENCE, BASE_SPEED, STARTING_COINS);
    }

    @Override
    public void levelUp() {
        level++;
        maxHealth     += 8;
        currentHealth  = maxHealth;
        attack        += 2;
        defense       += 2;
        intelligence  += 4;
        xpToNextLevel  = (int) (xpToNextLevel * 1.5);
        System.out.println(name + " leveled up to Lv." + level + "! Wealth and wit grow sharper.");
    }

    @Override
    public String getClassDescription() {
        return "Banker: Masters of coin and intellect. High starting gold and intelligence "
             + "let them out-trade enemies and secure the best deals at any shop.";
    }

    public int applyWealthBonus(int baseCoins) {
        double multiplier = 1.0 + (intelligence * 0.02);
        return (int) (baseCoins * multiplier);
    }

    public double getShopDiscount() {
        return Math.min(0.30, intelligence * 0.01);
    }
}
