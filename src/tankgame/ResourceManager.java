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
            // update sounds pls
            ResourceManager.sounds.put("bullethit", loadSound("bullet.wav"));
            ResourceManager.sounds.put("bulletshot", loadSound("bullet.wav"));
            ResourceManager.sounds.put("explosion", loadSound("bullet.wav"));
            ResourceManager.sounds.put("pickup", loadSound("bullet.wav"));
            ResourceManager.sounds.put("shooting", loadSound("bullet.wav"));
            ResourceManager.sounds.put("bg", loadSound("bullet.wav"));

        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
           System.out.println("Could not load sounds");
            throw  new RuntimeException(e);
        }
    }

    private static void loadAnimtations() {
        (new HashMap<String, Integer>(){{
            put("bullethit", 24);
            put("bulletshot", 24);
            put("explostion_lg", 6);
            put("explostion_sm", 6);
            put("pickup", 32);
        }}).forEach((animationName, frameCount) -> {

            String baseName = "animations/%s/%s_%04d.png";

            // this way there are no shifs
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
