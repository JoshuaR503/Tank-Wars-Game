package tankgame.game.powerup;

import tankgame.game.GameObject;
import tankgame.game.Tank;
import tankgame.game.Updatable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class PowerUp extends GameObject implements Updatable {

    private static final long DURATION = 3 * 10000;

    private final long spawnTime;
    private boolean isAvailable;
    private Tank affectedTank;

    public PowerUp(float x, float y, BufferedImage img) {
        super(x, y, img);
        this.spawnTime = System.currentTimeMillis();
        this.isAvailable = true;
    }

    public abstract void apply(Tank tank);

    public abstract void remove(Tank tank);

    public void setAffectedTank(Tank tank) {
        if (this.isAvailable) {
            this.apply(tank);
            this.affectedTank = tank;
            this.isAvailable = false;
            System.out.println("Applied power up!");
        }
    }

    @Override
    public void update() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - spawnTime > DURATION) {
            if (affectedTank != null) {
                remove(affectedTank);
            }
            this.markCollision(); // Mark for removal
        }
    }

    @Override
    public void draw(Graphics g) {
        if (isAvailable) {
            g.drawImage(this.img, (int) x, (int) y, null);
        }
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String spawnTimeFormatted = sdf.format(new Date(spawnTime));
        String removalTimeFormatted = sdf.format(new Date(spawnTime + DURATION));
        return "Power up information: x=" + x + ", y=" + y + ", spawn time=" + spawnTimeFormatted + " removal time=" + removalTimeFormatted;
    }
}
