package no.kash.gamedev.jag.game.gameobjects.players;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector;

import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.game.gamecontext.physics.tilecollisions.TileCollisionDetector;
import no.kash.gamedev.jag.game.gamecontext.physics.tilecollisions.TileCollisionListener;
import no.kash.gamedev.jag.game.gameobjects.AbstractGameObject;
import no.kash.gamedev.jag.game.gameobjects.GameObject;
import no.kash.gamedev.jag.game.gameobjects.bullets.Bullet;
import no.kash.gamedev.jag.game.gameobjects.players.guns.GunType;
import no.kash.gamedev.jag.game.levels.Level;

public class LaserSight extends AbstractGameObject implements Bullet {
	private final static int WIDTH = 1000;
	private final static int HEIGHT = 1;
	private Player player;
	public TileCollisionListener tileCollisionListener;
	boolean stillColliding = false;
	public boolean disposed;

	public LaserSight(Player player, float x, float y, float direction) {
		super(x, y, WIDTH, HEIGHT);
		this.player = player;
		setSprite(new Sprite(Assets.laserSight));
		getSprite().setOrigin(0, 0);
		getSprite().setColor(1.0f, 1.0f, 1.0f, 0.3f);
		setRotation(direction);
		bounds.setOrigin(0, 0);

		this.tileCollisionListener = new TileCollisionListener() {
			@Override
			public void onCollide(MapObject rectangleObject, MinimumTranslationVector col) {
				stillColliding = true;
				String data = (String) rectangleObject.getProperties().get("collision_level");
				if (data != null) {
					if (Integer.parseInt(data) <= 2) {
						// TODO
						setWidth(getWidth() - 5);
						if (stillColliding) {
							stillColliding = false;
							TileCollisionDetector.checkTileCollisions(
									LaserSight.this.player.getGameContext().getLevel(), LaserSight.this,
									LaserSight.this.tileCollisionListener);
						}
					}
				}
			}

		};
	}

	@Override
	public void update(float delta) {
		setWidth(WIDTH);
		setRotation((float) (player.getRotation() + (Math.PI / 2)));
		setX((float) (player.getBulletOriginX()));
		setY((float) (player.getBulletOriginY()));
		setHeight((float) (0.75f + 0.25f * Math.sin(player.getAliveTime() * 10f)) * HEIGHT);
		TileCollisionDetector.checkTileCollisions(player.getGameContext().getLevel(), this, this.tileCollisionListener);
	}

	@Override
	public float getDamage() {
		return GunType.awp.getDamage();
	}

	@Override
	public Player getShooter() {
		return player;
	}

	@Override
	public void onImpact(Player target) {
		// TODO Auto-generated method stub

	}

	@Override
	public float getDirection() {
		return (float) (player.getRotation() + Math.PI/2);
	}

	@Override
	public void dispose() {
		super.dispose();
		disposed = true;
	}

}
