package entities;

import co.hzyy.celestial.screens.CelestialEngine;
import co.hzyy.celestial.screens.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import data.TileGrid;
import data.TileType;

import java.util.ArrayList;

public class Player extends Sprite implements InputProcessor {

    //player anim
    private Animation playerAnimWalk, playerAnimIdle, playerAnimHead, playerAnimWrench;
    private Texture playerWalk, playerIdle, playerHead, playerWrench;
    private TextureRegion[] playerFrames, playerIdleFrames, playerHeadFrames, playerWrenchFrames;
    private TextureRegion playerWalkAnim, playerIdleAnim, playerHeadAnim, playerWrenchAnim;

    private final CelestialEngine game;
    private float x, y, width, height, walkSpeed = 2;
    private float speed = 60 * 2;

    private Sprite body, arms;
    private String playerBodyPath = "player/playerBody.png";
    private String playerArmPath = "player/playerArms.png";
    private String playerJumpPath = "player/playerJump.png";
    private String playerHeadPath;
    private String playerHoldPath;
    private Rectangle bounds;
    private Vector2 movement = new Vector2();
    private Boolean equipedWeapon = true;
    private Boolean equipedTool = false;
    private TileType selectedBlock;
    private float stateTime;
    private float angleTurn;
    private float headAngleTurn;
    private int playerDirection = 1, playerDirectionMove = 5;
    int headRotation = 0;
    private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
    private Sprite crossbowBolt;
    private boolean isBreakingBlocks = false, isAddingBlocks = false;
    private float blockBreakTime = 100, blockBreakTimer;
    //Music wavSound = Gdx.audio.newMusic(Gdx.files.internal("sounds/mining/blockbreak.wav"));
    //Music wavSound1 = Gdx.audio.newMusic(Gdx.files.internal("sounds/mining/pick.wav"));
    private float armTurn;

    public static int selectedSlot = 1;

    public static boolean isInventoryOn = false;
    private Node node;


    public static Vector3 mouse_position = new Vector3(0, 0, 0);
    public static boolean selectDistance = false;

    private Sprite cursor;

    private float multiplier;

    public Player(float x, float y, final CelestialEngine game) {
        this.game = game;
        this.x = x;
        this.y = y;
        this.body = new Sprite(new Texture(playerBodyPath));
        this.arms = new Sprite(new Texture(playerArmPath));
        this.width = body.getWidth();
        this.height = body.getHeight();
        this.bounds = new Rectangle(x, y, width, height);
        init();

        playerHeadPath = "player/playerHead.png";
        playerHoldPath = "player/items/crossbow.png";
    }

    public Player(float x, float y, Sprite body, Sprite arms, final CelestialEngine game) {
        this.game = game;
        this.x = x;
        this.y = y;
        this.body = body;
        this.arms = arms;
        this.width = body.getWidth();
        this.height = body.getHeight();
        this.bounds = new Rectangle(x, y, width, height);
        init();

        playerHeadPath = "player/playerHead.png";
        playerHoldPath = "player/items/crossbow.png";
    }

    // Update and renders the projectiles (bolts)
    private void projectileUpdate() {
        for (int i = 0; i < projectiles.size(); i++) {
            Projectile projectile = projectiles.get(i);
            game.batch.draw(crossbowBolt, projectile.getLocX(), projectile.getLocY());
        }
        for (int i = 0; i < projectiles.size(); i++) {
            Projectile projectile = projectiles.get(i);
            projectile.move();
        }
    }


