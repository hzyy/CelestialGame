package co.hzyy.celestial.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import console.Console;
import data.*;
import entities.Player;
import math.Constants;

public class Game implements Screen {

	// General
	private Constants constants;
	public static Player mainPlayer;
	public static TileGrid worldTiles;
	public static ItemCollection<Item> itemCollection;
	public static Inventory<Item> backPack;
	private final CelestialEngine game;
	public static OrthographicCamera camera;

	//Music
	//private Music music;

	// Game Sprites (not HUD)
	private Sprite sky, nightsky;

	// Math
	public static float gravity = 0;
	private double timeofday = 0.8;
	private float timeOfDayCycle;
	private Vector3 position;
	private float lerp = 0.08f;

	// Cheats and debug stuff
	private String state;
	public static boolean isConsoleOn = false;
	public static boolean isGravityOn = true;
	public static boolean isDebugOn = false;
	public static boolean isMobile = false;
	public static boolean isDebugTextOn = false;
	private ShapeRenderer sr;

	// parallax stuff
	private Sprite cloud1, cloud2, cloud3;
	private Sprite mountain1, mountain2;
	private Sprite fgTrees, bgTrees;
	private float cloudMovement = 0;
	private float mountainX1, mountainX2, mountainX3;

	// HUD
	private final SpriteBatch hud;
	private Sprite actionBar, actionBarDecoration, actionBarGlow, healthBar, magickaBar;
	private boolean tileAmountHasBeenChanged = false;
	public static Console c;


	// Init method for the MainMenu screen
	public Game(final CelestialEngine gam) {
		game = gam;

		//Initializing all the objects
		c = new Console();
		constants = new Constants();
		itemCollection = new ItemCollection<Item>();
		backPack = new Inventory<Item>();
		worldTiles = new TileGrid(game);
		hud = new SpriteBatch();
		camera = new OrthographicCamera();
		sr = new ShapeRenderer();

		// All the default textures being implemented
		cloud1 = new Sprite(new Texture("sky/cloud1.png"));
		cloud2 = new Sprite(new Texture("sky/cloud2.png"));
		cloud3 = new Sprite(new Texture("sky/cloud3.png"));
		mountain1 = new Sprite(new Texture("sky/mountain1.png"));
		mountain2 = new Sprite(new Texture("sky/mountain2.png"));
		fgTrees = new Sprite(new Texture("sky/forground trees.png"));
		bgTrees = new Sprite(new Texture("sky/mountain trees.png"));
		actionBar = new Sprite(new Texture("UI/uibar.png"));
		actionBarDecoration = new Sprite(new Texture("UI/outsideBar.png"));
		magickaBar = new Sprite(new Texture("UI/magicka.png"));
		actionBarGlow = new Sprite(new Texture("UI/glow.png"));
		healthBar = new Sprite(new Texture("UI/health.png"));
		sky = new Sprite(new Texture("sky/sky.png"));
		nightsky = new Sprite(new Texture("sky/night sky.png"));


		// Not orthographic camera
		camera.setToOrtho(false, camera.viewportWidth, camera.viewportHeight);

		// Sets the opening effect of the player falling
		mainPlayer = new Player((SizeSelect.maxX * TileGrid.tileSize) / 2, 2350, game);
		// Default hitbox
		// The setbounds +5 raises the hitbox since I drew the player down 5 pixels before
		camera.position.x = mainPlayer.getX();
		camera.position.y = mainPlayer.getY() + 25;


		// adding items to backpack by default
		for (Item item : Item.values()) {
			itemCollection.add(item);
		}
		backPack.addItemByID(12);
		backPack.addItemByID(16);
		backPack.addItemByID(5);
		backPack.addItemByID(4);
		backPack.addItemByID(4);
		backPack.addItemByID(8);
		backPack.addItemByID(13);
		backPack.addItemByID(15);
		backPack.addItemByID(2);
		backPack.addItemByID(13);
		backPack.addItemByID(15);
		backPack.addItemByID(5);
		backPack.addItemByID(2);
		backPack.addItemByID(13);
		backPack.addItemByID(15);
		backPack.addItemByID(2);
		backPack.addItemByID(13);
		backPack.addItemByID(4);
		backPack.addItemByID(1);
		backPack.addItemByID(5);
		backPack.addItemByID(4);
		backPack.addItemByID(4);
		backPack.addItemByID(2);
		backPack.addItemByID(13);
		backPack.addItemByID(15);

		System.out.println();


		// Sets the default input processor
		Gdx.input.setInputProcessor(mainPlayer);

	}

