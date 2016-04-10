package no.kash.gamedev.jag.game.gameobjects.grenades;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.game.gamecontext.physics.Collidable;
import no.kash.gamedev.jag.game.gamecontext.physics.Collision;
import no.kash.gamedev.jag.game.gameobjects.particles.AbstractParticle;
import no.kash.gamedev.jag.game.gameobjects.players.CircularHitbox;
import no.kash.gamedev.jag.game.gameobjects.players.Player;

public class Explosion extends AbstractParticle implements Collidable {
	public static final float WIDTH = 128, HEIGHT = 128;
	private static final int FRAMES = 9;
	private static final float FRAME_SECONDS = 0.045f;
	private static final float TIME_TO_LIVE = FRAMES * FRAME_SECONDS;
	public static final float EXPLOSION_RADIUS = 65;

	Animation explosion_animation;

	CircularHitbox hitbox;
	
	AbstractGrenade grenade;
	List<Player> damaged;

	public Explosion(AbstractGrenade grenade, float x, float y) {
		super(x, y, 128, 128, TIME_TO_LIVE);
		this.grenade = grenade;
		hitbox = new CircularHitbox(getCenterX() - EXPLOSION_RADIUS/2, getCenterY() - EXPLOSION_RADIUS/2, EXPLOSION_RADIUS, EXPLOSION_RADIUS);
		explosion_animation = new Animation(FRAME_SECONDS, Assets.explosion_frames);
		explosion_animation.setPlayMode(PlayMode.NORMAL);
		setRotation((float) (Math.random() * 2 * Math.PI));
		damaged = new ArrayList<>();
	}

	@Override
	public void draw(SpriteBatch batch) {
		batch.draw(explosion_animation.getKeyFrame(getAliveTime()), getX(), getY(), getWidth() / 2, getHeight() / 2,
				getWidth(), getHeight(), 1.0f, 1.0f, getRotation());
	}

	@Override
	public void onTimeout() {
		destroy();
	}

	@Override
	public void updateParticle(float delta) {
	}
	
	@Override
	public Polygon getBounds() {
		return hitbox.poly;
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
	
	public AbstractGrenade getSourceGrenade() {
		return grenade;
	}

}
