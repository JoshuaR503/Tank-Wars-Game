package tankgame.game;

import tankgame.ResourceManager;

import java.awt.image.BufferedImage;

public class BreakableWall extends GameObject implements Colliable {

    private int life = 5;

    public BreakableWall(float x, float y, BufferedImage img) {
        super(x, y, img);
    }

    @Override
    public void onCollision(GameObject by) {

        if (by instanceof Bullet) {

            // TODO: reduce by the bullet's amount of damage (i.e) if there is a power up.
            life--;

            if (life <= 0) {
                this.markCollision();
                GameWorld.createAnimation(new Animation(x, y, ResourceManager.getAnimation("puffsmoke")));
            }

            System.out.println("breakable wall collision with bullet, reducing life: " + life);
        }
    }

}
