package no.kash.gamedev.jag.game.gamesession.roundhandlers;

import no.kash.gamedev.jag.game.gameobjects.players.Player;

public class RoundResult {
	public static RoundResult NO_RESULT = new RoundResult(true, null, false);

	public boolean draw = false;
	public boolean gameEnding = false;
	public Player winner;

	public RoundResult(boolean draw, Player winner, boolean gameEnding) {
		this.draw = draw;
		this.winner = winner;
		this.gameEnding = gameEnding;
	}

}
