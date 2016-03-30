package data;


public enum Item {

    // Tiles
    Dirt(1, "Dirt", "Tile", TileType.Dirt),
    Grass(2, "Grass", "Tile", TileType.Grass),
    Stone(3, "Stone", "Tile", TileType.Stone),
    Wood(4, "Wood", "Tile", TileType.Wood),
    Water(5, "Water", "Tile", TileType.Water),
    Emerald(6, "Emerald", "Tile", TileType.Emerald),
    Naurite(7, "Naurite", "Tile", TileType.Naurite),
    Fire(8, "Fire", "Tile", TileType.Fire),
    DirtWall(9, "DirtWall", "Tile", TileType.DirtWall),
    WoodWall(10, "WoodWall", "Tile", TileType.WoodWall),
    GlassWall(11, "GlassWall", "Tile", TileType.GlassWall),
    Creator(12, "Creator", "Tile", TileType.Creator),
    Snow(13, "Snow", "Tile", TileType.Snow),

    // Weapons
    Wrench(16, "Wrench", "Tool", ToolType.Wrench),
    Hammer(17, "Hammer", "Tool", ToolType.Hammer),
    BronziteCrossbow(14, "Bronzite Crossbow", "Weapon", WeaponType.BronziteCrossbow),
    EpicBronziteCrossbow(15, "Epic Crossbow", "Weapon", WeaponType.EpicBronziteCrossbow);

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public TileType getTileType() {
        return tileType;
    }

    public void setTileType(TileType tileType) {
        this.tileType = tileType;
    }

    public WeaponType getWeaponType() {
        return weaponType;
    }

    public ToolType getToolType() {
        return toolType;
    }

    public void setWeaponType(WeaponType weaponType) {
        this.weaponType = weaponType;
    }

    int itemID;
    String itemName;
    String itemType;
    TileType tileType;
    WeaponType weaponType;
    ToolType toolType;

    Item(int itemID, String itemName, String itemType, TileType tileType) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.itemType = itemType;
        this.tileType = tileType;
    }

    Item(int itemID, String itemName, String itemType, WeaponType weaponType) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.itemType = itemType;
        this.weaponType = weaponType;
    }

    Item(int itemID, String itemName, String itemType, ToolType toolType) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.itemType = itemType;
        this.toolType = toolType;
    }

}