package no.kash.gamedev.jag.game.gamesettings;

import no.kash.gamedev.jag.game.gameobjects.players.Player;
import no.kash.gamedev.jag.game.screens.GameScreen;

public interface RoundHandler {
	public void playerKilled(Player killer, Player killed);

	public Player winner();
	
	public boolean canJoin();

	public void win(GameScreen gameScreen);

}
