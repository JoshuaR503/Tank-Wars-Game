package tankgame.game.powerup;
import tankgame.GameConstants;
import tankgame.game.Tank;

import java.awt.image.BufferedImage;

public class IncreaseDamage extends PowerUp {
    public IncreaseDamage(float x, float y, BufferedImage img) {
        super(x, y, img);
    }

    @Override
    public void applyEffect(Tank tank) {
        tank.setBulletDamage(5);
    }

    @Override
    public void removeEffect(Tank tank) {
        tank.setBulletDamage(GameConstants.DEFAULT_TANK_BULLET_DAMAGE);
    }
}