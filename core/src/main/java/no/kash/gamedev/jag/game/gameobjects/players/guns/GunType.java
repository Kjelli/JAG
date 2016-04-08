package no.kash.gamedev.jag.game.gameobjects.players.guns;

import com.badlogic.gdx.graphics.Texture;

import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.game.gameobjects.bullets.Bullet;

public enum GunType {

	pistol("pistol", 0, 0.4f, -1, Assets.pistol, -1, -1, Assets.pistol_ground, 15f, 300f, 0f, 0f,true),
	m4("m4", 1, 0.08f, 0.7f, Assets.m4, 60, 30,Assets.m4_ground, 10f, 460f, -Math.PI / 18, 0.2f,true),
	shotgun("shotgun", 1, 0.7f, 0.6f, Assets.shotgun, 16, 8,Assets.shotgun_ground, 10f, 400f, -Math.PI / 18, 0.2f,true),
	goldengun("goldengun", 3, 0.7f, 1.2f, Assets.goldengun, 3, 3, Assets.goldengun_ground, 1400f, 800f, 0f, 0.02f,false),
	mac10("mac10", 1, 0.1f, 0.7f, Assets.mac10, 60, 60,Assets.mac10_ground, 8f, 400f, 0, 0.2f,true),
	flamethrower("flamethrower", 2, 0.005f, 0.7f, Assets.flamethrower, 0, 200,Assets.flamethrower_ground, 10f, 100f, -Math.PI / 18, 0.1f,true),
	crossbow("goldengun", 2, 0.5f, 1.2f, Assets.crossbow, 0, 8, Assets.crossbow_ground, 20f, 800f,  -Math.PI / 18, 0.1f,false),;

	
	public static GunType random(){
		return pistol;
	}

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


	
}
