import java.util.HashMap;
import java.util.Map;

public class Inventory {
    private int money;
    private Map<String, Integer> items; // Mahsuller ve ürünler
    private Map<String, Boolean> unlockedItems; // Kilidi açık mahsuller

    public Inventory(int initialMoney) {
        this.money = initialMoney;
        this.items = new HashMap<>();
        this.unlockedItems = new HashMap<>();

        // Varsayılan olarak sadece "Wheat" kilidi açık
        unlockedItems.put("Wheat", true);
        unlockedItems.put("Corn", false);
        unlockedItems.put("Tomato", false);
        unlockedItems.put("Potato", false);
    }

    public int getMoney() {
        return money;
    }

    public void decreaseMoney(int amount) {
        money -= amount;
    }

    public void addItem(String itemName, int quantity) {
        items.put(itemName, items.getOrDefault(itemName, 0) + quantity);
    }

    public int getItemQuantity(String itemName) {
        return items.getOrDefault(itemName, 0); // Ürün miktarını döndür
    }



    public Map<String, Integer> getItems() {
        return items;
    }

    public boolean isUnlocked(String itemName) {
        return unlockedItems.getOrDefault(itemName, false);
    }

    public void unlockItem(String itemName) {
        unlockedItems.put(itemName, true);
    }

    public Map<String, Boolean> getUnlockedItems() {
        return unlockedItems;
    }

    public int getSellPrice(String itemName) {
        return switch (itemName) {
            case "Wheat" -> 30;
            case "Corn" -> 50;
            case "Tomato" -> 75;
            case "Potato" -> 100;
            case "Egg" -> 20;
            case "Milk" -> 50;
            case "Wool" -> 100;
            case "Cheese" -> 300;
            case "Cake" -> 500;
            case "Ketchup" -> 300;
            case "Popcorn" -> 250;
            case "Chips" -> 350;
            case "Bread" -> 200;
            case "Cloth" -> 400;
            case "Iron" -> 50;      // Yeni
            case "Gold" -> 200;     // Yeni
            case "Diamond" -> 500;  // Yeni
            case "Coal" -> 30;      // Yeni
            case "Copper" -> 40;    // Yeni
            default -> 0;
        };
    }

}
