package game.entity;

import game.LLMClient;
import java.util.Random;

public class AncientGolem extends Boss {

    private static final String[] VALID_MOVES = {
        "energy_cannon", "double_fist_slam", "stone_sweep",
        "core_overcharge", "stone_prison", "seismic_wave", "petrify_shell"
    };

    private boolean shieldActive;
    private int     shieldCooldown;
    private Player  currentOpponent;
    private boolean chargeReady;
    private int     playerAtkDebuffAmount;
    private int     playerAtkDebuffTurns;
    private int     playerDefDebuffAmount;
    private int     playerDefDebuffTurns;

    public AncientGolem() {
        super("Zytheron the Ancient Golem",
              800, 75, 75, 10, 4, 30,
              1000, 200, 500,
              2,   // phases
              3);  // unlocks dungeon stage 3
        this.shieldActive          = false;
        this.shieldCooldown        = 0;
        this.currentOpponent       = null;
        this.chargeReady           = false;
        this.playerAtkDebuffAmount = 0;
        this.playerAtkDebuffTurns  = 0;
        this.playerDefDebuffAmount = 0;
        this.playerDefDebuffTurns  = 0;
        phaseDialogue[0] = "The ground trembles: 'INTRUDER DETECTED. ELIMINATING.'";
        phaseDialogue[1] = "Zytheron's core cracks: 'CORE OVERLOAD. MAXIMUM FORCE ENGAGED.'";
    }

    public void setBattleContext(Player player) {
        this.currentOpponent = player;
    }

    @Override
    public void enterNextPhase() {
        if (phase < maxPhases) {
            phase++;
            attack     += 25;
            defense    -= 20;
            maxHealth  += 200;
            currentHealth += 200;
            System.out.println("\n" + phaseDialogue[phase - 1]);
            System.out.println(">> Zytheron enters Phase " + phase
                    + "! Core exposed - defence down, attack devastatingly high!");
        }
    }

    @Override
    public int performAttack() {
        tickDebuffs();
        if (chargeReady) {
            chargeReady = false;
            System.out.println(name + "'s core detonates with overcharged energy!");
            return (int) (attack * 2.5);
        }
        String move = null;
        if (currentOpponent != null) {
            move = LLMClient.askMove(buildSystemPrompt(), buildUserPrompt(), VALID_MOVES);
        }
        if (move == null) {
            move = VALID_MOVES[new Random().nextInt(VALID_MOVES.length)];
        }
        return executeMove(move);
    }

    private void tickDebuffs() {
        if (playerAtkDebuffTurns > 0 && currentOpponent != null) {
            playerAtkDebuffTurns--;
            if (playerAtkDebuffTurns == 0) {
                currentOpponent.setAttack(currentOpponent.getAttack() + playerAtkDebuffAmount);
                System.out.println(">> Stone Prison crumbles - "
                        + currentOpponent.getName() + "'s attack is restored.");
                playerAtkDebuffAmount = 0;
            }
        }
        if (playerDefDebuffTurns > 0 && currentOpponent != null) {
            playerDefDebuffTurns--;
            if (playerDefDebuffTurns == 0) {
                currentOpponent.setDefense(currentOpponent.getDefense() + playerDefDebuffAmount);
                System.out.println(">> Seismic dust clears - "
                        + currentOpponent.getName() + "'s defence is restored.");
                playerDefDebuffAmount = 0;
            }
        }
    }

    private void applyAtkDebuff(int amount, int turns) {
        if (playerAtkDebuffTurns > 0) {
            currentOpponent.setAttack(currentOpponent.getAttack() + playerAtkDebuffAmount);
        }
        playerAtkDebuffAmount = amount;
        playerAtkDebuffTurns  = turns;
        currentOpponent.setAttack(Math.max(1, currentOpponent.getAttack() - amount));
    }

    private void applyDefDebuff(int amount, int turns) {
        if (playerDefDebuffTurns > 0) {
            currentOpponent.setDefense(currentOpponent.getDefense() + playerDefDebuffAmount);
        }
        playerDefDebuffAmount = amount;
        playerDefDebuffTurns  = turns;
        currentOpponent.setDefense(Math.max(0, currentOpponent.getDefense() - amount));
    }

    public void restorePlayerDebuffs() {
        if (currentOpponent == null) return;
        if (playerAtkDebuffTurns > 0) {
            currentOpponent.setAttack(currentOpponent.getAttack() + playerAtkDebuffAmount);
            playerAtkDebuffAmount = 0;
            playerAtkDebuffTurns  = 0;
        }
        if (playerDefDebuffTurns > 0) {
            currentOpponent.setDefense(currentOpponent.getDefense() + playerDefDebuffAmount);
            playerDefDebuffAmount = 0;
            playerDefDebuffTurns  = 0;
        }
    }

    private String buildSystemPrompt() {
        return "You are the combat AI for Zytheron the Ancient Golem, the final dungeon boss "
             + "in a text-based RPG. Your goal is to defeat the player. "
             + "Respond with ONLY one of the exact move names provided - "
             + "no explanation, no punctuation, just the move name.";
    }

