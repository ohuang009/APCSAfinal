package game.entity;

public abstract class NPC extends Entity {

    protected String npcType;
    protected String dialogue;

    public NPC(String name, String npcType, String dialogue,
               int maxHealth, int attack, int defense,
               int intelligence, int speed, int level) {
        super(name, maxHealth, attack, defense, intelligence, speed, level);
        this.npcType  = npcType;
        this.dialogue = dialogue;
    }

    public abstract void interact(Player player);

    public abstract String getNPCType();

    @Override
    public int takeDamage(int incomingDamage) {
        int damage    = Math.max(1, incomingDamage - defense);
        currentHealth = Math.max(0, currentHealth - damage);
        return damage;
    }

    @Override
    public String getStatusDisplay() {
        return String.format("[%s | %s | Lv.%d | HP: %d/%d]",
                name, npcType, level, currentHealth, maxHealth);
    }

    public String getDialogue() { return dialogue; }
}
