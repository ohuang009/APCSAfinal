import java.util.ArrayList;
import java.util.List;

/**
 * Holds all locations and tracks the player's current position.
 *
 * The map is a simple graph of Location nodes connected by named exits.
 * Use GameMap.buildDefaultMap() to get a pre-built starter world.
 */
public class GameMap {
    private List<Location> locations;
    private Location startLocation;

    public GameMap() {
        locations = new ArrayList<>();
    }

    /** Registers a location with the map. */
    public void addLocation(Location location) {
        locations.add(location);
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }

    public List<Location> getAllLocations() {
        return locations;
    }

    /**
     * Builds and returns a small default world:
     *
     *   [Town Square] --north--> [Dark Forest]
     *                --east-->  [Market]
     *
     *   [Dark Forest] --south--> [Town Square]
     *                 --north--> [Cave Entrance]
     *
     *   [Market] --west--> [Town Square]
     *
     *   [Cave Entrance] --south--> [Dark Forest]
     *                   --north--> [Dragon's Lair]
     */
    public static GameMap buildDefaultMap() {
        GameMap map = new GameMap();

        // ── Locations ──────────────────────────────────────────────────────
        Location townSquare = new Location("Town Square",
                "The bustling center of a small village. Merchants shout their wares.");
        Location darkForest = new Location("Dark Forest",
                "Tall trees block out most of the sunlight. Strange sounds echo nearby.");
        Location market = new Location("Market",
                "Stalls filled with all kinds of goods line the cobblestone street.");
        Location caveEntrance = new Location("Cave Entrance",
                "A gaping maw in the hillside. A cold wind blows outward.");
        Location dragonsLair = new Location("Dragon's Lair",
                "The floor is scorched black. Piles of gold glitter in the dim light.");

        // ── Connections ────────────────────────────────────────────────────
        townSquare.connect("north", darkForest,    "south");
        townSquare.connect("east",  market,        "west");
        darkForest.connect("north", caveEntrance,  "south");
        caveEntrance.connect("north", dragonsLair, "south");

        // ── Shop ───────────────────────────────────────────────────────────
        Shop generalStore = new Shop("General Store");
        generalStore.addItem(new Potion("Health Potion",   "Restores 30 HP",        25, 30));
        generalStore.addItem(new Potion("Mega Potion",     "Restores 80 HP",        60, 80));
        generalStore.addItem(new Weapon("Iron Sword",      "A reliable blade",      50, 10));
        generalStore.addItem(new Weapon("Steel Sword",     "Sharper than iron",    120, 20));
        market.setShop(generalStore);

        // ── NPCs ───────────────────────────────────────────────────────────
        NPC goblin = new NPC("Goblin", "A sneaky green creature", 30, 8, 2, 15);
        goblin.addLoot(new Potion("Health Potion", "Restores 30 HP", 25, 30));
        darkForest.addNpc(goblin);

        NPC troll = new NPC("Troll", "A hulking brute that blocks the path", 60, 14, 5, 40);
        caveEntrance.addNpc(troll);

        NPC dragon = new NPC("Dragon", "An ancient dragon with scales like obsidian", 150, 25, 10, 200);
        dragon.addLoot(new Weapon("Dragon Fang", "A blade carved from a dragon's tooth", 300, 35));
        dragonsLair.addNpc(dragon);

        // ── Register ───────────────────────────────────────────────────────
        map.addLocation(townSquare);
        map.addLocation(darkForest);
        map.addLocation(market);
        map.addLocation(caveEntrance);
        map.addLocation(dragonsLair);

        map.setStartLocation(townSquare);
        return map;
    }
}
