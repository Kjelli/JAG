package no.kash.gamedev.jag.game.levels;

import com.badlogic.gdx.math.Vector2;

import no.kash.gamedev.jag.game.gameobjects.collectables.items.ItemType;

public class ItemSpawnTileInfo {
	Vector2 pos;
	ItemType itemType;
	float spawnRate;
	
	public ItemSpawnTileInfo(Vector2 pos, ItemType itemType, float spawnRate){
		this.pos = pos;
		this.itemType = itemType;
		this.spawnRate = spawnRate;
	}
}
