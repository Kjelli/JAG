package no.kash.gamedev.jag.commons.network.packets;

public class PlayerFeedback implements GamePacket {
	public static final int FEEDBACK_VIBRATION  = 1;
	
	
	public int feedbackId;
	public float[] state;

	public PlayerFeedback() {
		// Empty constructor required by Kryo
	}

	public PlayerFeedback(int actionId, float[] state) {
		this.feedbackId = actionId;
		this.state = state;
	}
}
