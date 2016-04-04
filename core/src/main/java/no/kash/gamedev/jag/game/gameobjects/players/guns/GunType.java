package no.kash.gamedev.jag.game.gameobjects.players.guns;

import com.badlogic.gdx.graphics.Texture;

import no.kash.gamedev.jag.assets.Assets;

public enum GunType {

	pistol("pistol", 0.4f, 0.5f, Assets.pistol, -1, -1, Assets.pistol_ground, 15f, 300f, 0f),
	m4("m4", 0.05f, 0.1f, Assets.m4, 60, 30,Assets.m4_ground, 10f, 460f, -Math.PI / 18),
	shotgun("shotgun", 0.7f, 0.1f, Assets.shotgun, 16, 8,Assets.shotgun_ground, 10f, 400f, -Math.PI / 18),
	goldengun("goldengun", 0.7f, 0.5f, Assets.goldengun, 3, 3, Assets.goldengun_ground, 1400f, 800f, 0f),
	mac10("m4", 0.1f, 0.1f, Assets.mac10, 60, 60,Assets.mac10_ground, 8f, 400f, 0),;
	

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

	GunType(String name, float cooldown, float reloadTime, Texture gunTexture, int maxAmmo, int magazineSize,
			Texture onGroundTexture, float damage, float bulletSpeed, double angleOffset) {
		this.name = name;
		this.cooldown = cooldown;
		this.gunTexture = gunTexture;
		this.maxAmmo = maxAmmo;
		this.reloadTime = reloadTime;
		this.onGroundTexture = onGroundTexture;
		this.magazineSize = magazineSize;
		this.damage = damage;
		this.bulletSpeed = bulletSpeed;
		this.angleOffset = angleOffset;
	}

	public int getMaxAmmo() {
		return maxAmmo;
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

}
