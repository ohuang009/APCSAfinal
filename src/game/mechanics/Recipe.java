package game.mechanics;

import game.entity.Player;
import game.item.Item;
import java.util.ArrayList;

public class Recipe {

    private String            recipeName;
    private Item              result;
    private ArrayList<String>  ingredientNames;
    private ArrayList<Integer> ingredientQuantities;
    private int               craftingLevel;
    private String            description;

    public Recipe(String recipeName, Item result,
                  int craftingLevel, String description) {
        this.recipeName            = recipeName;
        this.result                = result;
        this.craftingLevel         = craftingLevel;
        this.description           = description;
        this.ingredientNames       = new ArrayList<>();
        this.ingredientQuantities  = new ArrayList<>();
    }

    public void addIngredient(String itemName, int quantity) {
        ingredientNames.add(itemName);
        ingredientQuantities.add(quantity);
    }

    public boolean canCraft(Player player) {
        if (player.getLevel() < craftingLevel) return false;
        for (int i = 0; i < ingredientNames.size(); i++) {
            if (player.getInventory().countItem(ingredientNames.get(i)) < ingredientQuantities.get(i)) {
                return false;
            }
        }
        return true;
    }

    public void craft(Player player) {
        for (int i = 0; i < ingredientNames.size(); i++) {
            int toRemove = ingredientQuantities.get(i);
            while (toRemove-- > 0) {
                player.getInventory().removeItemByName(ingredientNames.get(i));
            }
        }
        player.getInventory().addItem(result);
        System.out.println("Crafted: " + result.getName() + "!");
    }

    public void displayRecipe() {
        System.out.println("=== Recipe: " + recipeName + " ===");
        System.out.println("  Result      : " + result.getName()
                + " [" + result.getRarity() + "]");
        System.out.println("  Min Level   : " + craftingLevel);
        System.out.println("  Ingredients :");
        for (int i = 0; i < ingredientNames.size(); i++) {
            System.out.println("    - " + ingredientNames.get(i) + " x" + ingredientQuantities.get(i));
        }
        System.out.println("  Description : " + description);
    }

    public String             getRecipeName()           { return recipeName; }
    public Item               getResult()               { return result; }
    public ArrayList<String>  getIngredientNames()      { return ingredientNames; }
    public ArrayList<Integer> getIngredientQuantities() { return ingredientQuantities; }
    public int                getCraftingLevel()         { return craftingLevel; }
    public String             getDescription()           { return description; }
}
