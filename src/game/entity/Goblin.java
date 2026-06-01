package game.entity;

import java.util.Random;

public class Goblin extends Enemy {

    public Goblin() {
        super("Goblin", 30, 6, 3, 5, 12, 1,
              30, 2, 8, 0.30, "stabs with a rusty dagger");
    }

    public Goblin(int levelScale) {
        super("Goblin",
              30 + levelScale * 5,
              6  + levelScale * 2,
              3  + levelScale,
              5, 12, levelScale,
              30 + levelScale * 14,
              2  + levelScale,
              8  + levelScale * 2,
              0.30,
              "stabs with a rusty dagger");
    }

    @Override
    public int performAttack() {
        boolean doubleStrike = new Random().nextInt(3) == 0;
        if (doubleStrike) {
            System.out.println(name + " attacks twice!");
            return attack * 2;
        }
        return attack;
    }

    @Override
    public String getSpecialAbilityDescription() {
        return "Double Strike (33% chance): attacks twice in one turn.";
    }
}
