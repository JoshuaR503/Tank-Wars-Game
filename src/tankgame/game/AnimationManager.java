package tankgame.game;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AnimationManager {

    private List<Animation> animations = new ArrayList<>();

    public void addAnimation(Animation animation) {
        animations.add(animation);
    }

    public void updateAnimations() {
        for (int i = 0; i < animations.size(); i++) {
            Animation animation = animations.get(i);
            if (animation.isComplete()) {
                animations.remove(i);
                i--;
            } else {
                animation.update();
            }
        }
    }

    public void draw(Graphics2D g) {
        for (Animation animation : animations) {
            animation.render(g);
        }
    }
}
