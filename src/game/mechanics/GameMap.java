package game.mechanics;

import game.entity.Player;
import java.util.ArrayList;
import java.util.Random;

public class GameMap {

    private ArrayList<Location> locations;
    private String currentLocationName;
    private Player player;

    public GameMap(Player player) {
        this.player = player;
        this.locations = new ArrayList<>();
        initializeMap();
    }

    private void initializeMap() {

        // --- Locations -----------------------------------------------
        Location startTown = new Location(
                "Hearthstone Village",
                "A quiet village at the edge of the wilderness. A good place to rest and resupply.",
                "Town", 1);
        startTown.setHasMerchant(true);
        startTown.discover();

        Location darkForest = new Location(
                "Darkwood Forest",
                "Dense trees block the sunlight. Goblins and wolves lurk within.",
                "Forest", 2);

        Location crystalCave = new Location(
                "Crystal Caverns",
                "Glimmering ore veins line the walls. Strange creatures dwell in the deep.",
                "Cave", 5);

        Location banditCamp = new Location(
                "Bandit Outpost",
                "A rough encampment of outlaws. High risk - high reward.",
                "Camp", 4);

        Location ancientRuins = new Location(
                "Ancient Ruins",
                "Crumbling stone monuments hide treasure and danger in equal measure.",
                "Ruins", 8);

        Location lakeside = new Location(
                "Silverfish Lake",
                "A serene lake teeming with fish. The ideal spot to cast a line.",
                "Lake", 1);
        lakeside.discover();

        Location dragonKeep = new Location(
                "Dragon's Keep",
                "A fortress of scorched stone. The air reeks of sulphur.",
                "Dungeon", 10);
        dragonKeep.setHasDungeon(true);

        Location demonRealm = new Location(
                "Demon Realm Gate",
                "A crackling portal to a hellish dimension.",
                "Dungeon", 20);
        demonRealm.setHasDungeon(true);

        Location ancientSanctum = new Location(
                "Ancient Sanctum",
                "The resting place of an immortal golem, frozen in time.",
                "Dungeon", 30);
        ancientSanctum.setHasDungeon(true);

        // --- Connections (bi-directional) ---
        connect(startTown,    darkForest);
        connect(startTown,    lakeside);
        connect(darkForest,   crystalCave);
        connect(darkForest,   banditCamp);
        connect(banditCamp,   ancientRuins);
        connect(ancientRuins, dragonKeep);
        connect(dragonKeep,   demonRealm);
        connect(demonRealm,   ancientSanctum);

        register(startTown, darkForest, crystalCave, banditCamp,
                 ancientRuins, lakeside, dragonKeep, demonRealm, ancientSanctum);

        currentLocationName = startTown.getName();
    }

    private void connect(Location a, Location b) {
        a.connectTo(b.getName());
        b.connectTo(a.getName());
    }

    private void register(Location... locs) {
        for (Location loc : locs) {
            locations.add(loc);
        }
    }

    public boolean travel(String destination) {
        Location current = getCurrentLocation();
        if (!current.isConnectedTo(destination)) {
            System.out.println("You can't travel directly to '" + destination
                    + "' from " + current.getName() + ".");
            return false;
        }
        Location dest = getLocation(destination);
        if (dest == null) {
            System.out.println("Unknown destination: " + destination);
            return false;
        }
        currentLocationName = destination;
        dest.discover();
        System.out.println("You travel to " + destination + ".");
        dest.displayInfo();
        return true;
    }

    public void displayMap() {
        System.out.println("=== WORLD MAP ===");
        System.out.println("  Current location: " + currentLocationName + "\n");
        System.out.println("  Discovered locations:");
        for (Location loc : locations) {
            if (loc.isDiscovered()) {
                String here = loc.getName().equals(currentLocationName)
                              ? " <-- YOU ARE HERE" : "";
                System.out.println("    - " + loc.getName()
                        + "  (Lv." + loc.getRecommendedLevel() + ")" + here);
            }
        }
        System.out.println("\n  Exits from here: "
                + String.join(", ", getCurrentLocation().getConnectedLocationNames()));
    }

    // ------------------------------------------------------------------ //
    //  Random encounter helper                                             //
    // ------------------------------------------------------------------ //

    /**
     * Returns an enemy class name appropriate for the current location type.
     * Used by Main.handleHunt() to spawn the correct enemy.
     */
    public String getRandomEncounterType() {
        String type = getCurrentLocation().getLocationType();
        Random rand = new Random();
        switch (type) {
            case "Forest":  return rand.nextBoolean() ? "Goblin" : "Wolf";
            case "Cave":    return rand.nextBoolean() ? "Skeleton" : "Troll";
            case "Camp":    return "Bandit";
            case "Ruins":   return rand.nextBoolean() ? "Skeleton" : "Bandit";
            case "Lake":    return "Goblin";
            default:        return "Goblin";
        }
    }

    public Location getCurrentLocation() { return getLocation(currentLocationName); }
    public String getCurrentLocationName() { return currentLocationName; }
    public Location getLocation(String name) {
        for (Location loc : locations) {
            if (loc.getName().equals(name)) return loc;
        }
        return null;
    }
    public ArrayList<Location> getAllLocations() { return locations; }
}
