package no.kash.gamedev.jag.game.gamesession.roundhandlers;

import java.util.Map;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import no.kash.gamedev.jag.commons.network.packets.PlayerNewStats;
import no.kash.gamedev.jag.commons.network.packets.PlayerStateChange;
import no.kash.gamedev.jag.commons.tweens.TweenGlobal;
import no.kash.gamedev.jag.commons.tweens.TweenableFloat;
import no.kash.gamedev.jag.commons.tweens.accessors.FloatAccessor;
import no.kash.gamedev.jag.controller.JustAnotherGameController;
import no.kash.gamedev.jag.game.JustAnotherGame;
import no.kash.gamedev.jag.game.gamecontext.GameContext;
import no.kash.gamedev.jag.game.gameobjects.players.Player;
import no.kash.gamedev.jag.game.gameobjects.players.PlayerInfo;
import no.kash.gamedev.jag.game.gameobjects.players.status.Status;
import no.kash.gamedev.jag.game.gamesession.GameSession;
import no.kash.gamedev.jag.game.screens.PlayScreen;
import no.kash.gamedev.jag.game.screens.LobbyScreen;

public abstract class AbstractRoundHandler<T> implements RoundHandler<T> {

	protected Map<Integer, Player> players;
	protected PlayScreen gameScreen;
	protected GameContext gameContext;
	protected GameSession gameSession;

	protected int currentRound = 0;

	public AbstractRoundHandler(PlayScreen screen, GameContext gameContext, GameSession gameSession,
			Map<Integer, Player> players) {
		this.players = players;
		this.gameScreen = screen;
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

		killer.getInfo().killed.add(killed.getInfo());
		killed.getInfo().killedBy.add(killer.getInfo());
		players.remove(killed.getId());
	}

	@Override
	public void playerKilled(Player killed, Status status) {
		killed.destroy();

		if (status.causer != null) {
			// Status was caused by someone else
			status.causer.getInfo().killed.add(killed.getInfo());
			killed.getInfo().killedBy.add(status.causer.getInfo());
			gameContext.getAnnouncer()
					.announce(killed + " " + status.type.killMessage + " " + status.causer.getInfo().name);
		} else {
			// SUICIDE OTHERWISE
			killed.getInfo().killedBy.add(killed.getInfo());
		}
		players.remove(killed.getId());
	}

	@Override
	public void proceed() {

		gameContext.setTimeModifier(0.25f);

		// No op
		TweenableFloat f = new TweenableFloat(0);
		final boolean gameOver = gameOver();

		TweenGlobal.start(Tween.from(f, FloatAccessor.TYPE_VALUE, gameOver ? 3.0f : 1.25f).target(1)
				.setCallback(new TweenCallback() {
					@Override
					public void onEvent(int arg0, BaseTween<?> arg1) {
						if (arg0 == TweenCallback.COMPLETE) {
							gameContext.setTimeModifier(1.0f);
							currentRound++;
							if (!gameOver) {
								gameScreen.restart();
							} else {
								statsHandler();
								gameScreen.getGame().setScreen(new LobbyScreen(gameScreen.getGame(), gameSession));
								gameScreen.getGame().getServer()
										.broadcast(new PlayerStateChange(JustAnotherGameController.LOBBY_STATE));
							}
						}
					}

				}));

	}

	private void statsHandler() {
		JustAnotherGame game = (JustAnotherGame) gameContext.getGame();
		for (PlayerInfo info : gameSession.players.values()) {
			int expEarned = 10;
			expEarned += info.killed.size() * 5;
			expEarned += info.roundsWon * 7;
			game.getServer().send(info.id, new PlayerNewStats(expEarned));
		}

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
		// Default is if no one has died yet
		return gameSession.dropIn && players.size() == gameSession.players.size();
	}

	@Override
	public void start() {
		for (PlayerInfo player : gameSession.players.values()) {
			gameScreen.spawnPlayer(player);
			// If spawning succeeded, block input
			if (players.containsKey(player.id)) {
				players.get(player.id).blockInput(true);
			}
		}
		// No op
		TweenableFloat f = new TweenableFloat(0);
		TweenGlobal.start(Tween.from(f, FloatAccessor.TYPE_VALUE, 3.0f).target(1).setCallback(new TweenCallback() {
			@Override
			public void onEvent(int arg0, BaseTween<?> arg1) {
				if (arg0 == TweenCallback.COMPLETE) {
					for (PlayerInfo player : gameSession.players.values()) {
						if (players.containsKey(player.id)) {
							players.get(player.id).blockInput(false);
						}
					}
				}
			}
		}));
		gameContext.getAnnouncer().announceRoundStart(gameSession.roundHandler.currentRound(), 3);
	}

	@Override
	public void reset() {
		currentRound = 0;
	}

	@Override
	public int currentRound() {
		return currentRound;
	}

}
