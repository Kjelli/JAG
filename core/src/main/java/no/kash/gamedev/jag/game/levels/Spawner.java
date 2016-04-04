package no.kash.gamedev.jag.game.levels;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

import no.kash.gamedev.jag.game.commons.utils.Cooldown;
import no.kash.gamedev.jag.game.gamecontext.GameContext;

public class Spawner {

	private GameContext gameContext;
	private ArrayList<WeaponSpawnTile> weaponTiles;

	private Cooldown timerInterval;
	private float interval = 6;

	public Spawner(ArrayList<Vector2> wepSpawns, GameContext gameContext) {
		this.gameContext = gameContext;
		timerInterval = new Cooldown(interval);

		weaponTiles = new ArrayList<WeaponSpawnTile>();
		for (Vector2 points : wepSpawns) {
			WeaponSpawnTile temp = new WeaponSpawnTile(points.x, points.y);
			weaponTiles.add(temp);
			this.gameContext.spawn(temp);
			temp.reSpawnCooldown.startCooldown();
		}

	}

	public void update(float delta) {
		timerInterval.update(delta);
		if (!timerInterval.isOnCooldown()) {
			for (WeaponSpawnTile tile : weaponTiles) {
				int randomNum = 1 + (int) (Math.random() * 2);
				if (randomNum == 1) {
					if (!tile.isOccupied() && !tile.reSpawnCooldown.isOnCooldown()) {
						tile.spawnWeapon();
						tile.reSpawnCooldown.startCooldown();
					}
				}
				timerInterval.startCooldown();
			}
		}
	}
}
