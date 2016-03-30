package hud;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ActionBar extends Sprite {

    private final SpriteBatch hud;
    private float x;
    private float y;

    public ActionBar(float x, float y, SpriteBatch hud) {
        this.x = x;
        this.y = y;
        this.hud = hud;
    }

    public void draw() {
        update(Gdx.graphics.getDeltaTime());
    }

    public void update(float delta){

    }
}
