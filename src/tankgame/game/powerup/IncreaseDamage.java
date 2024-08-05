package tankgame.game.powerup;
import tankgame.game.Tank;

import java.awt.image.BufferedImage;

// TODO: Implement this class
public class IncreaseDamage extends PowerUp {
    public IncreaseDamage(float x, float y, BufferedImage img) {
        super(x, y, img);
    }

    @Override
    public void applyEffect(Tank tank) {
        System.out.println("Power applied, damage increased.");
        tank.setBulletDamage(5);
    }

    @Override
    public void removeEffect(Tank tank) {
        System.out.println("Power up expired, damage to default.");
        tank.setBulletDamage(1);
    }
}