package no.kash.gamedev.jag.game.gameobjects.bullets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Rectangle;

import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.game.gamecontext.physics.Collidable;
import no.kash.gamedev.jag.game.gamecontext.physics.Collision;
import no.kash.gamedev.jag.game.gameobjects.AbstractGameObject;
import no.kash.gamedev.jag.game.gameobjects.players.Player;
import no.kash.gamedev.jag.game.levels.Level;
import no.kash.gamedev.jag.game.tilecollisions.TileCollisionDetector;
import no.kash.gamedev.jag.game.tilecollisions.TileCollisionListener;

public class Bullet extends AbstractGameObject implements Collidable {

	protected Player shooter;
	protected Level level;
	protected float damage;
	protected int ricochetMax = 3;
	protected int ricochet;
	protected float ricochetTimerMax = 0.1f;
	protected float ricochetTimer = 0f;

	protected float speed = 250f;

	TileCollisionListener tileCollisionListener;

	public Bullet(Level level, Player shooter, float x, float y, float direction) {
		super(x, y, 8, 2);
		this.shooter = shooter;
		this.level = level;

		Sprite sprite = new Sprite(Assets.bullet);
		sprite.setOrigin(getWidth() / 2, getHeight() / 2);
		setSprite(sprite);

		setRotation(direction);

		this.tileCollisionListener = new TileCollisionListener() {
			@Override
			public void onCollide(MapObject rectangleObject, Rectangle intersection) {
				String data = (String) rectangleObject.getProperties().get("collision_level");
				if (data != null) {
					if (Integer.parseInt(data) == 2) {
						destroy();
					}
				}
			}
		};
		velocity.x = (float) (Math.cos(getRotation()) * speed);
		velocity.y = (float) (Math.sin(getRotation()) * speed);
	}

	@Override
	public void update(float delta) {
		if (ricochetTimer > 0) {
			ricochetTimer -= delta;
		} else {
			ricochetTimer = 0;
		}
		move(delta);
		TileCollisionDetector.checkTileCollisions(level, this, tileCollisionListener);

		outOfBounds();
	}

	private void outOfBounds() {
		if (getX() > level.width * level.tileWidth || getX() < 0 || getY() > level.height * level.tileHeight
				|| getY() < 0) {
			destroy();
		}
	}

	@Override
	public void onCollide(Collision collision) {
		// Do nothing
	}

	public Player getShooter() {
		return shooter;
	}

}
