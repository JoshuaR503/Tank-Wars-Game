package tankgame.game;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

/// todo: have a class that maanges the animations lmao
/// animations that are tied need to belong to smth
/// this cpuld be game objexts
public class Animation {
    private List<BufferedImage> frames;
    private int currentFrame;
    private long delay;
    private boolean isRunning;
    private float x,y;
    private long timeSinceLastUpdate = 0L;


    // animation has to face the same direction of the tank lmao
    public void update() {
        long currentTime = System.currentTimeMillis();

        if (this.timeSinceLastUpdate + this.delay < currentTime) {

            // if object needs continous animation
            this.currentFrame = (this.currentFrame + 1) % frames.size();
        }
    }

    public void render(Graphics2D g) {

        if (isRunning) {
            g.drawImage(this.frames.get(currentFrame), (int)x, (int)y, null);
        }
    }
}
