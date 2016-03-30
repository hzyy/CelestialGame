package co.hzyy.celestial.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import data.TileGrid;

public class SplashScreen implements Screen {
    private final CelestialEngine game;
    private SpriteBatch hud;
    private BitmapFont font12;
    private BitmapFont scene;

    private OrthographicCamera camera;
    private String state;
    private int timer;

    // Init method for the MainMenu screen
    public SplashScreen(final CelestialEngine gam) {
        game = gam;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        hud = new SpriteBatch();
        timer = 150;


        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/main.ttf"));

        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 32;
        font12 = generator.generateFont(parameter); // font size 32 pixels
        font12.setColor(0f, 0f, 0f, 1);

        FreeTypeFontGenerator.FreeTypeFontParameter parameter3 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter3.size = 12;
        scene = generator.generateFont(parameter3); // font size 8 pixels
        generator.dispose(); // don't forget to dispose to avoid memory leaks!


    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta){
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        timer -= 0.01f;

        System.out.println(timer);

        // Camera stuff
        hud.setProjectionMatrix(camera.projection);

        camera.update();


        setFPSColor();

        hud.begin();
        drawDebugInfo();
        font12.draw(hud, "hzyy games",200,200);
        hud.end();

        if (timer ==0){
            game.setScreen(new MainMenu(game));
        }

    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
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
    }

    private void drawDebugInfo() {
        state = "" + game.getScreen();
        scene.draw(hud, "FPS: " + Gdx.graphics.getFramesPerSecond(), 25 - Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2 - 20);
        scene.draw(hud, "Delta Time: " + Gdx.graphics.getDeltaTime(), 25 - Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2 - 40);
        scene.draw(hud, "Game State: " + state.substring(26, state.length() - 9), 25 - Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2 - 60);
        scene.draw(hud, "Resolution: (" + Gdx.graphics.getWidth() + ", " + Gdx.graphics.getHeight() + ")", 25 - Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2 - 80);
        scene.draw(hud, "Mouse Coords = (" + Gdx.input.getX() / TileGrid.tileSize + ", " + Gdx.input.getY() / TileGrid.tileSize + ")", 25 - Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2 - 100);

    }

    // Sets the Debug info color depending on your frame rate
    private void setFPSColor() {
        if (Gdx.graphics.getFramesPerSecond() > 45) {
            scene.setColor(Color.GREEN);
        } else if (Gdx.graphics.getFramesPerSecond() > 20) {
            scene.setColor(Color.YELLOW);
        } else {
            scene.setColor(Color.RED);
        }
    }

}

