package no.kash.gamedev.jag.game.gameobjects.grenades;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector;

import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.game.commons.utils.Cooldown;
import no.kash.gamedev.jag.game.gamecontext.physics.Collidable;
import no.kash.gamedev.jag.game.gamecontext.physics.Collision;
import no.kash.gamedev.jag.game.gamecontext.physics.tilecollisions.TileCollisionDetector;
import no.kash.gamedev.jag.game.gamecontext.physics.tilecollisions.TileCollisionListener;
import no.kash.gamedev.jag.game.gameobjects.AbstractGameObject;
import no.kash.gamedev.jag.game.gameobjects.players.Player;
import no.kash.gamedev.jag.game.gameobjects.players.guns.LaserSight;

public class TripLaser extends AbstractGameObject implements Collidable {

	TripMine mine;
	Cooldown activationTime;
	TileCollisionListener tileCollisionListener;
	boolean activated = false;

	boolean stillColliding = false;

	public TripLaser(TripMine mine, float x, float y, float rot) {
		super(x, y, 1000, 1);
		setSprite(new Sprite(Assets.laserSight));
		getSprite().setColor(1, 1, 1, 0);
		getSprite().setOrigin(0, 0);
		this.mine = mine;
		this.activationTime = new Cooldown(2.0f);
		getBounds().setOrigin(0, 0);
		setRotation(rot);
	}

	@Override
	public void onSpawn() {
		activationTime.start();
		this.tileCollisionListener = new TileCollisionListener() {
			@Override
			public void onCollide(MapObject rectangleObject, MinimumTranslationVector col) {
				stillColliding = true;
				String data = (String) rectangleObject.getProperties().get("collision_level");
				if (data != null) {
					if (Integer.parseInt(data) > 1) {
						setWidth(getWidth() - 2);
						if (stillColliding) {
							stillColliding = false;
							TileCollisionDetector.checkTileCollisions(getGameContext().getLevel(), TripLaser.this,
									TripLaser.this.tileCollisionListener);
						}
					}
				}
			}

		};
		TileCollisionDetector.checkTileCollisions(getGameContext().getLevel(), TripLaser.this,
				TripLaser.this.tileCollisionListener);
	}

	@Override
	public void update(float delta) {
		if (!activated) {
			activationTime.update(delta);
			if (!activationTime.isOnCooldown()) {
				activated = true;
				System.out.println("Activated");
				getSprite().setColor(1, 1, 1, 0.3f);
			}
		}
		if(!mine.isAlive()){
			destroy();
		}
	}

	@Override
	public void onCollide(Collision collision) {
		if (activationTime.isOnCooldown()) {
			return;
		}
		if (collision.getTarget() instanceof Player) {
			mine.blowUp();
			destroy();
		}
	}

}
