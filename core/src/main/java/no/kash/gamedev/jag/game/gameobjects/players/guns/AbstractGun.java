package no.kash.gamedev.jag.game.gameobjects.players.guns;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.kash.gamedev.jag.commons.graphics.Draw;
import no.kash.gamedev.jag.game.gameobjects.bullets.Bullet;
import no.kash.gamedev.jag.game.gameobjects.players.Player;

public abstract class AbstractGun implements Gun {

	protected Magazine magazine;

	protected final float cooldown;
	protected final float reloadTime;
	protected float reloadTimer;
	protected float cooldownTimer;
	protected int maxAmmo;
	protected int ammo;

	protected Sprite sprite;

	protected Player player;

	public AbstractGun(GunType type) {
		this.cooldown = type.getCooldown();
		this.maxAmmo = type.getMaxAmmo();
		this.reloadTime = type.getReloadTime();
		this.sprite = new Sprite(type.getGunTexture());
	}

	protected void setMagazine(Magazine magazine) {
		this.magazine = magazine;
	}

	@Override
	public void update(float delta) {
		if (cooldownTimer > 0) {
			cooldownTimer -= delta;
		} else {
			cooldownTimer = 0;
		}
		if (reloadTimer > 0) {
			reloadTimer -= delta;
		} else {
			reloadTimer = 0;
		}
	}
	
	@Override
	public void equip(Player player) {
		this.player = player;
	}

	@Override
	public void shoot() {
		if (magazine.getBulletCount() > 0 && cooldownTimer == 0) {
			Bullet temp = new Bullet(player.getCenterX(), player.getCenterY(), 4, 4, player.getRotation());
			player.getGameContext().spawn(temp);
			magazine.setBulletCount(magazine.getBulletCount() - 1);
			cooldownTimer = cooldown;
			System.out.println("Pow!");
		}else{
			System.out.println("Couldn't shoot!");
			System.out.println("CD: "+cooldownTimer+", " + "MAG "+ magazine.getBulletCount());
		}
	}

	@Override
	public void reload() {
		if (magazine.getBulletCount() < magazine.getCapacity() && reloadTimer == 0 && ammo > 0) {
			int bulletsLeft = (int) Math.min(magazine.getCapacity(), ammo);
			magazine.setBulletCount(bulletsLeft);
			setAmmo(ammo - bulletsLeft);
			reloadTimer = reloadTime;
		}
	}

	@Override
	public Magazine getMagazine() {
		return magazine;
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

	public void setAmmo(int ammo) {
		this.ammo = ammo;
	}

	public void draw(SpriteBatch batch) {
		// TODO draw correctly
		Draw.sprite(batch, sprite, player.getCenterX(), player.getCenterY(), 5, 5, player.getRotation());
	}

}
