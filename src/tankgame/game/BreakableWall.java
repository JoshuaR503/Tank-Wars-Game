package tankgame.game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BreakableWall extends GameObject implements Colliable {
    public BreakableWall(float x, float y, BufferedImage img) {
        super(x, y, img);

    }

    @Override
    public void onCollision(GameObject by) {
        System.out.println("breakable wall collision with: " + by.toString());
    }
}
