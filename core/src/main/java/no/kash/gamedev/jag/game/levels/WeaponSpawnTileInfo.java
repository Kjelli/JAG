package no.kash.gamedev.jag.game.levels;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import no.kash.gamedev.jag.game.gameobjects.players.guns.GunType;

public class WeaponSpawnTileInfo {
	Vector2 pos;
	GunType gunType;
	float spawnRate;
	
	public WeaponSpawnTileInfo(Vector2 pos, GunType gunType, float spawnRate){
		this.pos = pos;
		this.gunType = gunType;
		this.spawnRate = spawnRate;
	}
}
