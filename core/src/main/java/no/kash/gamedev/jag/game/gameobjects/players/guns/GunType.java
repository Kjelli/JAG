package no.kash.gamedev.jag.game.gameobjects.players.guns;

import com.badlogic.gdx.graphics.Texture;

import no.kash.gamedev.jag.assets.Assets;

public enum GunType {

	pistol("pistol", 1, Assets.pistol, 100), mac10("mac10", 0.3f, Assets.pistol, 200);

	private String name;
	private float cooldown;
	private Texture gunTexture;
	private float maxAmmo;

	GunType(String name, float cooldown, Texture gunTexture, int maxAmmo) {

		this.name = name;
		this.cooldown = cooldown;
		this.gunTexture = gunTexture;

		this.maxAmmo = maxAmmo;
	}

}
