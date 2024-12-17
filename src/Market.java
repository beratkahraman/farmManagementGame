import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class Market {
    public void openMarketDialog(JFrame parent, Inventory inventory, Map<String, Integer> barnAnimals) {
        // Market Penceresi
        JDialog marketDialog = new JDialog(parent, "Market", true);
        marketDialog.setSize(600, 400);
        marketDialog.setLayout(new BorderLayout());

        // Para Bilgisi
        JLabel moneyLabel = new JLabel("Mevcut Para: " + inventory.getMoney() + " TL");
        moneyLabel.setFont(new Font("Arial", Font.BOLD, 16));
        moneyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        marketDialog.add(moneyLabel, BorderLayout.NORTH);

        // Sekmeler
        JTabbedPane tabbedPane = new JTabbedPane();

        // 1. Tohum Al Sekmesi
        JPanel seedPanel = new JPanel(new GridLayout(3, 2, 10, 10)); // 3x2 düzen
        addCropUnlockButton(seedPanel, inventory, "Corn", 200, moneyLabel);
        addCropUnlockButton(seedPanel, inventory, "Tomato", 300, moneyLabel);
        addCropUnlockButton(seedPanel, inventory, "Potato", 400, moneyLabel);
        tabbedPane.addTab("Tohum Al", seedPanel);

        // 2. Hayvan Al Sekmesi
        JPanel animalPanel = new JPanel(new GridLayout(3, 1, 10, 10)); // 3x1 düzen
        addAnimalPurchaseButton(animalPanel, inventory, barnAnimals, "Chicken", 500, moneyLabel);
        addAnimalPurchaseButton(animalPanel, inventory, barnAnimals, "Cow", 1000, moneyLabel);
        addAnimalPurchaseButton(animalPanel, inventory, barnAnimals, "Sheep", 1500, moneyLabel);
        tabbedPane.addTab("Hayvan Al", animalPanel);

        // 3. Ürün Sat Sekmesi
        JPanel sellPanel = new JPanel(new GridLayout(0, 2, 10, 10)); // Dinamik satır sayısı
        for (String item : inventory.getItems().keySet()) {
            JButton sellButton = new JButton(item + " Sat (1 Adet)");
            sellButton.addActionListener(e -> {
                int quantity = inventory.getItemQuantity(item);
                if (quantity > 0) {
                    inventory.addItem(item, -1);
                    inventory.decreaseMoney(-inventory.getSellPrice(item));
                    moneyLabel.setText("Mevcut Para: " + inventory.getMoney() + " TL");
                    JOptionPane.showMessageDialog(parent, item + " satıldı!");
                } else {
                    JOptionPane.showMessageDialog(parent, "Stokta " + item + " yok!", "Hata", JOptionPane.ERROR_MESSAGE);
                }
            });
            sellPanel.add(sellButton);
        }
        tabbedPane.addTab("Ürün Sat", sellPanel);

        // Market Sekmelerini Ana Panele Ekleyin
        marketDialog.add(tabbedPane, BorderLayout.CENTER);

        // Çıkış Butonu
        JButton closeButton = new JButton("Kapat");
        closeButton.addActionListener(e -> marketDialog.dispose());
        marketDialog.add(closeButton, BorderLayout.SOUTH);

        // Market Penceresini Göster
        marketDialog.setLocationRelativeTo(parent);
        marketDialog.setVisible(true);
    }

    private void addCropUnlockButton(JPanel panel, Inventory inventory, String cropName, int price, JLabel moneyLabel) {
        JButton unlockButton = new JButton(cropName + " Kilidini Aç (" + price + " TL)");
        unlockButton.addActionListener(e -> {
            if (!inventory.isUnlocked(cropName)) {
                if (inventory.getMoney() >= price) {
                    inventory.decreaseMoney(price);
                    inventory.unlockItem(cropName);
                    moneyLabel.setText("Mevcut Para: " + inventory.getMoney() + " TL");
                    JOptionPane.showMessageDialog(null, cropName + " kilidi açıldı!");
                } else {
                    JOptionPane.showMessageDialog(null, "Yeterli paranız yok!", "Hata", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, cropName + " zaten kilidi açık!", "Uyarı", JOptionPane.WARNING_MESSAGE);
            }
        });
        panel.add(unlockButton);
    }

    private void addAnimalPurchaseButton(JPanel panel, Inventory inventory, Map<String, Integer> barnAnimals, String animalName, int price, JLabel moneyLabel) {
        JButton buyButton = new JButton(animalName + " Satın Al (" + price + " TL)");
        buyButton.addActionListener(e -> {
            if (inventory.getMoney() >= price) {
                inventory.decreaseMoney(price);
                barnAnimals.put(animalName, barnAnimals.getOrDefault(animalName, 0) + 1);
                moneyLabel.setText("Mevcut Para: " + inventory.getMoney() + " TL");
                JOptionPane.showMessageDialog(null, animalName + " satın alındı!");
            } else {
                JOptionPane.showMessageDialog(null, "Yeterli paranız yok!", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(buyButton);
    }
}
