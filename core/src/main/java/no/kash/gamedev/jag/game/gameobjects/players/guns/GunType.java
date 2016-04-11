package no.kash.gamedev.jag.game.gameobjects.players.guns;

import com.badlogic.gdx.graphics.Texture;

import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.game.gameobjects.bullets.Bullet;

public enum GunType {

	pistol("Default", 0, 0.4f, -1, Assets.pistol, -1, -1, Assets.pistol_ground, 15f, 300f, 0f, 0f,true),
	m4("M4", 1, 0.08f, 1.5f, Assets.m4, 60, 30,Assets.m4_ground, 8f, 460f, -Math.PI / 18, 0.2f,true),
	shotgun("Shotgun", 1, 0.7f, 1.3f, Assets.shotgun, 16, 8,Assets.shotgun_ground, 10f, 400f, -Math.PI / 18, 0.2f,true),
	goldengun("Golden gun", 3, 0.7f, 1.6f, Assets.goldengun, 3, 3, Assets.goldengun_ground, 1400f, 800f, 0f, 0.02f,false),
	mac10("Mac10", 1, 0.1f, 1.7f, Assets.mac10, 60, 60,Assets.mac10_ground, 6f, 400f, 0, 0.2f,true),
	flamethrower("Flamethrower", 2, 0.005f, 1f, Assets.flamethrower, 0, 200,Assets.flamethrower_ground, 10f, 120f, -Math.PI / 18, 0.1f,true),
	crossbow("Crossbow", 2, 0.5f, 1.8f, Assets.crossbow, 0, 8, Assets.crossbow_ground, 20f, 600f,  -Math.PI / 18, 0.1f,false),
	awp("AWP", 2, 1f, 1.5f, Assets.awp, 10, 5,Assets.awp_ground, 40f, 0, -Math.PI / 18, 0.1f,false);



	private String name;
	private float cooldown;
	private int maxAmmo;
	private int magazineSize;
	private float reloadTime;
	private float damage;
	private Texture gunTexture;
	private Texture onGroundTexture;
	private float bulletSpeed;
	private double angleOffset;
	
	private float probability;
	private int tier;
	
	private boolean holdToShoot;

	GunType(String name, int tier, float cooldown, float reloadTime, Texture gunTexture, int maxAmmo, int magazineSize,
			Texture onGroundTexture, float damage, float bulletSpeed, double angleOffset, float probability, boolean holdToShoot) {
		this.name = name;
		this.tier = tier;
		this.cooldown = cooldown;
		this.gunTexture = gunTexture;
		this.maxAmmo = maxAmmo;
		this.reloadTime = reloadTime;
		this.onGroundTexture = onGroundTexture;
		this.magazineSize = magazineSize;
		this.damage = damage;
		this.bulletSpeed = bulletSpeed;
		this.angleOffset = angleOffset;
		this.probability = probability;
		this.holdToShoot = holdToShoot;
	}

	public int getMaxAmmo() {
		return maxAmmo;
	}
	
	public int getTier() {
		return tier;
	}
	
	public int getMagazineSize() {
		return magazineSize;
	}

	public float getCooldown() {
		return cooldown;
	}

	public String getName() {
		return name;
	}

	public Texture getGunTexture() {
		return gunTexture;
	}

	public float getReloadTime() {
		return reloadTime;
	}

	public Texture getOnGroundTexture() {
		return onGroundTexture;
	}

	public float getDamage() {
		return damage;
	}

	public float getBulletSpeed() {
		return bulletSpeed;
	}

	public double getAngleOffset() {
		return angleOffset;
	}

	public float getProbability() {
		return probability;
	}

	public boolean isHoldToShoot() {
		return holdToShoot;
	}
	
	@Override
	public String toString() {
		return name;
	}


	
}
