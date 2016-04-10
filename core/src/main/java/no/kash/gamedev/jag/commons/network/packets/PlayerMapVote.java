package no.kash.gamedev.jag.commons.network.packets;

public class PlayerMapVote implements GamePacket {
	public int mapIndex;

	public PlayerMapVote() {

	}

	public PlayerMapVote(int mapIndex) {
		this.mapIndex = mapIndex;
	}
}
