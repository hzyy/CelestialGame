package data;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public enum ToolType {

    Wrench(1, "Wrench", "player/items/wrench.png", new Sprite(new Texture("player/items/wrench.png"))),
    Hammer(2, "Hammer", "player/items/crossbow.png", new Sprite(new Texture("tiles/bronzitecrossbow.png")));

    public Sprite getTileName() {
        return tileName;
    }

    int toolId;
    String textureName;
    Sprite tileName;

    ToolType(int toolId, String toolName, String textureName, Sprite tileName) {
        this.toolId = toolId;
        this.textureName = textureName;
        this.tileName = tileName;
    }

    public String getTextureName() {
        return textureName;
    }

    public void setTextureName(String textureName) {
        this.textureName = textureName;
    }

}
