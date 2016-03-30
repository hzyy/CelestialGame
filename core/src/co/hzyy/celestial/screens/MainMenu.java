package co.hzyy.celestial.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import data.Fonts;
import data.TileGrid;

public class MainMenu implements Screen, InputProcessor {
    private final CelestialEngine game;
    private SpriteBatch hud;

    private OrthographicCamera camera;
    private String state;
    private Stage stage;
    private TextureAtlas buttonsAtlas;
    private Skin buttonSkin;
    private TextButton button;
    private Texture background;

    // Init method for the MainMenu screen
    public MainMenu(final CelestialEngine gam) {
        game = gam;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        hud = new SpriteBatch();
        background = new Texture("UI/bg/menu.png");

        Gdx.input.setInputProcessor(this);

        buttonsAtlas = new TextureAtlas("button.txt");
        buttonSkin = new Skin();
        buttonSkin.addRegions(buttonsAtlas);

        stage = new Stage();
        stage.clear();
        Gdx.input.setInputProcessor(stage);

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = buttonSkin.getDrawable("big");
        style.overFontColor = Color.NAVY;
        style.fontColor = Color.WHITE;
        style.font = game.font;

        button = new TextButton("play", style);
        button.setPosition((Gdx.graphics.getWidth()/2) -100, (Gdx.graphics.getHeight()/2)-200);
        button.setHeight(100);
        button.setWidth(200);
        button.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("Main Menu", "Play");
                game.setScreen(new SizeSelect(game));
                return true;  
            }
        });

        stage.addActor(button);

    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta){
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Camera stuff
        game.batch.setProjectionMatrix(camera.combined);
        hud.setProjectionMatrix(camera.projection);

        hud.begin();
        hud.draw(background,-camera.viewportWidth/2,-camera.viewportHeight/2,camera.viewportWidth,camera.viewportHeight);
        drawDebugInfo();
        //Fonts.mediumSizedFont.draw(hud,"coolest game ever\n                (Celestial)",-130,0);
        hud.end();

        stage.act();
        camera.update();

        game.batch.begin();
        stage.draw();
        game.batch.end();

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

