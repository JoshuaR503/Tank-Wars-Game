package tankgame.game.powerup;
import tankgame.game.Tank;

import java.awt.image.BufferedImage;

public class IncreasedDamage extends PowerUp {
    public IncreasedDamage(float x, float y, BufferedImage img) {
        super(x, y, img);
    }

    @Override
    public void applyEffect(Tank tank) {
        System.out.println("Power applied, damage increased.");
        tank.setBulletDamage(5);
        tank.addPowerUp(this);
    }

    @Override
    public void removeEffect(Tank tank) {
        System.out.println("Power up expired, damage to default.");
        tank.setBulletDamage(1);
        tank.removePowerUpById(this.getId());
    }
}