package tankgame.game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Wall extends GameObject implements Colliable {
    public Wall(float x, float y, BufferedImage img) {
        super(x, y, img);
    }

    @Override
    public void onCollision(GameObject by) {

    }
}
