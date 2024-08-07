package tankgame.game;

import tankgame.ResourceManager;
import tankgame.game.powerup.PowerUp;

import java.awt.image.BufferedImage;

public class BreakableWall extends GameObject implements Colliable {

    private int life = 10;

    public BreakableWall(float x, float y, BufferedImage img) {
        super(x, y, img);
    }

    // Behavior.
    @Override
    public void onCollision(GameObject by) {

        if (by instanceof Bullet) {
            ResourceManager.getSound("explosion").play();

            life -= ((Bullet) by).getDamage();

            if (life <= 0) {
                this.markCollision();
                GameWorld.createAnimation(new Animation(x, y, ResourceManager.getAnimation("puffsmoke")));
            }

            System.out.println("breakable wall collision with bullet, reducing life: " + life);
        }

        if (by instanceof PowerUp) {
            ((PowerUp) by).setUnavailable();

            by.markCollision();

            System.out.println("Power up named: " + by.getClass().getSimpleName() + " collided with wall at : [x= " + by.getHitbox().x + ", y= " + by.getHitbox().y + "]");
            System.out.println("Marked for immediate removal...");
        }
    }

}
