package tankgame.game;

import tankgame.GameConstants;
import tankgame.Launcher;
import tankgame.ResourceManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author anthony-pc
 */
public class GameWorld extends JPanel implements Runnable {

    private BufferedImage world;
    private Tank t1;
    private Tank t2;
    private final Launcher lf;
    private long tick = 0;

    ArrayList<GameObject> gObjs = new ArrayList<>(1000);

    public GameWorld(Launcher lf) {
        this.lf = lf;
    }

    @Override
    public void run() {
        this.resetGame();
        try {
            while (true) {
                this.tick++;
                this.t1.update(); // update tank
                this.t2.update();
                this.checkCollisions();
                this.repaint();   // redraw game
                /*
                 * Sleep for 1000/144 ms (~6.9ms). This is done to have our 
                 * loop run at a fixed rate per/sec. 
                */

                Thread.sleep(1000/144);
            }
        } catch (InterruptedException ignored) {
            System.out.println(ignored);
        }
    }

    private void checkCollisions() {
//        for (int i = 0; i < ; i++) {
//
//        }
    }

    /**
     * Reset game to its initial state.
     */
    public void resetGame() {
        this.tick = 0;
        this.t1.setX(300);
        this.t1.setY(300);
    }

    /**
     * Load all resources for Tank Wars Game. Set all Game Objects to their
     * initial state as well.
     */
    public void InitializeGame() {
        this.world = new BufferedImage(GameConstants.GAME_WORLD_WIDTH,
                GameConstants.GAME_WORLD_HEIGHT,
                BufferedImage.TYPE_INT_RGB);


        InputStreamReader isr = new InputStreamReader(
                Objects.requireNonNull(
                        ResourceManager.class.getResourceAsStream("/map.csv")
                )
        );

        int row = 0;

        try (BufferedReader mapReader = new BufferedReader(isr)) {
            while (mapReader.ready()) {
                String line = mapReader.readLine();
                String[] gameItems = line.split(",");

                for (int col = 0; col < gameItems.length; col++) {
                    if (gameItems[col].equals("0")) continue;
                    this.gObjs.add(GameObject.newInstance(gameItems[col], col*32, row*32));
                }

                row++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



        t1 = new Tank(300, 300, 0, 0, (short) 0, ResourceManager.getSprite("t1"));
        TankControl tc1 = new TankControl(t1, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);
        this.lf.getJf().addKeyListener(tc1);

        t2 = new Tank(400, 400, 0, 0, (short) 180, ResourceManager.getSprite("t2"));
        TankControl tc2 = new TankControl(t2, KeyEvent.VK_UP, KeyEvent.VK_LEFT, KeyEvent.VK_DOWN, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER);
        this.lf.getJf().addKeyListener(tc2);

        this.gObjs.add(t1);
        this.gObjs.add(t2);
    }

    private void renderFlor(Graphics buffer) {
        BufferedImage floor = ResourceManager.getSprite("floor");

        for (int i = 0; i < GameConstants.GAME_WORLD_WIDTH; i+=320) {
            for (int j = 0; j < GameConstants.GAME_WORLD_HEIGHT; j+=240) {
                buffer.drawImage(floor, i, j, null);
            }
        }
    }

    static double scaleFactor = .2;
    private void displayMiniMap(Graphics2D onScreenPanel) {
        BufferedImage mm = this.world.getSubimage(0, 0, GameConstants.GAME_WORLD_WIDTH, GameConstants.GAME_WORLD_HEIGHT);
        double mmx = GameConstants.GAME_SCREEN_WIDTH/2. - (GameConstants.GAME_WORLD_WIDTH * scaleFactor) / 2;
        double mmy = GameConstants.GAME_SCREEN_HEIGHT - (GameConstants.GAME_WORLD_HEIGHT * scaleFactor);
        AffineTransform scaler = AffineTransform.getTranslateInstance(mmx, mmy);
        scaler.scale(scaleFactor, scaleFactor);
        onScreenPanel.drawImage(mm, scaler, null);
    }

    private void displaySplitScreen(Graphics2D onScreenPanel) {
        BufferedImage lh = this.world.getSubimage(
                this.t1.getScreenX(),
                this.t1.getScreenY(), GameConstants.GAME_SCREEN_WIDTH/2, GameConstants.GAME_SCREEN_HEIGHT);

        BufferedImage rh = this.world.getSubimage(
                this.t2.getScreenX(),
                this.t2.getScreenY(), GameConstants.GAME_SCREEN_WIDTH/2, GameConstants.GAME_SCREEN_HEIGHT);

        onScreenPanel.drawImage(lh, 0, 0, null);
        onScreenPanel.drawImage(rh, GameConstants.GAME_SCREEN_WIDTH/2+2, 0, null);
    }

    private void renderFrame() {
        Graphics2D buffer = world.createGraphics();

        this.renderFlor(buffer);
        for (int i = 0; i < this.gObjs.size(); i++) {
            this.gObjs.get(i).draw(buffer);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        this.renderFrame();

        this.displaySplitScreen(g2);
        this.displayMiniMap(g2);

    }
}
