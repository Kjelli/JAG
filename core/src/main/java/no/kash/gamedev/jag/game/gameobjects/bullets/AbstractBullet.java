package no.kash.gamedev.jag.game.gameobjects.bullets;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Rectangle;

import no.kash.gamedev.jag.game.gamecontext.physics.Collidable;
import no.kash.gamedev.jag.game.gamecontext.physics.Collision;
import no.kash.gamedev.jag.game.gamecontext.physics.tilecollisions.TileCollisionDetector;
import no.kash.gamedev.jag.game.gamecontext.physics.tilecollisions.TileCollisionListener;
import no.kash.gamedev.jag.game.gameobjects.AbstractGameObject;
import no.kash.gamedev.jag.game.gameobjects.players.Player;

public abstract class AbstractBullet extends AbstractGameObject implements Bullet, Collidable {
	protected Player shooter;
	protected float damage;

	protected float speed;

	TileCollisionListener tileCollisionListener;

	public AbstractBullet(Player shooter, float x, float y, float width, float height, float direction, float damage,
			float speed) {
		super(x, y, width, height);
		this.shooter = shooter;
		this.damage = damage;
		this.speed = speed;
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
		acceleration.x = EPSILON;
		acceleration.y = EPSILON;
		velocity.x = (float) (Math.cos(getRotation()) * speed);
		velocity.y = (float) (Math.sin(getRotation()) * speed);
	}

	@Override
	public void onSpawn() {
	}

	@Override
	public void update(float delta) {
		move(delta);
		TileCollisionDetector.checkTileCollisions(getGameContext().getLevel(), this, tileCollisionListener);

		outOfBounds();
	}

	private void outOfBounds() {
		if (getX() > getGameContext().getLevel().width * getGameContext().getLevel().tileWidth || getX() < 0
				|| getY() > getGameContext().getLevel().height * getGameContext().getLevel().tileHeight || getY() < 0) {
			destroy();
		}
	}

	@Override
	public void onCollide(Collision collision) {
		if (collision.getTarget() instanceof Player) {
			Player target = (Player) collision.getTarget();
			if (isAlive() && shooter.equals(target)) {
				return;
			}

			boolean friendlyFireDisabled = !target.getGameSession().friendlyFire;
			boolean sameTeam = shooter.getInfo().teamId == target.getInfo().teamId;
			boolean teamBasedGame = target.getGameSession().gameMode.teamBased;
			if (teamBasedGame && sameTeam && friendlyFireDisabled) {
				return;
			}

			onImpact(target);
		}
	}

	public Player getShooter() {
		return shooter;
	}

	public float getDamage() {
		return damage;
	}

}
