package no.kash.gamedev.jag.game.gameobjects.particles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import no.kash.gamedev.jag.assets.Assets;

public class Confetti extends AbstractParticle {
	public static final float WIDTH = 9, HEIGHT = 5, TIME_TO_LIVE = 10f;
	public static final float BASE_ROTATION_SPEED = 60.0f;

	Color color;
	float rotationSpeed;
	Vector2 velocity;

	public Confetti(float x, float y) {
		super(x, y, WIDTH, HEIGHT, TIME_TO_LIVE);
		color = new Color((float) (Math.random() * 0.5 + 0.5), (float) (Math.random() * 0.5 + 0.5),
				(float) (Math.random() * 0.5 + 0.5), 1.0f);
		setSprite(new Sprite(Assets.confetti));
		setMaxSpeed(3000);
		getSprite().setColor(color);
		velocity = new Vector2();
		velocity.y = -250f - (float) (Math.random()) * 75f;
		velocity.x = (float) (Math.random() - 0.5) * 150f;
		rotationSpeed = (float) (BASE_ROTATION_SPEED * (Math.random() - 0.5));
	}

	@Override
	public void onTimeout() {
		destroy();
	}

	@Override
	public void updateParticle(float delta) {
		velocity().x = velocity.x;
		velocity().y = velocity.y;
		move(delta);
		setRotation(getAliveTime() * rotationSpeed);
		getSprite().setOrigin(getWidth() / 2, getHeight() / 2);
		getBounds().setOrigin(getWidth()/2, getHeight()/2);
		setWidth((float) (WIDTH * Math.cos(getAliveTime() * rotationSpeed)));
	}

}
