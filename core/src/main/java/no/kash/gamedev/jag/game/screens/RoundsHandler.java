package no.kash.gamedev.jag.game.screens;

import java.util.Map;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;

import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.game.gamecontext.functions.Cooldown;
import no.kash.gamedev.jag.game.gameobjects.players.Player;
import no.kash.gamedev.jag.game.gameobjects.players.PlayerInfo;
import no.kash.gamedev.jag.game.gamesettings.GameSettings;

public class RoundsHandler {
	
	private GameScreen gameScreen;
	private Map<Integer, Player> players;
	private GameSettings settings;
	
	private Cooldown newRound;
	private Cooldown startRound;
	private GlyphLayout text;
	
	private boolean roundFinished;
	private boolean roundStarted;;

	private String announcment;

	public RoundsHandler(Map<Integer, Player> players, GameScreen gameScreen, GameSettings settings) {
		this.gameScreen = gameScreen;
		this.players = players;
		this.settings = settings;
		
		roundFinished = false;
		roundStarted = true;
		announcment = "";
		
		newRound = new Cooldown(8);
		startRound = new Cooldown(5);
		
		text = new GlyphLayout(Assets.font, announcment);
	}

	public void update(float delta) {
		newRound.update(delta);
		
		if (!newRound.isOnCooldown()) {
			
		}
		
		if(!startRound.isOnCooldown() && !roundStarted){
			roundStarted = true;
			reSpawn();
		}
	}
	

	private void reSpawn() {
		for (PlayerInfo player : settings.players.values()) {
			gameScreen.spawnPlayer(player);
		}
	}
	

	public void checkWinCondition() {
		int alivePlayers = 0;
		int player = 0;
		for (int i = 0; i <= players.size(); i++) {
			if(players.get(i) != null){
				if(players.get(i).isAlive()){
					alivePlayers++;
					player = i;
				}
			}
		}
		if (alivePlayers == 1) {
			roundFinished(players.get(player));
		}
	}

	private void roundFinished(Player player) {
		announcment = "Player " + player.getName() + " wins!";
		System.out.println(announcment);
		roundFinished = true;
		newRound.startCooldown();
	}
	private void roundStart() {
		announcment = "Round " ;
		roundStarted = false;
		startRound.startCooldown();
		
	}
}
