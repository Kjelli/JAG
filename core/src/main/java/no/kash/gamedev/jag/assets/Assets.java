package no.kash.gamedev.jag.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.Array;

public class Assets {
	public static Texture plain;
	public static Texture laserSight;

	// Player
	public static Texture man, man_holding_grenade, man_holding_snakebite, man_holding_tripwire;

	// Projectiles
	public static Texture bullet;
	public static Texture dart;
	public static Texture golden_bullet;
	public static Texture spike;

	// Weapons
	public static Texture pistol;
	public static Texture m4;
	public static Texture mac10;
	public static Texture shotgun;
	public static Texture goldengun;
	public static Texture flamethrower;
	public static Texture crossbow;
	public static Texture awp;

	public static Texture grenade;
	public static Texture snakebite;

	// Items
	public static Texture tripmine_ground, tripmine_placed;

	// Particles
	public static Texture blood;
	public static Texture blood2;
	public static Texture blood3;
	public static Texture fire;
	public static Texture poison;
	public static Texture star;
	public static Texture healing;
	public static Texture confetti;
	public static Texture wepSpawnEffect;
	public static Array<TextureRegion> explosion_frames;

	// Collectibles
	public static Texture m4_ground;
	public static Texture pistol_ground;
	public static Texture shotgun_ground;
	public static Texture goldengun_ground;
	public static Texture mac10_ground;
	public static Texture flamethrower_ground;
	public static Texture crossbow_ground;
	public static Texture awp_ground;

	// Items
	public static Texture healthpack;

	// Icons
	public static Texture health_icon;
	public static Texture icon_border;
	public static Texture tied_for_lead_star;
	public static Texture lead_star;

	// MapTiles
	public static Texture spawntile_regular;
	public static Texture spawntile_common;
	public static Texture spawntile_rare;
	public static Texture spawntile_epic;

	public static Texture itemtile_regular;
	public static Texture itemtile_common;

	// Hud
	public static Texture health, health_lost, health_bg, health_border, health_shine;
	public static Texture card_long_border, card_long_border_ready, card_long_bg, card_square_border, card_square_bg,
			card_square_border_selected, card_xl_border, card_xl_bg;
	public static Texture kills, deaths;
	public static Texture kdbg;

	// Fonts
	public static BitmapFont font, fontSmall, fontLarge;
	public static BitmapFont announcerFont;

	public static void load() {
		plain = load("plain.png");
		man = load("playerSprite/player.png");
		man_holding_grenade = load("playerSprite/holding_grenade.png");
		man_holding_snakebite = load("playerSprite/holding_snakebite.png");
		man_holding_tripwire = load("playerSprite/holding_tripwire.png");
		laserSight = load("dot.png");

		// Projectiles
		bullet = load("bullet.png");
		golden_bullet = load("goldenbullet.png");
		dart = load("dart.png");
		spike = load("spike.png");

		// Weapons
		pistol = load("playerSprite/pistol.png");
		m4 = load("playerSprite/m4.png");
		shotgun = load("playerSprite/shotgun.png");
		goldengun = load("playerSprite/goldengun.png");
		mac10 = load("playerSprite/mac10.png");
		flamethrower = load("playerSprite/flamethrower.png");
		crossbow = load("playerSprite/crossbow.png");
		awp = load("playerSprite/awp.png");

		// Items
		grenade = load("sprites/grenade.png");
		tripmine_ground = load("sprites/tripmine_ground.png");
		tripmine_placed = load("sprites/tripmine_placed.png");

		// Particles
		blood = load("blood.png");
		blood2 = load("blood2.png");
		blood3 = load("blood3.png");
		fire = load("fire.png");
		poison = load("poison.png");
		healing = load("heal.png");
		wepSpawnEffect = load("sprites/spawnEffect.png");
		star = load("sprites/star.png");
		confetti = load("sprites/confetti.png");

		// Collectibles
		m4_ground = load("sprites/gun_m4.png");
		pistol_ground = load("sprites/gun_pistol.png");
		shotgun_ground = load("sprites/gun_shotgun.png");
		goldengun_ground = load("sprites/gun_goldengun.png");
		flamethrower_ground = load("sprites/gun_flamethrower.png");
		awp_ground = load("sprites/gun_awp.png");
		goldengun_ground = load("sprites/gun_goldengun.png");
		crossbow_ground = load("sprites/gun_crossbow.png");
		mac10_ground = load("sprites/gun_mac10.png");

		// Items
		healthpack = load("sprites/health_pack.png");
		snakebite = load("sprites/snakebite_grenade.png");

		// Icons
		lead_star = load("sprites/lead_star.png");
		tied_for_lead_star = load("sprites/tied_for_lead_star.png");
		icon_border = load("sprites/health_icon.png");
		health_icon = load("sprites/health_icon.png");

		// MapTiles
		spawntile_regular = load("maps/sprites/spawner_regular.png");
		spawntile_common = load("maps/sprites/spawner_common.png");
		spawntile_rare = load("maps/sprites/spawner_rare.png");
		spawntile_epic = load("maps/sprites/spawner_epic.png");

		itemtile_regular = load("maps/sprites/itenspawner_regular.png");
		itemtile_common = load("maps/sprites/itenspawner_common.png");

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

		card_xl_bg = load("sprites/card_xl_bg.png");
		card_xl_border = load("sprites/card_xl_border.png");

		kills = load("sprites/kills.png");
		deaths = load("sprites/deaths.png");
		kdbg = load("sprites/kd_bg.png");
		// Animations

		Texture explosionSheet = load("animations/explosion1.png");
		explosion_frames = new Array<>();
		for (int y = 0; y < explosionSheet.getHeight(); y += 128) {
			TextureRegion region = new TextureRegion(explosionSheet, 0, y, 128, 128);
			explosion_frames.add(region);
		}

		FreeTypeFontGenerator fontgen = new FreeTypeFontGenerator(Gdx.files.internal("novamono.ttf"));
		FreeTypeFontParameter font20params = new FreeTypeFontParameter();
		font20params.minFilter = Texture.TextureFilter.Nearest;
		font20params.magFilter = Texture.TextureFilter.MipMapLinearNearest;
		font20params.size = 35;

		font = fontgen.generateFont(font20params);

		FreeTypeFontParameter font12params = new FreeTypeFontParameter();
		font12params.minFilter = Texture.TextureFilter.Nearest;
		font12params.magFilter = Texture.TextureFilter.MipMapLinearNearest;
		font12params.size = 24;

		fontSmall = fontgen.generateFont(font12params);

		FreeTypeFontParameter font62params = new FreeTypeFontParameter();
		font62params.minFilter = Texture.TextureFilter.Nearest;
		font62params.magFilter = Texture.TextureFilter.MipMapLinearNearest;
		font62params.size = 80;

		fontLarge = fontgen.generateFont(font62params);

		FreeTypeFontParameter font40params = new FreeTypeFontParameter();
		font40params.minFilter = Texture.TextureFilter.Nearest;
		font40params.magFilter = Texture.TextureFilter.MipMapLinearNearest;
		font40params.size = 60;

		announcerFont = fontgen.generateFont(font40params);
	}

	private static Texture load(String filename) {
		Texture newTexture = new Texture(filename);
		return newTexture;
	}

}
