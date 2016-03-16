package no.kash.gamedev.jag.commons.network.packets;

public class PlayerStateChangeResponse implements GamePacket {

	public int stateId;

	public PlayerStateChangeResponse() {
		// No-arg constructor required by kryonet
	}

	public PlayerStateChangeResponse(int stateId) {
		this.stateId = stateId;
	}

}
