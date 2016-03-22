package no.kash.gamedev.jag.game.gameobjects.particles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;

import no.kash.gamedev.jag.assets.Assets;

public class Star extends AbstractParticle {
	public static final float WIDTH = 8, HEIGHT = 8, TIME_TO_LIVE = 1.0f;
	private static final float BASE_ROTATION_SPEED = 50f;

	Color color;
	float rotationSpeed;
	float startingScale;

	public Star(float x, float y) {
		super(x, y, WIDTH, HEIGHT, TIME_TO_LIVE);
		setSprite(new Sprite(Assets.star));
		velocity().x = 0;
		velocity().y = -150.0f;
		rotationSpeed = (float) ((Math.random() - 0.5) * BASE_ROTATION_SPEED);
		startingScale = (float) Math.random();
		setScale(startingScale);
		color = new Color(Color.WHITE);
	}

	@Override
	public void onSpawn() {

	}

	@Override
	public void onTimeout() {
		destroy();
	}

	@Override
	public void updateParticle(float delta) {
		color.a = timeToLiveTimer / TIME_TO_LIVE;
		getSprite().setColor(color);
		setScale(startingScale * timeToLiveTimer / TIME_TO_LIVE);
		setRotation(getAliveTime() * rotationSpeed);
		move(delta);
	}

}
