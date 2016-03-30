package co.hzyy.celestial.desktop;

import co.hzyy.celestial.screens.CelestialEngine;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new CelestialEngine(), config);
		//1024, 720
		//config.width = 1024;
		//config.height = 720;
		config.title = "Celestial";
		config.width = 1024;
		config.height = 720;
		config.backgroundFPS = 30;
		config.foregroundFPS = 60;
		config.fullscreen = false;
		config.resizable = true;
		config.useHDPI = false;
	}
}
