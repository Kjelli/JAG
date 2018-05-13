package no.kash.gamedev.jag.game.levels;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

import no.kash.gamedev.jag.game.commons.utils.Cooldown;
import no.kash.gamedev.jag.game.gamecontext.GameContext;
import no.kash.gamedev.jag.game.gameobjects.GameObject;
import no.kash.gamedev.jag.game.gameobjects.collectables.weapons.Weapon;
import no.kash.gamedev.jag.game.gameobjects.players.guns.GunType;

public class WeaponSpawner {

	public static final float SPAWN_RATE = 0.05f;
	private GameContext gameContext;
	private ArrayList<WeaponSpawnTile> weaponTiles;
	private ArrayList<WeaponSpawnTileInfo> wepSpawns;

	private Cooldown timerInterval;
	private float interval = 3;
	private boolean stopped;

	public final float cumProbs;

	public WeaponSpawner(ArrayList<WeaponSpawnTileInfo> tempList, GameContext gameContext) {
		this.gameContext = gameContext;
		this.wepSpawns = tempList;
		timerInterval = new Cooldown(interval);

		weaponTiles = new ArrayList<WeaponSpawnTile>();

		float realProbs = 0;
		for (GunType type : GunType.values()) {
			realProbs += type.getProbability();
		}
		cumProbs = realProbs;

	}

	public void spawnWeaponTiles() {
		for (WeaponSpawnTileInfo info : wepSpawns) {
			WeaponSpawnTile temp = new WeaponSpawnTile(this, info);
			weaponTiles.add(temp);
			this.gameContext.spawn(temp);
			temp.reSpawnCooldown.start();
		}
	}

	public void update(float delta) {
		if (stopped) {
			return;
		}
		timerInterval.update(delta);
		if (!timerInterval.isOnCooldown()) {
			for (WeaponSpawnTile tile : weaponTiles) {
				float random = (float) Math.random();
				if (random <= tile.spawnRate) {
					if (!tile.isOccupied() && !tile.reSpawnCooldown.isOnCooldown()) {
						tile.preSpawnWeapon();
						tile.reSpawnCooldown.start();
					}
					timerInterval.start();
				}
			}
		}
	}

	public void start() {
		stopped = false;
	}

	public void stop() {
		stopped = true;
		for (Object o : gameContext.getByClass(new Class[] { Weapon.class })) {
			((GameObject)o).destroy();
		}
	}

	public boolean isStopped() {
		return stopped;
	}
}
