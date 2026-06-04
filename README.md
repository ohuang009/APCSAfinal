# Text-Based AI Adventure RPG

APCS A Final Project. A console-based, turn-based RPG written in Java.

---

## How to Execute

    cd src
    cd game

    java game.Main

The OPENAI_API_KEY environment variable must be set for the third dungeon boss AI to work. If it is not set, the boss goes back to random move selection.

---

## File List

src/game/Main.java
  Entry point. Contains main(), the game loop, all command handlers, and all content
  builders (shop inventory, dungeon stages, enchantments, recipes, world map).

src/game/LLMClient.java
  Makes an HTTP POST request to the OpenAI chat completions API. Returns a single move
  name string for the AncientGolem to use. Falls back to null on any error so the caller
  can handle it gracefully.

src/game/entity/Entity.java
  Abstract base class for every living thing in the game. Holds name, currentHealth,
  maxHealth, attack, defense, intelligence, speed, and level. Defines takeDamage(int),
  heal(int), isAlive(), and getStatusDisplay().

src/game/entity/Player.java
  Abstract class extending Entity. Adds coins, xp, xpToNextLevel, Inventory, equipped
  Weapon, and equipped Armor. Defines gainXP(int), gainCoins(int), spendCoins(int),
  getTotalAttack(), getTotalDefense(), equipWeapon(Weapon), equipArmor(Armor),
  and the abstract levelUp().

src/game/entity/Warrior.java
  Extends Player. Base stats: HP 150, ATK 20, DEF 15, INT 5, SPD 8, 50 coins.
  Level-up gains: HP +20, ATK +5, DEF +3, INT +1.
  Extra methods: powerStrike() returns ATK * 1.5, getBerserkBonus() returns ATK/2
  when HP is below 25%.

src/game/entity/Mage.java
  Extends Player. Base stats: HP 100, ATK 12, DEF 8, INT 20, SPD 12, 100 coins.
  Adds mana and maxMana fields. Level-up gains: HP +12, ATK +4, DEF +2, INT +5,
  mana +15. Extra methods: castSpell(int manaCost) returns INT * 2 damage and
  deducts mana, regenMana(int) restores mana up to the cap.

src/game/entity/Banker.java
  Extends Player. Base stats: HP 80, ATK 8, DEF 8, INT 18, SPD 10, 250 coins.
  Level-up gains: HP +8, ATK +2, DEF +2, INT +4.
  Extra methods: getWealthBonus() returns INT * 2 bonus coins on enemy kill,
  getShopDiscount() returns a discount multiplier capped at 0.30.

src/game/entity/NPC.java
  Abstract class extending Entity. Base for non-hostile characters.

src/game/entity/Enemy.java
  Abstract class extending Entity. Adds xpReward, minCoins, maxCoins, dropChance,
  and attackDescription. Defines performAttack(), getSpecialAbilityDescription(),
  rollCoinDrop(), and rollItemDrop(). All enemy constructors take a levelScale
  int that scales HP, ATK, DEF, and XP reward.

src/game/entity/Goblin.java
  Extends Enemy. Base: HP 30+5L, ATK 6+2L, DEF 3+L. XP: 30+14L.
  performAttack() has a 33% chance to return ATK * 2 (double strike).

src/game/entity/Wolf.java
  Extends Enemy. Base: HP 45+8L, ATK 10+3L, DEF 4+L. XP: 50+17L.
  performAttack() returns ATK + Random(0 to 5).

src/game/entity/Bandit.java
  Extends Enemy. Base: HP 55+10L, ATK 12+3L, DEF 6+2L. XP: 65+22L.
  performAttack() returns ATK + Random(0 to 5).
  attemptPickpocket(Player) has a 25% chance to steal 5-15 coins, fires once per battle.
  Uses boolean hasStolen to track whether it has already stolen this fight.

src/game/entity/Skeleton.java
  Extends Enemy. Base: HP 35+6L, ATK 14+4L, DEF 2+L. XP: 80+25L.
  performAttack() has a 25% chance to deal 1.75x critical damage.

