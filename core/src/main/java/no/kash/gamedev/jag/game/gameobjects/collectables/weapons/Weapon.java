package no.kash.gamedev.jag.game.gameobjects.collectables.weapons;

import com.badlogic.gdx.graphics.g2d.Sprite;

import no.kash.gamedev.jag.game.gamecontext.physics.Collidable;
import no.kash.gamedev.jag.game.gamecontext.physics.Collision;
import no.kash.gamedev.jag.game.gameobjects.AbstractGameObject;
import no.kash.gamedev.jag.game.gameobjects.collectables.AbstractCollectable;
import no.kash.gamedev.jag.game.gameobjects.collectables.Collectable;
import no.kash.gamedev.jag.game.gameobjects.players.Player;
import no.kash.gamedev.jag.game.gameobjects.players.guns.Gun;
import no.kash.gamedev.jag.game.gameobjects.players.guns.GunType;

public class Weapon extends AbstractCollectable {

	public GunType gun;
	public int ammo;
	public int mag;

	float startingX, startingY;

	public Weapon(float x, float y, GunType gun) {
		this(x, y, gun, gun.getMaxAmmo(), gun.getMagazineSize());
	}

	public Weapon(float x, float y, GunType gun, int ammo, int mag) {
		super(x, y, 32, 32);
		this.gun = gun;
		this.ammo = ammo;
		this.mag = mag;

		Sprite sprite = new Sprite(gun.getOnGroundTexture());
		setSprite(sprite);

		startingX = x;
		startingY = y;
	}

	@Override
	public void onSpawn() {
		getGameContext().bringToFront(this);
	}

	@Override
	public boolean canCollect(Player player) {
		return true;
	}

	@Override
	public void collect(Player player) {
		super.collect(player);
		player.equipWeapon(this);
		destroy();
	}

	@Override
	public void update(float delta) {
		setScale((float) (Math.sin(getGameContext().getElapsedTime() * 5.0f)) * 0.1f + 1.0f);
		setX(startingX - getWidth() / 2);
		setY(startingY - getHeight() / 2);
	}

}
