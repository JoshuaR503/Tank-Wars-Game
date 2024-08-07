package tankgame;

import tankgame.game.Sound;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

public class ResourceManager {

    private final static Map<String, BufferedImage> sprites = new HashMap<>();
    private final static Map<String, Sound> sounds = new HashMap<>();
    private final static Map<String, List<BufferedImage>> animations = new HashMap<>();

    // Load All Assets
    public static void loadAssets() {
        try {
            loadSprites();
            loadSounds();
            loadAnimations();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Sprite
    private static BufferedImage loadSprite(String path) throws IOException {
        return ImageIO.read(Objects.requireNonNull(
                ResourceManager.class.getClassLoader().getResource(path),
                "Cannot find %s ".formatted(path)
        ));
    }

    private static void loadSprites() throws IOException {
        ResourceManager.sprites.put("t1", loadSprite("tank/tank1.png"));
        ResourceManager.sprites.put("t2", loadSprite("tank/tank2.png"));
        ResourceManager.sprites.put("menuBackground", loadSprite("menu/title.png"));
        ResourceManager.sprites.put("uwall", loadSprite("wall/wall1.png"));
        ResourceManager.sprites.put("bwall", loadSprite("wall/wall2.png"));
        ResourceManager.sprites.put("floor", loadSprite("floor/bg.bmp"));
        ResourceManager.sprites.put("bullet", loadSprite("bullet/bullet.jpg"));

        // Power ups
        ResourceManager.sprites.put("damage", loadSprite("powerups/damage.png"));
        ResourceManager.sprites.put("health", loadSprite("powerups/health.png"));
        ResourceManager.sprites.put("shield", loadSprite("powerups/mshield.png"));

        ResourceManager.sprites.put("pp", loadSprite("powerups/pp.png"));

        ResourceManager.sprites.put("speed", loadSprite("powerups/speed.png"));
        ResourceManager.sprites.put("shooting_speed", loadSprite("powerups/shooting_speed.png"));
    }

    // Sound
    private static Sound loadSound(String path) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        AudioInputStream ais = AudioSystem.getAudioInputStream(
                Objects.requireNonNull(
                        ResourceManager.class.getClassLoader().getResourceAsStream(path)
                )
        );

        Clip c = AudioSystem.getClip();
        c.open(ais);

        return new Sound(c);
    }

    private static void loadSounds() {
        try {
            ResourceManager.sounds.put("bg", loadSound("sounds/music_fixed.wav"));
            ResourceManager.sounds.put("bgm", loadSound("sounds/bgm.wav"));

            ResourceManager.sounds.put("peso_pluma", loadSound("sounds/peso_pluma.wav"));

            ResourceManager.sounds.put("bullet_shoot", loadSound("sounds/bullet_shoot.wav"));
            ResourceManager.sounds.put("explosion", loadSound("sounds/explosion.wav"));
            ResourceManager.sounds.put("pickup", loadSound("sounds/pickup.wav"));

            ResourceManager.sounds.put("ladygaga", loadSound("sounds/ladygaga.wav"));
            ResourceManager.sounds.put("hotline", loadSound("sounds/hotline.wav"));

        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Animation
    private static void loadAnimations() {
        (new HashMap<String, Integer>() {{
            put("bullethit", 24);
            put("bulletshoot", 24);
            put("powerpick", 32);
            put("puffsmoke", 32);
            put("rockethit", 32);
        }}).forEach((animationName, frameCount) -> {

            String baseName = "animations/%s/%s_%04d.png";

            List<BufferedImage> frames = new ArrayList<>(frameCount);

            try {
                for (int i = 0; i < frameCount; i++) {
                    frames.add(loadSprite(baseName.formatted(animationName, animationName, i)));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            ResourceManager.animations.put(animationName, frames);
        });
    }

    // Getters
    public static BufferedImage getSprite(String key) {
        if (!ResourceManager.sprites.containsKey(key)) {
            throw new IllegalArgumentException("Resource %s is not in map".formatted(key));
        }
        return ResourceManager.sprites.get(key);
    }

    public static Sound getSound(String key) {
        if (!ResourceManager.sounds.containsKey(key)) {
            throw new IllegalArgumentException("Resource %s is not in map".formatted(key));
        }
        return ResourceManager.sounds.get(key);
    }

    public static List<BufferedImage> getAnimation(String key) {
        if (!ResourceManager.animations.containsKey(key)) {
            throw new IllegalArgumentException("Resource %s is not in map".formatted(key));
        }
        return ResourceManager.animations.get(key);
    }
}
