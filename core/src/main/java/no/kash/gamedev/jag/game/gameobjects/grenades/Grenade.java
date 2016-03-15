package no.kash.gamedev.jag.game.gameobjects.grenades;

import com.badlogic.gdx.graphics.g2d.Sprite;

import aurelienribon.tweenengine.Tween;
import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.commons.tweens.TweenGlobal;
import no.kash.gamedev.jag.commons.tweens.accessors.Vector2Accessor;
import no.kash.gamedev.jag.game.gameobjects.AbstractGameObject;
import no.kash.gamedev.jag.game.gameobjects.particles.Explosion;
import no.kash.gamedev.jag.game.gameobjects.players.Player;

public class Grenade extends AbstractGameObject {

	public static final float TIME_TO_LIVE_MAX = 1.0f;
	public static final float SPEED = 300f;
	public static final float AIR_TIME = 1.0f;

	public float timeToLive = TIME_TO_LIVE_MAX;
	public final float direction;
	public final float power;

	public Grenade(Player thrower, float x, float y, float direction, float power) {
		super(x, y, 16, 16);
		setSprite(new Sprite(Assets.grenade));
		getSprite().setOrigin(getWidth() / 2, getHeight() / 2);
		this.direction = direction;
		this.power = power;
		velocity().x = (float) (thrower.velocity().x + Math.cos(direction) * power * SPEED);
		velocity().y = (float) (thrower.velocity().y + Math.sin(direction) * power * SPEED);
		TweenGlobal.start(Tween.to(velocity, Vector2Accessor.TYPE_XY, AIR_TIME).target(0, 0));
	}

	@Override
	public void update(float delta) {
		if ((timeToLive -= delta) <= 0) {
			blowUp();
		}

		setRotation((Math.max(timeToLive - TIME_TO_LIVE_MAX / 2, 0) / TIME_TO_LIVE_MAX) * power * 10 + power * direction );

		move(delta);
	}

	private void blowUp() {
		getGameContext().spawn(new Explosion(getCenterX() - Explosion.WIDTH / 2, getCenterY() - Explosion.HEIGHT / 2));
		destroy();
	}

}
