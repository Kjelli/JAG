package no.kash.gamedev.jag.controller.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.controller.JustAnotherGameController;
import no.kash.gamedev.jag.game.gameobjects.AbstractGameObject;
import no.kash.gamedev.jag.game.gameobjects.players.hud.HealthHud;

public class InGameHud extends AbstractGameObject{
	
	private Sprite weaponImg, healthImg;
	private int ammo, magazineAmmo, health;
	
	private GlyphLayout healthLabel;

	public InGameHud(float x, float y, float width, float height) {
		super(x, y, width, height);
		health = 100;
		ammo = 0;
		magazineAmmo = 0;
		
		healthImg = new Sprite(Assets.health_icon);
		healthImg.setScale(0.5f);
		healthImg.setX(x-healthImg.getWidth());
		healthImg.setY(y);
		
		weaponImg = null;
		
		healthLabel = new GlyphLayout(Assets.font, "" + health);

	}
	
	public void draw(SpriteBatch batch){
		healthImg.draw(batch);
		Assets.font.draw(batch, healthLabel, healthImg.getX() + healthImg.getWidth()*0.5f,
				healthImg.getY() - healthImg.getHeight()*0.5f);
	}



	public void setAmmo(int ammo) {
		this.ammo = ammo;
	}


	public void setMagazineAmmo(int magzineAmmo) {
		this.magazineAmmo = magzineAmmo;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	@Override
	public void update(float delta) {
		
	}
	
	
}
