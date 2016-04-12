package no.kash.gamedev.jag.game.levels;

import com.badlogic.gdx.graphics.g2d.Sprite;

import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.game.commons.utils.Cooldown;
import no.kash.gamedev.jag.game.gameobjects.AbstractGameObject;
import no.kash.gamedev.jag.game.gameobjects.collectables.items.CollectableItem;
import no.kash.gamedev.jag.game.gameobjects.collectables.items.Item;
import no.kash.gamedev.jag.game.gameobjects.collectables.items.ItemType;
import no.kash.gamedev.jag.game.gameobjects.particles.WeaponSpawnEffect;

public class ItemSpawnTile extends AbstractGameObject {

	public static final float WIDTH = 32;
	public static final float HEIGHT = 32;

	public Cooldown reSpawnCooldown;

	private Sprite regular;
	private Sprite common, rare, epic;

	private boolean occupied;

	private Cooldown cooldown;

	private CollectableItem item;
	private boolean preStage;

	public ItemType nextItem;
	public ItemType limitingItemType;

	public ItemSpawner spawner;
	public float spawnRate;

	public ItemSpawnTile(ItemSpawner spawner, ItemSpawnTileInfo info) {
		super(info.pos.x, info.pos.y, WIDTH, HEIGHT);
		this.spawner = spawner;
		this.limitingItemType = info.itemType;
		this.spawnRate = info.spawnRate;
		regular = new Sprite(Assets.itemtile_regular);
		epic = new Sprite(Assets.itemtile_common);
		rare = new Sprite(Assets.itemtile_common);
		common = new Sprite(Assets.itemtile_common);
		setSprite(regular);
		cooldown = new Cooldown(5);
		reSpawnCooldown = new Cooldown(5);
		occupied = false;
		preStage = false;

	}

	@Override
	public void update(float delta) {
		cooldown.update(delta);
		reSpawnCooldown.update(delta);
		if (item != null) {
			if (item.isAlive()) {
				occupied = true;
			} else {
				occupied = false;
			}
		}

		if (preStage == true && cooldown.getCooldownTimer() <= 0) {
			preStage = false;
			occupied = true;
			setSprite(regular);
			spawnItem();
		}
	}

	private void spawnItem() {
		if (spawner.isStopped()) {
			return;
		}
		item = new CollectableItem(getX(), getY(), new Item(nextItem));
		getGameContext().spawn(item);
		getGameContext().spawn(new WeaponSpawnEffect(getCenterX(), getCenterY()));
	}

	public void preSpawnItem() {
		if (limitingItemType == null) {
			nextItem = randomItem();
		}else{
			nextItem = limitingItemType;
		}
		switch (nextItem.getTier()) {
		default:
		case 1:
			setSprite(common);
			break;
		case 2:
			setSprite(rare);
			break;
		case 3:
			setSprite(epic);
			break;
		}
		cooldown.start();
		preStage = true;
	}

	private ItemType randomItem() {
		ItemType next = null;
		double random = Math.random() * spawner.cumProbs;
		double oldTreshold = 0;
		double treshold = 0;
		for (ItemType type : ItemType.values()) {
			treshold += type.getProbability();
			if (random > oldTreshold && random < treshold) {
				next = type;
				break;
			}
			oldTreshold = treshold;
		}
		return next;
	}

	public boolean isOccupied() {
		return occupied;
	}

	public boolean isSpawning() {
		return preStage;
	}

}
