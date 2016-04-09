package no.kash.gamedev.jag.game.gameobjects.players.guns;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.kash.gamedev.jag.commons.graphics.Draw;
import no.kash.gamedev.jag.commons.network.packets.PlayerUpdate;
import no.kash.gamedev.jag.game.JustAnotherGame;
import no.kash.gamedev.jag.game.commons.utils.Cooldown;
import no.kash.gamedev.jag.game.gamecontext.GameContext;
import no.kash.gamedev.jag.game.gameobjects.GameObject;
import no.kash.gamedev.jag.game.gameobjects.bullets.Bullet;
import no.kash.gamedev.jag.game.gameobjects.bullets.Dart;
import no.kash.gamedev.jag.game.gameobjects.bullets.Fire;
import no.kash.gamedev.jag.game.gameobjects.bullets.GoldenBullet;
import no.kash.gamedev.jag.game.gameobjects.bullets.NormalBullet;
import no.kash.gamedev.jag.game.gameobjects.players.Dot;
import no.kash.gamedev.jag.game.gameobjects.players.Player;
import no.kash.gamedev.jag.game.gameobjects.players.PlayerInfo;

public class Gun {

	protected GunType type;

	protected int magazineAmmo;
	protected int magazineSize;
	protected int maxAmmo;
	protected int ammo;
	protected float damage;
	protected float bulletSpeed;

	protected boolean holdToShoot;

	protected double angleOffset;

	protected Sprite sprite;

	protected Player player;

	private Cooldown bulletCooldown;
	private Cooldown reloadCooldown;

	public Gun(GunType type) {
		this.type = type;
		this.maxAmmo = type.getMaxAmmo();
		this.sprite = new Sprite(type.getGunTexture());
		this.ammo = maxAmmo;
		this.magazineSize = type.getMagazineSize();
		this.magazineAmmo = magazineSize;
		this.damage = type.getDamage();
		this.bulletSpeed = type.getBulletSpeed();
		this.angleOffset = type.getAngleOffset();
		this.holdToShoot = type.isHoldToShoot();

		bulletCooldown = new Cooldown(type.getCooldown());
		reloadCooldown = new Cooldown(type.getReloadTime());
	}

	public void update(float delta) {
		bulletCooldown.update(delta);
		reloadCooldown.update(delta);
	}

	public void equip(Player player) {
		this.player = player;
		sprite.setOrigin(player.getWidth() / 2, player.getHeight() / 2);
	}

	public void checkOutOfAmmo() {
		if (ammo == 0 && magazineAmmo == 0) {
			player.equipGun(GunType.pistol);
		}
	}

