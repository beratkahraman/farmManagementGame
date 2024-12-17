public class Crop {
    private String name;
    private int growthTime; // Büyüme için gereken süre (saniye)
    private int sellPrice;  // Satış fiyatı
    private int currentGrowth; // Mevcut büyüme durumu

    public Crop(String name, int growthTime, int sellPrice) {
        this.name = name;
        this.growthTime = growthTime;
        this.sellPrice = sellPrice;
        this.currentGrowth = 0;
    }

    public String getName() {
        return name;
    }

    public int getSellPrice() {
        return sellPrice;
    }

    public boolean isGrown() {
        return currentGrowth >= growthTime;
    }

    public void grow() {
        if (!isGrown()) {
            currentGrowth++;
        }
    }
}
