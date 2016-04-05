package no.kash.gamedev.jag.commons.network.packets;

public class GameSessionUpdate implements GamePacket {

	public int gameModeIndex;
	public int roundsToWin;
	public int roundTime;
	public int startingHealth;

	public boolean dropIn;
	public boolean testMode;

	public GameSessionUpdate() {

	}

}
