package tankgame.game;

import tankgame.ResourceManager;
import tankgame.game.powerup.PowerUp;

import java.awt.image.BufferedImage;

public class Wall extends GameObject implements Colliable {
    public Wall(float x, float y, BufferedImage img) {
        super(x, y, img);
    }

    @Override
    public void onCollision(GameObject by) {

        if (by instanceof Bullet ) {
            ResourceManager.getSound("explosion").play();
        }

        if (by instanceof PowerUp) {

            ((PowerUp) by).setUnavailable();

            by.markCollision();

            System.out.println("Power up named: " + by.getClass().getSimpleName() + " collided with wall at : [x= " + by.getHitbox().x + ", y= " + by.getHitbox().y + "]");
            System.out.println("Marked for immediate removal...");
        }

    }
}
