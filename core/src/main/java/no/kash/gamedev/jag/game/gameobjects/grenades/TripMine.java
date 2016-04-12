package no.kash.gamedev.jag.game.gameobjects.grenades;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector;

import aurelienribon.tweenengine.Tween;
import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.commons.tweens.TweenGlobal;
import no.kash.gamedev.jag.commons.tweens.accessors.Vector2Accessor;
import no.kash.gamedev.jag.game.commons.utils.Cooldown;
import no.kash.gamedev.jag.game.gamecontext.physics.Collidable;
import no.kash.gamedev.jag.game.gamecontext.physics.Collision;
import no.kash.gamedev.jag.game.gamecontext.physics.tilecollisions.TileCollisionDetector;
import no.kash.gamedev.jag.game.gameobjects.bullets.Fire;
import no.kash.gamedev.jag.game.gameobjects.collectables.items.CollectableItem;
import no.kash.gamedev.jag.game.gameobjects.collectables.items.Item;
import no.kash.gamedev.jag.game.gameobjects.collectables.items.ItemType;
import no.kash.gamedev.jag.game.gameobjects.players.Player;

public class TripMine extends AbstractGrenade implements Collidable {
	private static final float WIDTH = 6, HEIGHT = 18;
	private static final float TIME_BEFORE_ITEMIZING = 1.5f;
	boolean placed;
	Cooldown placingCooldown;

	public TripMine(Player thrower, float x, float y, float direction, float power) {
		super(thrower, Assets.tripmine_ground, x, y, WIDTH, HEIGHT, direction, power, TIME_BEFORE_ITEMIZING);
		placingCooldown = new Cooldown(0.1f);
	}

	@Override
	public void onSpawn() {
		super.onSpawn();
		placingCooldown.start();
	}

	@Override
	public void timeOut() {
		if (!placed) {
			getGameContext().spawn(
					new CollectableItem(getX() - getWidth(), getY() - getHeight(), new Item(ItemType.tripmine, 1)));
			destroy();
		}
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		if (!placed) {
			placingCooldown.update(delta);
			TileCollisionDetector.checkTileCollisions(getGameContext().getLevel(), this, tileListener);
		}
	}

	@Override
	protected void collision(MapObject rectangleObject, MinimumTranslationVector col) {
		if (Integer.parseInt((String) rectangleObject.getProperties().get("collision_level")) == 1) {
			return;
		}
		if (placed || placingCooldown.isOnCooldown()) {
			return;
		}
		TileCollisionDetector.nudge(this, col);
		if (Math.abs(col.normal.x) > Math.abs(col.normal.y)) {
			// Vertical placement
			if (velocity.x < 0) {
				setRotation(0);
				setX((Float) rectangleObject.getProperties().get("x")
						+ (Float) rectangleObject.getProperties().get("width"));
			} else {
				setRotation((float) Math.PI);
				setX((Float) rectangleObject.getProperties().get("x") - getWidth());
			}

		} else {
			// Horizontal placement
			if (velocity.y > 0) {
				setRotation((float) -Math.PI / 2);
				setY((Float) rectangleObject.getProperties().get("y") - 2f * getWidth());
			} else {
				setRotation((float) Math.PI / 2);
				setY((Float) rectangleObject.getProperties().get("y")
						+ (Float) rectangleObject.getProperties().get("height") - getWidth());
			}
		}
		place();
	}

	private void place() {
		placed = true;
		velocity().x = 0;
		velocity().y = 0;
		setSprite(new Sprite(Assets.tripmine_placed));
		getGameContext().spawn(new TripLaser(this, (float) (getCenterX() + Math.cos(getRotation()) * 3),
				(float) (getCenterY() + Math.sin(getRotation()) * 3), getRotation()));
	}

	public void blowUp() {
		getGameContext()
				.spawn(new Explosion(this, getCenterX() - Explosion.WIDTH / 2, getCenterY() - Explosion.HEIGHT / 2));
		destroy();
	}

	@Override
	public void onCollide(Collision collision) {
		if (collision.getTarget() instanceof Fire) {
			blowUp();
		}
	}

}
