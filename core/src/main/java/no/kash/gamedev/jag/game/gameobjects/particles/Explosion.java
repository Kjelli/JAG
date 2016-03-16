package no.kash.gamedev.jag.game.gameobjects.particles;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.game.gamecontext.physics.Collidable;
import no.kash.gamedev.jag.game.gamecontext.physics.Collision;
import no.kash.gamedev.jag.game.gameobjects.players.Player;

public class Explosion extends AbstractParticle implements Collidable {
	public static final float WIDTH = 128, HEIGHT = 128;
	private static final int FRAMES = 9;
	private static final float FRAME_SECONDS = 0.045f;
	private static final float TIME_TO_LIVE = FRAMES * FRAME_SECONDS;

	private float time = 0;

	Animation explosion_animation;

	List<Player> damaged;

	public Explosion(float x, float y) {
		super(x, y, 128, 128, TIME_TO_LIVE);
		explosion_animation = new Animation(FRAME_SECONDS, Assets.explosion_frames);
		explosion_animation.setPlayMode(PlayMode.NORMAL);
		setRotation((float) (Math.random() * 2 * Math.PI));
		damaged = new ArrayList<>();
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

	@Override
	public void onCollide(Collision collision) {
		if (collision.getTarget() instanceof Player) {
			Player player = (Player) collision.getTarget();
			if (!damaged.contains(player)) {
				damaged.add(player);
				player.damage(this);
			}
		}
	}

}
