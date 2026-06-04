package game.entity;

import java.util.Random;

public class Troll extends Enemy {

    private int regenCooldown;

    public Troll() {
        super("Troll", 120, 18, 10, 3, 5, 5,
              120, 8, 20, 0.25, "smashes with a massive club");
        this.regenCooldown = 2;
    }

    public Troll(int levelScale) {
        super("Troll",
              120 + levelScale * 20,
              18 + levelScale * 5,
              10 + levelScale * 3,
              3, 5, levelScale,
              120 + levelScale * 35,
              8 + levelScale * 4,
              20 + levelScale * 8,
              0.25,
              "smashes with a massive club");
        this.regenCooldown = 2;
    }

    @Override
    public int performAttack() {
        return attack + new Random().nextInt(7);
    }

    public int regenerate() {
        if (regenCooldown <= 0) {
            int healAmount = (int) (maxHealth * 0.05);
            heal(healAmount);
            regenCooldown = 3;
            System.out.println(name + " regenerates " + healAmount + " HP!");
            return healAmount;
        }
        regenCooldown--;
        return 0;
    }

    @Override
    public String getSpecialAbilityDescription() {
        return "Regeneration: heals 5% of max HP every 3 turns.";
    }
}
