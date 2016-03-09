package no.kash.gamedev.jag.game.gameobjects.players.guns;

public interface Gun {
	// TODO ser det greit ut ?
	Magazine getMagazine();
	void shoot();
	void reload();
}
