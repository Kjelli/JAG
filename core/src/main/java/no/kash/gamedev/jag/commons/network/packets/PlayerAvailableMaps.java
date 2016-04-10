package no.kash.gamedev.jag.commons.network.packets;

public class PlayerAvailableMaps implements GamePacket {
	public int availableMaps;
	
	public PlayerAvailableMaps(){
		
	}
	
	public PlayerAvailableMaps(int availableMaps){
		this.availableMaps = availableMaps;
	}
}
