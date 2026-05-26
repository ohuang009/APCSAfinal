import java.util.Random;

/**
 * Controls AI decision-making for NPCs during battle.
 *
 * The AI chooses one of several weighted actions each turn:
 *   - ATTACK  – standard attack (most common)
 *   - HEAVY   – heavy attack dealing 150 % damage but only 40 % chance to hit
 *   - DEFEND  – temporarily boost defense this turn (not yet wired into Battle,
 *               reserved for future expansion)
 *
 * The weights can be adjusted per NPC by subclassing or by changing the constants.
 */
public class AIController {

    /** Possible actions the AI can take on its turn. */
    public enum Action {
        ATTACK,
        HEAVY_ATTACK,
        DEFEND
    }

    private static final Random random = new Random();

    // Probability weights (must sum to 100).
    private static final int WEIGHT_ATTACK       = 60;
    private static final int WEIGHT_HEAVY_ATTACK = 25;
    private static final int WEIGHT_DEFEND       = 15;

    /**
     * Chooses and executes the AI's action for this turn.
     *
     * @param npc    the acting NPC
     * @param target the player being targeted
     */
    public static void takeTurn(NPC npc, Player target) {
        Action action = chooseAction(npc, target);
        executeAction(action, npc, target);
    }

    /**
     * Selects an action based on the NPC's current HP percentage and weighted
     * random rolls.
     */
    private static Action chooseAction(NPC npc, Player target) {
        // If the NPC is below 30 % HP it prefers to defend more often.
        int defendWeight = WEIGHT_DEFEND;
        int attackWeight = WEIGHT_ATTACK;
        int heavyWeight  = WEIGHT_HEAVY_ATTACK;

        double hpRatio = (double) npc.getHp() / npc.getMaxHp();
        if (hpRatio < 0.3) {
            defendWeight += 20;
            attackWeight -= 10;
            heavyWeight  -= 10;
        }

        int roll = random.nextInt(attackWeight + heavyWeight + defendWeight);
        if (roll < attackWeight) {
            return Action.ATTACK;
        } else if (roll < attackWeight + heavyWeight) {
            return Action.HEAVY_ATTACK;
        } else {
            return Action.DEFEND;
        }
    }

    /**
     * Executes the chosen action.
     */
    private static void executeAction(Action action, NPC npc, Player target) {
        switch (action) {
            case ATTACK:
                System.out.println(npc.getName() + " attacks " + target.getName() + "!");
                target.takeDamage(npc.getAttack());
                break;

            case HEAVY_ATTACK:
                System.out.println(npc.getName() + " winds up a heavy attack on "
                        + target.getName() + "!");
                // 40 % hit chance; on hit deals 150 % damage.
                if (random.nextInt(100) < 40) {
                    int heavyDamage = (int) (npc.getAttack() * 1.5);
                    target.takeDamage(heavyDamage);
                } else {
                    System.out.println("...but it missed!");
                }
                break;

            case DEFEND:
                System.out.println(npc.getName() + " takes a defensive stance!");
                // Defense boost is handled conceptually; real stat change would be
                // applied and reversed within Battle if expanded.
                break;

            default:
                System.out.println(npc.getName() + " does nothing.");
        }
    }
}
