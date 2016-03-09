package no.kash.gamedev.jag.game.gameobjects.players.guns;

public interface Magazine {
	int getBulletCount();

	int getCapacity();

	void setBulletCount(int bulletCount);
}
