package no.kash.gamedev.jag.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class Assets {
	public static Texture plain;

	public static Texture man;
	public static Texture bullet;
	public static Texture pistol;
	public static Texture blood;

	public static BitmapFont font;

	public static void load() {
		plain = load("plain.png");
		man = load("playerSprite/player.png");
		bullet = load("bullet.png");
		pistol = load("playerSprite/pistol.png");
		blood = load("blood.png");
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
