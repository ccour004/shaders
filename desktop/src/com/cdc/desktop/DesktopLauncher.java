package com.cdc.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.cdc.MyGdxGame;

import java.io.File;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new MyGdxGame(), config);
        File file = Gdx.files.internal("test.yml").parent().file();
        new FileWatcher(Gdx.files.internal("test.yml").file(),file).start();
	}
}