	@Override
	public void show() {
	}

	@Override
	public void render(float delta) {
		mainPlayer.setBounds(new Rectangle(mainPlayer.getX(), mainPlayer.getY(), (mainPlayer.getWidth() / 3), mainPlayer.getHeight()));
		// Default openGl code, called to clear the screen.
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// Unprojects the mouse position so the coordinates are global through the game.
		Player.mouse_position.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		camera.unproject(Player.mouse_position);


		// Gravity code
		if (isGravityOn) {
			if (!TileGrid.hitBottom) {
				gravity -= constants.getGravity() * delta;
				mainPlayer.setMovement(new Vector2(mainPlayer.getMovement().x, mainPlayer.getMovement().y + gravity));
			} else {
				mainPlayer.setMovement(new Vector2(mainPlayer.getMovement().x, mainPlayer.getMovement().y));
			}
			//clamp gravity
			if (mainPlayer.getMovement().y > mainPlayer.getSpeed()) {
				mainPlayer.setMovement(new Vector2(mainPlayer.getMovement().x, mainPlayer.getSpeed()));
			} else if (mainPlayer.getMovement().y < -mainPlayer.getSpeed()) {
				mainPlayer.setMovement(new Vector2(mainPlayer.getMovement().x, mainPlayer.getMovement().y));
			}
		}

		// Camera magic
		game.batch.setProjectionMatrix(camera.combined);
		hud.setProjectionMatrix(camera.projection);
		position = camera.position;
		position.x += (mainPlayer.getX() - position.x) * lerp;
		position.y += (mainPlayer.getY() - position.y + 100) * lerp;
		clamp();
		camera.position.set(position.x, position.y, 0);
		camera.update();

		// Made for debug mode only
		if (!isDebugOn) {
			timeofday += 0.05f * delta;
			timeOfDayCycle = (float) Math.sin(timeofday);
			if (timeOfDayCycle < 0) {
				timeOfDayCycle *= -1;
			}
			hud.begin();
			hud.draw(sky, 0 - camera.viewportWidth / 2, 0 - camera.viewportHeight / 2, camera.viewportWidth, camera.viewportHeight);
			nightsky.draw(hud, timeOfDayCycle);
			nightsky.setBounds(0 - camera.viewportWidth / 2, 0 - camera.viewportHeight / 2, camera.viewportWidth, camera.viewportHeight);
			cloudMovement += 0.25f;
			if (cloudMovement > camera.viewportWidth + 1000) {
				cloudMovement = -1450;
			}

			hud.draw(cloud2, 2000 * lerp - cloudMovement, 30);
			hud.draw(cloud3, -4628 * lerp - cloudMovement, 120);
			hud.end();
			drawBackground();
		}

		// Renders the main game
		game.batch.begin();
		worldTiles.draw();
		if (!isDebugOn)
			mainPlayer.draw();
		game.batch.end();

		// Debug renderer
		if (isDebugOn){
			setFPSColor();
			sr.setProjectionMatrix(camera.combined);
			sr.begin(ShapeRenderer.ShapeType.Line);
			sr.setColor(1, 0, 1, 1);
			sr.rect(mainPlayer.getX(), mainPlayer.getY(), mainPlayer.getWidth(), mainPlayer.getHeight());
			sr.end();
		}

		// HUD renderer
		hud.begin();
		if (isDebugOn || isDebugTextOn)
			drawDebugInfo();
		//hud.draw(actionBar,-camera.viewportWidth/2+10,camera.viewportHeight/2- actionBar.getHeight()-8);
		hud.draw(actionBar, -actionBar.getWidth() / 2, camera.viewportHeight / 2 - actionBar.getHeight() - 8);
		hud.draw(actionBarDecoration, -actionBarDecoration.getWidth() / 2, camera.viewportHeight / 2 - actionBar.getHeight() - 8);
		hud.draw(healthBar, -actionBar.getWidth() / 2, camera.viewportHeight / 2 - (actionBar.getHeight() * 1.75f) - 4);
		hud.draw(magickaBar, actionBar.getWidth() / 2 - magickaBar.getWidth(), camera.viewportHeight / 2 - (actionBar.getHeight() * 1.75f) - 4);
		drawActionBarTiles();
		if (Player.isInventoryOn) {
			drawInventoryHUD();
			drawInventoryItems();
		}
		//hud.draw(new TextureRegion(new TextureAtlas(Gdx.files.internal("tiles/tiles.atlas")).findRegion("dirt")),2,2);
		hud.end();

		// Console renderer
		if (isConsoleOn) {
			c.draw();
			c.refresh();
		}

	}

	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = width / 1.35f;
		camera.viewportHeight = height / 1.35f;
	}

	// Draws the inventory base HUD
	private void drawInventoryHUD() {
		for (int i = 0; i <= 4; i++) {
			hud.draw(actionBar, -actionBar.getWidth() / 2, camera.viewportHeight / 2 - (120 + i) - actionBar.getHeight() * i);
		}
	}

	// Draws the background behind the player
	private void drawBackground() {
		game.batch.begin();
		//mountains
		mountainX1 = (mainPlayer.getX() * 0.4f) + 7000;
		mountainX1 = (mainPlayer.getX() * 0.4f) + 7000 + (mainPlayer.getX() * 0.4f) + 7000;
		//game.batch.draw(mountain1, (camera.position.x - Gdx.graphics.getWidth() / 2), -250, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() * 3);
		game.batch.draw(mountain2, (camera.position.x - Gdx.graphics.getWidth() / 2), -250, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() * 3);
		game.batch.draw(bgTrees, (mainPlayer.getX()) - Gdx.graphics.getWidth() / 2, 1350, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		game.batch.draw(fgTrees, (mainPlayer.getX()) - Gdx.graphics.getWidth() / 2, 1250, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		game.batch.end();
		hud.begin();
		//clouds
		hud.draw(cloud1, 8000 * lerp - cloudMovement, 50);
		hud.draw(cloud3, 437 * lerp - cloudMovement, 80);
		hud.draw(cloud1, (45) * lerp - cloudMovement, 167);
		hud.draw(cloud3, -5000 * lerp - cloudMovement, 120);
		hud.draw(cloud3, -8000 * lerp - cloudMovement, 80);
		hud.draw(cloud1, -10000 * lerp - cloudMovement, 180);
		hud.end();
	}

	// Loop to draw the players items
	private void drawInventoryItems() {
		for (int i = 9; i <= backPack.size() - 1; i++) {
			for (int j = 0; j <= 0; j++) {
				if (!(i > 9 + 9)) {
					//-actionBar.getWidth()/2
					if (backPack.get(i).getItemType().equalsIgnoreCase("Tile")) {
						hud.draw(backPack.get(i).getTileType().getTexture(), (-actionBar.getWidth() / 2 + 14) + ((i - 9) * 45) - i, camera.viewportHeight / 2 - (117 + j) - actionBar.getHeight() * j, TileGrid.tileSize * 2, TileGrid.tileSize * 2);

					} else if (backPack.get(i).getItemType().equalsIgnoreCase("Weapon")) {
						hud.draw(backPack.get(i).getWeaponType().getTileName(), (-actionBar.getWidth() / 2 + 14) + ((i - 9) * 45) - i, camera.viewportHeight / 2 - (117 + j) - actionBar.getHeight() * j, TileGrid.tileSize * 2, TileGrid.tileSize * 2);

					}
					//if (Gdx.input.getX() > ((-camera.viewportWidth / 2 + 25) + ((i - 9) * 45) - i) && G)

				}
			}
		}

		for (int i = 18; i <= backPack.size() - 1; i++) {
			for (int j = 1; j <= 1; j++) {
				if (!(i > 18 + 9)) {
					if (backPack.get(i).getItemType().equalsIgnoreCase("Tile")) {
						hud.draw(backPack.get(i).getTileType().getTexture(), (-actionBar.getWidth() / 2 + 5) + ((i - 18) * 45) - (i - 18), camera.viewportHeight / 2 - (117 + j) - actionBar.getHeight() * j, TileGrid.tileSize * 2, TileGrid.tileSize * 2);

					} else if (backPack.get(i).getItemType().equalsIgnoreCase("Weapon")) {
						hud.draw(backPack.get(i).getWeaponType().getTileName(), (-actionBar.getWidth() / 2 + 5) + ((i - 18) * 45) - (i - 18), camera.viewportHeight / 2 - (117 + j) - actionBar.getHeight() * j, TileGrid.tileSize * 2, TileGrid.tileSize * 2);

					} else {

					}
				}
			}
		}

		for (int i = 27; i <= backPack.size() - 1; i++) {
			for (int j = 2; j <= 2; j++) {
				if (!(i > 27 + 9)) {
					if (backPack.get(i).getItemType().equalsIgnoreCase("Tile")) {
						hud.draw(backPack.get(i).getTileType().getTexture(), (-actionBar.getWidth() / 2 + 5) + ((i - 27) * 45) - (i - 27), camera.viewportHeight / 2 - (117 + j) - actionBar.getHeight() * j, TileGrid.tileSize * 2, TileGrid.tileSize * 2);

					} else if (backPack.get(i).getItemType().equalsIgnoreCase("Weapon")) {
						hud.draw(backPack.get(i).getWeaponType().getTileName(), (-actionBar.getWidth() / 2 + 5) + ((i - 27) * 45) - (i - 27), camera.viewportHeight / 2 - (117 + j) - actionBar.getHeight() * j, TileGrid.tileSize * 2, TileGrid.tileSize * 2);

					} else {

					}
				}
			}
		}

		for (int i = 36; i <= backPack.size() - 1; i++) {
			for (int j = 3; j <= 3; j++) {
				if (!(i > 36 + 9)) {
					if (backPack.get(i).getItemType().equalsIgnoreCase("Tile")) {
						hud.draw(backPack.get(i).getTileType().getTexture(), (-actionBar.getWidth() / 2 + 5) + ((i - 36) * 45) - (i - 36), camera.viewportHeight / 2 - (117 + j) - actionBar.getHeight() * j, TileGrid.tileSize * 2, TileGrid.tileSize * 2);

					} else if (backPack.get(i).getItemType().equalsIgnoreCase("Weapon")) {
						hud.draw(backPack.get(i).getWeaponType().getTileName(), (-actionBar.getWidth() / 2 + 5) + ((i - 36) * 45) - (i - 36), camera.viewportHeight / 2 - (117 + j) - actionBar.getHeight() * j, TileGrid.tileSize * 2, TileGrid.tileSize * 2);

					} else {

					}
				}
			}
		}

		for (int i = 45; i <= backPack.size() - 1; i++) {
			for (int j = 4; j <= 4; j++) {
				if (!(i > 45 + 9)) {
					if (backPack.get(i).getItemType().equalsIgnoreCase("Tile")) {
						hud.draw(backPack.get(i).getTileType().getTexture(), (-actionBar.getWidth() / 2 + 5) + ((i - 45) * 45) - (i - 45), camera.viewportHeight / 2 - (117 + j) - actionBar.getHeight() * j, TileGrid.tileSize * 2, TileGrid.tileSize * 2);

					} else if (backPack.get(i).getItemType().equalsIgnoreCase("Weapon")) {
						hud.draw(backPack.get(i).getWeaponType().getTileName(), (-actionBar.getWidth() / 2 + 5) + ((i - 45) * 45) - (i - 45), camera.viewportHeight / 2 - (117 + j) - actionBar.getHeight() * j, TileGrid.tileSize * 2, TileGrid.tileSize * 2);

					}
				}
			}
		}


	}


	private void drawActionBarTiles() {
		for (int i = 0; i <= backPack.size() - 1; i++) {
			if (!(i > 9)) {
				i++;
				if (Player.selectedSlot == i) {
					hud.draw(actionBarGlow, (-actionBar.getWidth() / 2 - 10) + (11 * ((4 * i) - 3)), camera.viewportHeight / 2 - actionBar.getHeight() - 8);
				}
				i--;
				if (backPack.get(i).getItemType().equalsIgnoreCase("Tile")) {
					hud.draw(backPack.get(i).getTileType().getTexture(), (-actionBar.getWidth() / 2 + 5) + (i * 45) - i, camera.viewportHeight / 2 - actionBar.getHeight() - 5, TileGrid.tileSize * 2, TileGrid.tileSize * 2);
				} else if (backPack.get(i).getItemType().equalsIgnoreCase("Weapon")) {
					hud.draw(backPack.get(i).getWeaponType().getTileName(), (-actionBar.getWidth() / 2 + 5) + (i * (45)) - i, camera.viewportHeight / 2 - actionBar.getHeight() - 5, TileGrid.tileSize * 2, TileGrid.tileSize * 2);

				} else if (backPack.get(i).getItemType().equalsIgnoreCase("Tool")) {
					hud.draw(backPack.get(i).getToolType().getTileName(), (-actionBar.getWidth() / 2 + 5) + (i * (45)) - i, camera.viewportHeight / 2 - actionBar.getHeight() - 5, TileGrid.tileSize * 2, TileGrid.tileSize * 2);

				}
			}
		}
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
		hud.dispose();
		c.dispose();
	}

	// Contains all the debug data
	private void drawDebugInfo() {
		state = "" + game.getScreen();
		game.font.draw(hud, "FPS: " + Gdx.graphics.getFramesPerSecond(), 25 - camera.viewportWidth / 2, camera.viewportHeight / 2 - 20);
		game.font.draw(hud, "Delta Time: " + Gdx.graphics.getDeltaTime(), 25 - camera.viewportWidth / 2, camera.viewportHeight / 2 - 40);
		game.font.draw(hud, "Game State: " + state.substring(26, state.length() - 9), 25 - camera.viewportWidth / 2, camera.viewportHeight / 2 - 60);
		game.font.draw(hud, "Resolution: (" + camera.viewportWidth + ", " + camera.viewportHeight + ")", 25 - camera.viewportWidth / 2, camera.viewportHeight / 2 - 80);
		game.font.draw(hud, "World Size: (" + SizeSelect.maxX + ", " + SizeSelect.maxY + ")", 25 - camera.viewportWidth / 2, camera.viewportHeight / 2 - 100);
		game.font.draw(hud, "Mouse Coords = (" + Player.mouse_position.x / TileGrid.tileSize + ", " + Player.mouse_position.y / TileGrid.tileSize + ")", 25 - camera.viewportWidth / 2, camera.viewportHeight / 2 - 120);
		game.font.draw(hud, "Player Coords on tile: (" + mainPlayer.getX() / TileGrid.tileSize + ", "
				+ mainPlayer.getY() / TileGrid.tileSize + ")", 25 - camera.viewportWidth / 2, camera.viewportHeight / 2 - 140);
		game.font.draw(hud, "Player Coords: (" + mainPlayer.getX() + ", " + mainPlayer.getY() + ")", 25 - camera.viewportWidth / 2, camera.viewportHeight / 2 - 160);
		game.font.draw(hud, "Selected Block: " + mainPlayer.getSelectedBlock(), 25 - camera.viewportWidth / 2, camera.viewportHeight / 2 - 180);
		game.font.draw(hud, "Is Weapon On: " + mainPlayer.getEquipedWeapon(), 25 - camera.viewportWidth / 2, camera.viewportHeight / 2 - 200);
	}

	// Sets the Debug info color depending on your frame rate
	private void setFPSColor() {
		if (Gdx.graphics.getFramesPerSecond() > 45) {
			game.font.setColor(Color.GREEN);
		} else if (Gdx.graphics.getFramesPerSecond() > 20) {
			game.font.setColor(Color.YELLOW);
		} else {
			game.font.setColor(Color.RED);
		}
	}

	// clamps the player to not walk off the screen
	private void clamp() {
		if (mainPlayer.getX() / TileGrid.tileSize <= TileGrid.halfScreenTileX) {
			mainPlayer.setX(mainPlayer.getX() + mainPlayer.getWalkSpeed());
		}
		if (mainPlayer.getY() / TileGrid.tileSize <= TileGrid.halfScreenTileY) {
			mainPlayer.setY(mainPlayer.getY() + mainPlayer.getWalkSpeed());

		}
		if (mainPlayer.getX() / TileGrid.tileSize >= (TileGrid.worldSizeInPixelsX/16 - TileGrid.halfScreenTileX)) {
			mainPlayer.setX(mainPlayer.getX() - mainPlayer.getWalkSpeed());
		}
		if (mainPlayer.getY() / TileGrid.tileSize >= (TileGrid.worldSizeInPixelsY/16 - TileGrid.halfScreenTileY)) {
			mainPlayer.setY(mainPlayer.getY() - mainPlayer.getWalkSpeed());

		}
	}

	// Finds the distance between two vectors
	public static float findDistanceBetweenVectors(float mouseX, float mouseY, float playerX, float playerY) {
		return (float) Math.sqrt((Math.pow((mouseX - playerX), 2)) + (Math.pow((mouseY - playerY), 2)));
	}


	// Adjusts the amount of tiles that are rendered with respect to screen size.
	public static void changeScreenTileAmnt() {
		TileGrid.halfScreenTileX = (((int) camera.viewportWidth / TileGrid.tileSize) / 2) + 2;
		TileGrid.halfScreenTileY = (((int) camera.viewportHeight / TileGrid.tileSize) / 2) + 2;
	}

}

