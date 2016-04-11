package no.kash.gamedev.jag.game.levels;

import com.badlogic.gdx.graphics.g2d.Sprite;

import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.game.commons.utils.Cooldown;
import no.kash.gamedev.jag.game.gameobjects.collectables.weapons.Weapon;
import no.kash.gamedev.jag.game.gameobjects.players.guns.GunType;

public class WeaponSpawnTile extends AbstractSpawnTile{

	public GunType nextWeapon;
	public GunType limitingGunType;
	
	private Weapon weapon;
	
	public WeaponSpawnTile(WeaponSpawner spawner, WeaponSpawnTileInfo info) {
		super(spawner, info);
		this.limitingGunType = info.gunType;
		this.spawnRate = info.spawnRate;
		regular = new Sprite(Assets.spawntile_regular);
		epic = new Sprite(Assets.spawntile_epic);
		rare = new Sprite(Assets.spawntile_rare);
		common = new Sprite(Assets.spawntile_common);
		setSprite(regular);
		cooldown = new Cooldown(3);
	}

	@Override
	public void spawnObject() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void randomObject() {
		// TODO Auto-generated method stub
		
	}

}
