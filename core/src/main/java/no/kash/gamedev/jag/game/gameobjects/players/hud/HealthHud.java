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

	public static final int WIDTH = 20, HEIGHT = 4;

	private final Player player;
	private boolean visible;

	private final Sprite health, health_lost, health_bg, health_border;
	private Color color;

	private Tween tween = newFadeOutTween();

	private float movingHealthPercentage;

	public HealthHud(Player player, float x, float y) {
		super(x, y, WIDTH, HEIGHT);
		this.player = player;
		this.color = new Color(1, 1, 1, 0);
		health = new Sprite(Assets.health);
		health_lost = new Sprite(Assets.health_lost);
		health_bg = new Sprite(Assets.health_bg);
		health_border = new Sprite(Assets.health_border);
		movingHealthPercentage = player.getHealthPercentage();
	}

	static float factor = 0.90f;

	@Override
	public void update(float delta) {
		movingHealthPercentage = factor * movingHealthPercentage + (1 - factor) * player.getHealthPercentage();
	}

	@Override
	public void draw(SpriteBatch batch) {
		Draw.sprite(batch, health_bg, getX(), getY(), getWidth(), getHeight(), 1.0f, 1.0f, getRotation(), color, false);
		Draw.sprite(batch, health_lost, getX(), getY(), getWidth() * movingHealthPercentage, getHeight(), 1.0f, 1.0f,
				getRotation(), color, false);
		Draw.sprite(batch, health, getX(), getY(), getWidth() * player.getHealthPercentage(), getHeight(), 1.0f, 1.0f,
				getRotation(), color, false);
		Draw.sprite(batch, health_border, getX(), getY(), getWidth(), getHeight(), 1.0f, 1.0f, getRotation(), color,
				false);
	}

	public void display() {
		color.a = 1;
		visible = true;
		
		player.getGameContext().bringToFront(this);
		
		tween.kill();
		tween = newFadeOutTween();
		TweenGlobal.start(tween);
	}

	public boolean isVisible() {
		return visible;
	}

	private Tween newFadeOutTween() {
		return Tween.to(color, ColorAccessor.TYPE_RGBA, 1.0f).delay(1.0f).target(1, 1, 1, 0)
				.setCallback(new TweenCallback() {
					@Override
					public void onEvent(int arg0, BaseTween<?> arg1) {
						if (arg0 == TweenCallback.COMPLETE) {
							visible = false;
						}
					}
				});
	}

}
