package tankgame.game;

import tankgame.GameConstants;
import tankgame.Launcher;
import tankgame.ResourceManager;
import tankgame.game.powerup.PowerUpFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameWorld extends JPanel implements Runnable {

    private BufferedImage world;
    private Tank t1;
    private Tank t2;
    private final Launcher lf;
    private long tick = 0;
    private int powerUps = 0;

    private static final ArrayList<GameObject> gObjs = new ArrayList<>(3000);
    private static final List<Animation> animations = new CopyOnWriteArrayList<>();

    private final Rectangle futureBounds = new Rectangle();
    private static SoundManager soundManager;
    private String winner;

    static double scaleFactor = .2;

    public GameWorld(Launcher lf) {
        this.lf = lf;

        soundManager = new SoundManager();
    }

    public static SoundManager getSoundManager() {
        return soundManager;
    }

    private GameState gameState = GameState.PAUSED;

    public void setGameState(GameState newState) {
        this.gameState = newState;
    }

    // State related.
    @Override
    public void run() {
        setGameState(GameState.RUNNING);


        soundManager.playBackgroundMusic("bgm");

        try {
            while (gameState == GameState.RUNNING) {
                    this.tick++;

                    // Check winner.
                    if (t1.getLife() <= 0 || t2.getLife() <= 0) {
                        setGameState(GameState.ENDED);
                        this.resetGame();
                        this.lf.setFrame("end",  t1.getLife() <= 0 ? "Tank 2" : "Tank 1");

                        soundManager.playBackgroundMusic("bgm");
                    }

                    for (int i = gObjs.size() - 1; i >= 0; i--) {
                        if (gObjs.get(i) instanceof Updatable u) {
                            u.update();
                        } else {
                            break;
                        }
                    }

                    for (Animation ani : animations) {
                        if (ani != null) {
                            ani.update();
                        }
                    }

                    this.checkCollisions();

                    gObjs.removeIf(GameObject::hasCollided);

                    if (this.tick % 100 == 0 && powerUps < GameConstants.GAME_WORLD_POWERUPS_LIMIT) {
                        System.out.println("Current game objects: " + gObjs.size());
                        powerUps++;
                        spawnPowerUp();
                    }

                    this.repaint();

                    Thread.sleep(1000 / 144);

            }
        } catch (InterruptedException ignored) {
            System.out.println(ignored);
        }
    }

    public void resetGame() {
        this.tick = 0;

        gObjs.clear();
        animations.clear();

        this.t1.setX(300);
        this.t1.setY(300);
    }

    public void InitializeGame() {

        PowerUpFactory.init();
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

        t1 = new Tank(100, 300, 0, 0, (short) 0, ResourceManager.getSprite("t1"));
        TankControl tc1 = new TankControl(t1, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);
        this.lf.getJf().addKeyListener(tc1);

        t2 = new Tank(1100, 300, 0, 0, (short) 180, ResourceManager.getSprite("t2"));
        TankControl tc2 = new TankControl(t2, KeyEvent.VK_UP, KeyEvent.VK_LEFT, KeyEvent.VK_DOWN, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER);
        this.lf.getJf().addKeyListener(tc2);

        gObjs.add(t1);
        gObjs.add(t2);
    }

    public static void addGameObject(GameObject gameObject) {
        gObjs.add(gameObject);
    }

    // Animation related.
    public static void createAnimation(Animation ani) {
        animations.add(ani);
    }

    // Power up related.
    private void spawnPowerUp() {
        // TODO: Make sure it doesn't show up where there are walls.
        Random rand = new Random();
        int x = rand.nextInt(GameConstants.GAME_WORLD_WIDTH);
        int y = rand.nextInt(GameConstants.GAME_WORLD_HEIGHT);

        // Requires small offset because it gets trippy...
        GameWorld.addGameObject(PowerUpFactory.newRandomInstance(x + 45, y + 45));

        GameWorld.createAnimation(new Animation(x, y, ResourceManager.getAnimation("powerpick"), 75));
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

                    // Automatically mark bullet for deletion & make an animation.
                    if (obj instanceof Bullet) {
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

        for (Animation animation : animations) {
            if (animation != null) {
                animation.draw(buffer);
                if (animation.isComplete()) {
                    animations.remove(animation);
                }
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
