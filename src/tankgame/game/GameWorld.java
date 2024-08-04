package tankgame.game;

import tankgame.GameConstants;
import tankgame.Launcher;
import tankgame.ResourceManager;

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

public class GameWorld extends JPanel implements Runnable {

    private BufferedImage world;
    private Tank t1;
    private Tank t2;
    private final Launcher lf;
    private long tick = 0;

    private static final ArrayList<GameObject> gObjs = new ArrayList<>(1000);
    private static final ArrayList<Animation> animations = new ArrayList<>(1000);

    private final Rectangle futureBounds = new Rectangle();

    static double scaleFactor = .2;

    public GameWorld(Launcher lf) {
        this.lf = lf;
    }

    // State related.
    @Override
    public void run() {
        this.resetGame();
        try {
            while (true) {
                this.tick++;

                for (int i = gObjs.size() - 1; i >= 0; i--) {
                    if (gObjs.get(i) instanceof Updatable u) {
                        u.update();
                    } else {
                        break;
                    }
                }

                for (Animation ani : animations) {
                    ani.update();
                }

                this.checkCollisions();

                gObjs.removeIf(GameObject::hasCollided);

                this.repaint();

                Thread.sleep(1000 / 144);
            }
        } catch (InterruptedException ignored) {
            System.out.println(ignored);
        }
    }

    private void resetGame() {
        this.tick = 0;

        /// TODO: Make them spawn opposite sides at the middle.
        this.t1.setX(300);
        this.t1.setY(300);
    }

    public void InitializeGame() {
        GameObject.setGameWorld(this);
        this.world = new BufferedImage(GameConstants.GAME_WORLD_WIDTH,
                GameConstants.GAME_WORLD_HEIGHT,
                BufferedImage.TYPE_INT_RGB);

        InputStreamReader isr = new InputStreamReader(
                Objects.requireNonNull(
                        ResourceManager.class.getResourceAsStream("/maps/map2.csv")
                )
        );

        int row = 0;

        try (BufferedReader mapReader = new BufferedReader(isr)) {
            while (mapReader.ready()) {
                String line = mapReader.readLine();
                String[] gameItems = line.split(",");

                for (int col = 0; col < gameItems.length; col++) {
                    if (gameItems[col].equals("0") || gameItems[col].isEmpty()) continue;
                    gObjs.add(GameObject.newInstance(gameItems[col], col * 32, row * 32));
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

        gObjs.add(t1);
        gObjs.add(t2);
    }

    // Animation related.
    public static void createAnimation(Animation ani) {
        animations.add(ani);
    }

    public static void addGameObject(GameObject gameObject) {
        gObjs.add(gameObject);
    }

    // Collision related.
    private void checkCollisions() {
        for (int i = 0; i < gObjs.size(); i++) {
            GameObject obj = gObjs.get(i);

            if (!(obj instanceof Updatable)) {
                continue;
            }

            for (int j = 0; j < gObjs.size(); j++) {
                if (i == j) continue;

                GameObject obj2 = gObjs.get(j);

                if (!(obj2 instanceof Colliable)) {
                    continue;
                }

                if (obj.getHitbox().intersects(obj2.getHitbox())) {
                    ((Colliable) obj2).onCollision(obj);

                    if (obj instanceof Bullet) {
                        // Automatically mark bullet for deletion & make an animation.
                        obj.markCollision();
                        createAnimation(new Animation(obj2.x, obj2.y, ResourceManager.getAnimation("bullethit")));
                    }
                }
            }
        }
    }

    public boolean willCollideWithWall(float x, float y, float width, float height) {
        futureBounds.setBounds((int) x, (int) y, (int) width, (int) height);

        for (GameObject obj : gObjs) {
            if (obj instanceof BreakableWall && futureBounds.intersects(obj.getHitbox())) {
                return true;
            }
        }
        return false;
    }

    // Rendering related.
    private void renderFlor(Graphics buffer) {
        BufferedImage floor = ResourceManager.getSprite("floor");

        for (int i = 0; i < GameConstants.GAME_WORLD_WIDTH; i += 320) {
            for (int j = 0; j < GameConstants.GAME_WORLD_HEIGHT; j += 240) {
                buffer.drawImage(floor, i, j, null);
            }
        }
    }

    private void displayMiniMap(Graphics2D onScreenPanel) {
        BufferedImage mm = this.world.getSubimage(0, 0, GameConstants.GAME_WORLD_WIDTH, GameConstants.GAME_WORLD_HEIGHT);
        double mmx = GameConstants.GAME_SCREEN_WIDTH / 2. - (GameConstants.GAME_WORLD_WIDTH * scaleFactor) / 2;
        double mmy = GameConstants.GAME_SCREEN_HEIGHT - (GameConstants.GAME_WORLD_HEIGHT * scaleFactor);
        AffineTransform scaler = AffineTransform.getTranslateInstance(mmx, mmy);
        scaler.scale(scaleFactor, scaleFactor);
        onScreenPanel.drawImage(mm, scaler, null);
    }

    private void displaySplitScreen(Graphics2D onScreenPanel) {
        BufferedImage lh = this.world.getSubimage(
                this.t1.getScreenX(),
                this.t1.getScreenY(), GameConstants.GAME_SCREEN_WIDTH / 2, GameConstants.GAME_SCREEN_HEIGHT);

        BufferedImage rh = this.world.getSubimage(
                this.t2.getScreenX(),
                this.t2.getScreenY(), GameConstants.GAME_SCREEN_WIDTH / 2, GameConstants.GAME_SCREEN_HEIGHT);

        onScreenPanel.drawImage(lh, 0, 0, null);
        onScreenPanel.drawImage(rh, GameConstants.GAME_SCREEN_WIDTH / 2 + 2, 0, null);
    }

    private void renderFrame() {
        Graphics2D buffer = world.createGraphics();

        this.renderFlor(buffer);

        for (GameObject gObj : gObjs) {
            gObj.draw(buffer);
        }

        for (Animation ani : animations) {
            ani.draw(buffer);
            if (ani.isComplete()) {
                animations.remove(ani);
            }
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