    public void draw() {
        update(Gdx.graphics.getDeltaTime());
        stateTime += Gdx.graphics.getDeltaTime();
        playerWalkAnim = playerAnimWalk.getKeyFrame(stateTime, true);
        playerIdleAnim = playerAnimIdle.getKeyFrame(stateTime, true);
        playerHeadAnim = playerAnimHead.getKeyFrame(stateTime, true);
        playerWrenchAnim = playerAnimWrench.getKeyFrame(stateTime, true);

        // Moves the player
        setY(getY() + getMovement().y * Gdx.graphics.getDeltaTime());
        setX(getX() + getMovement().x * Gdx.graphics.getDeltaTime());

        selectDistance = Game.findDistanceBetweenVectors(mouse_position.x / TileGrid.tileSize, mouse_position.y / TileGrid.tileSize, getX() / TileGrid.tileSize, getY() / TileGrid.tileSize) < 10;

        //remove blocks loop if it's not air
        if (selectDistance) {
            if (TileGrid.map[(int) (mouse_position.x / TileGrid.tileSize)][(int) (mouse_position.y / TileGrid.tileSize)].getType() != TileType.Air) {
                if (isBreakingBlocks) {
                /* Player breaking animation */
                    if (playerDirection > 0)
                        multiplier += 5f;
                    else
                        multiplier -= 5f;
                    if (Math.abs(multiplier) == 105)
                        multiplier = 0;
                    setSelectedBlock(null);
                    setPlayerHold("player/items/wrench.png");
                    // this creates the timer to break
                    if (blockBreakTimer == Game.worldTiles.getTile((int) mouse_position.x / TileGrid.tileSize, (int) mouse_position.y / TileGrid.tileSize).getType().getTimeToDestruct()) {
                        TileType n = Game.worldTiles.getTile((int) mouse_position.x / TileGrid.tileSize, (int) mouse_position.y / TileGrid.tileSize).getType();
                        node = new Node(n, (int) mouse_position.x / TileGrid.tileSize, (int) mouse_position.y / TileGrid.tileSize);
                        Game.worldTiles.setTile((int) mouse_position.x / TileGrid.tileSize, (int) mouse_position.y / TileGrid.tileSize,
                                TileType.Air);
                        game.batch.draw(node.getT().getTexture(), mouse_position.x, mouse_position.y);
                        //wavSound.play();
                        blockBreakTimer = 0;
                    } else {
                        //wavSound1.play();
                        blockBreakTimer++;
                    }
                } else {
                    multiplier = 0;
                }
            }
        }
        // Add blocks loop
        if (isAddingBlocks) {
            if (getSelectedBlock().isWallTile()) {
                if (Game.worldTiles.getTile((int) mouse_position.x / TileGrid.tileSize,
                        (int) mouse_position.y / TileGrid.tileSize).getType() == TileType.Air) {
                    Game.worldTiles.setTile((int) mouse_position.x / TileGrid.tileSize,
                            (int) mouse_position.y / TileGrid.tileSize, getSelectedBlock());

                }
            } else if (!((mouse_position.x > (getX() - 40) && mouse_position.x < (getX() + 2) + getWidth()) && (mouse_position.y > getY() - 5 && mouse_position.y < getY() + 20 + getHeight()))) {
                if (Game.worldTiles.getTile((int) mouse_position.x / TileGrid.tileSize,
                        (int) mouse_position.y / TileGrid.tileSize).getType() == TileType.Air) {
                    Game.worldTiles.setTile((int) mouse_position.x / TileGrid.tileSize,
                            (int) mouse_position.y / TileGrid.tileSize, getSelectedBlock());
                }
            }
        }

        // Changes opacity of block selectors
        if (!Game.mainPlayer.getEquipedWeapon()) {
            if (selectDistance) {
                if (blockBreakTimer < 10) {
                    cursor.draw(game.batch, 1);
                } else if (blockBreakTimer < 20) {
                    cursor.draw(game.batch, 0.85f);
                } else if (blockBreakTimer < 30) {
                    cursor.draw(game.batch, 0.7f);
                } else if (blockBreakTimer < 40) {
                    cursor.draw(game.batch, 0.5f);
                } else if (blockBreakTimer < 50) {
                    cursor.draw(game.batch, 0.4f);
                } else if (blockBreakTimer < 60) {
                    cursor.draw(game.batch, 0.3f);
                } else if (blockBreakTimer < 70) {
                    cursor.draw(game.batch, .2f);
                } else if (blockBreakTimer < 90) {
                    cursor.draw(game.batch, .1f);
                }
            }
        }


        mouseAngleCalculations();

        projectileUpdate();
        switchBlock();

        // The -5 draws the player to the ground since the hitbox floats
        if (!(Game.gravity < -1)) {
            if (movement.x != 0)
                game.batch.draw(playerWalkAnim, getX() - playerDirectionMove, getY(), 0, 0, getBodyTexture().getWidth(), getBodyTexture().getHeight(), playerDirection, 1, 0);
            else
                game.batch.draw(playerIdleAnim, getX() - playerDirectionMove, getY(), 0, 0, getBodyTexture().getWidth(), getBodyTexture().getHeight(), playerDirection, 1, 0);
        } else
            game.batch.draw(new Sprite(new Texture(playerJumpPath)), getX() - playerDirectionMove, getY(), 0, 0, getBodyTexture().getWidth(), getBodyTexture().getHeight(), playerDirection, 1, 0);
        game.batch.draw(getArmTexture(), getX(), getY() + 22, 4, 20,
                getArmTexture().getWidth(), getArmTexture().getHeight(), playerDirection, 1,
                angleTurn + multiplier);

        game.batch.draw(playerHeadAnim, getX(), getY() + 43, 5,
                0, new Sprite(new Texture(getPlayerHead())).getWidth(),
                new Sprite(new Texture(getPlayerHead())).getHeight(), playerDirection, 1,
                headAngleTurn);


        if (isBreakingBlocks) {
            game.batch.draw(playerWrenchAnim, getX(), getY() + 22, 4, 20,
                    getArmTexture().getWidth(), getArmTexture().getHeight(), playerDirection, 1,
                    angleTurn + multiplier);
        } else {
            game.batch.draw(new Sprite(new Texture(getPlayerHold())), getX(), getY() + 22, 4, 20,
                    new Texture(getPlayerHold()).getWidth(), getArmTexture().getHeight(), playerDirection, 1,
                    angleTurn);
        }

    }

