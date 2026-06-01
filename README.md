# Text-Based AI Adventure RPG

APCS A Final Project. A console-based, turn-based RPG written in Java.

---

## How to Compile

From the project root folder:

    javac -cp src -d out $(find src -name "*.java")

On Windows (PowerShell):

    $files = Get-ChildItem -Recurse -Filter "*.java" -Path src | ForEach-Object { $_.FullName }
    javac -cp src -d out $files

## How to Execute

    java -cp out game.Main

The OPENAI_API_KEY environment variable must be set for the third dungeon boss AI to work. If it is not set, the boss falls back to random move selection.

---

## Dependencies

- Java 11 or higher (uses java.net.http.HttpClient for the LLM call)
- No external libraries or build tools required
- OpenAI API key (optional, only needed for AncientGolem AI moves)

Data types used throughout the project: int, double, boolean, long, String, array, ArrayList, Scanner, Random

No HashMap, List interface, Set, Queue, or LinkedList are used anywhere.

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

---

## Method Rationale

Constructors set all fields to their initial values. Every class that adds fields
provides its own constructor rather than relying on setters after construction.

Getters and setters are used for all fields that need to be read or modified from
outside the class. Fields are private; access goes through methods.

Abstract methods (levelUp, performAttack, enterNextPhase, getClassDescription,
getSpecialAbilityDescription) are declared in the parent class and overridden in
each subclass so the battle loop and game logic can call them without knowing the
specific subclass type.

takeDamage(int) is defined in Entity and uses Math.max(1, incomingDamage - defense)
so every hit deals at least 1 damage. Player overrides this to include the equipped
armor bonus.

performAttack() in each Enemy subclass encapsulates all randomness and special
ability logic. Battle.java calls this one method and never needs to know what type
of enemy it is dealing with, except for the three cases that require post-processing
(Troll regeneration, Bandit pickpocket, AncientGolem debuff context).

shouldTransitionPhase() in Boss is called at the start of every turn so that a
phase change happens before the player acts, giving the player a chance to see the
announcement before taking damage at the new stats.

LLMClient.askMove() is static and stateless. It builds the HTTP request, sends it,
parses the response string, and validates that the returned word is one of the
allowed move names. Any exception causes it to return null.
