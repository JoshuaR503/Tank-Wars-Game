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
    ArrayList<Animation> anims = new ArrayList<>();

    public GameWorld(Launcher lf) {
        this.lf = lf;
    }

    @Override
    public void run() {
        this.resetGame();

//        Sound bg = ResourceManager.getSound("bg");
//        bg.loopContinusly();
//        bg.play();

        try {

            this.anims.add(new Animation(100, 100, ResourceManager.getAnimation("puffsmoke")));
            while (true) {
                this.tick++;

                for (int i = this.gObjs.size() -1; i >= 0; i--) {
                    if (this.gObjs.get(i) instanceof Updatable u) {
                        u.update(this);
                    } else {
                        break;
                    }
                }

                this.checkCollisions();
                this.gObjs.removeIf(GameObject::hasCollided);

                this.repaint();



                Thread.sleep(1000/144);
            }
        } catch (InterruptedException ignored) {
            System.out.println(ignored);
        }
    }

    private void checkCollisions() {
        for (int i = 0; i < this.gObjs.size(); i++) {
            GameObject obj = this.gObjs.get(i);

            if (!(obj instanceof Updatable)) {continue;}

            for (int j = 0; j < this.gObjs.size(); j++) {

                if (i == j) continue;

                GameObject obj2 = this.gObjs.get(j);

                if (!(obj2 instanceof Colliable)) {continue;}

                if (obj.getHitbox().intersects(obj2.getHitbox())) {

                    ((Colliable) obj2).onCollision(obj);

                    System.out.println("\n\n--------------------------");
                    System.out.println(obj);
                    System.out.println(obj2);

                    // Mark bullet for removal regardless of what it hit.
                    if (obj instanceof Bullet) {
                        System.out.println("Marked bullet for removal");
                        obj.markCollision();
                    }

                }
            }
        }
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
                        ResourceManager.class.getResourceAsStream("/maps/map.csv")
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

//        for (int i = 0; i < this.anims.size(); i++) {
//            this.anims.get(i).render(buffer);
//        }
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        this.renderFrame();

        this.displaySplitScreen(g2);
        this.displayMiniMap(g2);

    }

    public void addGameObject(GameObject gameObject) {
        this.gObjs.add(gameObject);
    }
}
