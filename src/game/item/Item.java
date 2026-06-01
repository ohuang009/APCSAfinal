package game.item;

public abstract class Item {

    protected String name;
    protected String description;
    protected int    value;
    protected String rarity;
    protected String itemType;

    public Item(String name, String description,
                int value, String rarity, String itemType) {
        this.name        = name;
        this.description = description;
        this.value       = value;
        this.rarity      = rarity;
        this.itemType    = itemType;
    }

    public abstract String getDisplayInfo();

    public String getName()        { return name; }
    public String getDescription() { return description; }
    public int    getValue()       { return value; }
    public String getRarity()      { return rarity; }
    public String getItemType()    { return itemType; }
    public void   setValue(int v)  { this.value = v; }

    @Override
    public String toString() {
        return String.format("[%s] %s (%s) - %d coins", rarity, name, itemType, value);
    }
}
