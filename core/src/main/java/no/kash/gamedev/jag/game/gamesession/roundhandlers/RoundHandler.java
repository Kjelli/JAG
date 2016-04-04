package no.kash.gamedev.jag.game.gamesession.roundhandlers;

import no.kash.gamedev.jag.game.gameobjects.players.Player;
import no.kash.gamedev.jag.game.screens.GameScreen;

public interface RoundHandler {
	public void playerKilled(Player killer, Player killed);

	public RoundResult roundEnded();

	public boolean canJoin();

	public void proceed(GameScreen gameScreen);

	public int currentRound();

	public void reset();

}
