# Text-Based AI Adventure RPG

APCS A Final Project - a text-based, turn-based RPG built entirely in Java.

## Overview

The player chooses a character class, explores a 9-location world map, fights enemies, completes dungeon boss stages, buys and sells items, gathers materials, crafts gear, and enchants equipment. All game logic is console-based with typed commands.

**Language:** Java  
**Data types used:** `ArrayList`, arrays, `int`, `boolean`, `double`, `long`, `String`, `Scanner`, `Random`

---

## Player Classes

| Class | HP | ATK | DEF | INT | SPD | Starting Coins | Special Abilities |

| **Warrior** | 150 | 20 | 15 | 5 | 8 | 50 | Power Strike (1.5x ATK), Berserk Bonus (ATK/2 when HP < 25%) |
| **Mage** | 100 | 12 | 8 | 20 | 12 | 100 | Cast Spell (INT x2 damage, costs mana), Mana system (100 MP) |
| **Banker** | 80 | 8 | 8 | 18 | 10 | 250 | Wealth Bonus (extra coins scaled by INT), Shop Discount (up to 30% off) |

All classes gain stats on level up. XP threshold grows by 1.5x each level.

---

## Commands (15 total)

| Command | Description |

| hunt | Battle a random enemy scaled to your current level |
| adventure | Trigger a random world event: battle, treasure, or roaming merchant |
| shop | Browse the main shop's inventory |
| buy | Purchase an item from the shop (Banker receives a discount) |
| sell | Sell an item from your inventory |
| dungeon | Choose and attempt a dungeon boss stage |
| inventory | Display all items you are carrying |
| profile | View your full character stats and equipped gear |
| chop | Gather 1-3 Oak Wood from the forest |
| fish | Cast a line at a lake (70% chance to catch a fish) |
| craft | Craft an item using materials from your inventory |
| recipes | List all known craftable recipes |
| enchant | Apply an enchantment to a weapon or armor piece |
| map | View discovered locations and exits |
| travel | Move to a connected location |
| quit | Exit the game |

---

## World Map (9 Locations)

Hearthstone Village
 +-- Dark Forest
 |    +-- Crystal Caverns
 |    +-- Bandit Outpost
 |         +-- Ancient Ruins
 |              +-- Dragon's Keep (Dungeon 1)
 |                   +-- Demon Realm Gate (Dungeon 2)
 |                        +-- Ancient Sanctum (Dungeon 3)
 +-- Silverfish Lake


| Location | Type | Rec. Level | Notes |
|
| Hearthstone Village | Town | 1 | Starting location, has merchant |
| Dark Forest | Forest | 2 | Goblins and Wolves |
| Crystal Caverns | Cave | 5 | Skeletons and Goblins |
| Bandit Outpost | Camp | 4 | Bandits |
| Ancient Ruins | Ruins | 8 | Skeletons and Bandits |
| Silverfish Lake | Lake | 1 | Fishing spot, weak Goblins |
| Dragon's Keep | Dungeon | 10 | Stage 1 boss |
| Demon Realm Gate | Dungeon | 20 | Stage 2 boss |
| Ancient Sanctum | Dungeon | 30 | Stage 3 boss |

---

## Enemies

### Regular Enemies

| Enemy | HP | ATK | DEF | SPD | Special Ability |
|
| **Goblin** | 30+ | 6+ | 3+ | 12 | Double Strike: 20% chance to attack twice |
| **Wolf** | 45+ | 10+ | 4+ | 15 | Pack Instinct: +0-5 random bonus damage per hit |
| **Bandit** | 55+ | 12+ | 6+ | 11 | Pickpocket: 25% chance (once per battle) to steal coins |
| **Skeleton** | 35+ | 14+ | 2+ | 9 | Critical Strike: 25% chance for 1.75x damage |
| **Troll** | 120+ | 18+ | 10+ | 5 | Regeneration: heals 5% max HP every 3 turns |

All enemies scale their stats with a `levelScale` parameter.

### Dungeon Bosses

| Boss | HP | Phases | Location | Required Level | Special |
|
| **Ignarath the Flame Dragon** | 300 | 2 | Dragon's Keep | 10 | Fire Breath (1.4x), Claw Swipe (+random), Phase 2 at 50% HP: stats surge |
| **Malachar the Demon Lord** | 450 | 3 | Demon Realm Gate | 20 | Dark Curse (stacking +5 dmg/turn), Hellfire Bolt, Shadow Blades (2x ATK); 3 phases |
| **Zytheron the Ancient Golem** | 700 | 2 | Ancient Sanctum | 30 | Stone Shield (absorbs 1 hit every 4 turns), Energy Cannon (1.6x), Phase 2 at 50% HP |

