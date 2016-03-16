package no.kash.gamedev.jag.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.Array;

public class Assets {
	public static Texture plain;

	public static Texture man;

	public static Texture bullet;
	public static Texture pistol;
	public static Texture m4;
	public static Texture blood;
	public static Texture grenade;

	

	//Collectibles
	public static Texture m4_ground;

	public static Texture health, health_bg, health_border, health_shine;



	public static Array<TextureRegion> explosion_frames;

	public static BitmapFont font, fontSmall;

	public static void load() {
		plain = load("plain.png");
		man = load("playerSprite/player.png");

		bullet = load("bullet.png");
		pistol = load("playerSprite/pistol.png");
		m4 = load("playerSprite/m4.png");
		grenade = load("sprites/grenade.png");
		blood = load("blood.png");


		//Collectibles
		m4_ground = load("sprites/gun_m4.png");


		health = load("hud/health.png");
		health_bg = load("hud/health_bg.png");
		health_border = load("hud/health_border.png");

		Texture explosionSheet = load("animations/explosion1.png");
		explosion_frames = new Array<>();
		for (int y = 0; y < explosionSheet.getHeight(); y += 128) {
			TextureRegion region = new TextureRegion(explosionSheet, 0, y, 128, 128);
			explosion_frames.add(region);
		}


		FreeTypeFontGenerator fontgen = new FreeTypeFontGenerator(Gdx.files.internal("pixelmix.ttf"));
		FreeTypeFontParameter font20params = new FreeTypeFontParameter();
		font20params.minFilter = Texture.TextureFilter.Nearest;
		font20params.magFilter = Texture.TextureFilter.MipMapLinearNearest;
		font20params.size = 20;
		
		font = fontgen.generateFont(font20params);
		
		FreeTypeFontParameter font12params = new FreeTypeFontParameter();
		font12params.minFilter = Texture.TextureFilter.Nearest;
		font12params.magFilter = Texture.TextureFilter.MipMapLinearNearest;
		font12params.size = 12;
		
		fontSmall = fontgen.generateFont(font12params);
	}

	private static Texture load(String filename) {
		Texture newTexture = new Texture(filename);
		return newTexture;
	}

}
