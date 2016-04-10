package no.kash.gamedev.jag.game.gameobjects.grenades;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector;

import aurelienribon.tweenengine.Tween;
import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.commons.tweens.TweenGlobal;
import no.kash.gamedev.jag.commons.tweens.accessors.Vector2Accessor;
import no.kash.gamedev.jag.game.gamecontext.physics.tilecollisions.TileCollisionDetector;
import no.kash.gamedev.jag.game.gameobjects.players.Player;

public class TripMine extends AbstractGrenade {
	public static final float AIR_TIME = 1.3f;

	public TripMine(Player thrower, float x, float y, float direction, float power) {
		super(thrower, Assets.grenade, x, y, direction, power);
		TweenGlobal.start(Tween.to(velocity, Vector2Accessor.TYPE_XY, AIR_TIME).target(0, 0));
	}

	@Override
	public void timeOut() {
		getGameContext()
				.spawn(new Explosion(this, getCenterX() - Explosion.WIDTH / 2, getCenterY() - Explosion.HEIGHT / 2));
		destroy();
	}

	@Override
	protected void collision(MapObject rectangleObject, MinimumTranslationVector col) {
		TileCollisionDetector.nudge(this, col);
	}

}
