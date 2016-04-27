package no.kash.gamedev.jag.commons.network.packets;

public class PlayerUpdate implements GamePacket {
	public static final int FEEDBACK_VIBRATION = 1;
	public static final int HEALTH = 2;
	public static final int AMMO = 3;
	public static final int GUN = 4;
	public static final int PLAYER_INFO = 5;
	public static final int GAME_MASTER = 6;
	public static final int ITEM = 7;
	public static final int KILL_DEATH = 8;
	
	public int fields;
	public int[] fieldId;
	public float[][] state;
	public String[] info;

	public PlayerUpdate() {
		// Empty constructor required by Kryo
	}

	public PlayerUpdate(int fields, int[] fieldId, float[][] state, String[] info) {
		this.fields = fields;
		this.fieldId = fieldId;
		this.state = state;
		this.info = info;
	}

	public PlayerUpdate(int fields, int[] fieldId, float[][] state) {
		this.fields = fields;
		this.fieldId = fieldId;
		this.state = state;
	}
}
