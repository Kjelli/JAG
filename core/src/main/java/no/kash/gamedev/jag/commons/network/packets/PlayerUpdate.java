package no.kash.gamedev.jag.commons.network.packets;

public class PlayerUpdate implements GamePacket {
	public static final int FEEDBACK_VIBRATION  = 1;
	public static final int HEALTH = 2;
	public static final int AMMO = 3;
	public static final int MAG_AMMO = 4;
	
	
	public int feedbackId;
	public float[] state;

	public PlayerUpdate() {
		// Empty constructor required by Kryo
	}

	public PlayerUpdate(int actionId, float[] state) {
		this.feedbackId = actionId;
		this.state = state;
	}
}
