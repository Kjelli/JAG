package no.kash.gamedev.jag.game.gameobjects.bullets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;

import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.game.gamecontext.physics.Collidable;
import no.kash.gamedev.jag.game.gamecontext.physics.Collision;
import no.kash.gamedev.jag.game.gameobjects.AbstractGameObject;
import no.kash.gamedev.jag.game.gameobjects.players.Player;

public class Bullet extends AbstractGameObject implements Collidable {

	protected Player shooter;
	protected float damage;

	protected float speed = 300f;

	public Bullet(Player shooter, float x, float y, float direction) {
		super(x, y, 8, 2);
		this.shooter = shooter;

		Sprite sprite = new Sprite(Assets.bullet);
		sprite.setOrigin(getWidth() / 2, getHeight() / 2);
		setSprite(sprite);

		setRotation(direction + 90);
	}

	@Override
	public void update(float delta) {
		velocity.x = (float) (Math.cos((getRotation() * Math.PI / 180)) * speed);
		velocity.y = (float) (Math.sin((getRotation() * Math.PI / 180)) * speed);

		move(delta);

		outOfBounds();
	}

	private void outOfBounds() {
		if (getX() > Gdx.graphics.getWidth() || getX() < 0 || getY() > Gdx.graphics.getHeight() || getY() < 0) {
			destroy();
		}
	}

	@Override
	public void onCollide(Collision collision) {
		if (collision.getTarget() instanceof Player) {
			Player target = (Player) collision.getTarget();
			if (target.equals(shooter)) {
				return;
			}
			target.setScale(target.getScale() * 0.9f);
			destroy();
		}
	}

}
