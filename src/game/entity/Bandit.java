package game.entity;

import java.util.Random;

public class Bandit extends Enemy {

    private boolean hasStolen;

    public Bandit() {
        super("Bandit", 55, 12, 6, 8, 11, 3,
              65, 10, 25, 0.40, "slashes with a short sword");
        this.hasStolen = false;
    }

    public Bandit(int levelScale) {
        super("Bandit",
              55 + levelScale * 10,
              12 + levelScale * 3,
              6 + levelScale * 2,
              8, 11, levelScale,
              65 + levelScale * 22,
              10 + levelScale * 5,
              25 + levelScale * 8,
              0.40,
              "slashes with a short sword");
        this.hasStolen = false;
    }

    @Override
    public int performAttack() {
        return attack + new Random().nextInt(6);
    }

    public int attemptPickpocket(Player player) {
        if (hasStolen) return 0;
        Random rand = new Random();
        if (rand.nextDouble() < 0.25) {
            int stolen = Math.min(player.getCoins(), 5 + rand.nextInt(11));
            player.spendCoins(stolen);
            hasStolen = true;
            System.out.println(name + " pickpockets " + stolen + " coins from you!");
            return stolen;
        }
        return 0;
    }

    @Override
    public String getSpecialAbilityDescription() {
        return "Pickpocket (25% chance, once per battle): steals coins from the player.";
    }
}
