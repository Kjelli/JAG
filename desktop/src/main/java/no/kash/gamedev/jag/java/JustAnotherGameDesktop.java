package no.kash.gamedev.jag.java;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import no.kash.gamedev.jag.DesktopActionResolver;
import no.kash.gamedev.jag.actionresolvers.ActionResolver;
import no.kash.gamedev.jag.commons.defs.Defs;
import no.kash.gamedev.jag.controller.JustAnotherGameController;
import no.kash.gamedev.jag.game.JustAnotherGame;

public class JustAnotherGameDesktop {
	public static void main(String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = (int) Defs.WIDTH;
		config.height = (int) Defs.HEIGHT;
		ActionResolver resolver = new DesktopActionResolver();
		if (args.length == 0) {
			new LwjglApplication(new JustAnotherGame(resolver), config);
		} else {
			config.width = 640;
			config.height = 400;
			new LwjglApplication(new JustAnotherGameController(resolver), config);
		}
	}
}
