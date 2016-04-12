package no.kash.gamedev.jag.game.gameobjects.grenades;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector;

import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.game.gamecontext.physics.tilecollisions.TileCollisionDetector;
import no.kash.gamedev.jag.game.gameobjects.bullets.Spike;
import no.kash.gamedev.jag.game.gameobjects.collectables.items.ItemType;
import no.kash.gamedev.jag.game.gameobjects.players.Player;

public class Snakebite extends AbstractGrenade{
	
	public static final float AIR_TIME = 1.3f, TIME_TO_LIVE_MAX = 1.3f;
	private static final float WIDTH = 16, HEIGHT = 16;
	
	public static final float SPIKESPEED = 500f;
	public static final float SPIKEAMOUNT = 16f;
	
	
	private  ItemType itemtype;

	public Snakebite(Player thrower, float x, float y, float direction, float power, ItemType type) {
		super(thrower, Assets.grenade, x, y, WIDTH, HEIGHT, direction, power, TIME_TO_LIVE_MAX);
		setSprite(new Sprite(Assets.snakebite));
		this.itemtype = type;
	}

	@Override
	public void timeOut() {
		float degreeIncrease = 360/SPIKEAMOUNT;
		float spikeDirection = 0;
		for(int i = 0; i < SPIKEAMOUNT; i++){
			Spike temp = new Spike(thrower, getX(), getY(), spikeDirection,itemtype.getMagnitude(), SPIKESPEED);
			thrower.getGameContext().spawn(temp);
			spikeDirection+=degreeIncrease;
		}
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
				bounceCooldown.start();
				if (Math.abs(col.normal.x) > Math.abs(col.normal.y)) {
					velocity().x *= -1;
				} else {
					velocity().y *= -1;
				}
			}
		}
		
	}

}
