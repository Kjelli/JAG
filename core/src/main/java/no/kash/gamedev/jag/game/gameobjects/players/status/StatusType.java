package no.kash.gamedev.jag.game.gameobjects.players.status;

public enum StatusType {
	burn(0.05f, "got roasted by"), stun(0.0f, ""), slow(0.0f, ""),
	poison(0.07f,"got toxicated by");

	public float damage;
	public String killMessage;

	private StatusType(float damage, String killMessage) {
		this.damage = damage;
		this.killMessage = killMessage;
	}
}