    private void mouseAngleCalculations() {
        angleTurn = (float) Math.toDegrees(Math.atan2(-(Gdx.input.getY() - (Gdx.graphics.getHeight() / 2)),
                (Gdx.input.getX() - (Gdx.graphics.getWidth() / 2)))); // - 20f);
        headAngleTurn = angleTurn;
        if (angleTurn > 90 && angleTurn < 180) {
            //left
            playerDirection = -1;
            angleTurn += 180;
            headAngleTurn += 180;
            playerDirectionMove = -22;
            headRotation = 40;
        } else if (angleTurn > 0 && angleTurn < 90) {
            //right
            playerDirection = 1;
            playerDirectionMove = 15;
            headRotation = -40;
        } else if (angleTurn > -180 && angleTurn < 0) {
            angleTurn += 360;
            if (angleTurn > 180 && angleTurn < 270) {
                //left
                angleTurn += 180;
                headAngleTurn += 180;
                playerDirection = -1;
                playerDirectionMove = -22;
                if (headAngleTurn >= 25) {
                    headAngleTurn = 25;
                }
                headRotation = 40;

            }
            if (angleTurn > 270 && angleTurn < 360) {
                //right
                playerDirection = 1;
                playerDirectionMove = 15;
                headRotation = -40;
                if (headAngleTurn <= 350) {
                    headAngleTurn = 350;

                }


            }
        }
    }


    public void update(float delta) {

        //movement.y -= gravity * delta;

    }

