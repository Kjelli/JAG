package no.kash.gamedev.jag.game.levels;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

import no.kash.gamedev.jag.game.commons.utils.Cooldown;
import no.kash.gamedev.jag.game.gamecontext.GameContext;
import no.kash.gamedev.jag.game.gameobjects.GameObject;
import no.kash.gamedev.jag.game.gameobjects.collectables.items.Item;
import no.kash.gamedev.jag.game.gameobjects.collectables.items.ItemType;
import no.kash.gamedev.jag.game.gameobjects.collectables.weapons.Weapon;
import no.kash.gamedev.jag.game.gameobjects.players.guns.GunType;

public class ItemSpawner {

	public static final float SPAWN_RATE = 0.05f;
	private GameContext gameContext;
	private ArrayList<ItemSpawnTile> itemTiles;
	private ArrayList<ItemSpawnTileInfo> itemSpawns;

	private Cooldown timerInterval;
	private float interval = 3;
	private boolean stopped;

	public final float cumProbs;

	public ItemSpawner(ArrayList<ItemSpawnTileInfo> tempList, GameContext gameContext) {
		this.gameContext = gameContext;
		this.itemSpawns = tempList;
		timerInterval = new Cooldown(interval);
		
		itemTiles = new ArrayList<ItemSpawnTile>();

		float realProbs = 0;
		for (ItemType type : ItemType.values()) {
			realProbs += type.getProbability();
		}
		cumProbs = realProbs;

	}

	public void spawnItemTiles() {
		for (ItemSpawnTileInfo info : itemSpawns) {
			System.out.println("GREETINGS");
			ItemSpawnTile temp = new ItemSpawnTile(this, info);
			itemTiles.add(temp);
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
			for (ItemSpawnTile tile : itemTiles) {
				float random = (float) Math.random();
				if (random <= tile.spawnRate) {
					if (!tile.isOccupied() && !tile.reSpawnCooldown.isOnCooldown()) {
						tile.preSpawnItem();
						tile.reSpawnCooldown.startCooldown();
					}
					timerInterval.startCooldown();
				}
			}
		}
	}

	public void start() {
		stopped = false;
	}

	public void stop() {
		stopped = true;
		for (GameObject o : gameContext.getByClass(new Class[] { Item.class })) {
			o.destroy();
		}
	}

	public boolean isStopped() {
		return stopped;
	}
}
