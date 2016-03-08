package no.kash.gamedev.jag.commons.network.packets;

public class PlayerConnect implements GamePacket {
	public int senderId;
	public String name;

	public PlayerConnect() {
		// No-arg constructor required by kryonet
	}

	public PlayerConnect(int senderId, String name) {
		this.senderId = senderId;
		this.name = name;
	}

}
