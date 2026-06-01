package game.item;

public class Material extends Item {

    private String materialType;
    private int    quantity;

    public Material(String name, String description, int value,
                    String rarity, String materialType) {
        super(name, description, value, rarity, "Material");
        this.materialType = materialType;
        this.quantity     = 1;
    }

    public Material(String name, String description, int value,
                    String rarity, String materialType, int quantity) {
        this(name, description, value, rarity, materialType);
        this.quantity = quantity;
    }

    @Override
    public String getDisplayInfo() {
        return String.format("[%s] %s | %s | Qty: %d | %d coins each",
                rarity, name, materialType, quantity, value);
    }

    public void addQuantity(int amount)  { quantity += amount; }
    public void setQuantity(int amount)  { quantity = Math.max(0, amount); }

    public String getMaterialType() { return materialType; }
    public int    getQuantity()     { return quantity; }
}
