package tankgame.game;

import tankgame.GameConstants;
import tankgame.ResourceManager;
import tankgame.ResourcePools;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 *
 * @author anthony-pc
 */
public class Tank extends GameObject implements Updatable, Colliable {


    private int tankId;
    private float screenX;
    private float screenY;

    private float vx;
    private float vy;
    private float angle;

    private float R = 5;
    private float ROTATIONSPEED = 3.0f;

    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;


    private boolean shootPressed;

    private long coolDown = 500;
    private long timeSinceLastShot = 0;

    private float prevX;
    private float prevY;

    Tank(float x, float y, float vx, float vy, float angle, BufferedImage img) {
        super(x, y, img);
        this.tankId = new Random().nextInt(300);
        this.screenX = x;
        this.screenY = y;
        this.vx = vx;
        this.vy = vy;
        this.angle = angle;
    }

    void setX(float x){ this.x = x; }

    void setY(float y) { this. y = y;}

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

    void unToogleShoot() {
        this.shootPressed = false;
    }

    public int getScreenX() {
        return (int)screenX;
    }

    public int getScreenY() {
        return (int)screenY;
    }

    @Override
    public void update(GameWorld gameWorld) {

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

            gameWorld.addGameObject((b));

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

    private void centerScreen () {
        this.screenX = this.x - GameConstants.GAME_SCREEN_WIDTH/4f;
        this.screenY = this.y - GameConstants.GAME_SCREEN_HEIGHT/4f;

        if (this.screenX < 0) screenX = 0;
        if (this.screenY < 0) screenY = 0;


        if (this.screenX > GameConstants.GAME_WORLD_WIDTH - GameConstants.GAME_SCREEN_WIDTH/2f) {
            this.screenX = GameConstants.GAME_WORLD_WIDTH - GameConstants.GAME_SCREEN_WIDTH/2f;
        }

        if (this.screenY > GameConstants.GAME_WORLD_HEIGHT - GameConstants.GAME_SCREEN_HEIGHT) {
            this.screenY = GameConstants.GAME_WORLD_HEIGHT - GameConstants.GAME_SCREEN_HEIGHT;
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
    public void onCollision(GameObject by) {

//        if (by instanceof Bullet) {
//
//            by.hasCollided = true;
//            System.out.println("This tank was hit by a bullet, decreasing life");
//        }

        if (by instanceof BreakableWall) {
            // I think players should lose points if they hit a wall.
        }
    }
}
