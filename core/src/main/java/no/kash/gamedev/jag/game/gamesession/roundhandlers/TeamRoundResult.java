package no.kash.gamedev.jag.game.gamesession.roundhandlers;

public class TeamRoundResult implements RoundResult<Integer> {
	public static TeamRoundResult NO_RESULT = new TeamRoundResult(true, -1, false);

	public boolean draw = false;
	public boolean gameEnding = false;
	public int winnerTeam;

	public TeamRoundResult(boolean draw, int winnerTeam, boolean gameEnding) {
		this.draw = draw;
		this.winnerTeam = winnerTeam;
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
	public Integer winner() {
		return winnerTeam;
	}

}
