package no.kash.gamedev.jag.game.gameobjects.players.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.commons.graphics.Draw;
import no.kash.gamedev.jag.commons.tweens.TweenGlobal;
import no.kash.gamedev.jag.commons.tweens.accessors.ColorAccessor;
import no.kash.gamedev.jag.game.gameobjects.AbstractGameObject;
import no.kash.gamedev.jag.game.gameobjects.players.Player;

public class HealthHud extends AbstractGameObject {

	public static final int WIDTH = 64, HEIGHT = 8;

	private final Player player;
	private boolean visible;

	private final Sprite health, health_border, health_shine;
	private Color color;

	public HealthHud(Player player, float x, float y) {
		super(x, y, WIDTH, HEIGHT);
		this.player = player;
		this.color = new Color(1, 1, 1, 1);
		health = new Sprite(Assets.health);
		health_border = new Sprite(Assets.health_border);
		health_shine = new Sprite(Assets.health_shine);
	}

	@Override
	public void update(float delta) {

	}

	@Override
	public void draw(SpriteBatch batch) {
		Draw.sprite(batch, health, getX(), getY(), getWidth(), getHeight(), getRotation(), color, false);
		Draw.sprite(batch, health_border, getX(), getY(), getWidth(), getHeight(), getRotation());
		Draw.sprite(batch, health_shine, getX(), getY(), getWidth(), getHeight(), getRotation());

	}

	public void display() {
		color.a = 1;
		visible = true;
		TweenGlobal.start(Tween.to(color, ColorAccessor.TYPE_RGBA, 3.0f).delay(3.0f).target(1, 1, 1, 0)
				.setCallback(new TweenCallback() {

					@Override
					public void onEvent(int arg0, BaseTween<?> arg1) {
						if (arg0 == TweenCallback.COMPLETE) {
							visible = false;
						}
					}
				}));
	}

	public boolean isVisible() {
		return visible;
	}

}
