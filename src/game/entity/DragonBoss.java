package game.entity;

import java.util.Random;

public class DragonBoss extends Boss {

    public DragonBoss() {
        super("Ignarath the Flame Dragon",
              300, 38, 18, 15, 7, 10,
              300, 50, 120,
              2,
              1);
        phaseDialogue[0] = "Ignarath spreads its wings: 'Puny mortal, face my flames!'";
        phaseDialogue[1] = "Ignarath's scales glow red-hot: 'You dare wound me?! BURN!'";
    }

    @Override
    public void enterNextPhase() {
        if (phase < maxPhases) {
            phase++;
            attack  += 12;
            defense += 5;
            System.out.println("\n" + phaseDialogue[phase - 1]);
            System.out.println(">> Ignarath enters Phase " + phase
                    + "! Attack and defence surge!");
        }
    }

    @Override
    public int performAttack() {
        Random rand = new Random();
        switch (rand.nextInt(3)) {
            case 0:
                System.out.println(name + " breathes a torrent of fire!");
                return (int) (attack * 1.4);
            case 1:
                System.out.println(name + " swipes with razor-sharp claws!");
                return attack + rand.nextInt(9);
            default:
                System.out.println(name + " slams with its tail!");
                return attack;
        }
    }

    @Override
    public String getSpecialAbilityDescription() {
        return "Fire Breath (33%): 1.4× damage. Claw Swipe (33%): attack + random. "
             + "Phase 2 at 50% HP: stats surge.";
    }
}
