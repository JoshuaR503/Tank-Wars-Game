package tankgame.game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AssetManager {
    private static final Map<String, BufferedImage> cache = new HashMap<>();
    private static final int FIXED_WIDTH = 60; // Set this to desired width
    private static final int FIXED_HEIGHT = 60; // Set this to desired height
    private static final Map<String, Boolean> doNotScale = new HashMap<>();

    static {
        doNotScale.put("title.png", true);
        doNotScale.put("tank1.png", true);
    }

    public static BufferedImage getAsset(String path) {
        if (cache.containsKey(path)) {
            System.out.println("Returning cached image: " + path);
            return cache.get(path);
        }

        try {
            BufferedImage image = ImageIO.read(
                    Objects.requireNonNull(AssetManager.class.getClassLoader().getResource(path),
                            "Could not find " + path)
            );

            if (!doNotScale.containsKey(path)) {
                image = scaleImage(image, FIXED_WIDTH, FIXED_HEIGHT);
            }

            cache.put(path, image);
            return image;
        } catch (IOException e) {
            System.err.println("Error loading asset: " + path);
            e.printStackTrace();
            return null;
        }
    }

    private static BufferedImage scaleImage(BufferedImage originalImage, int width, int height) {
        BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = scaledImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(originalImage, 0, 0, width, height, null);
        g2d.dispose();
        return scaledImage;
    }
}
