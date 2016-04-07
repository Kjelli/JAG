package no.kash.gamedev.jag.game.gameobjects.particles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.commons.tweens.TweenGlobal;
import no.kash.gamedev.jag.commons.tweens.accessors.ColorAccessor;
import no.kash.gamedev.jag.game.gamecontext.physics.tilecollisions.TileCollisionDetector;
import no.kash.gamedev.jag.game.gamecontext.physics.tilecollisions.TileCollisionListener;
import no.kash.gamedev.jag.game.gameobjects.GameObject;
import no.kash.gamedev.jag.game.gameobjects.players.Player;

public class BloodSplatter extends AbstractParticle {

	public TileCollisionListener tileCollisionListener = new TileCollisionListener() {
		@Override
		public void onCollide(MapObject rectangleObject, MinimumTranslationVector intersection) {
			velocity.x = 0;
			velocity.y = 0;
			stopped = true;
		}
	};

	public static final int BASE_SPEED = 20;
	public static final float TIME_TO_LIVE = 0.5f;
	private static final float FADE_OUT_TIME = 60f;
	public float direction;
	public float power;
	public boolean stopped = false;

	private Color color;

	public BloodSplatter(float x, float y, float direction, float power) {
		super(x, y, 8, 8, (float) (TIME_TO_LIVE * Math.random()));
		setSprite(new Sprite(Assets.blood));

		this.color = new Color(Color.WHITE);
		this.power = power;
		this.direction = (float) direction;

		getSprite().setColor(color);
		velocity.x = (float) Math.cos(direction) * BASE_SPEED * power;
		velocity.y = (float) Math.sin(direction) * BASE_SPEED * power;
		setRotation(direction);

	}

	@Override
	public void onSpawn() {
		getGameContext().bringToBack(this);
	}

	@Override
	public void updateParticle(float delta) {
		move(delta);
		if (!timedOut && !stopped) {
			velocity.x = (float) Math.cos(direction) * BASE_SPEED * power * getTimeToLive() / TIME_TO_LIVE;
			velocity.y = (float) Math.sin(direction) * BASE_SPEED * power * getTimeToLive() / TIME_TO_LIVE;
			TileCollisionDetector.checkTileCollisions(getGameContext().getLevel(), this, tileCollisionListener);
		}
		getSprite().setColor(color);
	}

	@Override
	public void onTimeout() {
		velocity.x = 0;
		velocity.y = 0;
		TweenGlobal.start(Tween.to(color, ColorAccessor.TYPE_RGBA, FADE_OUT_TIME).target(0, 0, 0, 0)
				.setCallback(new TweenCallback() {

					@Override
					public void onEvent(int arg0, BaseTween<?> arg1) {
						if (arg0 == TweenCallback.COMPLETE) {
							destroy();
						}
					}
				}));
	}

}
