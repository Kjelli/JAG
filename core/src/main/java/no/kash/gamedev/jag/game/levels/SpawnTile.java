package no.kash.gamedev.jag.game.levels;

import com.badlogic.gdx.graphics.g2d.Sprite;

import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.game.gamecontext.GameContext;
import no.kash.gamedev.jag.game.gamecontext.functions.Cooldown;
import no.kash.gamedev.jag.game.gameobjects.AbstractGameObject;
import no.kash.gamedev.jag.game.gameobjects.collectables.weapons.Weapon;
import no.kash.gamedev.jag.game.gameobjects.particles.WeaponSpawnEffect;
import no.kash.gamedev.jag.game.gameobjects.players.guns.GunType;

public class SpawnTile extends AbstractGameObject {

	public Cooldown reSpawnCooldown;
	
	private Sprite regular;
	private Sprite pre;
	private Sprite golden;

	private boolean occupied;

	private Cooldown cooldown;
	
	
	private Weapon weapon;
	int goldenGunDrop;
	int dropChancegolden = 15;
	private boolean preStage;

	public SpawnTile(float x, float y) {
		super(x, y, 32, 32);
		cooldown = new Cooldown(3);
		reSpawnCooldown = new Cooldown(8);;
		occupied = false;
		preStage = false;

		regular = new Sprite(Assets.spawntile_regular);
		setSprite(regular);
		golden = new Sprite(Assets.spawntile_golden);
		pre = new Sprite(Assets.spawntile_pre);
	}

	@Override
	public void update(float delta) {
		cooldown.update(delta);
		reSpawnCooldown.update(delta);
		if (weapon != null) {
			if(weapon.isAlive()){
				occupied = true;
			}else{
				occupied = false;
			}
		}
		

		if (preStage == true && cooldown.getCooldownTimer() <= 0) {
			preStage = false;
			occupied = true;
			setSprite(regular);
			if (goldenGunDrop == 1) {
				createGoldenGun();
			} else {
				createRegularWeapon();
			}
		}
	}

	private void createWeapon(GunType type) {
		weapon = new Weapon(getX(), getY(), type);
		getGameContext().spawn(weapon);
		//getGameContext().spawn(new WeaponSpawnEffect(getX(),getY(),64,64,2));
	}

	private void createGoldenGun() {
		createWeapon(GunType.goldengun);
	}

	private void createRegularWeapon() {
		createWeapon(randomGun());
	}

	public void spawnWeapon() {
		goldenGunDrop = 1 + (int) (Math.random() * dropChancegolden);
		if (goldenGunDrop == 1) {
			setSprite(golden);
		} else {
			setSprite(pre);
		}
		cooldown.startCooldown();
		preStage = true;
	}

	private GunType randomGun() {
		int randomNum = 1 + (int) (Math.random() * 2);
		switch (randomNum) {
		case 1:
			return GunType.m4;
		case 2:
			return GunType.shotgun;
		default:
			return GunType.m4;
		}
	}

	public boolean isOccupied() {
		return occupied;
	}

}
