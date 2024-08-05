package tankgame.game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class Animation {

    private static final long DEFAULT_DELAY = 10; // Default delay in milliseconds

    private final float x, y;
    private final List<BufferedImage> frames;
    private final long delay;
    private final int middleIndex;

    // Dynamic attributes
    private int currentFrame;
    private boolean isRunning;
    private long lastUpdateTime;

    public Animation(float x, float y, List<BufferedImage> frames, long delay) {
        this.x = x;
        this.y = y;
        this.frames = frames;
        this.delay = delay;
        this.middleIndex = frames.size() / 2;
        this.currentFrame = 0;
        this.isRunning = true;
        this.lastUpdateTime = System.currentTimeMillis();
    }

    public Animation(float x, float y, List<BufferedImage> frames) {
        this(x, y, frames, DEFAULT_DELAY);
    }

    // Getters
    public int getFrameWidth() {
        return frames.get(middleIndex).getWidth();
    }

    public int getFrameHeight() {
        return frames.get(middleIndex).getHeight();
    }

    public boolean isComplete() {
        return !isRunning;
    }

    // Behavior
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

    // Drawing
    public void draw(Graphics2D g) {
        if (isRunning) {
            g.drawImage(this.frames.get(currentFrame), (int) x, (int) y, null);
        }
    }
}
