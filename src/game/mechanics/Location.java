package game.mechanics;

import java.util.ArrayList;

public class Location {

    private String            name;
    private String            description;
    private String            locationType;
    private int               recommendedLevel;
    private boolean           discovered;
    private ArrayList<String> connectedLocationNames;
    private boolean           hasMerchant;
    private boolean           hasDungeon;

    public Location(String name, String description,
                    String locationType, int recommendedLevel) {
        this.name                   = name;
        this.description            = description;
        this.locationType           = locationType;
        this.recommendedLevel       = recommendedLevel;
        this.discovered             = false;
        this.connectedLocationNames = new ArrayList<>();
        this.hasMerchant            = false;
        this.hasDungeon             = false;
    }

    public void connectTo(String locationName) {
        if (!connectedLocationNames.contains(locationName)) {
            connectedLocationNames.add(locationName);
        }
    }

    public boolean isConnectedTo(String locationName) {
        return connectedLocationNames.contains(locationName);
    }

    public void discover() {
        if (!discovered) {
            discovered = true;
            System.out.println("  *** Discovered new location: " + name + "! ***");
        }
    }

    public void displayInfo() {
        System.out.println("=== " + name.toUpperCase() + " ===");
        System.out.println(description);
        System.out.println("Type: " + locationType
                + "  |  Recommended Level: " + recommendedLevel);
        System.out.println("Connected to: "
                + String.join(", ", connectedLocationNames));
        if (hasMerchant) System.out.println("[Merchant available]");
        if (hasDungeon)  System.out.println("[Dungeon entrance nearby]");
    }

    public String             getName()                   { return name; }
    public String             getDescription()            { return description; }
    public String             getLocationType()           { return locationType; }
    public int                getRecommendedLevel()       { return recommendedLevel; }
    public boolean            isDiscovered()              { return discovered; }
    public ArrayList<String>  getConnectedLocationNames() { return connectedLocationNames; }
    public boolean            hasMerchant()               { return hasMerchant; }
    public boolean            hasDungeon()                { return hasDungeon; }
    public void               setHasMerchant(boolean b)   { this.hasMerchant = b; }
    public void               setHasDungeon(boolean b)    { this.hasDungeon  = b; }
}
