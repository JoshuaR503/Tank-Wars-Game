package tankgame.game;

import tankgame.GameConstants;
import tankgame.ResourceManager;
import tankgame.ResourcePools;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * Tank class representing the player's tank in the game.
 * Implements Updatable and Colliable interfaces.
 *
 * Author: anthony-pc
 */
public class Tank extends GameObject implements Updatable, Colliable {

    private float screenX;
    private float screenY;
    private float vx;
    private float vy;
    private float angle;
    private float R = 5;
    private float ROTATIONSPEED = 2.0f;

    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;
    private boolean shootPressed;

    private long coolDown = 500;
    private long timeSinceLastShot = 0;

    private int lives = 5;

    // Constructor
    Tank(float x, float y, float vx, float vy, float angle, BufferedImage img) {
        super(x, y, img);
        this.screenX = x;
        this.screenY = y;
        this.vx = vx;
        this.vy = vy;
        this.angle = angle;
    }

    // Getters
    public int getScreenX() {
        return (int) screenX;
    }

    public int getScreenY() {
        return (int) screenY;
    }

    public int getLives() {
        return lives;
    }

    // Setters
    void setX(float x) {
        this.x = x;
    }

    void setY(float y) {
        this.y = y;
    }

    void setLives(int lives) {
        this.lives = lives;
    }

    // Toggle methods for controls
    void toggleUpPressed() {
        this.UpPressed = true;
    }

    void toggleDownPressed() {
        this.DownPressed = true;
    }

    void toggleRightPressed() {
        this.RightPressed = true;
    }

    void toggleLeftPressed() {
        this.LeftPressed = true;
    }

    void toggleShoot() {
        this.shootPressed = true;
    }

    void unToggleUpPressed() {
        this.UpPressed = false;
    }

    void unToggleDownPressed() {
        this.DownPressed = false;
    }

    void unToggleRightPressed() {
        this.RightPressed = false;
    }

    void unToggleLeftPressed() {
        this.LeftPressed = false;
    }

    void unToggleShoot() {
        this.shootPressed = false;
    }

    // Behavior
    @Override
    public void update() {
        if (this.UpPressed) {
            move(gameWorld, R);
        }
        if (this.DownPressed) {
            move(gameWorld, -R);
        }
        if (this.LeftPressed) {
            this.rotateLeft();
        }
        if (this.RightPressed) {
            this.rotateRight();
        }

        long currentTime = System.currentTimeMillis();
        if (this.shootPressed && currentTime > this.timeSinceLastShot + this.coolDown) {
            this.timeSinceLastShot = currentTime;
            var p = ResourcePools.getPooledInstance("bullet");

            float offset = 20.0f; // Increased offset to ensure bullet is spawned outside the tank's hitbox
            float bulletX = x + img.getWidth() / 2.0f + (float) Math.cos(Math.toRadians(angle)) * (img.getWidth() / 2.0f + offset);
            float bulletY = y + img.getHeight() / 2.0f + (float) Math.sin(Math.toRadians(angle)) * (img.getHeight() / 2.0f + offset);

            p.initObject(bulletX, bulletY, angle);
            Bullet b = (Bullet) p;

            System.out.println("Bullet spawn position: (" + bulletX + ", " + bulletY + ")");
            System.out.println("Tank hitbox position: (" + x + ", " + y + ")");
            System.out.println("Bullet hitbox before init: " + b.getHitbox());

            GameWorld.addGameObject((b));
            ResourceManager.getSound("shooting").play();
        }

        centerScreen();
        this.hitbox.setLocation((int) x, (int) y);
    }

    private void move(GameWorld gameWorld, float direction) {
        float vx = (float) (direction * Math.cos(Math.toRadians(angle)));
        float vy = (float) (direction * Math.sin(Math.toRadians(angle)));
        float newX = x + vx;
        float newY = y + vy;

        boolean willCollideWithWallX = gameWorld.willCollideWithWall(newX, y, img.getWidth(), img.getHeight());
        boolean willCollideWithWallY = gameWorld.willCollideWithWall(x, newY, img.getWidth(), img.getHeight());

        if (!willCollideWithWallX) {
            x = newX;
        }

        if (!willCollideWithWallY) {
            y = newY;
        }

        checkBorder();
    }

    private void rotateLeft() {
        this.angle -= this.ROTATIONSPEED;
    }

    private void rotateRight() {
        this.angle += this.ROTATIONSPEED;
    }

    // Collisions
    @Override
    public void onCollision(GameObject by) {
        if (by instanceof BreakableWall) {
            // I think players should lose points if they hit a wall.
        }

        if (by instanceof Bullet) {
            this.lives--;
            System.out.println("Tank hit! Lives remaining: " + this.lives);

            if (this.lives <= 0) {
                System.out.println("Tank destroyed!");
                // Handle tank destruction (e.g., remove from game)
            }
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

    // Bounds and screen rendering methods
    private void checkBorder() {
        if (x < 30) x = 30;
        if (y < 40) y = 40;

        if (x >= GameConstants.GAME_WORLD_WIDTH - 88) {
            x = GameConstants.GAME_WORLD_WIDTH - 88;
        }

        if (y >= GameConstants.GAME_WORLD_HEIGHT - 80) {
            y = GameConstants.GAME_WORLD_HEIGHT - 80;
        }
    }

    private void centerScreen() {
        this.screenX = this.x - GameConstants.GAME_SCREEN_WIDTH / 4f;
        this.screenY = this.y - GameConstants.GAME_SCREEN_HEIGHT / 4f;

        if (this.screenX < 0) screenX = 0;
        if (this.screenY < 0) screenY = 0;

        if (this.screenX > GameConstants.GAME_WORLD_WIDTH - GameConstants.GAME_SCREEN_WIDTH / 2f) {
            this.screenX = GameConstants.GAME_WORLD_WIDTH - GameConstants.GAME_SCREEN_WIDTH / 2f;
        }

        if (this.screenY > GameConstants.GAME_WORLD_HEIGHT - GameConstants.GAME_SCREEN_HEIGHT) {
            this.screenY = GameConstants.GAME_WORLD_HEIGHT - GameConstants.GAME_SCREEN_HEIGHT;
        }
    }
}