package no.kash.gamedev.jag.game.gameobjects.bullets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;

import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.game.commons.utils.Cooldown;
import no.kash.gamedev.jag.game.gameobjects.players.Player;
import no.kash.gamedev.jag.game.gameobjects.players.status.Status;
import no.kash.gamedev.jag.game.gameobjects.players.status.StatusType;

public class Fire extends AbstractBullet {

	public static final float WIDTH = 8, HEIGHT = 8;
	private static final float DAMAGE = 0.1f;

	private static final float FADE_OUT_TIME = 1f;

	private float direction;
	private float speed;
	protected Player shooter;

	private Cooldown livetime;
	private Color color;

	public Fire(Player shooter, float x, float y, float direction, float speed) {
		super(shooter, x, y, WIDTH, HEIGHT, direction, DAMAGE, speed);
		Sprite sprite = new Sprite(Assets.fire);
		this.shooter = shooter;
		this.direction = direction;
		this.speed = speed;
		setSprite(sprite);
		this.color = new Color(Color.WHITE);

		setRotation(direction * (10 + ((int) (Math.random() * 20))));
		livetime = new Cooldown(FADE_OUT_TIME);
		livetime.start();

		acceleration.x = EPSILON;
		acceleration.y = EPSILON;
		velocity.x = (float) (Math.cos(direction) * speed) + shooter.velocity().x;
		velocity.y = (float) (Math.sin(direction) * speed) + shooter.velocity().y;
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		livetime.update(delta);
		color.a = (float) Math.sqrt(livetime.getCooldownTimer() / FADE_OUT_TIME);
		sprite.setColor(color);
		if (!livetime.isOnCooldown()) {
			destroy();
		}

	}

	@Override
	public void onImpact(Player target) {
		target.applyStatus(new Status(shooter, StatusType.burn, 3.0f));
		target.damage(this);
	}

}
