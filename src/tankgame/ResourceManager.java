package tankgame;

import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ResourceManager {

    private final static Map<String, BufferedImage> sprites = new HashMap<>();
    private final static Map<String, Clip> sounds = new HashMap<>();
    private final static Map<String, List<BufferedImage>> animations = new HashMap<>();


    private static BufferedImage loadSprite(String path) throws IOException {
        return ImageIO.read(Objects.requireNonNull(
                ResourceManager.class.getClassLoader().getResource(path),
                "Cannot find %s ".formatted(path)
        ));
    }

    private static void initSprites() throws IOException {
        ResourceManager.sprites.put("t1", loadSprite("tank1.png"));
        ResourceManager.sprites.put("t2", loadSprite("tank2.png"));
        ResourceManager.sprites.put("menuBackground", loadSprite("title.png"));
        ResourceManager.sprites.put("uwall", loadSprite("wall1.png"));
        ResourceManager.sprites.put("floor", loadSprite("Background.bmp"));
        ResourceManager.sprites.put("bullet", loadSprite("Shell.gif"));
    }

    public static void loadAssets() {
        try {
            initSprites();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage getSprite(String key) {
        if (!ResourceManager.sprites.containsKey(key)) {
            throw new IllegalArgumentException("Resource %s is not in map".formatted(key));
        }
        return ResourceManager.sprites.get(key);
    }

    public static void main(String[] args) {
        ResourceManager.loadAssets();
    }

}
