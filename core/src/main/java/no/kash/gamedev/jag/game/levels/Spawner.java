package no.kash.gamedev.jag.game.levels;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

import no.kash.gamedev.jag.game.gamecontext.GameContext;
import no.kash.gamedev.jag.game.gamecontext.functions.Cooldown;
import no.kash.gamedev.jag.game.gameobjects.collectables.weapons.Weapon;
import no.kash.gamedev.jag.game.gameobjects.players.guns.GunType;

public class Spawner {

	private GameContext gameContext;
	private ArrayList<SpawnTile> weaponTiles;

	private Cooldown timerInterval;
	private float interval = 5;

	public Spawner(ArrayList<Vector2> wepSpawns, GameContext gameContext) {
		this.gameContext = gameContext;
		timerInterval = new Cooldown(interval);

		weaponTiles = new ArrayList<SpawnTile>();
		for (Vector2 points : wepSpawns) {
			SpawnTile temp = new SpawnTile(points.x, points.y);
			weaponTiles.add(temp);
			gameContext.spawn(temp);
		}

	}

	public void update(float delta) {
		timerInterval.update(delta);
		if (!timerInterval.isOnCooldown()) {
			for (SpawnTile tile : weaponTiles) {
				int randomNum = 1 + (int) (Math.random() * 4);
				if (randomNum == 1) {
					//if (!tile.isOccupied()){
						tile.spawnWeapon();
					//}
				}
			}
			timerInterval.startCooldown();
		}
	}

}
