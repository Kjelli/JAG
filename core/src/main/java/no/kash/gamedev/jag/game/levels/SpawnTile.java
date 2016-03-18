package no.kash.gamedev.jag.game.levels;

import com.badlogic.gdx.graphics.g2d.Sprite;

import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.game.gamecontext.GameContext;
import no.kash.gamedev.jag.game.gamecontext.functions.Cooldown;
import no.kash.gamedev.jag.game.gameobjects.AbstractGameObject;
import no.kash.gamedev.jag.game.gameobjects.collectables.weapons.Weapon;
import no.kash.gamedev.jag.game.gameobjects.players.guns.GunType;

public class SpawnTile extends AbstractGameObject {

	private Sprite regular;
	private Sprite pre;

	private boolean occupied;

	private Cooldown cooldown;
	
	private Weapon weapon;
	
	private boolean preStage;

	public SpawnTile(float x, float y) {
		super(x, y, 32, 32);
		cooldown = new Cooldown(3);
		occupied = false;
		preStage = false; 

		regular = new Sprite(Assets.spawntile_regular);
		setSprite(regular);

		pre = new Sprite(Assets.spawntile_pre);
	}

	@Override
	public void update(float delta) {
		cooldown.update(delta);
		
		/*
		if(!weapon.isAlive()){
			occupied = true;
		}
		*/
		
		if(preStage == true && cooldown.getCooldownTimer() <= 0){
			preStage = false;
			occupied = true;
			setSprite(regular);
			createWeapon();
		}
	}
	
	
	private void createWeapon() {
		weapon = new Weapon(getX(), getY(), randomGun());
		getGameContext().spawn(weapon);
	}

	public void spawnWeapon() {
		setSprite(pre);
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
