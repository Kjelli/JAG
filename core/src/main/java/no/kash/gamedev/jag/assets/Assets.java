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
	public static Texture blood;
	public static Texture grenade;

	public static Texture health, health_bg, health_border, health_shine;

	public static Array<TextureRegion> explosion_frames;

	public static BitmapFont font;

	public static void load() {
		plain = load("plain.png");
		man = load("playerSprite/player.png");

		bullet = load("bullet.png");
		pistol = load("playerSprite/pistol.png");
		grenade = load("sprites/grenade.png");
		blood = load("blood.png");

		health = load("hud/health.png");
		health_bg = load("hud/health_bg.png");
		health_border = load("hud/health_border.png");
		health_shine = load("hud/health_shine.png");

		Texture explosionSheet = load("animations/explosion1.png");
		explosion_frames = new Array<>();
		for (int y = 0; y < explosionSheet.getHeight(); y += 128) {
			TextureRegion region = new TextureRegion(explosionSheet, 0, y, 128, 128);
			explosion_frames.add(region);
		}

		FreeTypeFontGenerator font10gen = new FreeTypeFontGenerator(Gdx.files.internal("pixelmix.ttf"));
		FreeTypeFontParameter font10params = new FreeTypeFontParameter();
		font10params.minFilter = Texture.TextureFilter.Nearest;
		font10params.magFilter = Texture.TextureFilter.MipMapLinearNearest;
		font10params.size = 20;
		font = font10gen.generateFont(font10params);
	}

	private static Texture load(String filename) {
		Texture newTexture = new Texture(filename);
		return newTexture;
	}

}
