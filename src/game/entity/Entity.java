package game.entity;

public abstract class Entity {

    protected String name;
    protected int maxHealth;
    protected int currentHealth;
    protected int attack;
    protected int defense;
    protected int intelligence;
    protected int speed;
    protected int level;

    public Entity(String name, int maxHealth, int attack, int defense,
                  int intelligence, int speed, int level) {
        this.name          = name;
        this.maxHealth     = maxHealth;
        this.currentHealth = maxHealth;
        this.attack        = attack;
        this.defense       = defense;
        this.intelligence  = intelligence;
        this.speed         = speed;
        this.level         = level;
    }

    public abstract int takeDamage(int incomingDamage);

    public abstract String getStatusDisplay();

    public boolean isAlive() {
        return currentHealth > 0;
    }

    public void heal(int amount) {
        currentHealth = Math.min(currentHealth + amount, maxHealth);
    }

    public String getName()         { return name; }
    public int getMaxHealth()       { return maxHealth; }
    public int getCurrentHealth()   { return currentHealth; }
    public int getAttack()          { return attack; }
    public int getDefense()         { return defense; }
    public int getIntelligence()    { return intelligence; }
    public int getSpeed()           { return speed; }
    public int getLevel()           { return level; }

    public void setCurrentHealth(int hp) {
        this.currentHealth = Math.max(0, Math.min(hp, maxHealth));
    }
    public void setMaxHealth(int hp)        { this.maxHealth = hp; this.currentHealth = hp; }
    public void setAttack(int attack)       { this.attack = attack; }
    public void setDefense(int defense)     { this.defense = defense; }
    public void setLevel(int level)         { this.level = level; }
}
