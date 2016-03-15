package no.kash.gamedev.jag.game.gameobjects.collectables.weapons;

import com.badlogic.gdx.graphics.g2d.Sprite;

import no.kash.gamedev.jag.game.gamecontext.physics.Collidable;
import no.kash.gamedev.jag.game.gamecontext.physics.Collision;
import no.kash.gamedev.jag.game.gameobjects.AbstractGameObject;
import no.kash.gamedev.jag.game.gameobjects.collectables.Collectable;
import no.kash.gamedev.jag.game.gameobjects.players.Player;
import no.kash.gamedev.jag.game.gameobjects.players.guns.GunType;

public class Weapon extends AbstractGameObject implements Collectable, Collidable {
	
	public GunType gun;
	private int ammo;
	
	public Weapon(float x, float y, GunType gun) {
		super(x, y, 64, 64);
		this.gun = gun;
		
		Sprite sprite = new Sprite(gun.getOnGroundTexture());
		setSprite(sprite);
		
		determineAmmo();
	}

	private void determineAmmo() {
		int randomNum = 0 + (int)(Math.random() * 6);
		ammo = gun.getMaxAmmo() * (1-randomNum);
	}

	@Override
	public boolean canCollect(Player player) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void collect(Player player) {
		player.equipWeapon(this);
		destroy();
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCollide(Collision collision) {
		if(collision.getTarget() instanceof Player){
			collect((Player)collision.getTarget());
		}
	}

}
