package no.kash.gamedev.jag.game.gamesession.roundhandlers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.kash.gamedev.jag.game.gameobjects.players.Player;
import no.kash.gamedev.jag.game.gameobjects.players.status.Status;

public interface RoundHandler<T> {
	public void playerKilled(Player killer, Player killed);

	public void playerKilled(Player killed, Status status);

	public RoundResult<T> roundEnded();

	public boolean canJoin();

	public void proceed();

	public int currentRound();

	public void reset();

	public void setup();

	public void start();

	public float getRoundTimer();

	public void update(float delta);
	
	public boolean isSuddenDeath();

	public void drawRoundTime(SpriteBatch batch);

}
