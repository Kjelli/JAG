package no.kash.gamedev.jag.game.gameobjects.players.guns;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.game.gameobjects.bullets.Bullet;

public enum GunType {

	pistol("pistol", 1, Assets.pistol);

	private String name;
	private float cooldown;
	private Texture gunTexture;

	GunType(String name, float cooldown, Texture gunTexture) {
		this.name = name;
		this.cooldown = cooldown;
		this.gunTexture = gunTexture;
	}

}
