package no.kash.gamedev.jag.controller.hud;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.game.gameobjects.AbstractGameObject;
import no.kash.gamedev.jag.game.gameobjects.collectables.items.ItemType;
import no.kash.gamedev.jag.game.gameobjects.players.guns.GunType;

public class InGameHud extends AbstractGameObject {

	private Sprite weaponImg, healthImg, itemImg;
	private int ammo, magazineAmmo, magazineSize, health;

	private BitmapFont font;

	private GlyphLayout healthLabel;
	private GlyphLayout ammoLabel;
	private GlyphLayout itemLabel;

	public InGameHud(float x, float y, float width, float height) {
		super(x, y, width, height);
		health = 100;
		ammo = 0;
		magazineAmmo = 0;
		magazineSize = 0;
		font = Assets.font;

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

		healthLabel = new GlyphLayout(font, "" + health);
		ammoLabel = new GlyphLayout(font, magazineAmmo + " / " + ammo);
		itemLabel = new GlyphLayout(font, "");

	}

	public void draw(SpriteBatch batch) {
		healthImg.draw(batch);
		weaponImg.draw(batch);
		itemImg.draw(batch);

		font.draw(batch, healthLabel, healthImg.getX() + healthImg.getWidth() + 5f,
				getY() - healthImg.getHeight() + healthImg.getHeight() / 4 + healthLabel.height);

		font.draw(batch, ammoLabel, weaponImg.getX() + weaponImg.getWidth() + 5f,
				getY() - healthImg.getHeight() - weaponImg.getHeight() + weaponImg.getHeight() / 4 + ammoLabel.height);
		font.draw(batch, itemLabel, itemImg.getX() + itemImg.getWidth() + 5f, getY() - healthImg.getHeight()
				- weaponImg.getHeight() - itemImg.getHeight() + itemImg.getHeight() / 4 + itemLabel.height);
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
		ammoLabel.setText(font, magazineAmmo + " / " + ammo);
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
