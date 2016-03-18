package no.kash.gamedev.jag.game.gameobjects.particles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.commons.tweens.TweenGlobal;
import no.kash.gamedev.jag.commons.tweens.accessors.ColorAccessor;

public class WeaponSpawnEffect extends AbstractParticle{
	private static final float FADE_OUT_TIME = 2f;
	private float scalingValue;
	private Color color;
	
	
	public WeaponSpawnEffect(float x, float y, float width, float height, float timeToLive) {
		super(x, y, width, height, timeToLive);
		setSprite(new Sprite(Assets.wepSpawnEffect));
		scalingValue = 0.25f;
		setScale(scalingValue);
	}
	
	public void draw(SpriteBatch batch) {
		batch.draw(getSprite(),getX(),getY());
	}


	@Override
	public void onTimeout() {
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

	@Override
	public void updateParticle(float delta) {
	}

}
