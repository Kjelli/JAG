package no.kash.gamedev.jag.game.gamesettings;

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
import no.kash.gamedev.jag.game.screens.GameScreen;
import no.kash.gamedev.jag.game.screens.LobbyScreen;

public class FFARoundHandler implements RoundHandler {

	public Map<Integer, Player> players;
	public GameContext gameContext;
	public GameSettings gameSettings;

	public int currentRound = 0;

	public FFARoundHandler(GameContext gameContext, GameSettings gameSettings, Map<Integer, Player> players) {
		this.players = players;
		this.gameContext = gameContext;
		this.gameSettings = gameSettings;
	}

	@Override
	public void playerKilled(Player killer, Player killed) {
		players.remove(killed.getId());
		killed.destroy();

		// TODO announce
		System.out.println(killer + " killed " + killed);

	}

	@Override
	public Player winner() {
		System.out.println(players.size() + "  //  " + gameSettings.players.size());
		if (players.size() == 1 && gameSettings.players.size() > 1) {
			Player winner = players.values().iterator().next();
			System.out.println("Winner: " + winner);
			return winner;
		}
		return null;
	}

	@Override
	public void win(final GameScreen screen) {
		currentRound++;
		if (currentRound < gameSettings.rounds) {
			screen.restart();
		} else {
			gameContext.setTimeModifier(0.33f);

			// No op
			TweenableFloat f = new TweenableFloat(0);
			TweenGlobal.start(Tween.from(f, FloatAccessor.TYPE_VALUE, 2.0f).target(1).setCallback(new TweenCallback() {
				@Override
				public void onEvent(int arg0, BaseTween<?> arg1) {
					if (arg0 == TweenCallback.COMPLETE) {
						JustAnotherGame game = screen.getGame();
						game.setScreen(new LobbyScreen(game));
						game.getServer().broadcast(new PlayerStateChange(JustAnotherGameController.LOBBY_STATE));
					}
				}
			}));

			// TODO flashy effects on winner
		}
	}

	@Override
	public boolean canJoin() {
		// If no one has died yet
		return players.size() == gameSettings.players.size();
	}

}
