package tankgame.game;

import tankgame.GameConstants;
import tankgame.ResourceManager;
import tankgame.ResourcePools;
import tankgame.game.powerup.PowerUp;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

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

    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;
    private boolean shootPressed;

    private long timeSinceLastShot = 0;

    // Dynamic attributes
    private int lives = GameConstants.DEFAULT_TANK_LIVES;
    private int bulletDamage = GameConstants.DEFAULT_TANK_BULLET_DAMAGE;
    private long coolDown = GameConstants.DEFAULT_TANK_COOLDOWN;
    private float radius = GameConstants.DEFAULT_TANK_RADIUS;
    private float rotationSpeed = GameConstants.DEFAULT_TANK_ROTATION_SPEED;
    private boolean hasShield = GameConstants.DEFAULT_TANK_ACTIVE_SHIELD;
    private final List<PowerUp> powerups = new ArrayList<>();

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

    // Setters
    void setX(float x) {
        this.x = x;
    }

    void setY(float y) {
        this.y = y;
    }

    public void setSpeed(float R) {
        this.radius = R;
    }

    public void setRotationSpeed(float rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    public void setCoolDown(long coolDown) {
        this.coolDown = coolDown;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void setBulletDamage(int bulletDamage) {
        this.bulletDamage = bulletDamage;
    }

    public void setShield(boolean hasShield) {
        this.hasShield = hasShield;
    }

    public void addPowerUp(PowerUp powerUp) {
        System.out.println("Added power up to tank: " + powerUp.toString());
        this.powerups.add(powerUp);
    }

    public void removePowerUpById(String id) {
        this.powerups.removeIf(powerUp -> powerUp.getId().equals(id));
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
            move(gameWorld, this.radius);
        }
        if (this.DownPressed) {
            move(gameWorld, -this.radius);
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

            float offset = 10.0f; // Increased offset to ensure bullet is spawned outside the tank's hitbox and it doesn't hit itself.
            float bulletX = x + img.getWidth() / 2.0f + (float) Math.cos(Math.toRadians(angle)) * (img.getWidth() / 2.0f + offset);
            float bulletY = y + img.getHeight() / 2.0f + (float) Math.sin(Math.toRadians(angle)) * (img.getHeight() / 2.0f + offset);

            p.initObject(bulletX, bulletY, angle, this.bulletDamage);
            Bullet b = (Bullet) p;

            System.out.println(b);

            GameWorld.addGameObject((b));
            ResourceManager.getSound("bullet_shoot").play();
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
        this.angle -= this.rotationSpeed;
    }

    private void rotateRight() {
        this.angle += this.rotationSpeed;
    }

    // Collisions
    @Override
    public void onCollision(GameObject by) {

        if (by instanceof BreakableWall) {
            // I think players should lose points if they intentionally hit a wall.
        }

        if (by instanceof Bullet) {

            ResourceManager.getSound("explosion").play();

            if (!this.hasShield) {

                System.out.println("Tank does not have a shield, applying damage of: "+ this.bulletDamage);
                this.lives -= ((Bullet) by).getDamage();

                System.out.println("Tank hit! Lives remaining: " + this.lives);

                if (this.lives <= 0) {
                    System.out.println("Tank destroyed!");
                    // TODO: Handle tank destruction
                }
            }
        }

        if (by instanceof PowerUp) {
            ((PowerUp) by).apply(this);
        }
    }

    // Drawing
    @Override
    public void draw(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);
        drawTankStats(g2d);
    }

    private void drawTankStats(Graphics2D g2d) {
        int barWidth = 50;
        int barHeight = 5;
        int barX = (int) this.x + (this.img.getWidth() / 2) - (barWidth / 2);
        int barY = (int) this.y - 10;

        // Background of the health bar
        g2d.setColor(Color.RED);
        g2d.fillRect(barX, barY, barWidth, barHeight);

        // Foreground of the health bar representing the current health
        g2d.setColor(Color.GREEN);
        int currentBarWidth = (int) ((double) this.lives / 5 * barWidth);
        g2d.fillRect(barX, barY, currentBarWidth, barHeight);

        // Border of the health bar
        g2d.setColor(Color.darkGray);
        g2d.drawRect(barX, barY, barWidth, barHeight);

        // Display power up image so the player knows whats up.
        int powerUpY = barY - 20; // Small spacing in case there's more power-ups.
        for (PowerUp powerUp : powerups) {

            final BufferedImage powerUpImage = powerUp.img;

            g2d.drawImage(powerUpImage, barX, powerUpY, null);

            String className = powerUp.getClass().getSimpleName();
            g2d.setColor(Color.WHITE);

            int textX = barX + powerUpImage.getWidth() + 5;
            int textY = powerUpY + powerUpImage.getHeight() / 2 + 5;
            g2d.drawString(  className, textX, textY);

            powerUpY -= powerUpImage.getHeight() + 10; // Stack icons and class names vertically
        }
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