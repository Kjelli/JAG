package no.kash.gamedev.jag.game.gameobjects.grenades;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector;

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
	public TripLaser(TripMine mine, float x, float y, float width, float height) {
		super(x, y, width, height);
		this.mine = mine;

		this.tileCollisionListener = new TileCollisionListener() {
			@Override
			public void onCollide(MapObject rectangleObject, MinimumTranslationVector col) {
				stillColliding = true;
				String data = (String) rectangleObject.getProperties().get("collision_level");
				if (data != null) {
					if (Integer.parseInt(data) > 1) {
						// TODO
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
	public void onSpawn() {
		getSprite().setColor(1,1,1,0);
	}

	@Override
	public void update(float delta) {
		move(delta);
		
		if(!activated){
			activationTime.update(delta);
			if(!activationTime.isOnCooldown()){
				activated = true;
				getSprite().setColor(1,1,1,0.3f);
			}
		}
	}

	@Override
	public void onCollide(Collision collision) {
		if(activationTime.isOnCooldown()){
			return;
		}
		if (collision.getTarget() instanceof Player) {
			Player p = (Player) collision.getTarget();

		}
	}

}