    private void init() {
        cursor = new Sprite(new Texture("tiles/cursor.gif"));
        crossbowBolt = new Sprite(new Texture("player/bullet.png"));
        playerWalk = new Texture("player/walksheet.png");
        playerIdle = new Texture("player/idlesheet.png");
        playerHead = new Texture("player/playerHeadSheet.png");
        playerWrench = new Texture("player/items/wrenchsheet.png");

        /* Player Animations */
        //Player Walk
        int indexWalk = 0;
        TextureRegion[][] tmpWalk = TextureRegion.split(playerWalk,
                playerWalk.getWidth() / 4,
                playerWalk.getHeight() / 1);
        playerFrames = new TextureRegion[4 * 1];
        playerAnimWalk = new Animation(.2f, playerFrames);
        //Player Idle
        int indexIdle = 0;
        TextureRegion[][] tmpIdle = TextureRegion.split(playerIdle,
                playerIdle.getWidth() / 4,
                playerIdle.getHeight() / 1);
        playerIdleFrames = new TextureRegion[4 * 1];
        playerAnimIdle = new Animation(.2f, playerIdleFrames);
        //Head
        int indexHead = 0;
        TextureRegion[][] tmpHead = TextureRegion.split(playerHead,
                playerHead.getWidth() / 4,
                playerHead.getHeight() / 1);
        playerHeadFrames = new TextureRegion[4 * 1];
        playerAnimHead = new Animation(.2f, playerHeadFrames);
        //Wrench
        int indexWrench = 0;
        TextureRegion[][] tmpWrench = TextureRegion.split(playerWrench,
                playerWrench.getWidth() / 8,
                playerWrench.getHeight() / 1);
        playerWrenchFrames = new TextureRegion[8 * 1];
        playerAnimWrench = new Animation(.1f, playerWrenchFrames);

        stateTime = 0f;


        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 4; j++) {
                playerFrames[indexWalk++] = tmpWalk[i][j];
                playerIdleFrames[indexIdle++] = tmpIdle[i][j];
                playerHeadFrames[indexHead++] = tmpHead[i][j];
            }
            for (int d = 0; d < 8; d++) {
                playerWrenchFrames[indexWrench++] = tmpWrench[i][d];
            }
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        Game.c.setKeyID(keycode);
        if (keycode == Input.Keys.SPACE) {
            if (TileGrid.hitBottom)
                setMovement(new Vector2(0, getMovement().y));
            TileGrid.hitBottom = false;
            movement.y += speed * 30;
        } else if (keycode == Input.Keys.A) {
            if (!TileGrid.isTileCollidableLeft())
                movement.x = -speed;
        } else if (keycode == Input.Keys.D) {
            if (!TileGrid.isTileCollidableRight())
                movement.x = +speed;
        } else if (keycode == Input.Keys.S) {
            if (TileGrid.hitBottom)
                setMovement(new Vector2(0, getMovement().y));
            TileGrid.hitTop = false;
            movement.y = -speed;
        } else if (keycode == Input.Keys.W) {
            if (!Game.isGravityOn) {
                movement.y = +speed;
            }
        }
        switch (keycode) {
        case Input.Keys.NUM_1:
            selectedSlot = 1;
            break;
        case Input.Keys.NUM_2:
            selectedSlot = 2;
            break;
        case Input.Keys.NUM_3:
            selectedSlot = 3;
            break;
        case Input.Keys.NUM_4:
            selectedSlot = 4;
            break;
        case Input.Keys.NUM_5:
            selectedSlot = 5;
            break;
        case Input.Keys.NUM_6:
            selectedSlot = 6;
            break;
        case Input.Keys.NUM_7:
            selectedSlot = 7;
            break;
        case Input.Keys.NUM_8:
            selectedSlot = 8;
            break;
        case Input.Keys.NUM_9:
            selectedSlot = 9;
            break;
        case Input.Keys.NUM_0:
            selectedSlot = 10;
            break;

        case Input.Keys.TAB:
            isInventoryOn = !isInventoryOn;
            break;

        case Input.Keys.GRAVE:
            Game.c.setDisabled(false);
            Gdx.input.setInputProcessor(Game.c.getInputProcessor());
            Game.isConsoleOn = true;
            break;
        }

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.SPACE) {
        } else if (keycode == Input.Keys.A) {
            movement.x = 0;
        } else if (keycode == Input.Keys.D) {
            movement.x = 0;
        } else if (keycode == Input.Keys.S) {
            movement.y = 0;
        } else if (keycode == Input.Keys.W) {
            if (!Game.isGravityOn) {
                movement.y = 0;
            }
        }
        return true;
    }


    private void addNewProjectile(int x, int y) {
        projectiles.add(new Projectile((int) (getX()) + 20, (int) (getY()) + 26, x, y));
    }

    private void switchBlock() {
        if (Game.backPack.size() >= selectedSlot) {
            if (Game.backPack.get(selectedSlot - 1).getItemType().equalsIgnoreCase("Tile")) {
                equipedWeapon = false;
                equipedTool = false;
                selectedBlock = Game.backPack.get(selectedSlot - 1).getTileType();
                playerHoldPath = selectedBlock.getPlayerHold();
            } else if (Game.backPack.get(selectedSlot - 1).getItemType().equalsIgnoreCase("Weapon")) {
                equipedWeapon = true;
                equipedTool = false;
                playerHoldPath = Game.backPack.get(selectedSlot - 1).getWeaponType().getTextureName();
            } else if (Game.backPack.get(selectedSlot - 1).getItemType().equalsIgnoreCase("Tool")) {
                equipedWeapon = false;
                equipedTool = true;
                playerHoldPath = Game.backPack.get(selectedSlot - 1).getToolType().getTextureName();
            }
        } else {
            selectedBlock = TileType.Air;
            equipedWeapon = false;
            equipedTool = false;
            playerHoldPath = TileType.Air.getPlayerHold();
        }
    }



    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (equipedWeapon) {
            if (button == Input.Buttons.LEFT) {
                addNewProjectile(screenX, screenY);
                return true;
            }
        } else if (!equipedWeapon) {
            if (selectDistance) {
                if (equipedTool) {
                    isBreakingBlocks = true;
                    isAddingBlocks = false;
                    return true;
                }
                if (getSelectedBlock() != null) {
                    if (Gdx.input.isButtonPressed(1)) {
                        isAddingBlocks = true;
                        isBreakingBlocks = false;
                    }

                }
            }
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            isBreakingBlocks = false;
            blockBreakTimer = 0;
        }

        if (button == Input.Buttons.RIGHT) {
            isAddingBlocks = false;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    int numberScrolled = 0;

    @Override
    public boolean scrolled(int amount) {
        if (amount > 0)
            selectedSlot--;

        if (amount < 0)
            selectedSlot++;

        if (selectedSlot <= 1) {
            selectedSlot = 1;
        }
        if (selectedSlot >= 10) {
            selectedSlot = 10;
        }

        return false;
    }

    public Boolean getEquipedWeapon() {
        return equipedWeapon;
    }

    public void setEquipedWeapon(Boolean equipedWeapon) {
        this.equipedWeapon = equipedWeapon;
    }

    public TileType getSelectedBlock() {
        return selectedBlock;
    }

    public void setSelectedBlock(TileType selectedBlock) {
        this.selectedBlock = selectedBlock;
    }

    public String getPlayerHold() {
        return playerHoldPath;
    }

    public void setPlayerHold(String playerHoldPath) {
        this.playerHoldPath = playerHoldPath;
    }

    public String getPlayerHead() {
        return playerHeadPath;
    }

    public void setPlayerHead(String playerHeadPath) {
        this.playerHeadPath = playerHeadPath;
    }


    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWalkSpeed() {
        return walkSpeed;
    }

    public void setWalkSpeed(float walkSpeed) {
        this.walkSpeed = walkSpeed;
    }

    public Sprite getBodyTexture() {
        return body;
    }

    public void setBodyTexture(Sprite texture) {
        this.body = texture;
    }

    public Sprite getArmTexture() {
        return arms;
    }

    public void setArmTexture(Sprite texture) {
        this.arms = texture;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public Vector2 getMovement() {
        return movement;
    }

    public void setMovement(Vector2 movement) {
        this.movement = movement;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }


    public Sprite getCursor() {
        return cursor;
    }

    public void setCursor(Sprite cursor) {
        this.cursor = cursor;
    }
}
