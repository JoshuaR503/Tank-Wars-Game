package tankgame.game;

import tankgame.GameConstants;
import tankgame.ResourceManager;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * Bullet class representing a projectile in the game.
 * Implements Poolable, Updatable, and Colliable interfaces.
 * Author: anthony-pc
 */
public class Bullet extends GameObject implements Poolable, Updatable, Colliable {
    private float vx;
    private float vy;
    private float angle;

    // Dynamic attributes
    // TODO: ALLOW SPEED UPDATE
    private int damage;
    private final float R = 4;

    // Constructor
    // DO NOT REMOVE
    public Bullet(BufferedImage img) {
        super(0, 0, img);
        this.vx = 0;
        this.vy = 0;
        this.angle = 0;
        this.damage = 0;
    }

    @Override
    public void initObject(float x, float y, float angle, int damage) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.damage = damage;
        this.hitbox.setLocation((int) x, (int) y);
    }

    // Getters
    public int getDamage() {
        return damage;
    }

    // Behavior
    @Override
    public void update() {
        vx = Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
        checkBorder();
        this.hitbox.setLocation((int) x, (int) y);
    }

    // Bounds
    private void checkBorder() {
        if (x >= GameConstants.GAME_WORLD_WIDTH  || y >= GameConstants.GAME_WORLD_HEIGHT ) {
            // Bullet goes out of bounds, mark itself to removal.
            System.out.println("Marked bullet out of bounds");
            this.markCollision();

            // TODO: Add animation :3
        }
    }

    // Collision
    @Override
    public void onCollision(GameObject by) {
        if (by instanceof Bullet) {
            by.markCollision();
            GameWorld.createAnimation(new Animation(by.x, by.y, ResourceManager.getAnimation("rockethit")));
        }
    }

    // Drawing
    @Override
    public void draw(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);
    }

    @Override
    public String toString() {
        return "Bullet {" +
                "spawn position: (" + x + ", " + y + ")," +
                " damage: " + damage +
                '}';
    }
}
