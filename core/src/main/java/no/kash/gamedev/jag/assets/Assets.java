package no.kash.gamedev.jag.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Assets {
	public static Texture plain;
	
	public static Texture man;
	public static Texture bullet;
	public static Texture pistol;
	
	public static BitmapFont font;

	public static void load() {
		plain = load("plain.png");
		man = load("playerSprite/player.png");
		bullet = load("bullet.png");
		pistol = load("playerSprite/pistol.png");
		
		
		font = new BitmapFont(Gdx.files.internal("utf-font.fnt"));
		font.getData().scaleX = 0.5f;
		font.getData().scaleY = 0.5f;
	}

	private static Texture load(String filename) {
		Texture newTexture = new Texture(filename);
		return newTexture;
	}
}
