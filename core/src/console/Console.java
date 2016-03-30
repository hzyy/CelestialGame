

package console;

import co.hzyy.celestial.screens.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import data.TileType;

public class Console implements Disposable {

    public enum LogLevel {
        // The default log level. Prints in white to the console and has no special indicator in the log file.
        DEFAULT(new Color(1, 1, 1, 1), ""),
        // Use to print errors. Prints in red to the console and has the '<i>ERROR</i>' marking in the log file
        ERROR(new Color(1, 20f / 255f, 0, 1), "Error: "),
        // Prints in green. Use to print success notifications of events. Intentional Use: Print successful execution of console
        SUCCESS(new Color(0, 217f / 255f,20f / 255f, 1), "Success! "),
        // Prints in white with {@literal "> "} prepended to the command. Has that prepended text as the indicator in the log file.
        COMMAND(new Color(1, 1, 1, 1), "> ");

        private Color color;
        private String identifier;

        LogLevel(Color c, String identity) {
            this.color = c;
            identifier = identity;
        }

        Color getColor() {
            return color;
        }

        String getIdentifier() {
            return identifier;
        }
    }

    // Use to set the amount of entries to be stored to unlimited.
    public static final int UNLIMITED_ENTRIES = -1;

    private int keyID = Input.Keys.GRAVE;
    private boolean disabled;
    private Log log;
    private ConsoleDisplay display;
    private boolean hidden = true;
    private boolean usesMultiplexer = false;
    private InputProcessor appInput;
    private InputMultiplexer multiplexer;
    private Stage stage;
    private Window consoleWindow;
    private Boolean logToSystem;

    public Console() {
        this(new Skin(Gdx.files.internal("default_skin/uiskin.json")));
    }

    public Console(Skin skin) {
        this(skin, true);
    }

    public Console(boolean useMultiplexer) {
        this(new Skin(Gdx.files.internal("default_skin/uiskin.json")), useMultiplexer);
    }

    public Console(Skin skin, boolean useMultiplexer) {
        stage = new Stage();
        log = new Log();
        display = new ConsoleDisplay(skin);
        logToSystem = false;

        usesMultiplexer = useMultiplexer;
        if (useMultiplexer) {
            resetInputProcessing();
        }

        display.pad(4);
        display.padTop(22);
        display.setFillParent(true);

        consoleWindow = new Window("Debug Console", skin);
        consoleWindow.setMovable(true);
        consoleWindow.setResizable(true);
        consoleWindow.setKeepWithinStage(true);
        consoleWindow.addActor(display);
        consoleWindow.setTouchable(Touchable.disabled);

        stage.addActor(consoleWindow);
        stage.setKeyboardFocus(display);

        setSizePercent(100, 50);
        setPositionPercent(0, 50);
    }

    public void setMaxEntries(int numEntries) {
        if (numEntries > 0 || numEntries == UNLIMITED_ENTRIES)
            log.setMaxEntries(numEntries);
        else
            throw new IllegalArgumentException("Maximum entries must be greater than 0 or use Console.UNLIMITED_ENTRIES.");
    }

    public void clear() {
        log.getLogEntries().clear();
        display.refresh();
    }

