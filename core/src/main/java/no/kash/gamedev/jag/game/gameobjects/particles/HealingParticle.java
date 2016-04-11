package no.kash.gamedev.jag.game.gameobjects.particles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;

import no.kash.gamedev.jag.assets.Assets;

public class HealingParticle extends AbstractParticle{

	public static final float WIDTH = 8, HEIGHT = 8, TIME_TO_LIVE = 3.0f;
	private static final float BASE_ROTATION_SPEED = 50f;

	Color color;
	float startingScale;

	public HealingParticle(float x, float y, float scale) {
		this(x, y);
		startingScale = scale;
		setScale(startingScale);
		getSprite().setOrigin(getWidth() / 2, getHeight() / 2);

	}

	public HealingParticle(float x, float y) {
		this(x, y, 0, -150.0f);
	}

	public HealingParticle(float x, float y, float velx, float vely, float scale) {
		this(x, y, velx, vely);
		startingScale = scale;
		setScale(startingScale);
		getSprite().setOrigin(getWidth() / 2, getHeight() / 2);

	}

	public HealingParticle(float x, float y, float velx, float vely) {
		super(x, y, WIDTH, HEIGHT, TIME_TO_LIVE);
		setSprite(new Sprite(Assets.healing));
		velocity().x = velx;
		velocity().y = vely;
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
		move(delta);
	}

}
