package tankgame.game.powerup;

import tankgame.GameConstants;
import tankgame.ResourceManager;
import tankgame.game.GameWorld;
import tankgame.game.Tank;

import java.awt.image.BufferedImage;
import java.util.Random;

public class PesoPluma extends PowerUp {
    private long lastSoundTimeMillis;
    private static final int MIN_SOUND_INTERVAL = 1000; // Minimum interval between sounds in milliseconds
    private static final int MAX_SOUND_INTERVAL = 5000; // Maximum interval between sounds in milliseconds
    private Random random;
    private boolean isActive;

    public PesoPluma(float x, float y, BufferedImage img) {
        super(x, y, img);
        this.lastSoundTimeMillis = System.currentTimeMillis();
        this.random = new Random();
        this.isActive = false;

    }

    @Override
    public void applyEffect(Tank tank) {


        /// Remove all other previous effects to prevent the weird glicthing.
        tank.clearPowerUps();

        tank.setSpeed(5);
        tank.setBulletDamage(25);
        tank.setLives(GameConstants.DEFAULT_TANK_LIVES);
        tank.setShield(true);
        tank.setCoolDown(1);

        GameWorld.getSoundManager().playBackgroundMusic("peso_pluma");
        this.isActive = true;
    }

    @Override
    public void removeEffect(Tank tank) {
        // Peso Pluma mode doesn't end.
        GameWorld.getSoundManager().playBackgroundMusic("bgm");

    }

    @Override
    public void update() {
        super.update();

        if (isActive) {
            // Play explosion sound at random intervals
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - lastSoundTimeMillis >= random.nextInt(MAX_SOUND_INTERVAL - MIN_SOUND_INTERVAL) + MIN_SOUND_INTERVAL) {

                ResourceManager.getSound("bullet_shoot").play();

                ResourceManager.getSound("explosion").play();

                ResourceManager.getSound("bullet_shoot").play();


//                ResourceManager.getSound("pickup").play();

                lastSoundTimeMillis = currentTimeMillis;
            }
        }
    }
}
