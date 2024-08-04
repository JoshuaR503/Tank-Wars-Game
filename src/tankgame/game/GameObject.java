package tankgame.game;

import tankgame.ResourceManager;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class GameObject {

    protected float x, y;
    protected BufferedImage img;
    protected Rectangle hitbox;
    protected boolean hasCollided = false;

    public GameObject(float x, float y, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.img = img;
        hitbox = new Rectangle((int) x, (int) y, img.getWidth(), img.getHeight());
    }


    /**
     * Create new game object based on a type
     * @param type type of the game object
     * @param x pos
     * @param y pos
     * @return created sub-class of GameObject
     */
    public static GameObject newInstance(String type, float x, float y) {

        System.out.println(type);
        return switch (type) {
            case "9" -> new Wall(x, y, ResourceManager.getSprite("uwall"));
            case "3" -> new BreakableWall(x, y, ResourceManager.getSprite("bwall"));
            case "4" -> new Speed(x, y, ResourceManager.getSprite("tank"));
            case "5" -> new Shield(x, y, ResourceManager.getSprite("tank"));
            case "6" -> new Health(x, y, ResourceManager.getSprite("tank"));
            default -> throw new IllegalArgumentException("Unsupported type: " + type);
        };
    }

    public void draw(Graphics g) {
        g.drawImage(img, (int) x, (int) y, null);
    }

    public Rectangle getHitbox() {
        return hitbox.getBounds();
    }

    public boolean hasCollided() {
        return this.hasCollided;
    }

    public void markCollision() {
        this.hasCollided = true;
    }

//    public boolean isPassable() {
//        return this instanceof BreakableWall;
//    }
}
