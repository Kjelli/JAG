package no.kash.gamedev.jag.game.gameobjects.players.guns;

import com.badlogic.gdx.Gdx;
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
		sprite.setOrigin(player.getWidth() / 2, player.getHeight() / 2);
	}

	@Override
	public void shoot() {
		if (magazine.getBulletCount() > 0 && cooldownTimer == 0 && reloadTimer == 0) {
			Bullet temp = new Bullet(player.getLevel(), player, player.getBulletOriginX(), player.getBulletOriginY(),
					(float) (player.getRotation() + Math.PI / 2));
			player.getGameContext().spawn(temp);
			magazine.setBulletCount(magazine.getBulletCount() - 1);
			cooldownTimer = cooldown;
			// TODO Shoot sfx?

		} else {
			// TODO Empty mag sfx?
		}
	}

	@Override
	public void reload() {
		// If room in magazine, reloadtimer is over, ammo exists or unlimited
		// ammo
		if (magazine.getBulletCount() < magazine.getCapacity() && reloadTimer == 0 && (ammo > 0 || maxAmmo == -1)) {
			int bulletsLeft = (int) Math.min(magazine.getCapacity(), ammo);
			magazine.setBulletCount(bulletsLeft);
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

	public boolean isReloading() {
		return reloadTimer > 0;
	}

	public void draw(SpriteBatch batch) {
		// TODO draw correctly
		Draw.sprite(batch, sprite, player.getX(), player.getY(), player.getWidth(), player.getHeight(),
				player.getRotation());
	}

}
