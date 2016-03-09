package no.kash.gamedev.jag.game.gameobjects.players.guns;

public abstract class AbstractMagazine implements Magazine {
	int bulletCount;
	int capacity;

	public AbstractMagazine(int bulletCount, int capacity) {
		this.bulletCount = bulletCount;
		this.capacity = capacity;
	}

	@Override
	public int getBulletCount() {
		return bulletCount;
	}

	@Override
	public int getCapacity() {
		return capacity;
	}

	@Override
	public void setBulletCount(int bulletCount) {
		this.bulletCount = bulletCount;
	}

}
