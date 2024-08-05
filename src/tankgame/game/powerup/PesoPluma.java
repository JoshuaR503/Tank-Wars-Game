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
        tank.setSpeed(4);
        tank.setBulletDamage(25);
        tank.setLives(GameConstants.DEFAULT_TANK_LIVES);
        tank.setShield(true);

        GameWorld.getSoundManager().playBackgroundMusic("peso_pluma");
    }

    @Override
    public void removeEffect(Tank tank) {
       // Peso Pluma mode doesn't end.
    }
}
