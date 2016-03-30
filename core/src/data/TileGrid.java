
package data;

import co.hzyy.celestial.screens.CelestialEngine;
import co.hzyy.celestial.screens.Game;
import co.hzyy.celestial.screens.SizeSelect;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import entities.Player;
import math.PerlinNoise;

import java.util.Random;

public class TileGrid {

    //tile anim
    private Animation playerAnimWater, playerAnimFire;
    private TextureRegion[] playerWaterFrames, playerFireFrames;
    private TextureRegion playerWaterAnim, playerFireAnim;
    private float stateTime;
    private float[] distanceFromTile = new float[SizeSelect.maxX];

    public static int halfScreenTileX, halfScreenTileY;
    public static int tileSize = 16;
    public static Tile[][] map;
    public static Tile[][] mapWall;

    private final CelestialEngine game;
    private int minX = 0;
    public static int worldSizeInPixelsX;
    public static int worldSizeInPixelsY;
    private float columnHeight;
    private PerlinNoise octave0, octave1, octave2;
    private float scaleHills = 1.5f;
    private boolean topLayerIsDone = false;
    private int columnHeightIncrease = 100;
    private int[] seed = new int[3];
    private int randomGen = 0;
    private static int orgin;
    int randomNumber;
    private float[] tallestTile = new float[SizeSelect.maxX];

    private ShapeRenderer sr;


    public static Boolean hitTop = false, hitBottom = false;

    private static float lightIntensity;


