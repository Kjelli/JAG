package no.kash.gamedev.jag.game.gameobjects.players.guns;

public class Pistol extends AbstractGun {

	public Pistol(GunType type) {
		super(type);
		setMagazine(new PistolMagazine());
	}

}
