package no.kash.gamedev.jag.game.gamesession;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import no.kash.gamedev.jag.game.gamecontext.GameContext;
import no.kash.gamedev.jag.game.gameobjects.players.Player;
import no.kash.gamedev.jag.game.gameobjects.players.PlayerInfo;
import no.kash.gamedev.jag.game.gamesession.roundhandlers.FFARoundHandler;
import no.kash.gamedev.jag.game.gamesession.roundhandlers.RoundHandler;
import no.kash.gamedev.jag.game.gamesession.roundhandlers.TeamRoundHandler;
import no.kash.gamedev.jag.game.screens.PlayScreen;

public class GameSession {
	public String mapFilename = "maps/sumoarena_team1.tmx";

	public GameMode gameMode = GameMode.STANDARD_FFA;

	public boolean friendlyFire = false;
	public boolean dropIn = true;
	public boolean testMode = true;
	public boolean drawNames = true;

	public int startingHealth = 100;
	public int roundTime = 60;
	public int roundsToWin = 2;

	public Map<Integer, PlayerInfo> players;
	public RoundHandler<?> roundHandler;


	public GameSession() {
		players = new HashMap<>();
	}

	public void reset() {
		for (Iterator<PlayerInfo> it = players.values().iterator(); it.hasNext();) {
			PlayerInfo player = it.next();
			if (player.temporary) {
				it.remove();
			} else {
				player.resetSession();
			}
		}
		if (roundHandler != null) {
			roundHandler.reset();
		}
	}

	public void init(PlayScreen playScreen, GameContext gameContext, Map<Integer, Player> players) {
		switch (gameMode) {
		default:
			gameContext.getAnnouncer().announce("GameMode: " + gameMode.displayName + " not finished!");
		case STANDARD_FFA:
			roundHandler = new FFARoundHandler(playScreen, gameContext, this, players);
			break;
		case STANDARD_TEAM:
			roundHandler = new TeamRoundHandler(playScreen, gameContext, this, players);
			break;

		}

		roundHandler.setup();
	}

}