src/game/entity/Troll.java
  Extends Enemy. Base: HP 120+20L, ATK 18+5L, DEF 10+3L. XP: 120+35L.
  Adds regenCooldown field (starts at 2). regenerate() heals 5% maxHP when
  regenCooldown reaches 0, then resets the cooldown to 3.

src/game/entity/Boss.java
  Abstract class extending Enemy. Adds phase, maxPhases, and phaseDialogue array.
  Defines shouldTransitionPhase() which returns true when HP drops to or below
  50% of maxHealth and a new phase is available. enterNextPhase() is abstract.

src/game/entity/DragonBoss.java
  Extends Boss. HP 300, ATK 38, DEF 18, level 10. Two phases.
  enterNextPhase() adds ATK +12 and DEF +5.
  performAttack() picks one of three moves: Fire Breath (1.4x), Claw Swipe
  (ATK + random 0-8), or Tail Slam (ATK).

src/game/entity/DemonLord.java
  Extends Boss. HP 450, ATK 35, DEF 22, INT 25, level 20. Three phases.
  Adds curseStacks field. Each phase increases ATK and INT.
  performAttack() picks from: Dark Curse (stacks +1, deals INT*2),
  Hellfire Bolt (ATK*1.3 + INT), Shadow Blades (ATK*2), or Fist Slam.
  getCurseDamage() returns curseStacks * 5, added to every hit in Battle.

src/game/entity/AncientGolem.java
  Extends Boss. HP 800, ATK 75, DEF 75, level 30. Two phases.
  Phase 2 transition: ATK +25, DEF -20, HP +200.
  Fields: shieldActive, shieldCooldown, chargeReady, currentOpponent (Player
  reference for debuff application), playerAtkDebuffAmount, playerAtkDebuffTurns,
  playerDefDebuffAmount, playerDefDebuffTurns.
  Seven moves: energy_cannon (1.6x), double_fist_slam (1.7x), stone_sweep (1.5x),
  core_overcharge (0 dmg then 2.5x next turn), stone_prison (0.5x + ATK debuff -20
  for 2 turns), seismic_wave (0.8x + DEF debuff -15 for 3 turns), petrify_shell
  (heals 8% maxHP).
  Move selection is delegated to LLMClient using a prompt that includes the full
  battle state. Falls back to Random if the API call fails.
  setBattleContext(Player) must be called before performAttack().
  restorePlayerDebuffs() must be called when the battle ends to undo any active
  stat modifications on the player.

src/game/entity/Merchant.java
  Extends NPC. Wraps a Shop instance. Used for roaming merchant encounters.

src/game/item/Item.java
  Abstract base for all items. Fields: name, description, value, rarity.

src/game/item/Weapon.java
  Extends Item. Fields: attackBonus, intelligenceBonus, enchantLevel (max 5).

src/game/item/Armor.java
  Extends Item. Fields: defenseBonus, healthBonus, armorType, enchantLevel (max 5).

src/game/item/Consumable.java
  Extends Item. Fields: healthRestore, manaRestore.

src/game/item/Material.java
  Extends Item. Represents crafting materials like Oak Wood and fish.

src/game/mechanics/Inventory.java
  Holds an ArrayList of Item with a max capacity of 30.
  Methods: addItem(Item), removeItem(Item), removeItemByName(String),
  getItemByName(String), hasItem(String), countItem(String), displayInventory().

src/game/mechanics/Shop.java
  Holds an ArrayList of Item as stock. Handles buying (adds markup) and selling
  (applies markdown). Banker discount is applied in buyItem().

src/game/mechanics/Battle.java
  Manages the turn-based combat loop. Each round: checks boss phase transition,
  runs playerTurn(), then enemyTurn(). Troll regeneration fires after enemyTurn().
  playerTurn() accepts input 1 (attack), 2 (use item), or 3 (flee).
  enemyTurn() calls setBattleContext on AncientGolem before performAttack(), then
  adds DemonLord curse damage on top of the base damage.
  resolveOutcome() calls restorePlayerDebuffs() on AncientGolem if applicable.

