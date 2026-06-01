package game.entity;

import java.util.Random;

public class DemonLord extends Boss {

    private int curseStacks;

    public DemonLord() {
        super("Malachar the Demon Lord",
              450, 35, 22, 25, 10, 20,
              600, 100, 250,
              3,
              2);
        this.curseStacks = 0;
        phaseDialogue[0] = "Malachar sneers: 'A brave fool enters my realm!'";
        phaseDialogue[1] = "Malachar's eyes burn crimson: 'Kneel before my true form!'";
        phaseDialogue[2] = "Malachar screams: 'I AM ETERNAL - you cannot kill what cannot die!'";
    }

    @Override
    public void enterNextPhase() {
        if (phase < maxPhases) {
            phase++;
            attack       += 15;
            intelligence += 10;
            curseStacks   = 0;
            System.out.println("\n" + phaseDialogue[phase - 1]);
            System.out.println(">> Malachar enters Phase " + phase + "! Power intensifies!");
        }
    }

    @Override
    public int performAttack() {
        Random rand = new Random();
        switch (rand.nextInt(4)) {
            case 0:
                System.out.println(name + " casts a Dark Curse!");
                curseStacks++;
                return intelligence * 2;
            case 1:
                System.out.println(name + " launches a Hellfire Bolt!");
                return (int) (attack * 1.3) + intelligence;
            case 2:
                System.out.println(name + " summons Shadow Blades!");
                return attack * 2;
            default:
                System.out.println(name + " strikes with a demonic fist!");
                return attack;
        }
    }

    public int getCurseDamage() {
        return curseStacks * 5;
    }

    @Override
    public String getSpecialAbilityDescription() {
        return "Dark Curse: stacks dealing 5 bonus damage each. "
             + "Hellfire Bolt: INT-scaled. Shadow Blades: 2× attack. "
             + "3 escalating phases.";
    }

    public int getCurseStacks() { return curseStacks; }
}