    public TileGrid(final CelestialEngine game) {
        this.game = game;
        sr = new ShapeRenderer();
        TileType.setTileWalls();
        halfScreenTileX = ((Gdx.graphics.getWidth() / tileSize) / 2) + 2;
        halfScreenTileY = ((Gdx.graphics.getHeight() / tileSize) / 2) + 2;
        worldSizeInPixelsX = SizeSelect.maxX * tileSize;
        worldSizeInPixelsY = SizeSelect.maxY * tileSize;
        initializeOctaves();
        map = new Tile[SizeSelect.maxX][SizeSelect.maxY];
        mapWall = new Tile[SizeSelect.maxX][SizeSelect.maxY];
        // FireTile
        int indexFire = 0;
        TextureRegion[][] tmpFire = TextureRegion.split(TileType.Fire.getTexture(),
                TileType.Fire.getTexture().getWidth() / 4,
                TileType.Fire.getTexture().getHeight() / 1);
        playerFireFrames = new TextureRegion[4 * 1];
        playerAnimFire = new Animation(.1f, playerFireFrames);

        // WaterTile
        int indexWater = 0;
        TextureRegion[][] tmpWater = TextureRegion.split(TileType.Water.getTexture(),
                TileType.Water.getTexture().getWidth() / 4,
                TileType.Water.getTexture().getHeight() / 1);
        playerWaterFrames = new TextureRegion[4 * 1];
        playerAnimWater = new Animation(.1f, playerWaterFrames);

        stateTime = 0f;


        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 4; j++) {
                playerFireFrames[indexFire++] = tmpFire[i][j];
                playerWaterFrames[indexWater++] = tmpWater[i][j];
            }
        }


        for (int i = minX; i < SizeSelect.maxX; i++) {
            columnHeight = (float) ((12 * octave0.getNoiseLevelAtPosition(i, 2))+6) * scaleHills;
            //System.out.println((int)columnHeight);
            //System.out.println(columnHeight);
            // Generate top layer (Grass)
            for (int j = (int) columnHeight - 1; j < (columnHeightIncrease + columnHeight); j++) {
                tallestTile[i] = j;
                randomNumber = new Random().nextInt(25);
                map[i][j] = new Tile(i * tileSize, j * tileSize, tileSize, tileSize, TileType.Snow);
                mapWall[i][j] = new Tile(i * tileSize, j * tileSize, tileSize, tileSize, TileType.DirtWall);
            }
            // Generate second layer (Dirt)
            for (int j = 0; j < (columnHeightIncrease + columnHeight) - 1; j++) {
                map[i][j] = new Tile(i * tileSize, j * tileSize, tileSize, tileSize, TileType.Dirt);
                mapWall[i][j] = new Tile(i * tileSize, j * tileSize, tileSize, tileSize, TileType.DirtWall);
            }
            // Generate third layer (Water)
            /*
            for (int j = (int)((columnHeightIncrease + columnHeight) - (int)(Math.random()*5)); j < (columnHeightIncrease + columnHeight); j++) {
                System.out.println(columnHeight);
                if((int)(columnHeight)%3 ==0){
                    System.out.println("hey");
                    map[i][j] = new Tile(i * tileSize, j * tileSize, tileSize, tileSize, TileType.Water);}
            }
            */
            // Generate forth layer (Stone)
            for (int j = 0; j < (columnHeightIncrease + columnHeight) - 50; j++) {
                /*
                randomGen = (int) (Math.random() * 30 + 1);
                if (j <= 80){
                    if (map[i][j] == map[i][j%randomGen%2]) {
                        map[i][j] = new Tile(i * tileSize, j * tileSize, tileSize, tileSize, TileType.Emerald);
                    }else if (map[i][j] == map[i][j%randomGen]) {
                        map[i][j] = new Tile(i * tileSize, j * tileSize, tileSize, tileSize, TileType.Naurite);
                    }
                    else{
                        map[i][j] = new Tile(i * tileSize, j * tileSize, tileSize, tileSize, TileType.Stone);
                    }
                }else {
                    map[i][j] = new Tile(i * tileSize, j * tileSize, tileSize, tileSize, TileType.Stone);
                }*/
                map[i][j] = new Tile(i * tileSize, j * tileSize, tileSize, tileSize, TileType.Stone);

            }
            // Generate invisible tile layer (Air) afterwards
            for (int j = 0; j < SizeSelect.maxY; j++) {
                if (map[i][j] == null) {
                    map[i][j] = new Tile(i * tileSize, j * tileSize, tileSize, tileSize, TileType.Air);
                    mapWall[i][j] = new Tile(i * tileSize, j * tileSize, tileSize, tileSize, TileType.Air);
                    // draw the tree's until the height is greater than the tallest tile + the random number
                    // else, just fill the map[i][j]'s with air tiles
                    /*
                    if (randomNumber == 3 && !(j > (tallestTile[i]) + (randomNumber * 2))) {
                        mapWall[i][j] = new Tile(i * tileSize, j * tileSize, tileSize, tileSize, TileType.Wood);
                        Gdx.app.log("Generated", "Tree");
                    } else {
                        map[i][j] = new Tile(i * tileSize, j * tileSize, tileSize, tileSize, TileType.Air);
                        mapWall[i][j] = new Tile(i * tileSize, j * tileSize, tileSize, tileSize, TileType.Air);
                    }
                    */
                }

            }

        }


        /* write the file (save game)
        FileOutputStream outputUser;
        try {
            outputUser = new FileOutputStream("world.txt");
            for (int i = minX; i < maxX; i++) {
                for (int j = 0; j < maxY; j++) {
                    new PrintStream(outputUser).print(map[i][j].getType() + " ");
                }
                new PrintStream(outputUser).println();
            }
            outputUser.close();
        } catch (IOException e) {
            System.err.println("Unable to write to file");
            System.exit(-1);
        }
        */
    }

    // Seperate constructor used for premade maps, uses a 2D array with different numbers to represent different tiles.
    // Might be useful for map editors later on
    public TileGrid(final CelestialEngine gam, int[][] newMap) {
        this.game = gam;
        map = new Tile[250][91];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                switch (newMap[j][i]) {
                    case 0:
                        map[i][j] = new Tile(i * 8, j * 8, 8, 8, TileType.Grass);
                        break;
                    case 1:
                        map[i][j] = new Tile(i * 8, j * 8, 8, 8, TileType.Dirt);
                        break;
                    case 2:
                        map[i][j] = new Tile(i * 8, j * 8, 8, 8, TileType.Water);
                        break;
                }
            }
        }
    }

    // Renders the screen using basic frustum culling
    public void draw() {
        stateTime += Gdx.graphics.getDeltaTime();
        playerFireAnim = playerAnimFire.getKeyFrame(stateTime, true);
        playerWaterAnim = playerAnimWater.getKeyFrame(stateTime, true);
        for (int i = ((int) Game.mainPlayer.getX() / tileSize - halfScreenTileX); i <= ((int) Game.mainPlayer.getX() / tileSize + halfScreenTileX); i++) {
            for (int j = ((int) Game.mainPlayer.getY() / tileSize - halfScreenTileY)+(100/16); j <= ((int) Game.mainPlayer.getY() / tileSize + halfScreenTileY)+(100/16); j++) {
                Tile t = map[i][j];
                Tile tWall = mapWall[i][j];
                /*
                if (t.getY() - tallestTile > 5) {
                        map[i][j].getTexture().setAlpha(0.75f);
                    }
                    */


                if (Game.isDebugOn) {
                    if (t.getType() != TileType.Air) {
                        sr.begin(ShapeRenderer.ShapeType.Line);
                        sr.setColor(1, 1, 1, 1);
                        sr.rect(t.getX() - Game.mainPlayer.getX() + 720, t.getY() - Game.mainPlayer.getY() + 300, 16, 16);
                        sr.end();
                    }
                }
                if (t.getType() != TileType.Air) {
                    if (t.getType() == TileType.Fire) {
                        game.batch.draw(playerFireAnim, t.getX(), t.getY());
                    } else if (t.getType() == TileType.Water) {
                        game.batch.draw(playerWaterAnim, t.getX(), t.getY());
                    } else {
                        t.getTexture().setX(t.getX());
                        t.getTexture().setY(t.getY());
                        t.getTexture().draw(game.batch);
                    }
                }
                if (t.getType() == TileType.Air) {
                    if (tWall.getType() != TileType.Air) {
                        tWall.getTexture().setX(tWall.getX());
                        tWall.getTexture().setY(tWall.getY());
                        tWall.getTexture().draw(game.batch);
                    }

                }



                /*
                float dist;
                dist = Game.mainPlayer.getY()/16 - t.getY();
                System.out.println(dist);
                float lightPower;
                lightPower = dist / 100f;
                if (dist < 2){
                    map[i][j].getTexture().setColor(map[i][j].getTexture().getColor().r, map[i][j].getTexture().getColor().g, map[i][j].getTexture().getColor().b, dist);
                }else{
                    map[i][j].getTexture().setColor(map[i][j].getTexture().getColor().r, map[i][j].getTexture().getColor().g, map[i][j].getTexture().getColor().b, 1f);
                }*/


                // Color the blocks here (opacity = .5 if in dist, if else color red)
                if (!Game.mainPlayer.getEquipedWeapon()) {
                    if (Player.selectDistance) {
                        Game.mainPlayer.getCursor().setX(map[(int) Player.mouse_position.x / tileSize][(int) Player.mouse_position.y / tileSize].getX());
                        Game.mainPlayer.getCursor().setY(map[(int) Player.mouse_position.x / tileSize][(int) Player.mouse_position.y / tileSize].getY());

                       /*
                        map[(int) Game.mainPlayer.mouse_position.x / tileSize][(int) Game.mainPlayer.mouse_position.y / tileSize].getTexture().setColor(map[(int) Game.mainPlayer.mouse_position.x / tileSize][(int) Game.mainPlayer.mouse_position.y / tileSize].getTexture().getColor().r,
                                map[(int) Game.mainPlayer.mouse_position.x / tileSize][(int) Game.mainPlayer.mouse_position.y / tileSize].getTexture().getColor().g,
                                map[(int) Game.mainPlayer.mouse_position.x / tileSize][(int) Game.mainPlayer.mouse_position.y / tileSize].getTexture().getColor().b, 0.5f);
                        if (map[i][j] != map[(int) Game.mainPlayer.mouse_position.x / tileSize][(int) Game.mainPlayer.mouse_position.y / tileSize]) {
                            map[i][j].getTexture().setColor(map[i][j].getTexture().getColor().r, map[i][j].getTexture().getColor().g, map[i][j].getTexture().getColor().b, 1f);

                        }
                    }
                    if (map[i][j] != map[(int) Game.mainPlayer.mouse_position.x / tileSize][(int) Game.mainPlayer.mouse_position.y / tileSize]) {
                        map[i][j].getTexture().setColor(map[i][j].getTexture().getColor().r, map[i][j].getTexture().getColor().g, map[i][j].getTexture().getColor().b, 1f);
                    }
                    */
                    }
                }

                /*
                //Sun stuff
                //System.out.println(Game.findDistanceBetweenVectors(Game.sun.getX(),Game.sun.getY(), map[i][j].getX(),map[i][j].getY()));
                if (Game.sun.getY() - map[(int)Game.mainPlayer.getX()/ TileGrid.tileSize][(int)(Game.mainPlayer.getY()/ TileGrid.tileSize)].getY() < 400){
                    map[i][j].getTexture().setColor(map[i][j].getTexture().getColor().r, map[i][j].getTexture().getColor().g, map[i][j].getTexture().getColor().b, .5f);
                } else{
                    map[i][j].getTexture().setColor(map[i][j].getTexture().getColor().r, map[i][j].getTexture().getColor().g, map[i][j].getTexture().getColor().b, 1f);
                }
                */

                if (Game.isGravityOn) {
                    // Collision
                    if (t.getRect() != null) {
                        // Bottom Collisions (this was HELL) short code big thought
                        // Collisions where orgin is the players x pos
                        if (Game.mainPlayer.getMovement().x > 0) {
                            orgin = (int) ((Game.mainPlayer.getX() + (Game.mainPlayer.getWidth() / 3)) / TileGrid.tileSize);
                        }
                        if (Game.mainPlayer.getMovement().x < 0) {
                            orgin = (int) (Game.mainPlayer.getX() / TileGrid.tileSize);
                        }
                        if (Game.mainPlayer.getMovement().x == 0) {
                            orgin = (int) (Game.mainPlayer.getX() / TileGrid.tileSize);
                        }
                        if (map[orgin][(int) (Game.mainPlayer.getY() / TileGrid.tileSize - 0.12)].getType().collidable) {
                            if (isTileCollidableLeft())
                                Game.mainPlayer.setY(Game.mainPlayer.getY() + 0.1f);
                            if (isTileCollidableRight())
                                Game.mainPlayer.setY(Game.mainPlayer.getY() + 0.1f);
                            if (!isTileCollidableLeft() && !isTileCollidableRight())
                                Game.mainPlayer.setY(Game.mainPlayer.getY() + 0.1f);
                            //Gdx.app.log("Collided", "Bottom");
                            Game.gravity = 0;
                            hitBottom = true;
                        } else {
                            hitBottom = false;
                        }
                        // Top, collider
                        if (Intersector.overlaps(Game.mainPlayer.getBounds(), t.getRect())) {
                            if (Game.mainPlayer.getY() <= (t.getY())) {
                                Game.mainPlayer.setY(Game.mainPlayer.getY() - 0.1f);
                                Game.mainPlayer.setMovement(new Vector2(Game.mainPlayer.getMovement().x, 0));
                                hitTop = true;
                            } else {
                                hitTop = false;
                            }


                        }
                    }
                }
            }
        }
    }


    // Set a specific tile at given coords

    public void setTile(int xCoord, int yCoord, TileType type) {
        if (!type.wallTile) {
            map[xCoord][yCoord] = new Tile(xCoord * tileSize, yCoord * tileSize, tileSize, tileSize, type);
        } else {
            //mapWall[xCoord][yCoord] = new Tile(xCoord * tileSize, yCoord * tileSize, tileSize, tileSize, type);
        }

        /*if (type.getWallType() != null){
            mapWall[xCoord][yCoord] = new Tile(xCoord * tileSize, yCoord * tileSize, tileSize, tileSize, type.getWallType());
        }*/
    }

    // Returns a specific tile and the coords
    public Tile getTile(int xCoord, int yCoord) {
        return map[xCoord][yCoord];
    }


    // Creates three random octaves for the noise
    private void initializeOctaves() {
        for (int i = 0; i < 3; i++) {
            seed[i] = (int) (Math.random() * 60000 + 400);
            Gdx.app.log("Octave[" + i + "]", String.valueOf(seed[i]));
        }
        octave0 = new PerlinNoise(seed[0], 50);
        octave1 = new PerlinNoise((long) (10 * Math.sqrt(seed[1])), 5);
        octave2 = new PerlinNoise((long) Math.pow(seed[2], 2), 20);
    }


    public static boolean isTileCollidableLeft() {
        if (map[(int) (Game.mainPlayer.getX() / TileGrid.tileSize - 0.25)][(int) (Game.mainPlayer.getY() / TileGrid.tileSize + 1.5)].getType().collidable) {
            Game.mainPlayer.setMovement(new Vector2(0, 0));
            Game.mainPlayer.setX(Game.mainPlayer.getX() + 0.1f);
            return true;
        } else {
            return false;
        }
    }

    public static boolean isTileCollidableRight() {
        if (map[(int) (orgin + 0.25)][(int) (Game.mainPlayer.getY() / TileGrid.tileSize + 1.5)].getType().collidable) {
            Game.mainPlayer.setMovement(new Vector2(0, 0));
            Game.mainPlayer.setX(Game.mainPlayer.getX() - 0.1f);
            return true;
        } else {
            return false;
        }
    }

}

