package no.kash.gamedev.jag.game.gameobjects.players.guns;

import com.badlogic.gdx.graphics.Texture;

import no.kash.gamedev.jag.assets.Assets;

public enum GunType {

	pistol("pistol", 0.1f, 0.5f, Assets.pistol, -1, -1, 10.0f), mac10("mac10", 0.3f, 0.7f, Assets.pistol, 200, 30,
			15.0f);

	private String name;
	private float cooldown;
	private int maxAmmo;
	private int magazineSize;
	private float reloadTime;
	private float damage;
	private Texture gunTexture;

	GunType(String name, float cooldown, float reloadTime, Texture gunTexture, int maxAmmo, int magazineSize,
			float damage) {
		this.name = name;
		this.cooldown = cooldown;
		this.gunTexture = gunTexture;
		this.maxAmmo = maxAmmo;
		this.reloadTime = reloadTime;
		this.magazineSize = magazineSize;
		this.damage = damage;
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

	public float getDamage() {
		return damage;
	}
}
