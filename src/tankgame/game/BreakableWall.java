package tankgame.game;

import tankgame.ResourceManager;

import java.awt.image.BufferedImage;

public class BreakableWall extends GameObject implements Colliable {

    private int life = 5;

    public BreakableWall(float x, float y, BufferedImage img) {
        super(x, y, img);
    }

    // Behavior.
    @Override
    public void onCollision(GameObject by) {

        if (by instanceof Bullet) {
            ResourceManager.getSound("explosion").play();

            life =- ((Bullet) by).getDamage();

            if (life <= 0) {
                this.markCollision();
                GameWorld.createAnimation(new Animation(x, y, ResourceManager.getAnimation("puffsmoke")));
            }

            System.out.println("breakable wall collision with bullet, reducing life: " + life);
        }
    }

}
