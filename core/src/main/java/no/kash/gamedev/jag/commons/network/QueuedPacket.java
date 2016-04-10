package no.kash.gamedev.jag.commons.network;

import no.kash.gamedev.jag.commons.network.packets.GamePacket;

public class QueuedPacket {
	
	static enum Type{
		SEND, BROADCAST, BROADCAST_EXCEPT
	}
	
	Type type;
	int id;
	GamePacket packet;
	
	/**
	 * Broadcast
	 */
	
	public QueuedPacket(int sendToId, GamePacket packet, Type type){
		this.packet = packet;
		this.id = sendToId;
		this.type = type;
	}
	
	public Type getType() {
		return type;
	}
	
	
}
