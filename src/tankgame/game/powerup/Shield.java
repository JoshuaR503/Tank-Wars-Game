package tankgame.game.powerup;
import tankgame.GameConstants;
import tankgame.game.Tank;

import java.awt.image.BufferedImage;

public class Shield extends PowerUp {
    public Shield(float x, float y, BufferedImage img) {
        super(x, y, img);
    }

    @Override
    public void applyEffect(Tank tank) {
        tank.setShield(true);
    }

    @Override
    public void removeEffect(Tank tank) {
        tank.setShield(GameConstants.DEFAULT_TANK_ACTIVE_SHIELD);
    }
}
