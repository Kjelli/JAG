package no.kash.gamedev.jag.game.gameobjects.players.guns;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.game.gameobjects.bullets.Bullet;

public enum GunTypes {
	
	pistol("pistol",1,Assets.pistol);
	
	private String name;
	private float cooldown;
	private Texture gunSprite;
	
	GunTypes(String name, float cooldown, Texture gunSprite){
		this.name = name;
		this.cooldown = cooldown;
		this.gunSprite = gunSprite;
	}
	
}
