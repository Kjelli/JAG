package no.kash.gamedev.jag.game.gamesession.roundhandlers;

import no.kash.gamedev.jag.commons.network.packets.GamePacket;

public class PlayerNewStats implements GamePacket {
	public int xp;
	
	public PlayerNewStats(){
		
	}
	
	public PlayerNewStats(int xp){
		this.xp = xp;
	}
}
