package tankgame.game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class Animation {

    private float x,y;
    private List<BufferedImage> frames;
    private int currentFrame;
    private boolean isRunning;
    private long delay = 40;
    private long timeSinceLastUpdate = 0L;

    public Animation(float x, float y, List<BufferedImage> frames) {
        this.x = x;
        this.y = y;
        this.frames = frames;
        this.currentFrame = 0;
        this.isRunning = true;
    }

    // todo: animation has to face the same direction of the tank lmao
    public void update() {
        long currentTime = System.currentTimeMillis();

        if (this.timeSinceLastUpdate + this.delay < currentTime) {
            this.currentFrame = (this.currentFrame + 1) % frames.size();
        }
    }

    public void render(Graphics2D g) {
        if (isRunning) {
            g.drawImage(this.frames.get(currentFrame), (int)x, (int)y, null);
        }
    }

    public boolean isComplete() {
        return this.currentFrame == frames.size() - 1;
    }
}
