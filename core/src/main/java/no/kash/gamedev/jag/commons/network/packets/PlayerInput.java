package no.kash.gamedev.jag.commons.network.packets;

public class PlayerInput implements GamePacket {
	public int senderId;
	public int inputId;
	public float[] state;

	public PlayerInput() {
		// No-arg constructor required by kryonet
	}

	public PlayerInput(int senderId, int inputId, float[] state) {
		this.senderId = senderId;
		this.inputId = inputId;
		this.state = state;
	}

}
