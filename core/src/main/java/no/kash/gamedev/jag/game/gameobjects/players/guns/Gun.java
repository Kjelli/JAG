package no.kash.gamedev.jag.game.gameobjects.players.guns;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.kash.gamedev.jag.commons.graphics.Draw;
import no.kash.gamedev.jag.game.gameobjects.bullets.Bullet;
import no.kash.gamedev.jag.game.gameobjects.players.Player;

public class Gun {

	protected final float cooldown;
	protected final float reloadTime;
	protected float reloadTimer;
	protected float cooldownTimer;
	protected int bulletCount;
	protected int magasineSize;
	protected int maxAmmo;
	protected int ammo;
	protected float damage;

	protected Sprite sprite;

	protected Player player;

	public Gun(GunType type) {
		this.cooldown = type.getCooldown();
		this.maxAmmo = type.getMaxAmmo();
		this.reloadTime = type.getReloadTime();
		this.sprite = new Sprite(type.getGunTexture());
		this.ammo = maxAmmo;
		this.magasineSize = type.getMagazineSize();
		this.bulletCount = magasineSize;
		this.damage = type.getDamage();
	}

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

	public void equip(Player player) {
		this.player = player;
		sprite.setOrigin(player.getWidth() / 2, player.getHeight() / 2);
	}

	public void shoot() {
		if ((bulletCount == -1 || bulletCount > 0) && cooldownTimer == 0 && reloadTimer == 0) {
			Bullet temp = new Bullet(player, player.getBulletOriginX(), player.getBulletOriginY(),
					(float) (player.getRotation() + Math.PI / 2), damage);
			player.getGameContext().spawn(temp);
			if (bulletCount != -1) {
				bulletCount -= 1;
			}
			cooldownTimer = cooldown;
			// TODO Shoot sfx?
		} else {
			// TODO Empty mag sfx?
		}
	}

	public void reload() {
		// If room in magazine, reloadtimer is over, ammo exists or unlimited
		// ammo
		if (bulletCount < magasineSize && reloadTimer == 0 && (ammo > 0 || maxAmmo == -1)) {
			int bulletsLeft = (int) Math.min(magasineSize, ammo);
			bulletCount = bulletsLeft;
			// Update ammo if not unlimited
			if (maxAmmo != -1) {
				setAmmo(ammo - bulletsLeft);
			}
			reloadTimer = reloadTime;
			// TODO Reload sfx?
		} else {
			// TODO Out of ammo sfx?
		}
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

	public void setAmmo(int ammo) {
		this.ammo = ammo;
	}

	public boolean isReloading() {
		return reloadTimer > 0;
	}

	public void draw(SpriteBatch batch) {
		// TODO draw correctly
		Draw.sprite(batch, sprite, player.getX(), player.getY(), player.getWidth(), player.getHeight(),
				player.getRotation());
	}

}
