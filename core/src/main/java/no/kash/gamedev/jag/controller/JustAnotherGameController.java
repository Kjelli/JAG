package no.kash.gamedev.jag.controller;

import no.kash.gamedev.jag.JagEndpoint;
import no.kash.gamedev.jag.actionresolvers.ActionResolver;
import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.commons.defs.Prefs;
import no.kash.gamedev.jag.commons.network.JagClient;
import no.kash.gamedev.jag.commons.tweens.TweenGlobal;
import no.kash.gamedev.jag.controller.screens.ConfigureScreen;
import no.kash.gamedev.jag.controller.screens.ControllerScreen;

public class JustAnotherGameController extends JagEndpoint {
	public static final int PLAY_STATE = 1, LOBBY_STATE = 2;

	ActionResolver resolver;
	JagClient client;

	public JustAnotherGameController(ActionResolver resolver) {
		super(resolver);
	}

	@Override
	public void create() {
		init();
		setScreen(new ConfigureScreen(this));
	}

	private void init() {
		TweenGlobal.init();
		Assets.load();
		client = new JagClient();
	}

	public JagClient getClient() {
		return client;
	}

}
