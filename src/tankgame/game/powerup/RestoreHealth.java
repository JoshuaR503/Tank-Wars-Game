package tankgame.game.powerup;

import tankgame.GameConstants;
import tankgame.game.Tank;
import java.awt.image.BufferedImage;

public class RestoreHealth extends PowerUp {
    public RestoreHealth(float x, float y, BufferedImage img) {
        super(x, y, img);
    }

    @Override
    public void applyEffect(Tank tank) {
        tank.setLives(GameConstants.DEFAULT_TANK_LIVES);
    }

    @Override
    public void removeEffect(Tank tank) {
    }
}