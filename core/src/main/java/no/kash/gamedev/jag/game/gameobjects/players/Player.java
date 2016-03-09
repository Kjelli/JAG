package no.kash.gamedev.jag.game.gameobjects.players;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.game.gameobjects.AbstractGameObject;
import no.kash.gamedev.jag.game.gameobjects.bullets.Bullet;

import no.kash.gamedev.jag.game.gameobjects.players.guns.Gun;
import no.kash.gamedev.jag.game.gameobjects.players.guns.GunType;
import no.kash.gamedev.jag.game.gameobjects.players.guns.Pistol;

public class Player extends AbstractGameObject {
	private final int id;
	private final String name;

	private final GlyphLayout nameLabel;
	private Gun gun;

	public Player(int id, String name, float x, float y) {
		super(x, y, 32, 32);
		Sprite sprite = new Sprite(Assets.man);
		sprite.setOrigin(getWidth() / 2, getHeight() / 2);
		setSprite(sprite);

		this.position.x = x;
		this.position.y = y;
		this.id = id;
		this.name = name;

		nameLabel = new GlyphLayout(Assets.font, name);
		gun = new Pistol(GunType.pistol);
		gun.setAmmo(100);
		gun.reload();
		gun.equip(this);
	}

	@Override
	public void update(float delta) {
		gun.update(delta);
		move(delta);
	}

	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		Assets.font.draw(batch, nameLabel, position.x + width / 2 - nameLabel.width / 2,
				position.y + height + nameLabel.height);
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void fireBullet() {
		gun.shoot();
	}

}
