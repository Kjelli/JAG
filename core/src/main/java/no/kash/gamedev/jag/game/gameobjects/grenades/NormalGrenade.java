package no.kash.gamedev.jag.game.gameobjects.grenades;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector;

import aurelienribon.tweenengine.Tween;
import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.commons.tweens.TweenGlobal;
import no.kash.gamedev.jag.commons.tweens.accessors.Vector2Accessor;
import no.kash.gamedev.jag.game.gamecontext.physics.tilecollisions.TileCollisionDetector;
import no.kash.gamedev.jag.game.gameobjects.players.Player;

public class NormalGrenade extends AbstractGrenade {
	public static final float AIR_TIME = 1.3f;

	public NormalGrenade(Player thrower, float x, float y, float direction, float power) {
		super(thrower, Assets.grenade, x, y, direction, power);
	}

	@Override
	public void timeOut() {
		getGameContext()
				.spawn(new Explosion(this, getCenterX() - Explosion.WIDTH / 2, getCenterY() - Explosion.HEIGHT / 2));
		destroy();
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		setRotation(
				(Math.max(timeToLive - TIME_TO_LIVE_MAX / 2, 0) / TIME_TO_LIVE_MAX) * power * 10 + power * direction);
		TileCollisionDetector.checkTileCollisions(getGameContext().getLevel(), this, tileListener);
	}

	@Override
	protected void collision(MapObject rectangleObject, MinimumTranslationVector col) {
		if (Integer.parseInt((String) rectangleObject.getProperties().get("collision_level")) == 3) {
			TileCollisionDetector.nudge(this, col);
			if (!bounceCooldown.isOnCooldown()) {
				bounceCooldown.startCooldown();
				if (Math.abs(col.normal.x) > Math.abs(col.normal.y)) {
					velocity().x *= -1;
				} else {
					velocity().y *= -1;
				}
			}
		}
	}

}
