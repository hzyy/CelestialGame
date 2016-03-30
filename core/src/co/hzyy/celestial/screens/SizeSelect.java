package co.hzyy.celestial.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import data.Fonts;

public class SizeSelect implements Screen, InputProcessor {
    private final CelestialEngine game;
    private SpriteBatch hud;

    private OrthographicCamera camera;
    private String state;
    private Stage stage;
    private TextureAtlas buttonsAtlas;
    private Skin buttonSkin;
    private TextButton bigWorldButton, smallWorldButton, mediumWorldButton;

    private Sprite background;

    // Size of map
    public static int maxX = 1600;
    public static int maxY = 400;

    // Init method for the MainMenu screen
    public SizeSelect(final CelestialEngine gam) {
        game = gam;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, camera.viewportWidth, camera.viewportHeight);
        hud = new SpriteBatch();
        background = new Sprite(new Texture("UI/bg/sizeselect.png"));
        //background.scale(4);

        Gdx.input.setInputProcessor(this);

        buttonsAtlas = new TextureAtlas("button.txt");
        buttonSkin = new Skin();
        buttonSkin.addRegions(buttonsAtlas);

        stage = new Stage();
        stage.clear();
        Gdx.input.setInputProcessor(stage);

        TextButton.TextButtonStyle bigButtonStyle = new TextButton.TextButtonStyle();
        bigButtonStyle.up = buttonSkin.getDrawable("big");
        bigButtonStyle.overFontColor = Color.NAVY;
        bigButtonStyle.fontColor = Color.WHITE;
        bigButtonStyle.font = game.font;


        TextButton.TextButtonStyle mediumButtonStyle = new TextButton.TextButtonStyle();
        mediumButtonStyle.up = buttonSkin.getDrawable("medium");
        mediumButtonStyle.overFontColor = Color.NAVY;
        mediumButtonStyle.fontColor = Color.WHITE;
        mediumButtonStyle.font = game.font;

        TextButton.TextButtonStyle smallButtonStyle = new TextButton.TextButtonStyle();
        smallButtonStyle.up = buttonSkin.getDrawable("small");
        smallButtonStyle.overFontColor = Color.NAVY;
        smallButtonStyle.fontColor = Color.WHITE;
        smallButtonStyle.font = game.font;

        smallWorldButton = new TextButton("mini\n(800,200)", smallButtonStyle);
        smallWorldButton.setPosition((camera.viewportWidth + Gdx.graphics.getWidth() / 2 - 100) - 300, (camera.viewportHeight + Gdx.graphics.getHeight() / 2) - 200);
        smallWorldButton.setHeight(100);
        smallWorldButton.setWidth(200);
        smallWorldButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("Size Select", "Small");
                maxX = 800;
                maxY = 200;
                game.setScreen(new Game(game));
                return true;
            }
        });

        mediumWorldButton = new TextButton("standard\n(1600,400)", mediumButtonStyle);
        mediumWorldButton.setPosition((camera.viewportWidth + Gdx.graphics.getWidth() / 2 - 100), (camera.viewportHeight + Gdx.graphics.getHeight() / 2) - 200);
        mediumWorldButton.setHeight(100);
        mediumWorldButton.setWidth(200);
        mediumWorldButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("Size Select", "Medium");
                maxX = 1600;
                maxY = 400;
                game.setScreen(new Game(game));
                return true;
            }
        });

        bigWorldButton = new TextButton("vast\n(3200,600)", bigButtonStyle);
        bigWorldButton.setPosition((camera.viewportWidth + Gdx.graphics.getWidth() / 2 - 100) + 300, (camera.viewportHeight + Gdx.graphics.getHeight() / 2) - 200);
        bigWorldButton.setHeight(100);
        bigWorldButton.setWidth(200);
        bigWorldButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("Size Select", "Big");
                maxX = 3200;
                maxY = 600;
                game.setScreen(new Game(game));
                return true;
            }
        });

        stage.addActor(mediumWorldButton);
        stage.addActor(smallWorldButton);
        stage.addActor(bigWorldButton);

    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Camera stuff
        game.batch.setProjectionMatrix(camera.combined);
        hud.setProjectionMatrix(camera.projection);

        hud.begin();
        hud.draw(background, -camera.viewportWidth / 2, -camera.viewportHeight / 2, camera.viewportWidth, camera.viewportHeight);
        drawDebugInfo();
        game.font.draw(hud, "select your desired map size", -145, -10);
        hud.end();
        stage.act();
        camera.update();

        stage.draw();

        setFPSColor();


    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width / 1.25f;
        camera.viewportHeight = height / 1.25f;
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
        game.batch.dispose();
        buttonSkin.dispose();
        buttonsAtlas.dispose();
        stage.dispose();
        hud.dispose();
    }

    private void drawDebugInfo() {
        state = "" + game.getScreen();
        game.font.draw(hud, "FPS: " + Gdx.graphics.getFramesPerSecond(), 25 - camera.viewportWidth / 2, camera.viewportHeight / 2 - 20);
        game.font.draw(hud, "Delta Time: " + Gdx.graphics.getDeltaTime(), 25 - camera.viewportWidth / 2, camera.viewportHeight / 2 - 40);
        game.font.draw(hud, "Game State: " + state.substring(26, state.length() - 9), 25 - camera.viewportWidth / 2, camera.viewportHeight / 2 - 60);
        game.font.draw(hud, "Resolution: (" + camera.viewportWidth + ", " + camera.viewportHeight + ")", 25 - camera.viewportWidth / 2, camera.viewportHeight / 2 - 80);
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


    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.Z:
                Gdx.graphics.setDisplayMode(2560, 1440, false);
                break;
            case Input.Keys.X:
                Gdx.graphics.setDisplayMode(1920, 1080, true);
                break;
            case Input.Keys.C:
                Gdx.graphics.setDisplayMode(1600, 980, false);
                break;
            case Input.Keys.V:
                Gdx.graphics.setDisplayMode(1366, 768, false);
                break;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
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

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}

