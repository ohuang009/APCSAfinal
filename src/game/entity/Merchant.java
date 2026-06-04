package game.entity;

import game.mechanics.Shop;
import java.util.ArrayList;
import java.util.Random;

public class Merchant extends NPC {

    private Shop shop;
    private String merchantType;
    private ArrayList<String> greetings;

    public Merchant(String name, String merchantType, Shop shop) {
        super(name, "Merchant", "Welcome, traveler! Browse my wares.", 50, 5, 5, 10, 8, 1);
        this.merchantType = merchantType;
        this.shop = shop;
        this.greetings = new ArrayList<>();
        greetings.add("Welcome to my shop, adventurer!");
        greetings.add("Finest goods in all the land!");
        greetings.add("Looking to buy or sell today?");
    }

    @Override
    public void interact(Player player) {
        System.out.println(name + " says: \"" + getRandomGreeting() + "\"");
        shop.displayShop();
    }

    @Override
    public String getNPCType() { return "Merchant"; }

    public String getRandomGreeting() {
        return greetings.get(new Random().nextInt(greetings.size()));
    }

    public void addGreeting(String greeting) { greetings.add(greeting); }

    public Shop getShop() { return shop; }
    public String getMerchantType() { return merchantType; }
}
