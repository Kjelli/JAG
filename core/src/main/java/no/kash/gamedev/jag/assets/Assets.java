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

	// Player
	public static Texture man, man_holding_grenade;

	// Weapons
	public static Texture bullet;
	public static Texture pistol;
	public static Texture m4;
	public static Texture shotgun;
	public static Texture goldengun;
	public static Texture grenade;

	// Particles
	public static Texture blood;
	public static Texture star;
	public static Texture confetti;
	public static Texture wepSpawnEffect;
	public static Array<TextureRegion> explosion_frames;

	// Collectibles
	public static Texture m4_ground;
	public static Texture pistol_ground;
	public static Texture shotgun_ground;
	public static Texture goldengun_ground;

	// Icons
	public static Texture health_icon;
	public static Texture icon_border;

	// MapTiles
	public static Texture spawntile_regular;
	public static Texture spawntile_pre;
	public static Texture spawntile_golden;

	// Hud
	public static Texture health, health_lost, health_bg, health_border, health_shine;
	public static Texture card_long_border, card_long_border_ready, card_long_bg, card_square_border, card_square_bg,
			card_square_border_selected;

	// Fonts
	public static BitmapFont font, fontSmall, fontLarge;

	public static void load() {
		plain = load("plain.png");
		man = load("playerSprite/player.png");
		man_holding_grenade = load("playerSprite/holding_grenade.png");

		// Weapons
		bullet = load("bullet.png");
		pistol = load("playerSprite/pistol.png");
		m4 = load("playerSprite/m4.png");
		shotgun = load("playerSprite/shotgun.png");
		goldengun = load("playerSprite/goldengun.png");
		grenade = load("sprites/grenade.png");

		// Particles
		blood = load("blood.png");
		wepSpawnEffect = load("sprites/spawnEffect.png");
		star = load("sprites/star.png");
		confetti = load("sprites/confetti.png");
		
		// Collectibles
		m4_ground = load("sprites/gun_m4.png");
		pistol_ground = load("sprites/gun_pistol.png");
		shotgun_ground = load("sprites/gun_shotgun.png");
		goldengun_ground = load("sprites/gun_goldengun.png");

		// Icons
		icon_border = load("sprites/health_icon.png");
		health_icon = load("sprites/health_icon.png");

		// MapTiles
		spawntile_regular = load("maps/sprites/spawner_regular.png");
		spawntile_pre = load("maps/sprites/spawner_pre.png");
		spawntile_golden = load("maps/sprites/spawner_golden.png");

		// Hud
		health_icon = load("sprites/health_icon.png");
		health = load("hud/health.png");
		health_lost = load("hud/health_lost.png");
		health_bg = load("hud/health_bg.png");
		health_border = load("hud/health_border.png");

		card_long_bg = load("sprites/card_long_bg.png");
		card_long_border = load("sprites/card_long_border.png");
		card_long_border_ready = load("sprites/card_long_border_ready.png");

		card_square_bg = load("sprites/card_square_bg.png");
		card_square_border = load("sprites/card_square_border.png");
		card_square_border_selected = load("sprites/card_square_border_selected.png");
		// Animations

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

		FreeTypeFontParameter font62params = new FreeTypeFontParameter();
		font62params.minFilter = Texture.TextureFilter.Nearest;
		font62params.magFilter = Texture.TextureFilter.MipMapLinearNearest;
		font62params.size = 62;

		fontLarge = fontgen.generateFont(font62params);
	}

	private static Texture load(String filename) {
		Texture newTexture = new Texture(filename);
		return newTexture;
	}

}
