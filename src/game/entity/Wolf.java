package game.entity;

import java.util.Random;

public class Wolf extends Enemy {

    public Wolf() {
        super("Wolf", 45, 10, 4, 3, 15, 2,
              50, 1, 5, 0.20, "lunges and bites");
    }

    public Wolf(int levelScale) {
        super("Wolf",
              45 + levelScale * 8,
              10 + levelScale * 3,
              4  + levelScale,
              3, 15, levelScale,
              50 + levelScale * 17,
              1,
              5  + levelScale,
              0.20,
              "lunges and bites");
    }

    @Override
    public int performAttack() {
        return attack + new Random().nextInt(6);
    }

    @Override
    public String getSpecialAbilityDescription() {
        return "Pack Instinct: adds 0-5 random bonus damage on every hit.";
    }
}
