package no.kash.gamedev.jag.controller.lobby;

import no.kash.gamedev.jag.game.gamesession.GameMode;
import no.kash.gamedev.jag.game.gamesession.GameSession;

public class GameSessionControls {
	public GameSession session;

	public GameSessionControls() {
		session = new GameSession();
		session.dropIn = true;
		session.gameMode = GameMode.STANDARD_FFA;
		session.roundTime = 60;
		session.roundsToWin = 3;
		session.startingHealth = 100f;
		session.testMode = false;
	}
}
