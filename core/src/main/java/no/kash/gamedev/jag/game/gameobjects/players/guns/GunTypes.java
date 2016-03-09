package no.kash.gamedev.jag.game.gameobjects.players.guns;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.game.gameobjects.bullets.Bullet;

public enum GunTypes {
	
	pistol("pistol",1,Assets.pistol), 
	mac10("mac10",0.3f,Assets.pistol,20);
	
	private String name;
	private float cooldown;
	private Texture gunSprite;
	private int maxAmmo;
	
	GunTypes(String name, float cooldown, Texture gunSprite){
		this.name = name;
		this.cooldown = cooldown;
		this.gunSprite = gunSprite;
	}
	
	GunTypes(String name, float cooldown, Texture gunSprite, int maxAmmo){
		this.name = name;
		this.cooldown = cooldown;
		this.gunSprite = gunSprite;
		this.maxAmmo = maxAmmo;
	}
	
}
