package no.kash.gamedev.jag.game.gameobjects.players.guns;

import com.badlogic.gdx.graphics.Texture;

import no.kash.gamedev.jag.assets.Assets;

public enum GunType {

	pistol("pistol", 0.1f, 0.5f, Assets.pistol), mac10("mac10", 0.3f, 0.7f, Assets.pistol, 200);

	private String name;
	private float cooldown;
	private int maxAmmo;
	private float reloadTime;
	private Texture gunTexture;

	GunType(String name, float cooldown, float reloadTime, Texture gunTexture, int maxAmmo) {
		this.name = name;
		this.cooldown = cooldown;
		this.gunTexture = gunTexture;
		this.maxAmmo = maxAmmo;
		this.reloadTime = reloadTime;
	}
	
	GunType(String name, float cooldown, float reloadTime, Texture gunTexture) {
		this(name,cooldown,reloadTime,gunTexture, -1);
	}


	public int getMaxAmmo() {
		return maxAmmo;
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
}
