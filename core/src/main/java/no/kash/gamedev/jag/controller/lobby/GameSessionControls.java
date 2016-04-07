package no.kash.gamedev.jag.controller.lobby;

import no.kash.gamedev.jag.controller.preferences.GameSessionPreferences;
import no.kash.gamedev.jag.game.gamesession.GameSession;

public class GameSessionControls {
	public GameSession session;

	public GameSessionControls() {
		GameSessionPreferences.load();

		session = new GameSession();
		session.settings.fromPreferences();
	}

}
