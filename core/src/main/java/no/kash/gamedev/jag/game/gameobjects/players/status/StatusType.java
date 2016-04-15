package no.kash.gamedev.jag.game.gameobjects.players.status;

public enum StatusType {
	burn(5f, "got roasted by", true), stun(0.0f, "", true), slow(0.0f, "", true),
	poison(3f,"got toxicated by", false), healing(0,"", false);

	public float damage;
	public String killMessage;
	public boolean negative;

	private StatusType(float damage, String killMessage, boolean negative) {
		this.damage = damage;
		this.killMessage = killMessage;
		this.negative = negative;
	}
}
