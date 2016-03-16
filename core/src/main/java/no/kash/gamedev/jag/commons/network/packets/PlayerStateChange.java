package no.kash.gamedev.jag.commons.network.packets;

public class PlayerStateChange implements GamePacket {

	public int stateId;

	public PlayerStateChange() {
		// No-arg constructor required by kryonet
	}

	public PlayerStateChange(int stateId) {
		this.stateId = stateId;
	}

}
