package tankgame.game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class Animation {

    private float x, y;
    private List<BufferedImage> frames;
    private int currentFrame;
    private boolean isRunning;
    private long delay;
    private long lastUpdateTime;

    public Animation(float x, float y, List<BufferedImage> frames) {
        this.x = x;
        this.y = y;
        this.frames = frames;
        this.currentFrame = 0;
        this.isRunning = true;
        this.delay = 10;
        this.lastUpdateTime = System.currentTimeMillis();
    }

    public void update() {
        if (!isRunning) {
            return;
        }

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastUpdateTime > delay) {
            lastUpdateTime = currentTime;
            currentFrame++;
            if (currentFrame >= frames.size()) {
                isRunning = false;
            }
        }
    }

    public void draw(Graphics2D g) {
        if (isRunning) {
            g.drawImage(this.frames.get(currentFrame), (int) x, (int) y, null);
        }
    }

    public boolean isComplete() {
        return !isRunning;
    }

    public void stop() {
        isRunning = false;
    }

    public void start() {
        isRunning = true;
        currentFrame = 0;
        lastUpdateTime = System.currentTimeMillis();
    }
}
