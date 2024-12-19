import java.util.HashMap;
import java.util.Map;

public class Factory {
    private Inventory inventory; // Oyuncunun envanterini yönetmek için
    private Map<String, String> productionRules; // Ürün dönüşüm kuralları (Ham madde -> Nihai ürün)
    private Map<String, Integer> productionTimes; // Nihai ürünlerin üretim süreleri
    private Map<String, Integer> productionRequirements; // Üretim için gerekli miktarlar

    // Constructor
    public Factory(Inventory inventory) {
        this.inventory = inventory; // Envanter referansı
        this.productionRules = new HashMap<>();
        this.productionTimes = new HashMap<>();
        this.productionRequirements = new HashMap<>();
        initializeProductionRules(); // Üretim kurallarını başlat
    }

    // Ürün dönüşüm kurallarını ve üretim detaylarını başlatır
    private void initializeProductionRules() {
        // Ürün dönüşüm kuralları (Ham madde -> Nihai ürün)
        productionRules.put("Wheat", "Bread");
        productionRules.put("Tomato", "Ketchup");
        productionRules.put("Corn", "Popcorn");
        productionRules.put("Potato", "Chips");
        productionRules.put("Milk", "Cheese");
        productionRules.put("Egg", "Cake");
        productionRules.put("Wool", "Cloth");

        // Üretim süreleri
        productionTimes.put("Bread", 10);
        productionTimes.put("Ketchup", 20);
        productionTimes.put("Popcorn", 8);
        productionTimes.put("Chips", 15);
        productionTimes.put("Cheese", 12);
        productionTimes.put("Cake", 18);
        productionTimes.put("Cloth", 15);

        // Gerekli ham madde miktarları
        productionRequirements.put("Wheat", 5);
        productionRequirements.put("Tomato", 10);
        productionRequirements.put("Corn", 5);
        productionRequirements.put("Potato", 7);
        productionRequirements.put("Milk", 5);
        productionRequirements.put("Egg", 10);
        productionRequirements.put("Wool", 4);
    }

    // Nihai ürünün üretilmesi için gerekli ham madde miktarını döndürür
    public int getRequiredQuantity(String rawMaterial) {
        return productionRequirements.getOrDefault(rawMaterial, 0);
    }

    // Nihai ürünün hangi ham maddeden üretildiğini döndürür
    public String getRawMaterial(String product) {
        for (Map.Entry<String, String> entry : productionRules.entrySet()) {
            if (entry.getValue().equals(product)) {
                return entry.getKey(); // Ürün -> Ham madde eşleşmesi
            }
        }
        return null; // Eğer eşleşme yoksa null döndür
    }

    // Üretim işlemini başlatır
    public boolean startProduction(String rawMaterial) {
        // Ürün dönüşüm kuralı kontrolü
        if (!productionRules.containsKey(rawMaterial)) {
            System.out.println("Bu ham madde işlenemez!");
            return false;
        }

        String product = productionRules.get(rawMaterial); // Nihai ürün
        int requiredQuantity = productionRequirements.get(rawMaterial); // Gerekli miktar

        // Envanter kontrolü: Yeterli miktar var mı?
        if (inventory.getItemQuantity(rawMaterial) < requiredQuantity) {
            System.out.println("Yeterli miktarda " + rawMaterial + " yok!");
            return false;
        }

        // Ham madde miktarını azalt
        inventory.addItem(rawMaterial, -requiredQuantity);

        // Üretim süresi tamamlandıktan sonra nihai ürünü ekle
        new Thread(() -> {
            try {
                Thread.sleep(productionTimes.get(product) * 1000); // Üretim süresi
                inventory.addItem(product, 1); // Nihai ürünü envantere ekle
                System.out.println(product + " üretildi ve depoya eklendi!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        return true;
    }

    // Üretim kurallarını döndürür
    public Map<String, String> getProductionRules() {
        return productionRules;
    }
}
