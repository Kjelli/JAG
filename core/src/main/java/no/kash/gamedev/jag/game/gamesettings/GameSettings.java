package no.kash.gamedev.jag.game.gamesettings;

import java.util.HashMap;
import java.util.Map;

import no.kash.gamedev.jag.game.gameobjects.players.PlayerInfo;

public class GameSettings {
	public GameMode gameMode = GameMode.STANDARD_FFA;
	public String mapFilename = "maps/sumoarena2.tmx";

	public boolean dropIn = true;

	public float startingHealth = 100f;
	public float roundTime = 60.0f;
	public int rounds = 3;

	public Map<Integer, PlayerInfo> players;

	public GameSettings() {
		players = new HashMap<>();
	}

}
