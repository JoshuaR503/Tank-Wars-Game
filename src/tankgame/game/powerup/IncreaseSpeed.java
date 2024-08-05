package tankgame.game.powerup;

import tankgame.GameConstants;
import tankgame.game.Tank;
import java.awt.image.BufferedImage;

public class IncreaseSpeed extends PowerUp {
    public IncreaseSpeed(float x, float y, BufferedImage img) {
        super(x, y, img);
    }

    @Override
    public void applyEffect(Tank tank) {
        tank.setSpeed(5);
    }

    @Override
    public void removeEffect(Tank tank) {
        tank.setSpeed(GameConstants.DEFAULT_TANK_RADIUS);
    }
}
