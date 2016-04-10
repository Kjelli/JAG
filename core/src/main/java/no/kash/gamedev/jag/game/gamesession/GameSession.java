package no.kash.gamedev.jag.game.gamesession;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import no.kash.gamedev.jag.commons.defs.Defs;
import no.kash.gamedev.jag.game.gamecontext.GameContext;
import no.kash.gamedev.jag.game.gameobjects.players.Player;
import no.kash.gamedev.jag.game.gameobjects.players.PlayerInfo;
import no.kash.gamedev.jag.game.gamesession.roundhandlers.FFARoundHandler;
import no.kash.gamedev.jag.game.gamesession.roundhandlers.RoundHandler;
import no.kash.gamedev.jag.game.gamesession.roundhandlers.TeamRoundHandler;
import no.kash.gamedev.jag.game.screens.PlayScreen;

public class GameSession {
	public String mapFilename = "maps/sumoarena_team1.tmx";

	public GameSettings settings;

	public Map<Integer, PlayerInfo> players;
	public RoundHandler<?> roundHandler;


	public GameSession() {
		players = new HashMap<>();
		settings = new GameSettings();
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
		GameMode gameMode = settings.getSelectedValue(Defs.SESSION_GM, GameMode.class);
		switch (gameMode) {
		default:
			gameContext.getAnnouncer().announce("GameMode: " + gameMode.displayName + " not implemented!");
		case STANDARD_FFA:
			roundHandler = new FFARoundHandler(playScreen, gameContext, this, players);
			break;
		case STANDARD_TEAM:
			roundHandler = new TeamRoundHandler(playScreen, gameContext, this, players);
			break;

		}

		roundHandler.setup();
	}

	public int numberOfPlayers() {
		return players.size();
	}

}