    public void setSize(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Pixel size must be greater than 0.");
        }
        consoleWindow.setSize(width, height);
    }

    public void setLoggingToSystem(Boolean log) {
        this.logToSystem = log;
    }

    public void setSizePercent(float wPct, float hPct) {
        if (wPct <= 0 || hPct <= 0) {
            throw new IllegalArgumentException("Size percentage must be greater than 0.");
        }
        if (wPct > 100 || hPct > 100) {
            throw new IllegalArgumentException("Size percentage cannot be greater than 100.");
        }
        float w = Gdx.graphics.getWidth(), h = Gdx.graphics.getHeight();
        consoleWindow.setSize(w * wPct / 100.0f, h * hPct / 100.0f);
    }

    public void setPosition(int x, int y) {
        consoleWindow.setPosition(x, y);
    }

    public void setPositionPercent(float xPosPct, float yPosPct) {
        if (xPosPct > 100 || yPosPct > 100) {
            throw new IllegalArgumentException("Error: The console would be drawn outside of the screen.");
        }
        float w = Gdx.graphics.getWidth(), h = Gdx.graphics.getHeight();
        consoleWindow.setPosition(w * xPosPct / 100.0f, h * yPosPct / 100.0f);
    }

    public void resetInputProcessing() {
        usesMultiplexer = true;
        appInput = Gdx.input.getInputProcessor();
        if (appInput != null) {
            if (hasStage(appInput)) {
                log("Console already added to input processor!", LogLevel.ERROR);
                Gdx.app.log("Console", "Already added to input processor!");
                return;
            }
            multiplexer = new InputMultiplexer();
            multiplexer.addProcessor(stage);
            multiplexer.addProcessor(appInput);
            Gdx.input.setInputProcessor(multiplexer);
        } else
            Gdx.input.setInputProcessor(stage);
    }

    private boolean hasStage(InputProcessor processor) {
        if (!(processor instanceof InputMultiplexer)) {
            return processor == stage;
        }
        InputMultiplexer im = (InputMultiplexer) processor;
        Array<InputProcessor> ips = im.getProcessors();
        for (InputProcessor ip : ips) {
            if (hasStage(ip)) {
                return true;
            }
        }
        return false;
    }

    public InputProcessor getInputProcessor() {
        return stage;
    }

    public void draw() {
        hidden = false;
        stage.act();
        if (!disabled)
            stage.draw();
        if (disabled)
            clear();
    }


    public void refresh() {
        this.refresh(true);
    }

    public void refresh(boolean retain) {
        float oldWPct = 0, oldHPct = 0, oldXPosPct = 0, oldYPosPct = 0;
        if (retain) {
            oldWPct = consoleWindow.getWidth() / stage.getWidth() * 100;
            oldHPct = consoleWindow.getHeight() / stage.getHeight() * 100;
            oldXPosPct = consoleWindow.getX() / stage.getWidth() * 100;
            oldYPosPct = consoleWindow.getY() / stage.getHeight() * 100;
        }
        int width = Gdx.graphics.getWidth(), height = Gdx.graphics.getHeight();
        stage.getViewport().setWorldSize(width, height);
        stage.getViewport().update(width, height, true);
        if (retain) {
            this.setSizePercent(oldWPct, oldHPct);
            this.setPositionPercent(oldXPosPct, oldYPosPct);
        }
    }

    public void log(String msg, LogLevel level) {
        log.addEntry(msg, level);
        display.refresh();

        if (logToSystem) {
            System.out.println(msg);
        }
    }

    public void log(String msg) {
        this.log(msg, LogLevel.DEFAULT);
    }

    // Prints all log entries to the given file. Log entries include logs in the code and commands made from within in the console
    public void printLogToFile(String file) {
        this.printLogToFile(Gdx.files.local(file));
    }

    // Prints all log entries to the given file. Log entries include logs in the code and commands made from within in the console
    public void printLogToFile(FileHandle fh) {
        if (log.printToFile(fh))
            log("Successfully wrote logs to file.", LogLevel.SUCCESS);
        else
            log("Unable to write logs to file.", LogLevel.ERROR);
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        if (disabled && !hidden) ((KeyListener) display.getListeners().get(0)).keyDown(null, keyID);
        this.disabled = disabled;
    }

    public void setKeyID(int code) {
        if (code == Keys.ENTER) return;
        keyID = code;
    }

    private class ConsoleDisplay extends Table {
        private Table logEntries;
        private TextField input;
        private Skin skin;
        private Array<Label> labels;

        protected ConsoleDisplay(Skin skin) {
            super(skin);

            this.setFillParent(false);
            this.skin = skin;

            labels = new Array<Label>();

            logEntries = new Table(skin);

            input = new TextField("", skin);

            scroll = new ScrollPane(logEntries, skin);
            scroll.setFadeScrollBars(false);
            scroll.setScrollbarsOnTop(false);
            scroll.setOverscroll(false, false);

            this.add(scroll).expand().fill().pad(4).row();
            this.add(input).expandX().fillX().pad(4);
            this.addListener(new KeyListener(input));
        }

        protected void refresh() {
            Array<LogEntry> entries = log.getLogEntries();
            logEntries.clear();

            // expand first so labels start at the bottom
            logEntries.add().expand().fill().row();
            int size = entries.size;
            for (int i = 0; i < size; i++) {
                LogEntry le = entries.get(i);
                Label l;
                // recycle the labels so we don't create new ones every refresh
                if (labels.size > i) {
                    l = labels.get(i);
                } else {
                    l = new Label("", skin, "default-font", LogLevel.DEFAULT.getColor());
                    l.setWrap(true);
                    labels.add(l);
                }
                l.setText(le.toConsoleString());
                l.setColor(le.getColor());
                logEntries.add(l).expandX().fillX().top().left().padLeft(4).row();
            }
            scroll.validate();
            scroll.setScrollPercentY(1);
        }
    }

    private ScrollPane scroll;

    private class KeyListener extends InputListener {
        private TextField input;

        protected KeyListener(TextField tf) {
            input = tf;
        }

        int upCommand = log.getLogEntries().size;

        @Override
        public boolean keyDown(InputEvent event, int keycode) {
            String s = input.getText();
            if (keycode == Keys.GRAVE) {
                disabled = true;
                hidden = true;
                Game.isConsoleOn = false;
                Gdx.input.setInputProcessor(Game.mainPlayer);
            }
            if (!hidden) {
                if (Keys.UP == keycode) {
                    if (log.getLogEntries().size > 0 && upCommand >= 0) {
                        s = log.getLogEntries().get(upCommand).toConsoleString();
                        upCommand--;
                        input.setText(s);
                    } else {
                        upCommand++;
                    }
                } else {
                    setKeyID(keycode);
                    s += Keys.toString(keyID).toLowerCase();
                    input.setText(s);
                    upCommand = log.getLogEntries().size;
                }
                if (Keys.ENTER == keycode) {
                    if (s.substring(0, s.length() - 1).equalsIgnoreCase("debug")) {
                        Game.isDebugOn = !Game.isDebugOn;
                        log(s.substring(0, s.length() - 1), LogLevel.SUCCESS);
                    } else if (s.substring(0, s.length() - 1).equalsIgnoreCase("debugtext")) {
                        Game.isDebugTextOn = !Game.isDebugTextOn;
                        log(s.substring(0, s.length() - 1), LogLevel.SUCCESS);
                    } else if (s.substring(0, s.length() - 1).equalsIgnoreCase("gravity")) {
                        Game.mainPlayer.setY(Game.mainPlayer.getY() + 30);
                        Game.mainPlayer.setMovement(new Vector2(Game.mainPlayer.getMovement().x, 0));
                        Game.isGravityOn = !Game.isGravityOn;
                        log(s.substring(0, s.length() - 1), LogLevel.SUCCESS);
                    } else if (s.substring(0, s.length() - 1).equalsIgnoreCase("god")) {
                        Game.mainPlayer.setSpeed(200);
                        log(s.substring(0, s.length() - 1), LogLevel.SUCCESS);
                    } else if (s.substring(0, s.length() - 1).equalsIgnoreCase("snail")) {
                        Game.mainPlayer.setSpeed(20);
                        log(s.substring(0, s.length() - 1), LogLevel.SUCCESS);
                    } else if (s.substring(0, s.length() - 1).equalsIgnoreCase("cls")) {
                        clear();
                    } else if (s.substring(0, s.length() - 1).equalsIgnoreCase("tinyscreen")) {
                        Gdx.graphics.setDisplayMode(683, 384, false);
                        Game.changeScreenTileAmnt();
                        Game.isMobile = true;
                    } else if (s.substring(0, s.length() - 1).equalsIgnoreCase("smallscreen")) {
                        Gdx.graphics.setDisplayMode(1366, 768, false);
                        Game.changeScreenTileAmnt();
                        Game.isMobile = true;
                    } else if (s.substring(0, s.length() - 1).equalsIgnoreCase("mediumscreen")) {
                        Gdx.graphics.setDisplayMode(1600, 980, false);
                        Game.changeScreenTileAmnt();
                        Game.isMobile = true;
                    } else if (s.substring(0, s.length() - 1).equalsIgnoreCase("bigscreen")) {
                        Gdx.graphics.setDisplayMode(1920, 1080, true);
                        Game.changeScreenTileAmnt();
                        Game.isMobile = true;
                    } else if (s.substring(0, s.length() - 1).equalsIgnoreCase("fullscreen")) {
                        Gdx.graphics.setDisplayMode(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(), true);
                        Game.changeScreenTileAmnt();
                        Game.isMobile = true;
                    } else if (s.substring(0, s.length() - 1).equalsIgnoreCase("help")) {
                        log("debug\ndebugtext\ngravity\ngod\nsnail\ncls\ntinyscreen\nsmallscreen\nmediumscreen\nbigscreen\nfullscreen", LogLevel.SUCCESS);
                    }else{
                        log("The command you've entered does not exist!", LogLevel.ERROR);
                    }
                    input.setText("");
                }


            }
            return false;
        }
    }

    @Override
    public void dispose() {
        if (usesMultiplexer && appInput != null) {
            Gdx.input.setInputProcessor(appInput);
        }
        stage.dispose();
    }

    public boolean isHidden() {
        return hidden;
    }

}
