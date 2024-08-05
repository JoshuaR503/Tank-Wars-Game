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


    private static BufferedImage loadSprite(String path) throws IOException {
        return ImageIO.read(Objects.requireNonNull(
                ResourceManager.class.getClassLoader().getResource(path),
                "Cannot find %s ".formatted(path)
        ));
    }

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
            ResourceManager.sounds.put("bullet_collide", loadSound("sounds/bullet.wav"));
            ResourceManager.sounds.put("pickup", loadSound("sounds/pickup.wav"));
            ResourceManager.sounds.put("shooting", loadSound("sounds/shotfiring.wav"));
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void loadAnimtations() {
        (new HashMap<String, Integer>(){{
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

    private static void loadSprites() throws IOException {
        ResourceManager.sprites.put("t1", loadSprite("tank/tank1.png"));
        ResourceManager.sprites.put("t2", loadSprite("tank/tank2.png"));
        ResourceManager.sprites.put("menuBackground", loadSprite("menu/title.png"));
        ResourceManager.sprites.put("uwall", loadSprite("wall/wall1.png"));
        ResourceManager.sprites.put("bwall", loadSprite("wall/wall2.png"));
        ResourceManager.sprites.put("floor", loadSprite("floor/bg.bmp"));
        ResourceManager.sprites.put("bullet", loadSprite("bullet/bullet.jpg"));
        ResourceManager.sprites.put("shield", loadSprite("powerups/mshield.png"));
    }

    public static void loadAssets() {
        try {
            loadSprites();
            loadSounds();
            loadAnimtations();

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
