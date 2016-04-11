package no.kash.gamedev.jag.game.levels;

import no.kash.gamedev.jag.game.gameobjects.collectables.Collectable;

public interface SpawnTile<T extends Collectable> {
	
	public void update(float delta);
	
	public void spawnObject();
	
	public void preSpawnObject();
	
	public void randomObject();
	
	public boolean isOccupied();

	public boolean isSpawning();

	
}
