package no.kash.gamedev.jag.java;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import no.kash.gamedev.jag.DesktopActionResolver;
import no.kash.gamedev.jag.JustAnotherGame;
import no.kash.gamedev.jag.actionresolvers.ActionResolver;

public class JustAnotherGameDesktop {
	public static void main (String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 800;
		config.height = 600;
		ActionResolver resolver = new DesktopActionResolver();
		new LwjglApplication(new JustAnotherGame(resolver), config);
	}
}
