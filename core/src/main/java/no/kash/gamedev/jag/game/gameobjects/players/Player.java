package no.kash.gamedev.jag.game.gameobjects.players;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.game.gameobjects.AbstractGameObject;

public class Player extends AbstractGameObject {

	private static final float ACCELERATION = 4000;
	private static final float SPEED = 250;
	private final int id;
	private final String name;

	private final GlyphLayout nameLabel;

	public Player(int id, String name, float x, float y) {
		super(x, y, 16, 16);
		Sprite sprite = new Sprite(Assets.man);
		sprite.setOrigin(8, 8);
		setSprite(sprite);

		this.position.x = x;
		this.position.y = y;
		this.id = id;
		this.name = name;

		nameLabel = new GlyphLayout(Assets.font, name);

		max_acceleration.x = ACCELERATION;
		max_acceleration.y = ACCELERATION;
		max_velocity.x = SPEED;
		max_velocity.y = SPEED;
	}

	@Override
	public void update(float delta) {
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

}
