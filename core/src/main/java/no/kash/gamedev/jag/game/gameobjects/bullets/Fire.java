package no.kash.gamedev.jag.game.gameobjects.bullets;

import com.badlogic.gdx.graphics.g2d.Sprite;

import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.game.commons.utils.Cooldown;
import no.kash.gamedev.jag.game.gameobjects.AbstractGameObject;
import no.kash.gamedev.jag.game.gameobjects.players.Player;

public class Fire extends AbstractBullet {

	public static final float WIDTH = 8, HEIGHT = 8;
	private static final float DAMAGE = 10;

	private float direction;
	private float speed;
	protected Player shooter;

	private Cooldown livetime;

	public Fire(Player shooter, float x, float y, float direction, float speed) {
		super(shooter, x, y, WIDTH, HEIGHT, direction, DAMAGE, speed);
		Sprite sprite = new Sprite(Assets.fire);
		this.shooter = shooter;
		this.direction = direction;
		this.speed = speed;
		setSprite(sprite);

		setRotation(direction * (10 + ((int) (Math.random() * 20))));
		livetime = new Cooldown(0.2f);
		livetime.startCooldown();

		acceleration.x = EPSILON;
		acceleration.y = EPSILON;
		velocity.x = (float) (Math.cos(direction) * speed);
		velocity.y = (float) (Math.sin(direction) * speed);
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		livetime.update(delta);
		if (!livetime.isOnCooldown()) {
			destroy();
		}

	}

	@Override
	public void onImpact(Player target) {
		target.damage(this);
		destroy();
	}

}
