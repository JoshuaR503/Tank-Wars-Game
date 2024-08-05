package tankgame.game;

import tankgame.ResourceManager;
import tankgame.game.Sound;

public class SoundManager {
    private Sound currentBackgroundMusic;

    public void playBackgroundMusic(String soundKey) {
        if (currentBackgroundMusic != null) {
            currentBackgroundMusic.stop();
        }

        currentBackgroundMusic = ResourceManager.getSound(soundKey);
        if (currentBackgroundMusic != null) {
            currentBackgroundMusic.loopContinuously();
        } else {
            System.out.println("Sound key not found: " + soundKey);
        }
    }

    public void stopBackgroundMusic() {
        if (currentBackgroundMusic != null) {
            currentBackgroundMusic.stop();
        }
    }
}
