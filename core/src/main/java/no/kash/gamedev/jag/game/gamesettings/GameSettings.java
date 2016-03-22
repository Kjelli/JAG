package no.kash.gamedev.jag.game.gamesettings;

import java.util.HashMap;
import java.util.Map;

import no.kash.gamedev.jag.game.gameobjects.players.PlayerInfo;
import no.kash.gamedev.jag.game.gamesettings.roundhandlers.RoundHandler;

public class GameSettings {
	public GameMode gameMode = GameMode.STANDARD_FFA;
	public String mapFilename = "maps/sumoarena2.tmx";

	public boolean dropIn = true;
	public boolean testMode = true;

	public float startingHealth = 100f;
	public float roundTime = 60.0f;
	public int rounds = 3;

	public Map<Integer, PlayerInfo> players;
	public RoundHandler roundHandler;

	public GameSettings() {
		players = new HashMap<>();
	}

}
