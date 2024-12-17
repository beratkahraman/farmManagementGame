public class Animal {
    private String name;
    private String product; // Ürün türü (Yumurta, Süt, Yün)
    private int productionTime; // Üretim süresi (saniye)
    private int price; // Hayvanın satın alma fiyatı

    public Animal(String name, String product, int productionTime, int price) {
        this.name = name;
        this.product = product;
        this.productionTime = productionTime;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getProduct() {
        return product;
    }

    public int getProductionTime() {
        return productionTime;
    }

    public int getPrice() {
        return price;
    }
}
