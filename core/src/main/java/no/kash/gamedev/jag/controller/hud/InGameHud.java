package no.kash.gamedev.jag.controller.hud;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.game.gameobjects.AbstractGameObject;
import no.kash.gamedev.jag.game.gameobjects.collectables.items.ItemType;
import no.kash.gamedev.jag.game.gameobjects.players.guns.GunType;

public class InGameHud extends AbstractGameObject {

	private Sprite weaponImg, healthImg, itemImg, killsImg, deathsImg, kdbg;
	private int ammo, magazineAmmo, magazineSize, health, kills, deaths;

	private BitmapFont font;

	private GlyphLayout healthLabel;
	private GlyphLayout ammoLabel;
	private GlyphLayout itemLabel;
	private GlyphLayout killsLabel;
	private GlyphLayout deathsLabel;

	public InGameHud(Stage stage, float x, float y, float width, float height) {
		super(x, y, width, height);
		health = 100;
		ammo = 0;
		magazineAmmo = 0;
		magazineSize = 0;
		font = Assets.font;

		kdbg = new Sprite(Assets.kdbg);
		kdbg.setOrigin(0, 0);
		kdbg.setX(stage.getWidth() / 3);
		kdbg.setY(0);
		healthImg = new Sprite(Assets.health_icon);
		healthImg.setOrigin(0, 0);
		healthImg.setX(0);
		healthImg.setY(y - healthImg.getHeight());

		weaponImg = new Sprite(Assets.pistol_ground);
		weaponImg.setOrigin(0, 0);
		weaponImg.setX(0);
		weaponImg.setY(y - healthImg.getHeight() - weaponImg.getHeight());

		itemImg = new Sprite(Assets.grenade);
		itemImg.setOrigin(0, 0);
		itemImg.setX(0);
		itemImg.setY(y - healthImg.getHeight() - weaponImg.getHeight() - itemImg.getHeight());

		killsImg = new Sprite(Assets.kills);
		killsImg.setOrigin(0, 0);
		killsImg.setX((float) (stage.getWidth() / 2 - stage.getWidth() / 16) - killsImg.getWidth());
		killsImg.setY((float) (stage.getHeight() / 32));

		deathsImg = new Sprite(Assets.deaths);
		deathsImg.setOrigin(0, 0);
		deathsImg.setX((float) (stage.getWidth() / 2 + stage.getWidth() / 16 - deathsImg.getWidth()));
		deathsImg.setY((float) (stage.getHeight() / 32));

		healthLabel = new GlyphLayout(font, "" + health);
		ammoLabel = new GlyphLayout(font, magazineAmmo + " / " + ammo);
		itemLabel = new GlyphLayout(font, "");
		killsLabel = new GlyphLayout(font, kills + "");
		deathsLabel = new GlyphLayout(font, deaths + "");

	}

	public void draw(SpriteBatch batch) {
		healthImg.draw(batch);
		weaponImg.draw(batch);
		itemImg.draw(batch);
		kdbg.draw(batch);
		killsImg.draw(batch);
		deathsImg.draw(batch);

		font.draw(batch, healthLabel, healthImg.getX() + healthImg.getWidth() + 5f,
				getY() - healthImg.getHeight() + healthImg.getHeight() / 4 + healthLabel.height);

		font.draw(batch, ammoLabel, weaponImg.getX() + weaponImg.getWidth() + 5f,
				getY() - healthImg.getHeight() - weaponImg.getHeight() + weaponImg.getHeight() / 4 + ammoLabel.height);
		font.draw(batch, itemLabel, itemImg.getX() + itemImg.getWidth() + 5f, getY() - healthImg.getHeight()
				- weaponImg.getHeight() - itemImg.getHeight() + itemImg.getHeight() / 4 + itemLabel.height);

		font.draw(batch, killsLabel, killsImg.getX() + killsImg.getWidth() + 2f,
				killsImg.getY() + killsImg.getHeight());
		font.draw(batch, deathsLabel, deathsImg.getX() + deathsImg.getWidth() + 2f,
				deathsImg.getY() + deathsImg.getHeight());
	}

	public void setAmmo(int ammo) {
		this.ammo = ammo;
	}

	public void setMagazineAmmo(int magzineAmmo) {
		this.magazineAmmo = magzineAmmo;
	}

	public int getMagazineAmmo() {
		return magazineAmmo;
	}

	public void setMagazineSize(int magazineSize) {
		this.magazineSize = magazineSize;
	}

	public int getMagazineSize() {
		return magazineSize;
	}

	public int getAmmo() {
		return ammo;
	}

	public void setHealth(int health) {
		this.health = health;
		healthLabel.setText(font, health + "");
	}

	public int getHealth() {
		return health;
	}

	@Override
	public void update(float delta) {

	}

	public void updateAmmo(int magAmmo, int magSize, int ammo) {
		setMagazineAmmo(magAmmo);
		setMagazineSize(magSize);
		setAmmo(ammo);
		if (ammo == -1 && magAmmo == -1) {
			ammoLabel.setText(font, "");
		} else {
			ammoLabel.setText(font, magazineAmmo + " / " + ammo);
		}
	}

	public void updateKD(int kills, int deaths) {
		this.kills += kills;
		this.deaths += deaths;
		killsLabel.setText(font, this.kills + "");
		deathsLabel.setText(font, this.deaths + "");
	}

	public void updateGun(int gunTypeIndex) {
		try {
			Texture newTexture = GunType.values()[gunTypeIndex].getOnGroundTexture();
			if (newTexture != null) {
				weaponImg.setTexture(newTexture);
			} else {
				throw new Exception();
			}
		} catch (Exception e) {
			weaponImg.setTexture(Assets.man);
		}
	}

	public void updateItem(int itemTypeIndex, int uses) {
		try {
			Texture newTexture = ItemType.values()[itemTypeIndex].getTexture();
			if (newTexture != null) {
				itemImg.setTexture(newTexture);
			} else {
				throw new Exception();
			}
		} catch (Exception e) {
			itemImg.setTexture(Assets.grenade);
		}
		itemLabel.setText(font, (uses == -1 ? "" : uses + ""));
	}

}
