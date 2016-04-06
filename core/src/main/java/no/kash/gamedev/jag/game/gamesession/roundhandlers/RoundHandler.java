package no.kash.gamedev.jag.game.gamesession.roundhandlers;

import no.kash.gamedev.jag.game.gameobjects.players.Player;

public interface RoundHandler<T> {
	public void playerKilled(Player killer, Player killed);

	public RoundResult<T> roundEnded();

	public boolean canJoin();

	public void proceed();

	public int currentRound();

	public void reset();
	
	public void setup();

	public void start();

}
