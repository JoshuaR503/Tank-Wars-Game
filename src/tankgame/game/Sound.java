package tankgame.game;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;


/// you can have event listeners and controlers
public class Sound {


    private Clip clip;
    private int loopCount;

    public Sound(Clip clip) {
        this.clip = clip;
    }

    public Sound(Clip clip, int loopCount) {
        this.clip = clip;
        this.clip.loop(this.loopCount);
    }

    public void play() {

        if (clip.isRunning()) {
            clip.stop();
        }

        clip.setFramePosition(0);
        clip.start();

    }

    public void stop() {
        this.clip.stop();
    }

    public void loopContinusly() {
        this.clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void setVolume(float level) {
//        FloatControl v = (FloatControl) this.clip.
    }
}
