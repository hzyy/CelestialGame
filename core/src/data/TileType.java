package data;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public enum TileType {

    // Enum list with different TileTypes which are used in the game


    // tile 12

    // Tiles
    // Air(0, "Air", 0, new TextureRegion(new TextureAtlas(Gdx.files.internal("tiles/tiles.atlas")).findRegion("air")), "tiles/air.png", false, 0, false, true, null),
    // TileID, tileName, maxStackSize, textureName, playerhold, collidable, timetodestruct, walltiles, haswall,walltype
    Air(0, "Air", 0, new Sprite(new Texture("tiles/air.png")), "tiles/air.png", false, 0, false, true, null),
    Dirt(1, "Dirt", 64,new Sprite(new Texture("tiles/dirt.png")), "player/items/dirt.png", true, 1, false, true, null),
    Grass(2, "Grass", 64, new Sprite(new Texture("tiles/grass.png")), "player/items/grass.png", true, 20, false, true, null),
    Stone(3, "Stone", 64, new Sprite(new Texture("tiles/stone.png")), "player/items/stone.png", true, 50, false, true, null),
    Wood(4, "Wood", 64, new Sprite(new Texture(("tiles/wood.png"))), "player/items/wood.png", true, 80, false, true, null),
    Water(5, "Water", 2, new Sprite(new Texture("tiles/watersheet.png")), "player/items/water.png", false, 0, false, true, null),
    Emerald(6, "Emerald", 16, new Sprite(new Texture(("tiles/emeraldore.png"))), "player/items/emerald.png", true, 150, false, true, null),
    Naurite(7, "Naurite", 16, new Sprite(new Texture(("tiles/naurite.png"))), "player/items/emerald.png", true, 400, false, true, null),
    Fire(8, "Fire", 2, new Sprite(new Texture(("tiles/firesheet.png"))), "player/items/fire.png", false, 120, false, true, null),
    // Walls
    DirtWall(9, "Dirt Wall", 128, new Sprite(new Texture(("tiles/dirtwall.png"))), "player/items/wall.png", false, 20, true, false, null),
    WoodWall(10, "Wood Wall", 128, new Sprite(new Texture(("tiles/woodwall.png"))), "player/items/wall.png", false, 60, true, false, null),
    GlassWall(11, "Glass Pane", 128, new Sprite(new Texture(("tiles/glasswall.png"))), "player/items/wall.png", false, 45, true, false, null),
    //Tiles
    Creator(12, "Michael", 10000, new Sprite(new Texture(("tiles/michael.png"))), "player/items/creator.png", false, 10000000, false, false, null),
    Snow(13, "Snow", 64, new Sprite(new Texture("tiles/snow.png")), "player/items/snow.png", true, 20, false, true, null),
    Black(14, "Black", 0, new Sprite(new Texture("tiles/blackTile.png")), "tiles/air.png", false, 20, false, false, null),
    Light(15, "Light", 64, new Sprite(new Texture("tiles/blackTile.png")), "tiles/air.png", false, 20, false, false, null);


    int tileID;
    int maxStackSize;
    TextureRegion textureName;
    boolean collidable;
    boolean wallTile;
    boolean hasWall;
    TileType wallType;
    float timeToDestruct;
    String playerHold;
    String tileName;


    TileType(int tileID, String tileName, int maxStackSize, TextureRegion textureName, String playerHold, boolean collidable, float timeToDestruct, boolean wallTile, boolean hasWall, TileType wallType) {
        this.textureName = textureName;
        this.maxStackSize = maxStackSize;
        this.collidable = collidable;
        this.timeToDestruct = timeToDestruct;
        this.tileID = tileID;
        this.wallTile = wallTile;
        this.hasWall = hasWall;
        this.wallType = wallType;
        this.playerHold = playerHold;
        this.tileName = tileName;
    }

    public static void setTileWalls() {
        Dirt.setWallType(DirtWall);
        Grass.setWallType(DirtWall);
        Wood.setWallType(WoodWall);
    }

    public Texture getTexture() {
        return textureName.getTexture();
    }

    public int getTileID() {

        return this.tileID;
    }

    public boolean getCollidable() {
        return collidable;
    }

    public float getTimeToDestruct() {
        return timeToDestruct;
    }


    public boolean isHasWall() {
        return hasWall;
    }

    public void setHasWall(boolean hasWall) {
        this.hasWall = hasWall;
    }

    public boolean isWallTile() {
        return wallTile;
    }

    public TileType getWallType() {
        return wallType;
    }

    public void setWallType(TileType wallType) {
        this.wallType = wallType;
    }


    public String getPlayerHold() {
        return playerHold;
    }

    public void setPlayerHold(String playerHold) {
        this.playerHold = playerHold;
    }

    public String getTileName() {
        return tileName;
    }

    public void setTileName(String tileName) {
        this.tileName = tileName;
    }

}
