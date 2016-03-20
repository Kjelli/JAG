package no.kash.gamedev.jag.game.gameobjects.particles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.commons.tweens.TweenGlobal;
import no.kash.gamedev.jag.commons.tweens.accessors.ColorAccessor;

public class WeaponSpawnEffect extends AbstractParticle {
	private static final float FADE_OUT_TIME = 2f;
	private float scalingValue;
	private Color color;

	float startingX, startingY;

	public WeaponSpawnEffect(float x, float y) {
		super(x, y, 32, 32, FADE_OUT_TIME);
		setSprite(new Sprite(Assets.wepSpawnEffect));
		
		color = new Color(1, 1, 1, 1);
		scalingValue = 0.66f;
		startingX = x;
		startingY = y;
	}

	@Override
	public void onSpawn() {
		setScale(scalingValue);
		setX(startingX - getWidth() / 2);
		setY(startingY - getHeight() / 2);
		TweenGlobal.start(Tween.to(color, ColorAccessor.TYPE_RGBA, FADE_OUT_TIME).target(1, 1, 1, 0));
	}

	@Override
	public void onTimeout() {
		destroy();
	}

	@Override
	public void updateParticle(float delta) {
		scalingValue += delta * 0.25f;
		setScale(scalingValue);
		getSprite().setColor(color);
		setX(startingX - getWidth() / 2);
		setY(startingY - getHeight() / 2);
	}

}
