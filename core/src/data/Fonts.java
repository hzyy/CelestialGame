package data;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Fonts {

    public static FreeTypeFontGenerator generator;
    public static FreeTypeFontGenerator.FreeTypeFontParameter buttonParameter;
    public static FreeTypeFontGenerator.FreeTypeFontParameter sceneParameter;
    public static FreeTypeFontGenerator.FreeTypeFontParameter regularParameter;
    public static BitmapFont buttonFont;
    public static BitmapFont scene;
    public static BitmapFont mediumSizedFont;


    public static void create() {
        //generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/main.ttf"));


        //buttonParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        //buttonParameter.size = 32;
        //buttonFont = generator.generateFont(buttonParameter);
        buttonFont = new BitmapFont();

        //sceneParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        //sceneParameter.size = 12;
        //scene = generator.generateFont(sceneParameter);
        scene = new BitmapFont();

        //regularParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        //regularParameter.size = 20;
        //mediumSizedFont = generator.generateFont(regularParameter); // font size 8 pixels
        mediumSizedFont = new BitmapFont();

        generator.dispose(); // don't forget to dispose to avoid memory leaks!
    }


}
