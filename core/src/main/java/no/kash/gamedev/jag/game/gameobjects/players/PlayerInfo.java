package no.kash.gamedev.jag.game.gameobjects.players;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;

public class PlayerInfo {
	public int id = -1;
	public String name = "Minge";
	public Color color = new Color(Color.WHITE);

	public int timesPlayed = -1;
	public int level;
	public int xp;
	
	// session specific information
	public boolean ready = false;
	public int roundsWon = 0;
	
	public List<PlayerInfo> killed;
	public List<PlayerInfo> killedBy;

	public PlayerInfo() {
		killed = new ArrayList<>();
		killedBy = new ArrayList<>();
	}

	public void resetSession() {
		killed.clear();
		killedBy.clear();
	}

	@Override
	public String toString() {
		return String.format("PlayerInfo {name: %s}", name);
	}
}