    private String buildUserPrompt() {
        int bossHPPct   = (int) ((currentHealth * 100.0) / maxHealth);
        int playerHPPct = (int) ((currentOpponent.getCurrentHealth() * 100.0)
                                / currentOpponent.getMaxHealth());

        String shieldStatus;
        if (shieldActive)
            shieldStatus = "ACTIVE - next hit fully blocked";
        else if (shieldCooldown > 0)
            shieldStatus = "recharging (" + shieldCooldown + " turns remaining)";
        else
            shieldStatus = "ready";

        String phaseNote = (phase == 1)
            ? "Phase 2 at 50% HP: +20 ATK, -10 DEF"
            : "Core overloaded: max ATK, reduced DEF";

        String atkDebuff = (playerAtkDebuffTurns > 0)
            ? "ACTIVE (-" + playerAtkDebuffAmount + " ATK, " + playerAtkDebuffTurns + " turns left)"
            : "none";
        String defDebuff = (playerDefDebuffTurns > 0)
            ? "ACTIVE (-" + playerDefDebuffAmount + " DEF, " + playerDefDebuffTurns + " turns left)"
            : "none";

        int dmgCannon  = (int) (attack * 1.6);
        int dmgSlam    = (int) (attack * 1.7);
        int dmgSweep   = (int) (attack * 1.5);
        int dmgPrison  = attack / 2;
        int dmgSeismic = (int) (attack * 0.8);
        int dmgCharge  = (int) (attack * 2.5);
        int healShell  = (int) (maxHealth * 0.08);

        return "=== BATTLE STATE ===\n"
             + "Zytheron HP  : " + currentHealth + "/" + maxHealth
             + " (" + bossHPPct + "%) | Phase " + phase + "/" + maxPhases
             + " | " + phaseNote + "\n"
             + "Stone Shield : " + shieldStatus + "\n"
             + "Player HP    : " + currentOpponent.getCurrentHealth()
             + "/" + currentOpponent.getMaxHealth()
             + " (" + playerHPPct + "%)\n"
             + "Player stats : ATK " + currentOpponent.getAttack()
             + ", DEF " + currentOpponent.getDefense()
             + ", Level " + currentOpponent.getLevel() + "\n"
             + "ATK debuff   : " + atkDebuff + "\n"
             + "DEF debuff   : " + defDebuff + "\n"
             + "\n=== AVAILABLE MOVES ===\n"
             + "energy_cannon    : ~" + dmgCannon
             + " dmg (1.6x) | finisher when player HP is low\n"
             + "double_fist_slam : ~" + dmgSlam
             + " dmg (ATK+15) | reliable damage\n"
             + "stone_sweep      : ~" + dmgSweep
             + " dmg | safe baseline\n"
             + "core_overcharge  : 0 dmg this turn; NEXT turn fires ~" + dmgCharge
             + " dmg (2.5x) | use when player DEF is high or both debuffs are active\n"
             + "stone_prison     : ~" + dmgPrison
             + " dmg + player ATK -20 for 2 turns | use when player ATK is high and ATK debuff is none\n"
             + "seismic_wave     : ~" + dmgSeismic
             + " dmg + player DEF -15 for 3 turns | use when player DEF is high and DEF debuff is none\n"
             + "petrify_shell    : 0 dmg + heal self +" + healShell
             + " HP | use when your HP is below 30%\n"
             + "\nChoose ONE move name.";
    }

    private int executeMove(String move) {
        switch (move) {
            case "energy_cannon":
                System.out.println(name + " fires an energy cannon blast!");
                return (int) (attack * 1.6);
            case "double_fist_slam":
                System.out.println(name + " slams with both stone fists!");
                return (int) (attack * 1.7);
            case "core_overcharge":
                chargeReady = true;
                System.out.println(name + "'s core begins to glow with unstable energy - it's charging!");
                return 0;
            case "stone_prison":
                System.out.println(name + " encases " + currentOpponent.getName()
                        + " in stone shards! ATK reduced by 20 for 2 turns.");
                applyAtkDebuff(20, 2);
                return attack / 2;
            case "seismic_wave":
                System.out.println(name + " slams the ground sending a shockwave! "
                        + currentOpponent.getName() + "'s DEF reduced by 15 for 3 turns.");
                applyDefDebuff(15, 3);
                return (int) (attack * 0.8);
            case "petrify_shell":
                int healed = (int) (maxHealth * 0.08);
                heal(healed);
                System.out.println(name + " crystallises its outer shell, restoring " + healed + " HP!");
                return 0;
            default:
                System.out.println(name + " sweeps with its stone arm!");
                return (int) (attack * 1.5);
        }
    }

    public boolean tryActivateShield() {
        if (shieldCooldown <= 0 && !shieldActive) {
            shieldActive    = true;
            shieldCooldown  = 4;
            System.out.println(name + " raises its Stone Shield!");
            return true;
        }
        if (shieldCooldown > 0) shieldCooldown--;
        return false;
    }

    @Override
    public int takeDamage(int incomingDamage) {
        if (shieldActive) {
            shieldActive = false;
            System.out.println(name + "'s Stone Shield absorbs the blow completely!");
            return 0;
        }
        return super.takeDamage(incomingDamage);
    }

    @Override
    public String getSpecialAbilityDescription() {
        return "Stone Shield: absorbs one hit every 4 turns. "
             + "Core Overcharge: charges for 2.5x blast next turn. "
             + "Stone Prison: ATK debuff 2 turns. Seismic Wave: DEF debuff 3 turns. "
             + "Petrify Shell: self-heal 8% max HP. "
             + "Phase 2 at 50% HP: +20 ATK, -10 DEF.";
    }

    public boolean isShieldActive() { return shieldActive; }
}
