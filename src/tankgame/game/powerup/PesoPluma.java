package tankgame.game.powerup;

import tankgame.GameConstants;
import tankgame.game.GameWorld;
import tankgame.game.Tank;

import java.awt.image.BufferedImage;

public class PesoPluma extends PowerUp {
    public PesoPluma(float x, float y, BufferedImage img) {
        super(x, y, img);
    }

    @Override
    public void applyEffect(Tank tank) {
        tank.togglePP();
        tank.clearPowerUps();
        GameWorld.getSoundManager().playBackgroundMusic("peso_pluma");

        tank.setShield(true);
        tank.setSpeed(GameConstants.DEFAULT_TANK_RADIUS * 2);
        tank.setBulletDamage(GameConstants.DEFAULT_TANK_BULLET_DAMAGE * 4);
        tank.setLives(GameConstants.DEFAULT_TANK_LIVES);
        tank.setCoolDown(0);
    }

    @Override
    public void removeEffect(Tank tank) {

        tank.togglePP();
        GameWorld.getSoundManager().playBackgroundMusic("bgm");

        tank.setSpeed(GameConstants.DEFAULT_TANK_RADIUS);
        tank.setBulletDamage(GameConstants.DEFAULT_TANK_BULLET_DAMAGE);
        tank.setLives(GameConstants.DEFAULT_TANK_LIVES);
        tank.setShield(GameConstants.DEFAULT_TANK_ACTIVE_SHIELD);
        tank.setCoolDown(GameConstants.DEFAULT_TANK_COOLDOWN);
    }
}
