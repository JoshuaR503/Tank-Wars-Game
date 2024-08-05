package tankgame.game.powerup;

import tankgame.game.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public abstract class PowerUp extends GameObject implements Updatable {

    private static final long DURATION_MILLIS = 30_000; // Duration in milliseconds

    private final String uniqueId;
    private long activationTimeMillis;
    private boolean isAvailable;
    private Tank affectedTank;

    public PowerUp(float x, float y, BufferedImage img) {
        super(x, y, img);
        this.isAvailable = true;
        this.uniqueId = UUID.randomUUID().toString();
    }

    // Methods to be implemented by descendants
    protected abstract void applyEffect(Tank tank);
    protected abstract void removeEffect(Tank tank);

    // Template methods: https://www.digitalocean.com/community/tutorials/template-method-design-pattern-in-java
    // Default implementation that is common for all the subclasses
    public final void apply(Tank tank) {

        if (this.isAvailable) {
            this.isAvailable = false;

            // Check if tank doesn't already have a power-up of this kind.
            for (PowerUp powerUp : tank.getPowerUps()) {
                if (powerUp.getClass() == this.getClass()) {
                    System.out.println("Tank already has a power-up of this kind: " + this.getClass().getSimpleName());
                    return;
                }
            }

            this.affectedTank = tank;
            this.activationTimeMillis = System.currentTimeMillis(); // Reset time so it doesn't get removed right after pick up.

            this.applyEffect(tank);
            tank.addPowerUp(this);
            // System.out.println("Enabled power up: " + this.getClass().getSimpleName());
            // System.out.println("Power up info: " + this.uniqueId + " at " + formatTime(this.activationTimeMillis));
            System.out.println("Current tank status: " + tank);
        }
    }

    public final void remove(Tank tank) {
        if (this.affectedTank != null) {
            this.removeEffect(tank);
            tank.removePowerUpById(this.getId());
            System.out.println("Disabled power up: " + this.getClass().getSimpleName());
//            System.out.println("Removed power-up: " + this.uniqueId + " at " + formatTime(System.currentTimeMillis()));
        }
    }

    // Getters
    public String getId() {
        return this.uniqueId;
    }

    // Setters
    public void setUnavailable() {
        this.isAvailable = false;
    }

    // Behavior
    @Override
    public void update() {
        long currentTimeMillis = System.currentTimeMillis();
        if (activationTimeMillis > 0 && currentTimeMillis - activationTimeMillis > DURATION_MILLIS) {
            if (affectedTank != null) {
                remove(affectedTank);
            }
            this.markCollision(); // Mark for removal
        }
    }


    // Drawing
    @Override
    public void draw(Graphics g) {
        if (isAvailable) {
            g.drawImage(this.img, (int) x, (int) y, null);
        }
    }

    // Class helpers
    @Override
    public String toString() {
        String activationTimeFormatted = activationTimeMillis > 0 ? formatTime(activationTimeMillis) : "N/A";
        String removalTimeFormatted = activationTimeMillis > 0 ? formatTime(activationTimeMillis + DURATION_MILLIS) : "N/A";
        return "Power-up " + this.getClass().getSimpleName() + ": [x=" + x + ", y=" + y + ", activation time=" + activationTimeFormatted + ", removal time=" + removalTimeFormatted + "]";
    }

    private String formatTime(long timeMillis) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(new Date(timeMillis));
    }
}