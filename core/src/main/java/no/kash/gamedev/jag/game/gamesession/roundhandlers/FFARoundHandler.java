package no.kash.gamedev.jag.game.gamesession.roundhandlers;

import java.util.Map;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import no.kash.gamedev.jag.commons.network.packets.PlayerStateChange;
import no.kash.gamedev.jag.commons.tweens.TweenGlobal;
import no.kash.gamedev.jag.commons.tweens.TweenableFloat;
import no.kash.gamedev.jag.commons.tweens.accessors.FloatAccessor;
import no.kash.gamedev.jag.controller.JustAnotherGameController;
import no.kash.gamedev.jag.game.JustAnotherGame;
import no.kash.gamedev.jag.game.gamecontext.GameContext;
import no.kash.gamedev.jag.game.gameobjects.players.Player;
import no.kash.gamedev.jag.game.gameobjects.players.PlayerInfo;
import no.kash.gamedev.jag.game.gamesession.GameSession;
import no.kash.gamedev.jag.game.screens.GameScreen;
import no.kash.gamedev.jag.game.screens.LobbyScreen;

public class FFARoundHandler implements RoundHandler {

	public Map<Integer, Player> players;
	public GameContext gameContext;
	public GameSession gameSession;

	public int currentRound = 0;

	public FFARoundHandler(GameContext gameContext, GameSession gameSession, Map<Integer, Player> players) {
		this.players = players;
		this.gameContext = gameContext;
		this.gameSession = gameSession;
	}

	@Override
	public void playerKilled(Player killer, Player killed) {
		killed.destroy();
		if (killer.equals(killed)) {
			gameContext.getAnnouncer().announce(killer + " committed suicide", 3.0f);
		} else {
			gameContext.getAnnouncer().announce(killer + " killed " + killed, 3.0f);
		}

		PlayerInfo killerInfo = gameSession.players.get(killer.getId());
		PlayerInfo killedInfo = gameSession.players.get(killed.getId());
		killerInfo.killed.add(killedInfo);
		killedInfo.killedBy.add(killerInfo);

		players.remove(killed.getId());
	}

	@Override
	public int currentRound() {
		return currentRound;
	}

	@Override
	public RoundResult roundEnded() {
		if (players.size() == 1 && gameSession.players.size() > 1) {
			Player winner = players.values().iterator().next();
			winner.setInvincible(true);
			PlayerInfo winnerInfo = gameSession.players.get(winner.getId());
			winnerInfo.roundsWon++;
			boolean gameEnding;
			if (winnerInfo.roundsWon < gameSession.roundsToWin) {
				gameContext.getAnnouncer().announce(String.format("--- %s wins the round ---", winner));
				gameEnding = false;
			} else {
				gameContext.getAnnouncer().announce(String.format("*** %s WINS THE MATCH ***", winner));
				gameEnding = true;
			}
			return new RoundResult(false, winner, gameEnding);
		} else if (players.size() == 0) {
			gameContext.getAnnouncer().announce("--- DRAW ---");
			return RoundResult.NO_RESULT;
		}
		return null;
	}

	@Override
	public void proceed(final GameScreen screen) {

		gameContext.setTimeModifier(0.25f);

		// No op
		TweenableFloat f = new TweenableFloat(0);
		final boolean gameOver = gameOver();
		TweenGlobal.start(Tween.from(f, FloatAccessor.TYPE_VALUE, gameOver ? 3.75f : 1.25f).target(1).setCallback(new TweenCallback() {
			@Override
			public void onEvent(int arg0, BaseTween<?> arg1) {
				if (arg0 == TweenCallback.COMPLETE) {
					gameContext.setTimeModifier(1.0f);
					currentRound++;
					if (!gameOver) {
						screen.restart();
					} else {
						JustAnotherGame game = screen.getGame();
						game.setScreen(new LobbyScreen(game));
						game.getServer().broadcast(new PlayerStateChange(JustAnotherGameController.LOBBY_STATE));
					}
				}
			}
		}));

		// TODO flashy effects on winner

	}

	protected boolean gameOver() {
		for (PlayerInfo info : gameSession.players.values()) {
			if (info.roundsWon >= gameSession.roundsToWin) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean canJoin() {
		// If no one has died yet
		return players.size() == gameSession.players.size();
	}

}
