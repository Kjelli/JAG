package no.kash.gamedev.jag.assets;

import java.io.File;
import java.io.FilenameFilter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g3d.particles.influencers.RegionInfluencer.Animated;
import com.badlogic.gdx.utils.Array;

public class Assets {
	public static Texture plain;

	public static Texture man;

	public static Texture bullet;
	public static Texture pistol;
	public static Texture blood;
	public static Texture grenade;

	public static Animation explosion_animation;
	public static Array<TextureRegion> explosion_frames;

	public static BitmapFont font;

	public static void load() {
		plain = load("plain.png");
		man = load("playerSprite/player.png");

		bullet = load("bullet.png");
		pistol = load("playerSprite/pistol.png");
		grenade = load("playerSprite/grenade.png");
		blood = load("blood.png");

		// TODO load animation frames
		explosion_frames = new Array<>(loadMany("playerSprite/grenade_frames", ""));
		explosion_animation = new Animation(40, explosion_frames);
		explosion_animation.setPlayMode(PlayMode.NORMAL);

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

	private static TextureRegion[] loadMany(String foldername, final String prefix) {
		TextureRegion[] tempTextures;

		FileHandle[] handles = Gdx.files.internal(foldername).list(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				if (name.startsWith(prefix)) {
					return true;
				}
				return false;
			}
		});

		tempTextures = new TextureRegion[handles.length];

		for (int i = 0; i < handles.length; i++) {
			tempTextures[i] = new TextureRegion(load(handles[i].name()));
		}

		return tempTextures;

	}
}
