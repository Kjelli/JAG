package no.kash.gamedev.jag.game.gameobjects.players.status;

public enum StatusType {
	burn(0.1f, "got roasted by"), stun(0.0f, ""), slow(0.0f, "");

	public float damage;
	public String killMessage;

	private StatusType(float damage, String killMessage) {
		this.damage = damage;
		this.killMessage = killMessage;
	}
}