src/game/mechanics/Recipe.java
  Stores a recipe as parallel ArrayLists: ingredientNames and ingredientCounts,
  plus a result Item. isCraftable(Inventory) checks whether all ingredients are
  present. craft(Inventory) removes ingredients and returns the result item.

src/game/mechanics/Enchantment.java
  Stores enchantment name, stat type, bonus per level, and coin cost.
  apply(Item, Player) checks affordability and rolls success (base 80% plus a
  small bonus from player INT). On success, increments the item's enchant level.
  On failure, deducts half the cost.

src/game/mechanics/DungeonStage.java
  Stores a required player level, a Boss to fight, and a reward Item.
  attempt(Player, Scanner) checks the level requirement and runs a Battle.
  On victory it awards XP, coins, and the reward item. isCompleted() tracks
  whether the stage has been cleared.

src/game/mechanics/Location.java
  Represents a map node. Fields: name, type, recommendedLevel, discovered.
  Holds an ArrayList of neighbor location names.

src/game/mechanics/GameMap.java
  Holds an ArrayList of Location objects and tracks the current location.
  travel(String) moves to a neighbor if connected. getRandomEncounterType()
  returns an enemy type string based on the current location type.
  displayMap() prints discovered locations and their connections.

---

## Variables by Type

int: level, attack, defense, intelligence, speed, maxHealth, currentHealth, coins,
     xp, xpToNextLevel, turnCount, regenCooldown, curseStacks, phase, maxPhases,
     xpReward, minCoins, maxCoins, enchantLevel, attackBonus, defenseBonus,
     healthRestore, manaRestore, playerAtkDebuffAmount, playerAtkDebuffTurns,
     playerDefDebuffAmount, playerDefDebuffTurns, shieldCooldown

double: dropChance, successRate, discountRate

boolean: discovered, hasStolen, shieldActive, chargeReady, playerFled, completed

long: (used internally by xpToNextLevel at higher levels when accumulated)

String: name, description, rarity, armorType, attackDescription, type, stat

ArrayList: Inventory.items, Shop.stock, GameMap.locations, Location.neighborNames,
           Recipe.ingredientNames, Recipe.ingredientCounts, Main.knownRecipes,
           Main.dungeonStages, Main.enchantments

array: Boss.phaseDialogue (String[]), AncientGolem.VALID_MOVES (String[])

Scanner: Main.scanner (shared instance passed into Battle and all handlers)

Random: instantiated locally in performAttack() and other places needing randomness


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
 |    +-- Crystal Caverns
 |    +-- Bandit Outpost
 |         +-- Ancient Ruins
 |              +-- Dragon's Keep (Dungeon 1)
 |                   +-- Demon Realm Gate (Dungeon 2)
 |                        +-- Ancient Sanctum (Dungeon 3)
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


## Structure


src/
  game/
    Main.java                  - Entry point; all command handlers, game setup
    entity/
      Entity.java              - Abstract base (stats, takeDamage, heal)
      Player.java              - Abstract player (XP, coins, inventory, equipment)
      Warrior.java             - Power Strike, Berserk
      Mage.java                - Spells, mana system
      Banker.java              - Wealth Bonus, Shop Discount
      NPC.java                 - Abstract NPC base
      Enemy.java               - Abstract enemy (loot tables)
      Goblin.java
      Wolf.java
      Bandit.java
      Skeleton.java
      Troll.java
      Boss.java                - Abstract multi-phase boss
      DragonBoss.java
      DemonLord.java
      AncientGolem.java        - Stone Shield mechanic
      Merchant.java            - Friendly vendor wrapping a Shop
    item/
      Item.java                - Abstract item base
      Weapon.java
      Armor.java
      Consumable.java
      Material.java
    mechanics/
      Inventory.java           - 30-slot item bag
      Shop.java                - Buy/sell with markup/markdown
      Battle.java              - Turn-based combat loop
      Recipe.java              - Crafting recipes (parallel ArrayLists)
      Enchantment.java         - Weapon/armor enchanting
      DungeonStage.java        - Level-gated boss floor
      Location.java            - World map node
      GameMap.java             - World graph, travel, random encounters

