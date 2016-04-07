package no.kash.gamedev.jag.game.gamesession.roundhandlers;

import java.util.Map;

import no.kash.gamedev.jag.commons.defs.Defs;
import no.kash.gamedev.jag.game.gamecontext.GameContext;
import no.kash.gamedev.jag.game.gameobjects.players.Player;
import no.kash.gamedev.jag.game.gameobjects.players.PlayerInfo;
import no.kash.gamedev.jag.game.gamesession.GameSession;
import no.kash.gamedev.jag.game.screens.PlayScreen;

public class FFARoundHandler extends AbstractRoundHandler<Player> {

	public FFARoundHandler(PlayScreen gameScreen, GameContext gameContext, GameSession gameSession,
			Map<Integer, Player> players) {
		super(gameScreen, gameContext, gameSession, players);
	}

	@Override
	public RoundResult<Player> roundEnded() {
		if (players.size() == 1 && gameSession.players.size() > 1) {
			Player winner = players.values().iterator().next();
			winner.setInvincible(true);
			PlayerInfo winnerInfo = winner.getInfo();
			winnerInfo.roundsWon++;
			boolean gameEnding;
			if (winnerInfo.roundsWon < gameSession.settings.getSelectedValue(Defs.SESSION_RTW, Integer.class)) {
				gameContext.getAnnouncer().announce(String.format("--- %s wins the round ---", winner));
				gameEnding = false;
			} else {
				gameContext.getAnnouncer().announce(String.format("*** %s WINS THE MATCH ***", winner));
				gameEnding = true;
			}
			return new FFARoundResult(false, winner, gameEnding);
		} else if (players.size() == 0) {
			gameContext.getAnnouncer().announce("--- DRAW ---");
			return FFARoundResult.NO_RESULT;
		}
		return null;
	}


	public void setup() {
		if (gameSession.settings.getSelectedValue(Defs.SESSION_TEST_MODE, Boolean.class)) {
			PlayerInfo dummyPlayer = new PlayerInfo();
			dummyPlayer.temporary = true;
			gameSession.players.put(-500, dummyPlayer);
		}
	}

}