	public void shoot() {
		checkOutOfAmmo();
		if ((magazineAmmo == -1 || magazineAmmo > 0) && bulletCooldown.getCooldownTimer() == 0
				&& reloadCooldown.getCooldownTimer() == 0) {
			switch (type) {
			default:
				player.getGameContext().spawn(new NormalBullet(player, player.getBulletOriginX(),
						player.getBulletOriginY(), (float) (player.getRotation() + Math.PI / 2), damage, bulletSpeed));
				break;
			case shotgun:
				for (int i = 0; i < 6; i++) {
					player.getGameContext()
							.spawn(new NormalBullet(player, player.getBulletOriginX(), player.getBulletOriginY(),
									(float) (player.getRotation() + Math.PI / 32.0f * (i - 2) + Math.PI / 2), damage,
									bulletSpeed));
				}
				break;
			case mac10:
				float offset = (float) (Math.PI / 4);
				float bullWidth = NormalBullet.WIDTH;
				player.getGameContext()
						.spawn(new NormalBullet(player,
								player.getBulletOriginX()
										+ (float) Math.cos(player.getRotation() + offset + Math.PI / 2) * bullWidth,
								player.getBulletOriginY()
										+ (float) Math.sin(player.getRotation() + offset + Math.PI / 2) * bullWidth,
								(float) (player.getRotation() + Math.PI / 2), damage, bulletSpeed));
				player.getGameContext()
						.spawn(new NormalBullet(player,
								player.getBulletOriginX()
										+ (float) Math.cos(player.getRotation() - offset + Math.PI / 2) * bullWidth,
								player.getBulletOriginY()
										+ (float) Math.sin(player.getRotation() - offset + Math.PI / 2) * bullWidth,
								(float) (player.getRotation() + Math.PI / 2), damage, bulletSpeed));
				break;
			case flamethrower:
				float amountFire = 2 + (int) (Math.random() * 4);
				for (int i = 0; i < amountFire; i++) {
					float randDir = -4 + (int) (Math.random() * 8);
					player.getGameContext().spawn(new Fire(player, player.getBulletOriginX(), player.getBulletOriginY(),
							(float) (player.getRotation() + Math.PI / 32.0f * randDir + Math.PI / 2), bulletSpeed));

				}
				break;
			case crossbow:
				player.getGameContext().spawn(new Dart(player, player.getBulletOriginX(), player.getBulletOriginY(),
						(float) (player.getRotation() + Math.PI / 2), damage, bulletSpeed));
				break;
			case goldengun:
				player.getGameContext().spawn(new GoldenBullet(player, player.getBulletOriginX(),
						player.getBulletOriginY(), (float) (player.getRotation() + Math.PI / 2), damage, bulletSpeed));
				break;
			case awp:
				List<GameObject> playerList = player.getGameContext().getByClass(new Class[]{Player.class});
				for(GameObject obj : playerList){
					if(player.dot.intersects(obj))
					((Player)obj).damage(player.dot);
				}
				
				break;

			}

			if (magazineAmmo != -1) {
				magazineAmmo -= 1;
				((JustAnotherGame) player.getGameContext().getGame()).getServer().send(player.getId(), new PlayerUpdate(
						1, new int[] { PlayerUpdate.AMMO }, new float[][] { { magazineAmmo, magazineSize, ammo } }));
			}
			bulletCooldown.startCooldown();
			// TODO Shoot sfx?
		} else {
			// player.equipGun(GunType.pistol);
			// TODO Empty mag sfx?
		}

	}

	public void reload() {
		// If room in magazine, reloadtimer is over, ammo exists or unlimited
		// ammo
		if (magazineAmmo < magazineSize && reloadCooldown.getCooldownTimer() == 0 && (ammo > 0 || maxAmmo == -1)) {
			// Update ammo if not unlimited
			int newBullets = Math.min(magazineSize - magazineAmmo, ammo);
			if (maxAmmo != -1) {
				setAmmo(ammo - newBullets);

			}
			magazineAmmo = magazineAmmo + newBullets;
			reloadCooldown.startCooldown();

			((JustAnotherGame) player.getGameContext().getGame()).getServer().send(player.getId(), new PlayerUpdate(1,
					new int[] { PlayerUpdate.AMMO }, new float[][] { { magazineAmmo, magazineSize, ammo } }));
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
		return reloadCooldown.getCooldownTimer() > 0;
	}

	public void draw(SpriteBatch batch) {
		// TODO draw correctly
		Draw.sprite(batch, sprite, player.getX(), player.getY(), player.getWidth(), player.getHeight(),
				player.getRotation());
	}

	public double getAngleOffset() {
		return angleOffset;
	}

	public int getMagasineSize() {
		return magazineSize;
	}

	public int getAmmo() {
		return ammo;
	}

	public int getMagasineAmmo() {
		return magazineAmmo;
	}

	public boolean isHoldToShoot() {
		return holdToShoot;
	}

	public float getBulletWidth() {
		float width;
		switch (type) {
		case crossbow:
			width = Dart.WIDTH;
			break;
		case flamethrower:
			width = Fire.WIDTH;
			break;
		case goldengun:
			width = GoldenBullet.WIDTH;
			break;
		default:
			width = NormalBullet.WIDTH;
			break;

		}
		return width;
	}

	public float getBulletHeight() {
		float height;
		switch (type) {
		case crossbow:
			height = Dart.HEIGHT;
			break;
		case flamethrower:
			height = Fire.HEIGHT;
			break;
		case goldengun:
			height = GoldenBullet.HEIGHT;
			break;
		default:
			height = NormalBullet.HEIGHT;
			break;

		}
		return height;
	}

	public GunType getType() {
		return type;
	}

}
