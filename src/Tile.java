import java.awt.Image;
import java.util.Map;

public class Tile {
    private String type;
    private Crop crop;
    private Image defaultImage;
    private Image growingImage;
    private Map<String, Image> cropReadyImages;

    public Tile(String type, Image defaultImage, Image growingImage, Map<String, Image> cropReadyImages) {
        this.type = type;
        this.defaultImage = defaultImage;
        this.growingImage = growingImage;
        this.cropReadyImages = cropReadyImages;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Crop getCrop() {
        return crop;
    }

    public void plantCrop(Crop crop) {
        if ("field".equals(type)) {
            this.crop = crop;
        }
    }

    public void grow() {
        if (crop != null && !crop.isGrown()) {
            crop.grow();
        }
    }

    public boolean isReadyToHarvest() {
        return crop != null && crop.isGrown();
    }

    public void harvest() {
        if (isReadyToHarvest()) {
            crop = null;
        }
    }

    public Image getCurrentImage() {
        if (crop == null) {
            return defaultImage;
        } else if (crop.isGrown()) {
            return cropReadyImages.getOrDefault(crop.getName(), defaultImage);
        } else {
            return growingImage;
        }
    }
}
