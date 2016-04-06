package no.kash.gamedev.jag.commons.network.packets;

public class PlayerNewStats implements GamePacket {
	public int xp;
	
	public PlayerNewStats(){
		
	}
	
	public PlayerNewStats(int xp){
		this.xp = xp;
	}
}
