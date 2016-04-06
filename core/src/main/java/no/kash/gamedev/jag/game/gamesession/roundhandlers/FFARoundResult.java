package no.kash.gamedev.jag.game.gamesession.roundhandlers;

import no.kash.gamedev.jag.game.gameobjects.players.Player;

public class FFARoundResult implements RoundResult<Player> {
	public static FFARoundResult NO_RESULT = new FFARoundResult(true, null, false);

	public boolean draw = false;
	public boolean gameEnding = false;
	public Player winner;

	public FFARoundResult(boolean draw, Player winner, boolean gameEnding) {
		this.draw = draw;
		this.winner = winner;
		this.gameEnding = gameEnding;
	}

	@Override
	public boolean isDraw() {
		return draw;
	}

	@Override
	public boolean isGameEnding() {
		return gameEnding;
	}

	@Override
	public Player winner() {
		return winner;
	}

}
