package no.kash.gamedev.jag.game.gameobjects.particles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.commons.tweens.TweenGlobal;
import no.kash.gamedev.jag.commons.tweens.accessors.ColorAccessor;

public class BloodSplatter extends AbstractParticle {
	public static final int SPEED = 200;
	public static final float TIME_TO_LIVE = 0.5f;
	private static final float FADE_OUT_TIME = 10f;
	public float direction;

	private Color color;

	public BloodSplatter(float x, float y, float direction) {
		super(x, y, 8, 8, (float) (TIME_TO_LIVE * Math.random()));
		setSprite(new Sprite(Assets.blood));

		color = new Color(Color.WHITE);

		getSprite().setColor(color);

		this.direction = (float) direction;
		velocity.x = (float) Math.cos(direction) * SPEED;
		velocity.y = (float) Math.sin(direction) * SPEED;
		setRotation(direction);
	}
	
	@Override
	public void onSpawn() {
		getGameContext().bringToBack(this);
	}

	@Override
	public void updateParticle(float delta) {
		move(delta);
		if (!timedOut) {
			velocity.x = (float) Math.cos(direction) * SPEED * getTimeToLive() / TIME_TO_LIVE;
			velocity.y = (float) Math.sin(direction) * SPEED * getTimeToLive() / TIME_TO_LIVE;
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
