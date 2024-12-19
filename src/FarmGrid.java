import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FarmGrid extends JPanel implements KeyListener {
    private static final int TILE_SIZE = 70;
    private static final int ROWS = 10;
    private static final int COLS = 12;

    private Tile[][] tiles;
    private Player player;
    private Inventory inventory;
    private Market market;
    private Map<String, Integer> barnAnimals; // Ahırdaki hayvanlar
    private ScheduledExecutorService scheduler;
    private Factory factory;

    // Görseller
    private Image emptyFieldImage;
    private Image growingFieldImage;
    private Map<String, Image> readyImages; // Mahsul türüne göre "ready" görseller
    private Image marketIcon;
    private Image barnIcon;
    private Image storageIcon;
    private Image grassTileImage; // Yeni çimen dokusu
    private Image farmerIcon;
    private Image factoryIcon;


    public FarmGrid() {
        tiles = new Tile[ROWS][COLS];
        player = new Player(0, 0);
        inventory = new Inventory(1000); // Başlangıç parası
        market = new Market();
        barnAnimals = new HashMap<>();
        scheduler = Executors.newScheduledThreadPool(1);
        factory = new Factory(inventory);

        // Görselleri yükle
        try {
            emptyFieldImage = ImageIO.read(new File("resources/images/empty_field.png"));
            growingFieldImage = ImageIO.read(new File("resources/images/growing_field.png"));
            grassTileImage = ImageIO.read(new File("resources/images/grass_tile.png"));

            farmerIcon = ImageIO.read(new File("resources/images/farmer_icon.png"));

            readyImages = new HashMap<>();
            readyImages.put("Wheat", ImageIO.read(new File("resources/images/wheat_ready.png")));
            readyImages.put("Corn", ImageIO.read(new File("resources/images/corn_ready.png")));
            readyImages.put("Tomato", ImageIO.read(new File("resources/images/tomato_ready.png")));
            readyImages.put("Potato", ImageIO.read(new File("resources/images/potato_ready.png")));

            factoryIcon = ImageIO.read(new File("resources/images/factoryIcon.png"));
            marketIcon = ImageIO.read(new File("resources/images/market.png"));
            barnIcon = ImageIO.read(new File("resources/images/barn.png"));
            storageIcon = ImageIO.read(new File("resources/images/storage.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Görseller yüklenemedi!");
        }

        initializeTiles();
        setPreferredSize(new Dimension(COLS * TILE_SIZE, ROWS * TILE_SIZE));
        setFocusable(true);
        addKeyListener(this);

        // Büyüme ve hayvan ürün üretimi için zamanlayıcılar
        scheduler.scheduleAtFixedRate(this::updateCropGrowth, 0, 3, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(this::produceAnimalProducts, 0, 5, TimeUnit.SECONDS);
    }

    private void initializeTiles() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                // Çimen dokusunu boş alanlar için kullan
                tiles[row][col] = new Tile("empty", grassTileImage, null, null);
            }
        }

        // Tarla alanlarını ayarla
        for (int row = 0; row <= 5; row++) {
            for (int col = 0; col <= 4; col++) {
                tiles[row][col] = new Tile("field", emptyFieldImage, growingFieldImage, readyImages);
            }
        }

        // Market, depo ve ahır alanlarını ayarla
        tiles[4][8] = new Tile("market", marketIcon, null, null);
        tiles[3][6] = new Tile("storage", storageIcon, null, null);
        tiles[2][10] = new Tile("barn", barnIcon, null, null);
        tiles[5][5] = new Tile("factory", factoryIcon, null, null); // Fabrika
    }


    private void updateCropGrowth() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Tile tile = tiles[row][col];
                tile.grow();
            }
        }
        repaint();
    }

    private void produceAnimalProducts() {
        for (Map.Entry<String, Integer> entry : barnAnimals.entrySet()) {
            String animalType = entry.getKey();
            int count = entry.getValue();

            String product = switch (animalType) {
                case "Cow" -> "Milk";
                case "Chicken" -> "Egg";
                case "Sheep" -> "Wool";
                default -> null;
            };

            if (product != null) {
                inventory.addItem(product, count);
                System.out.println(count + " adet " + product + " üretildi!");
            }
        }
    }

    private void handleSpecialTiles() {
        Tile currentTile = tiles[player.getY()][player.getX()];
        switch (currentTile.getType()) {
            case "market" -> market.openMarketDialog((JFrame) SwingUtilities.getWindowAncestor(this), inventory, barnAnimals);
            case "storage" -> showInventory();
            case "barn" -> showBarn();
            case "factory" -> openFactoryDialog(); // Fabrika kutucuğuna özel işlem
        }
    }

    private void openFactoryDialog() {
        JDialog factoryDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Fabrika", true);
        factoryDialog.setSize(400, 400);
        factoryDialog.setLayout(new GridLayout(0, 2, 10, 10));

        for (String rawMaterial : factory.getProductionRules().keySet()) {
            String product = factory.getProductionRules().get(rawMaterial); // Nihai ürün
            int requiredQuantity = factory.getRequiredQuantity(rawMaterial); // Gerekli miktar
            String materialName = rawMaterial; // Ham madde ismi
            int currentQuantity = inventory.getItemQuantity(materialName); // Mevcut miktar

            // Üretim butonu
            JButton produceButton = new JButton("Üret: " + product);
            produceButton.addActionListener(e -> {
                if (factory.startProduction(rawMaterial)) {
                    JOptionPane.showMessageDialog(factoryDialog, product + " üretiliyor!");
                } else {
                    JOptionPane.showMessageDialog(factoryDialog, rawMaterial + " üretilemedi!", "Hata", JOptionPane.ERROR_MESSAGE);
                }
            });

            // Bilgilendirme etiketi (örneğin: "Gerekli: 5 Milk, Mevcut: 3")
            JLabel infoLabel = new JLabel("Gerekli: " + requiredQuantity + " " + materialName + ", Mevcut: " + currentQuantity);
            infoLabel.setHorizontalAlignment(SwingConstants.CENTER);

            // Buton ve bilgilendirici etiketi pencereye ekle
            factoryDialog.add(produceButton);
            factoryDialog.add(infoLabel);
        }

        // Kapat butonu
        JButton closeButton = new JButton("Kapat");
        closeButton.addActionListener(e -> factoryDialog.dispose());
        factoryDialog.add(closeButton);

        factoryDialog.setLocationRelativeTo(this); // Pencereyi ortala
        factoryDialog.setVisible(true); // Görünür yap
    }



    private void plantSeed() {
        Tile currentTile = tiles[player.getY()][player.getX()];
        if ("field".equals(currentTile.getType())) {
            // Kilidi açık mahsulleri listele
            String[] unlockedCrops = inventory.getUnlockedItems().keySet().stream()
                    .filter(inventory::isUnlocked)
                    .toArray(String[]::new);

            if (unlockedCrops.length == 0) {
                JOptionPane.showMessageDialog(this, "Hiçbir mahsul kilidi açık değil! Marketten kilit açabilirsiniz.", "Uyarı", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Kullanıcıdan mahsul seçimi
            String selectedCrop = (String) JOptionPane.showInputDialog(
                    this,
                    "Ekeceğiniz mahsulü seçin:",
                    "Mahsul Seçimi",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    unlockedCrops,
                    unlockedCrops[0]
            );

            if (selectedCrop != null) {
                Crop crop = switch (selectedCrop) {
                    case "Wheat" -> new Crop("Wheat", 3, 50);
                    case "Corn" -> new Crop("Corn", 5, 75);
                    case "Tomato" -> new Crop("Tomato", 4, 100);
                    case "Potato" -> new Crop("Potato", 6, 150);
                    default -> null;
                };

                if (crop != null) {
                    currentTile.plantCrop(crop);
                    repaint();
                }
            }
        }
    }


    private void harvestCrop() {
        Tile currentTile = tiles[player.getY()][player.getX()];
        if (currentTile.isReadyToHarvest()) {
            Crop harvestedCrop = currentTile.getCrop();
            inventory.addItem(harvestedCrop.getName(), 1);
            currentTile.harvest();
            repaint();
        }
    }

    private void showInventory() {
        StringBuilder inventoryText = new StringBuilder("Envanter:\n");
        for (String item : inventory.getItems().keySet()) {
            inventoryText.append(item).append(": ").append(inventory.getItemQuantity(item)).append("\n");
        }
        JOptionPane.showMessageDialog(this, inventoryText.toString(), "Depo", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showBarn() {
        StringBuilder barnText = new StringBuilder("Ahırdaki Hayvanlar:\n");
        for (String animal : barnAnimals.keySet()) {
            barnText.append(animal).append(": ").append(barnAnimals.get(animal)).append("\n");
        }
        JOptionPane.showMessageDialog(this, barnText.toString(), "Ahır", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        // 1. Haritadaki Döşemeleri Çiz
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Tile tile = tiles[row][col]; // Mevcut döşemeyi al
                Image tileImage = tile.getCurrentImage(); // Döşemenin görselini al

                if (tileImage != null) {
                    g2d.drawImage(tileImage, col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
                } else {
                    g2d.setColor(new Color(245, 245, 220)); // Varsayılan renk (boş döşeme)
                    g2d.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }
        }

        // 2. Oyuncuyu Çiz (Çiftçi İkonu ile)
        if (farmerIcon != null) {
            g2d.drawImage(farmerIcon,
                    player.getX() * TILE_SIZE + 10, // Çiftçi ikonu X pozisyonu
                    player.getY() * TILE_SIZE + 10, // Çiftçi ikonu Y pozisyonu
                    TILE_SIZE - 20, TILE_SIZE - 20, // İkonun boyutu
                    null);
        } else {
            // Eğer çiftçi ikonu yüklenememişse oval olarak çizer
            g2d.setColor(Color.BLUE);
            g2d.fillOval(player.getX() * TILE_SIZE + 10,
                    player.getY() * TILE_SIZE + 10,
                    TILE_SIZE - 20, TILE_SIZE - 20);
        }

        // 3. Sol Üst Köşeye Para Bilgisi Çiz
        g2d.setColor(new Color(0, 0, 0, 150)); // Şeffaf arka plan
        g2d.fillRoundRect(10, 10, 150, 30, 10, 10); // Arka plan şekli

        g2d.setColor(Color.WHITE); // Yazı rengi
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.drawString("Para: " + inventory.getMoney() + " TL", 20, 30); // Para bilgisi

        g2d.dispose(); // Kaynakları serbest bırak
    }





    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> player.moveUp();
            case KeyEvent.VK_DOWN -> player.moveDown(ROWS);
            case KeyEvent.VK_LEFT -> player.moveLeft();
            case KeyEvent.VK_RIGHT -> player.moveRight(COLS);
            case KeyEvent.VK_SPACE -> plantSeed();
            case KeyEvent.VK_H -> harvestCrop();
        }
        handleSpecialTiles();
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}
