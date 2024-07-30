package tankgame.game;

public interface Poolable {

    void initObject(float x, float y);

    void initObject(float x, float y, float angle);

    void resetObject();

}
