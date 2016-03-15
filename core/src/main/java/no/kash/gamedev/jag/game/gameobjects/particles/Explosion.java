package no.kash.gamedev.jag.game.gameobjects.particles;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.kash.gamedev.jag.assets.Assets;

public class Explosion extends AbstractParticle {
	public static final float WIDTH = 128, HEIGHT = 128;
	private static final int FRAMES = 9;
	private static final float FRAME_SECONDS = 0.045f;
	private static final float TIME_TO_LIVE = FRAMES * FRAME_SECONDS;

	private float time = 0;

	Animation explosion_animation;

	public Explosion(float x, float y) {
		super(x, y, 128, 128, TIME_TO_LIVE);
		explosion_animation = new Animation(FRAME_SECONDS, Assets.explosion_frames);
		explosion_animation.setPlayMode(PlayMode.NORMAL);
		setRotation((float) (Math.random() * 2 * Math.PI));
	}

	@Override
	public void draw(SpriteBatch batch) {
		batch.draw(explosion_animation.getKeyFrame(time), getX(), getY(), getWidth() / 2, getHeight() / 2, getWidth(),
				getHeight(), 1.0f, 1.0f, getRotation());
	}

	@Override
	public void onTimeout() {
		destroy();
	}

	@Override
	public void updateParticle(float delta) {
		time += delta;
	}

}
