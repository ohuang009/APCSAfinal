package game.entity;

import java.util.Random;

public class Skeleton extends Enemy {

    public Skeleton() {
        super("Skeleton", 35, 14, 2, 4, 9, 4,
              80, 5, 15, 0.35, "slashes with a bone blade");
    }

    public Skeleton(int levelScale) {
        super("Skeleton",
              35 + levelScale * 6,
              14 + levelScale * 4,
              2 + levelScale,
              4, 9, levelScale,
              80 + levelScale * 25,
              5 + levelScale * 3,
              15 + levelScale * 5,
              0.35,
              "slashes with a bone blade");
    }

    @Override
    public int performAttack() {
        Random rand = new Random();
        int rawDmg = attack + rand.nextInt(5);
        boolean crit = rand.nextInt(4) == 0;
        if (crit) {
            System.out.println(name + " lands a critical strike!");
            return (int) (rawDmg * 1.75);
        }
        return rawDmg;
    }

    @Override
    public String getSpecialAbilityDescription() {
        return "Critical Strike (25% chance): deals 1.75× damage.";
    }
}
