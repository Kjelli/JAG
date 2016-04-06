package no.kash.gamedev.jag.game.gameobjects.bullets;

import com.badlogic.gdx.graphics.g2d.Sprite;

import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.game.commons.utils.Cooldown;
import no.kash.gamedev.jag.game.gameobjects.AbstractGameObject;
import no.kash.gamedev.jag.game.gameobjects.players.Player;

public class Fire extends AbstractGameObject {

	public static final float WIDTH = 8, HEIGHT = 8;

	private float direction;
	private float speed;
	protected Player shooter;

	private Cooldown livetime;

	public Fire(Player shooter, float x, float y, float direction, float speed) {
		super(x, y, WIDTH, HEIGHT);
		Sprite sprite = new Sprite(Assets.fire);
		this.shooter = shooter;
		this.direction = direction;
		this.speed = speed;
		setSprite(sprite);

		livetime = new Cooldown(1);
		livetime.startCooldown();

	}

	@Override
	public void update(float delta) {
		livetime.update(delta);
		if (!livetime.isOnCooldown()) {
			destroy();
		}

	}

}
