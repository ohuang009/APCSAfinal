package game.item;

public class Consumable extends Item {

    private int healthRestore;
    private int manaRestore;
    private int attackBuff;
    private int defenseBuff;
    private int buffDuration;

    public Consumable(String name, String description, int value, String rarity,
                      int healthRestore, int manaRestore,
                      int attackBuff, int defenseBuff, int buffDuration) {
        super(name, description, value, rarity, "Consumable");
        this.healthRestore = healthRestore;
        this.manaRestore   = manaRestore;
        this.attackBuff    = attackBuff;
        this.defenseBuff   = defenseBuff;
        this.buffDuration  = buffDuration;
    }

    @Override
    public String getDisplayInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("[%s] %s | Consumable | %d coins\n  ", rarity, name, value));
        if (healthRestore > 0) sb.append("HP +").append(healthRestore).append("  ");
        if (manaRestore   > 0) sb.append("MP +").append(manaRestore).append("  ");
        if (attackBuff    > 0) sb.append("ATK +").append(attackBuff)
                                 .append(" (").append(buffDuration).append(" turns)  ");
        if (defenseBuff   > 0) sb.append("DEF +").append(defenseBuff)
                                 .append(" (").append(buffDuration).append(" turns)  ");
        return sb.toString().trim();
    }

    public int getHealthRestore() { return healthRestore; }
    public int getManaRestore()   { return manaRestore; }
    public int getAttackBuff()    { return attackBuff; }
    public int getDefenseBuff()   { return defenseBuff; }
    public int getBuffDuration()  { return buffDuration; }
}
