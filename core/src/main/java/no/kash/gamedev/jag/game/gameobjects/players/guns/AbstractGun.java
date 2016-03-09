package no.kash.gamedev.jag.game.gameobjects.players.guns;

public abstract class AbstractGun {

	protected float cooldown;

	protected GunType type;
	protected Magazine magazine;

	public AbstractGun(GunType type) {
		this.type = type;
	}

}