---

## Items

### Weapons

| Name | ATK Bonus | INT Bonus | Rarity | Value |
|
| Iron Sword | +8 | 0 | Common | 50 |
| Oak Staff | +4 | +6 | Common | 60 |
| Hunter's Bow | +7 | 0 | Common | 70 |
| Wooden Bow (craftable) | +5 | 0 | Common | 30 |
| Dragon Fang Sword (dungeon reward) | +22 | 0 | Rare | 300 |
| Golem Core Staff (dungeon reward) | +18 | +25 | Legendary | 800 |

### Armor

| Name | DEF Bonus | HP Bonus | Type | Rarity | Value |
|
| Leather Armor | +5 | 0 | Chestplate | Common | 40 |
| Iron Shield | +8 | 0 | Shield | Common | 55 |
| Reinforced Shield (craftable) | +12 | 0 | Shield | Uncommon | 90 |
| Demonhide Chestplate (dungeon reward) | +20 | +30 | Chestplate | Rare | 450 |

### Consumables

| Name | Effect | Value |
|
| Health Potion | Restores 40 HP | 20 |
| Mana Potion | Restores 30 MP | 25 |
| Strength Brew | ATK +5 for 3 turns | 35 |
| Grilled Fish (craftable) | Restores 30 HP | 12 |

### Materials (gathered)

| Name | Source | Use |
|
| Oak Wood | chop command | Crafting |
| Bass / Trout / Salmon / Carp | fish command | Crafting (Grilled Fish) |

---

## Crafting Recipes

| Recipe | Min Level | Ingredients | Result |
|
| Wooden Bow | 1 | 3x Oak Wood | Wooden Bow (ATK +5) |
| Grilled Fish | 1 | 1x Bass | Grilled Fish (restores 30 HP) |
| Reinforced Shield | 5 | 2x Oak Wood + 1x Iron Ore | Reinforced Shield (DEF +12) |

---

## Enchantment System

Enchanting costs coins and has an **80% success rate** (slightly higher with more INT). Failure costs half the fee. Each item has a max enchant level of 5.

| Enchantment | Stat Boosted | Bonus/Level | Cost |
|
| Sharpening | Attack | +3 | 50 coins |
| Fortification | Defense | +3 | 50 coins |
| Arcane Infusion | Intelligence | +4 | 80 coins |

---

## Dungeon Stages

| Stage | Name | Boss | Required Level | Reward |
|
| 1 | Dragon's Keep | Ignarath the Flame Dragon | Lv. 10 | Dragon Fang Sword |
| 2 | Demon Realm | Malachar the Demon Lord | Lv. 20 | Demonhide Chestplate |
| 3 | Ancient Sanctum | Zytheron the Ancient Golem | Lv. 30 | Golem Core Staff |

Clearing a stage advances your dungeon progress and unlocks new areas.

---

## Battle System

Each battle is turn-based:
1. **Player turn:** Attack, Use Item, or Flee
2. **Enemy turn:** Uses `performAttack()` which may trigger a special ability
3. After the enemy acts, Trolls regenerate HP automatically
4. Boss phase transitions are checked at the start of each turn

**Flee chance:** 40% + (player SPD - enemy SPD)

On victory: XP, coins, and a possible item drop are awarded. On defeat: game over.

---

## Package Structure

src/
  game/
    Main.java                  - Entry point; all command handlers, game setup
    entity/
      Entity.java              - Abstract base (stats, takeDamage, heal)
      Player.java              - Abstract player (XP, coins, inventory, equipment)
      Warrior.java             - Power Strike, Berserk
      Mage.java                - Spells, mana system
      Banker.java              - Wealth Bonus, Shop Discount
      NPC.java                 - Abstract NPC base
      Enemy.java               - Abstract enemy (loot tables)
      Goblin.java
      Wolf.java
      Bandit.java
      Skeleton.java
      Troll.java
      Boss.java                - Abstract multi-phase boss
      DragonBoss.java
      DemonLord.java
      AncientGolem.java        - Stone Shield mechanic
      Merchant.java            - Friendly vendor wrapping a Shop
    item/
      Item.java                - Abstract item base
      Weapon.java
      Armor.java
      Consumable.java
      Material.java
    mechanics/
      Inventory.java           - 30-slot item bag
      Shop.java                - Buy/sell with markup/markdown
      Battle.java              - Turn-based combat loop
      Recipe.java              - Crafting recipes (parallel ArrayLists)
      Enchantment.java         - Weapon/armor enchanting
      DungeonStage.java        - Level-gated boss floor
      Location.java            - World map node
      GameMap.java             - World graph, travel, random encounters
