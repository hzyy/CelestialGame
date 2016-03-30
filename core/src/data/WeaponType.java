package data;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public enum WeaponType{

    BronziteCrossbow(1,"Bronzite Crossbow", "player/items/crossbow.png", new Sprite(new Texture("tiles/bronzitecrossbow.png"))),
    EpicBronziteCrossbow(2,"Epic Bronzite Crossbow", "player/items/epiccrossbow.png", new Sprite(new Texture("tiles/bronzitecrossbow.png")));

    public Sprite getTileName() {
        return tileName;
    }

    int weaponId;
    String textureName;
    Sprite tileName;

    WeaponType(int weaponId,String weaponName, String textureName, Sprite tileName) {
        this.weaponId = weaponId;
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
