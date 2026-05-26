# APCS A Final – Text-Based Turn-Based AI Adventure RPG

A framework for a text-based, turn-based RPG written in Java.
Players explore a virtual map, engage in AI-driven battles, shop for items, and progress through the world.

---

## Class Structure

| File | Role |
|---|---|
| `Entity.java` | Abstract base for all characters – name, HP, attack, defense, equipped weapon |
| `Player.java` | Human-controlled character; extends Entity with an inventory and gold |
| `NPC.java` | AI-controlled enemy/character; extends Entity with a loot table |
| `Item.java` | Base item class (name, description, price) |
| `Weapon.java` | Equippable item that adds an attack bonus |
| `Potion.java` | Consumable item that restores HP |
| `Inventory.java` | Manages a list of items for a player or shop |
| `Shop.java` | Interactive buy/sell menu using player gold |
| `Location.java` | A single map node with a description, exits, NPCs, and an optional shop |
| `GameMap.java` | Graph of connected Locations; includes a default 5-room world |
| `AIController.java` | Weighted-random AI that picks ATTACK / HEAVY_ATTACK / DEFEND each turn |
| `Battle.java` | Turn-based combat loop – player first, then AI-controlled NPC |
| `Game.java` | Main game loop; handles all text commands and ties every system together |

---

## How to Compile & Run

```bash
cd src
javac *.java
java Game
```

Requires **Java 8** or newer.

---

## Gameplay Commands

| Command | Description |
|---|---|
| `look` | Describe the current location |
| `go <direction>` | Move (e.g. `go north`) |
| `fight` | Battle the first enemy at this location |
| `shop` | Open the shop here (if present) |
| `inventory` | List your items |
| `status` | Show your current stats |
| `equip <weapon>` | Equip a weapon from your inventory |
| `use potion` | Drink a potion outside of battle |
| `help` | Show all commands |
| `quit` | Exit the game |

---

## Default World Map

```
     [Dragon's Lair]
           |  south/north
     [Cave Entrance]
           |  south/north
     [Dark Forest]
           |  south/north
     [Town Square] --east/west-- [Market / Shop]
```
