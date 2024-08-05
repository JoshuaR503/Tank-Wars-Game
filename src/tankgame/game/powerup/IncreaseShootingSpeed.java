package tankgame.game.powerup;

import tankgame.GameConstants;
import tankgame.game.Tank;

import java.awt.image.BufferedImage;

public class IncreaseShootingSpeed extends PowerUp {
    public IncreaseShootingSpeed(float x, float y, BufferedImage img) {
        super(x, y, img);
    }

    @Override
    public void applyEffect(Tank tank) {
        tank.setCoolDown(100);
    }

    @Override
    public void removeEffect(Tank tank) {
        tank.setCoolDown(GameConstants.DEFAULT_TANK_COOLDOWN);
    }
}
