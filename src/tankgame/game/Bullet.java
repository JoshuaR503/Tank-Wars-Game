package tankgame.game;

import tankgame.GameConstants;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 *
 * @author anthony-pc
 */
public class Bullet extends GameObject implements Poolable, Updatable, Colliable {
    private float vx;
    private float vy;
    private float angle;

    private float R = 5;
    private float ROTATIONSPEED = 3.0f;

    public Bullet(BufferedImage img) {
        super(0, 0, img);
        this.vx = 0;
        this.vy = 0;
        this.angle = 0;
    }

    public Bullet(float x, float y, float angle, BufferedImage img) {
        super(x, y, img);
        this.vx = 0;
        this.vy = 0;
        this.angle = angle;
    }

    @Override
    public void update(GameWorld gameWorld) {
        vx = Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
        checkBorder();
        this.hitbox.setLocation((int) x, (int) y);
    }

    private boolean isOutOfBounds() {
        // Assuming the gameWorld has methods to get its dimensions
        return x < 0 || x > GameConstants.GAME_WORLD_WIDTH - 88 || y < 0 || y > GameConstants.GAME_WORLD_HEIGHT - 80;
    }

    private void checkBorder() {
        if (x < 30 || y < 40 || x >= GameConstants.GAME_WORLD_WIDTH - 88 || y >= GameConstants.GAME_WORLD_HEIGHT - 80) {
           this.markCollision();
        }
    }

    @Override
    public void draw(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);
    }


    @Override
    public void initObject(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void initObject(float x, float y, float angle) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.hitbox.setLocation((int) x, (int) y);

    }

    @Override
    public void resetObject() {
        this.x = -5;
        this.y = -5;
    }

    @Override
    public void onCollision(GameObject by) {
        System.out.println("Bullet collided, market for removal");
//        this.hasCollided = true;
    }
}
