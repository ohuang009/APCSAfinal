import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a single node (room / area) on the game map.
 *
 * Each location has:
 *   - a unique name and a short description
 *   - zero or one shop
 *   - a list of NPCs that may spawn here
 *   - a set of named exits that connect to adjacent locations
 */
public class Location {
    private String name;
    private String description;
    private Shop shop;
    private List<NPC> npcs;
    private Map<String, Location> exits; // direction -> neighboring location

    public Location(String name, String description) {
        this.name = name;
        this.description = description;
        this.npcs = new ArrayList<>();
        this.exits = new HashMap<>();
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Shop getShop() {
        return shop;
    }

    public boolean hasShop() {
        return shop != null;
    }

    public List<NPC> getNpcs() {
        return npcs;
    }

    public Map<String, Location> getExits() {
        return exits;
    }

    // ── Mutators ──────────────────────────────────────────────────────────────

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public void addNpc(NPC npc) {
        npcs.add(npc);
    }

    public void removeNpc(NPC npc) {
        npcs.remove(npc);
    }

    /**
     * Creates a one-way exit from this location.
     * To make it bidirectional call connect() instead.
     */
    public void addExit(String direction, Location target) {
        exits.put(direction.toLowerCase(), target);
    }

    /**
     * Connects two locations bidirectionally.
     *
     * @param direction      direction FROM this location TO other (e.g. "north")
     * @param other          the neighboring location
     * @param reverseDirection direction FROM other back TO this (e.g. "south")
     */
    public void connect(String direction, Location other, String reverseDirection) {
        this.addExit(direction, other);
        other.addExit(reverseDirection, this);
    }

    /** Looks up an exit by direction name; returns null if no exit exists. */
    public Location getExit(String direction) {
        return exits.get(direction.toLowerCase());
    }

    /** Prints a formatted description of the location. */
    public void describe() {
        System.out.println("\n=== " + name + " ===");
        System.out.println(description);

        if (!npcs.isEmpty()) {
            System.out.println("Enemies here:");
            for (NPC npc : npcs) {
                System.out.println("  - " + npc);
            }
        }

        if (hasShop()) {
            System.out.println("There is a shop here: " + shop.getName());
        }

        System.out.print("Exits: ");
        if (exits.isEmpty()) {
            System.out.println("none");
        } else {
            System.out.println(String.join(", ", exits.keySet()));
        }
    }
}
