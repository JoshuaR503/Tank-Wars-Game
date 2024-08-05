package tankgame.game.powerup;

import tankgame.game.Tank;
import java.awt.image.BufferedImage;

// TODO: Implement this class
public class IncreaseSpeed extends PowerUp {
    public IncreaseSpeed(float x, float y, BufferedImage img) {
        super(x, y, img);
    }

    @Override
    public void applyEffect(Tank tank) {
        System.out.println("Power applied, shield active.");
        tank.setShield(true);
    }

    @Override
    public void removeEffect(Tank tank) {
        System.out.println("Shield removed because it was marked for deletion.");
        tank.setShield(false);
    }
}
