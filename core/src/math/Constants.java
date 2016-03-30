package math;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Constants {

	private float gravity = 9.8f;

    public static TextureAtlas tilesAtlas = new TextureAtlas(Gdx.files.internal("tiles/tiles.atlas"));

	public float getGravity() {
		return gravity;
	}

	public void setGravity(float gravity) {
		this.gravity = gravity;
	}
}
