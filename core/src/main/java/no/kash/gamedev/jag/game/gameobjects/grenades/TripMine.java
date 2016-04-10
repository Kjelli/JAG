package no.kash.gamedev.jag.game.gameobjects.grenades;

import com.badlogic.gdx.graphics.g2d.Sprite;
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
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		if (!placed) {
			TileCollisionDetector.checkTileCollisions(getGameContext().getLevel(), this, tileListener);
		}
	}

	@Override
	protected void collision(MapObject rectangleObject, MinimumTranslationVector col) {
		if (placed) {
			return;
		}
		TileCollisionDetector.nudge(this, col);
		if (Math.abs(col.normal.x) > Math.abs(col.normal.y)) {
			// Vertical placement
			if (velocity.x < 0) {
				setRotation(0);
				setX((Float) rectangleObject.getProperties().get("x")
						+ (Float) rectangleObject.getProperties().get("width") - getWidth()/2);
			} else {
				setRotation((float) Math.PI);
				setX((Float)rectangleObject.getProperties().get("x") - getWidth()/2);
			}

		} else {
			// Horizontal placement
			if (velocity.y > 0) {
				setRotation((float) -Math.PI / 2);
				setY((Float) rectangleObject.getProperties().get("y") - getHeight()/2);
			} else {
				setRotation((float) Math.PI / 2);
				setY((Float) rectangleObject.getProperties().get("y")
						+ (Float) rectangleObject.getProperties().get("height") - getHeight()/2);
			}
		}
		place();
	}

	private void place() {
		placed = true;
		velocity().x = 0;
		velocity().y = 0;
		setSprite(new Sprite(Assets.tripmine_placed));
	}

}
