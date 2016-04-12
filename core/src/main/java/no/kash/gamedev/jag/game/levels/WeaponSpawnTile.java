package no.kash.gamedev.jag.game.levels;

import com.badlogic.gdx.graphics.g2d.Sprite;

import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.game.commons.utils.Cooldown;
import no.kash.gamedev.jag.game.gameobjects.AbstractGameObject;
import no.kash.gamedev.jag.game.gameobjects.collectables.weapons.Weapon;
import no.kash.gamedev.jag.game.gameobjects.particles.WeaponSpawnEffect;
import no.kash.gamedev.jag.game.gameobjects.players.guns.GunType;

public class WeaponSpawnTile extends AbstractGameObject {

	public static final float WIDTH = 32;
	public static final float HEIGHT = 32;

	public Cooldown reSpawnCooldown;

	private Sprite regular;
	private Sprite common, rare, epic;

	private boolean occupied;

	private Cooldown cooldown;

	private Weapon weapon;
	private boolean preStage;

	public GunType nextWeapon;
	public GunType limitingGunType;

	public WeaponSpawner spawner;
	public float spawnRate;

	public WeaponSpawnTile(WeaponSpawner spawner, WeaponSpawnTileInfo info) {
		super(info.pos.x, info.pos.y, WIDTH, HEIGHT);
		this.spawner = spawner;
		this.limitingGunType = info.gunType;
		this.spawnRate = info.spawnRate;
		regular = new Sprite(Assets.spawntile_regular);
		epic = new Sprite(Assets.spawntile_epic);
		rare = new Sprite(Assets.spawntile_rare);
		common = new Sprite(Assets.spawntile_common);
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
		if (weapon != null) {
			if (weapon.isAlive()) {
				occupied = true;
			} else {
				occupied = false;
			}
		}

		if (preStage == true && cooldown.getCooldownTimer() <= 0) {
			preStage = false;
			occupied = true;
			setSprite(regular);
			spawnWeapon();
		}
	}

	private void spawnWeapon() {
		if (spawner.isStopped()) {
			return;
		}
		weapon = new Weapon(getCenterX(), getCenterY(), nextWeapon);
		getGameContext().spawn(weapon);
		getGameContext().spawn(new WeaponSpawnEffect(getCenterX(), getCenterY()));
	}

	public void preSpawnWeapon() {
		if (limitingGunType == null) {
			nextWeapon = randomGun();
		}else{
			nextWeapon = limitingGunType;
		}
		switch (nextWeapon.getTier()) {
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

	private GunType randomGun() {
		GunType next = null;
		double random = Math.random() * spawner.cumProbs;
		double oldTreshold = 0;
		double treshold = 0;
		for (GunType type : GunType.values()) {
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
