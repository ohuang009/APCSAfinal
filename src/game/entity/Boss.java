package game.entity;

public abstract class Boss extends Enemy {

    protected int      phase;
    protected int      maxPhases;
    protected String[] phaseDialogue;
    protected int      dungeonStageUnlock;

    public Boss(String name, int maxHealth, int attack, int defense,
                int intelligence, int speed, int level,
                int xpReward, int minCoinDrop, int maxCoinDrop,
                int maxPhases, int dungeonStageUnlock) {
        super(name, maxHealth, attack, defense, intelligence, speed, level,
              xpReward, minCoinDrop, maxCoinDrop,
              1.0,                          // bosses always drop an item
              "strikes with tremendous force");
        this.phase               = 1;
        this.maxPhases           = maxPhases;
        this.dungeonStageUnlock  = dungeonStageUnlock;
        this.phaseDialogue       = new String[maxPhases];
    }

    public abstract void enterNextPhase();

    public boolean shouldTransitionPhase() {
        if (phase >= maxPhases) return false;
        double hpFraction  = (double) currentHealth / maxHealth;
        double threshold   = 1.0 - ((double) phase / maxPhases);
        return hpFraction <= threshold;
    }

    public int    getPhase()              { return phase; }
    public int    getMaxPhases()          { return maxPhases; }
    public int    getDungeonStageUnlock() { return dungeonStageUnlock; }

    public String getPhaseDialogue(int phaseIndex) {
        if (phaseIndex >= 0 && phaseIndex < phaseDialogue.length
                && phaseDialogue[phaseIndex] != null) {
            return phaseDialogue[phaseIndex];
        }
        return "";
    }
}
