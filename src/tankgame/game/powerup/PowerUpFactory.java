package tankgame.game.powerup;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import tankgame.ResourceManager;

// TODO: Add a collidable, and if it collides with a wall or another Power up mark it as delete it.
public final class PowerUpFactory {

    private static final Map<String, PowerUpInfo> powerUpInfoMap = new HashMap<>();
    private static String[] keys;

    private static final Random random = new Random();

    public static void init() {
        powerUpInfoMap.put("PesoPluma", new PowerUpInfo(PesoPluma.class, "pp"));
        powerUpInfoMap.put("IncreaseDamage", new PowerUpInfo(IncreaseDamage.class, "damage"));
        powerUpInfoMap.put("IncreaseSpeed", new PowerUpInfo(IncreaseSpeed.class, "speed"));
        powerUpInfoMap.put("IncreaseShootingSpeed", new PowerUpInfo(IncreaseShootingSpeed.class, "shooting_speed"));
        powerUpInfoMap.put("RestoreHealth", new PowerUpInfo(RestoreHealth.class, "health"));
        powerUpInfoMap.put("Shield", new PowerUpInfo(Shield.class, "shield"));

        // Is there really a need to keep track of only five keys? I do not know.
        keys = powerUpInfoMap.keySet().toArray(new String[0]);
    }

    public static PowerUp newRandomInstance(float x, float y) {

        // Get the power-up information using a random key.
        final PowerUpInfo powerUpInfo = powerUpInfoMap.get(keys[random.nextInt(keys.length)]);


        System.out.println(powerUpInfo.imageName);

        // Get the image for the power-up using the image name from the info.
        final BufferedImage powerUpImage = ResourceManager.getSprite(powerUpInfo.imageName);

        try {
            return powerUpInfo
                    .powerUpClass
                    .getDeclaredConstructor(float.class, float.class, BufferedImage.class)
                    .newInstance(x, y, powerUpImage);

        } catch (Exception e) {
            throw new RuntimeException("Failed to create PowerUp: " + e.getMessage());
        }
    }

    // IntelliJ suggested to turn this into a private record.
    private record PowerUpInfo(Class<? extends PowerUp> powerUpClass, String imageName) { }
}