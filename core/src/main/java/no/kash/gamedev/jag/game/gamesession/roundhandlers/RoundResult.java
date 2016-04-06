package no.kash.gamedev.jag.game.gamesession.roundhandlers;

public interface RoundResult<T> {

	boolean isDraw();

	boolean isGameEnding();

	T winner();

}
