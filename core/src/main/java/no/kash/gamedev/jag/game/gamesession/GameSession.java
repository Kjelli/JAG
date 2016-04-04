package no.kash.gamedev.jag.game.gamesession;

import java.util.HashMap;
import java.util.Map;

import no.kash.gamedev.jag.game.gameobjects.players.PlayerInfo;
import no.kash.gamedev.jag.game.gamesession.roundhandlers.RoundHandler;

public class GameSession {
	public GameMode gameMode = GameMode.STANDARD_FFA;
	public String mapFilename = "maps/sumoarena2.tmx";

	public boolean dropIn = true;
	public boolean testMode = true;

	public float startingHealth = 100f;
	public float roundTime = 60.0f;
	public int roundsToWin = 5;

	public Map<Integer, PlayerInfo> players;
	public RoundHandler roundHandler;

	public GameSession() {
		players = new HashMap<>();
	}

}
