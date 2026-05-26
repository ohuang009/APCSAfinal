/**
 * Base class for all characters in the game (Player and NPC).
 * Tracks core combat stats and equipped weapon.
 */
public abstract class Entity {
    private String name;
    private int maxHp;
    private int hp;
    private int baseAttack;
    private int defense;
    private Weapon equippedWeapon;

    public Entity(String name, int hp, int attack, int defense) {
        this.name = name;
        this.maxHp = hp;
        this.hp = hp;
        this.baseAttack = attack;
        this.defense = defense;
        this.equippedWeapon = null;
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    /** Total attack = base attack + weapon bonus (if any). */
    public int getAttack() {
        int total = baseAttack;
        if (equippedWeapon != null) {
            total += equippedWeapon.getAttackBonus();
        }
        return total;
    }

    public int getDefense() {
        return defense;
    }

    public Weapon getEquippedWeapon() {
        return equippedWeapon;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    // ── Setters / Mutators ────────────────────────────────────────────────────

    public void setHp(int hp) {
        this.hp = Math.max(0, Math.min(maxHp, hp));
    }

    public void heal(int amount) {
        setHp(hp + amount);
    }

    public void equipWeapon(Weapon weapon) {
        this.equippedWeapon = weapon;
        System.out.println(name + " equipped " + weapon.getName() + ".");
    }

    /**
     * Applies incoming damage after subtracting defense.
     * Always deals at least 1 damage.
     */
    public void takeDamage(int rawDamage) {
        int damage = Math.max(1, rawDamage - defense);
        hp = Math.max(0, hp - damage);
        System.out.println(name + " takes " + damage + " damage! (HP: " + hp + "/" + maxHp + ")");
    }

    @Override
    public String toString() {
        return name + " [HP " + hp + "/" + maxHp + " | ATK " + getAttack() + " | DEF " + defense + "]";
    }
}
