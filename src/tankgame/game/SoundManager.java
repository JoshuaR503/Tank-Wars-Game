package tankgame.game;

import tankgame.ResourceManager;

public class SoundManager {
    private Sound currentBackgroundMusic;

    public void playBackgroundMusic(String soundKey) {

        this.stopBackgroundMusic();

        currentBackgroundMusic = ResourceManager.getSound(soundKey);

        if (currentBackgroundMusic != null) {
            currentBackgroundMusic.loopContinuously();
        }
    }

    public void stopBackgroundMusic() {
        if (currentBackgroundMusic != null) {
            currentBackgroundMusic.stop();
        }
    }
}
