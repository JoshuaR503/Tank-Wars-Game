package tankgame.game;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sound {
    private Clip clip;
    private int loopCount;

    public Sound(Clip clip) {
        this.clip = clip;
        this.loopCount = 0;
    }

    public Sound(Clip clip, int loopCount) {
        this.clip = clip;
        this.loopCount = loopCount;
        this.clip.loop(this.loopCount);
    }

    public void play() {
        clip.setFramePosition(0);
        clip.start();
    }

    public void stop() {
        this.clip.stop();
    }

    public void loopContinuously() {
        this.clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void setVolume(float level) {
        FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        volumeControl.setValue(10.0f * (float) Math.log(level));
    }
}
