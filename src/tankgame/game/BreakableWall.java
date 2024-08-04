package tankgame.game;

import tankgame.ResourceManager;

import java.awt.image.BufferedImage;
import java.util.List;

public class BreakableWall extends GameObject implements Colliable, Animable {

    private int life = 5;
    private AnimationManager animationManager;

    public BreakableWall(float x, float y, BufferedImage img, AnimationManager animationManager) {
        super(x, y, img);
        this.animationManager = animationManager;
    }

    @Override
    public void onCollision(GameObject by) {

        if (by instanceof Bullet) {

            // TODO: reduce by the bullet's amount of damage (i.e) if there is a power up.
            life--;
            playAnimation(new Animation(x, y, ResourceManager.getAnimation("bullethit")));

            if (life <= 0) {
                this.markCollision();
                playAnimation(new Animation(x, y, ResourceManager.getAnimation("puffsmoke")));
            }

            System.out.println("breakable wall collision with bullet, reducing life: " + life);
        }
    }

    @Override
    public void playAnimation(Animation animation) {
        animationManager.addAnimation(animation);
    }
}
