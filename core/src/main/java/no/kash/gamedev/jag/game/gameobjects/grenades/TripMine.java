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
	boolean placed;

	public TripMine(Player thrower, float x, float y, float direction, float power) {
		super(thrower, Assets.tripmine_ground, x, y, direction, power);
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
		if (Math.abs(col.normal.x) > Math.abs(col.normal.y)) {
			// Vertical placement
			if(velocity.x < 0){
				setRotation((float) Math.PI);
			}else{
				setRotation(0);
			}
		} else {
			// Horizontal placement
			if(velocity.y < 0){
				setRotation((float) -Math.PI/2);
			}else{
				setRotation((float)Math.PI/2);
			}
		}
	}

}
