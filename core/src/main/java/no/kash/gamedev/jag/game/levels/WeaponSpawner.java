package no.kash.gamedev.jag.game.levels;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

import no.kash.gamedev.jag.game.commons.utils.Cooldown;
import no.kash.gamedev.jag.game.gamecontext.GameContext;
import no.kash.gamedev.jag.game.gameobjects.GameObject;
import no.kash.gamedev.jag.game.gameobjects.collectables.weapons.Weapon;
import no.kash.gamedev.jag.game.gameobjects.players.guns.GunType;

public class WeaponSpawner {

	private GameContext gameContext;
	private ArrayList<WeaponSpawnTile> weaponTiles;
	private ArrayList<Vector2> wepSpawns;

	private Cooldown timerInterval;
	private float interval = 6;
	private boolean stopped;

	public final float cumProbs;

	public WeaponSpawner(ArrayList<Vector2> wepSpawns, GameContext gameContext) {
		this.gameContext = gameContext;
		this.wepSpawns = wepSpawns;
		timerInterval = new Cooldown(interval);

		weaponTiles = new ArrayList<WeaponSpawnTile>();

		float realProbs = 0;
		for (GunType type : GunType.values()) {
			realProbs += type.getProbability();
		}
		cumProbs = realProbs;

	}

	public void spawnWeaponTiles() {
		for (Vector2 points : wepSpawns) {
			WeaponSpawnTile temp = new WeaponSpawnTile(this, points.x, points.y);
			weaponTiles.add(temp);
			this.gameContext.spawn(temp);
			temp.reSpawnCooldown.startCooldown();
		}
	}

	public void update(float delta) {
		if (stopped) {
			return;
		}
		timerInterval.update(delta);
		if (!timerInterval.isOnCooldown()) {
			for (WeaponSpawnTile tile : weaponTiles) {
				int randomNum = 1 + (int) (Math.random() * 2);
				if (randomNum == 1) {
					if (!tile.isOccupied() && !tile.reSpawnCooldown.isOnCooldown()) {
						tile.preSpawnWeapon();
						tile.reSpawnCooldown.startCooldown();
					}
				}
				timerInterval.startCooldown();
			}
		}
	}
	
	public void start(){
		stopped = false;
	}

	public void stop() {
		stopped = true;
		for (GameObject o : gameContext.getByClass(new Class[] { Weapon.class })) {
			o.destroy();
		}
	}

	public boolean isStopped() {
		return stopped;
	}
}
