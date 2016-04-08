package no.kash.gamedev.jag.game.gameobjects.bullets;

import com.badlogic.gdx.graphics.g2d.Sprite;

import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.game.gameobjects.particles.Star;
import no.kash.gamedev.jag.game.gameobjects.players.Player;

public class GoldenBullet extends AbstractBullet {

	public static final float WIDTH = 8, HEIGHT = 2;

	public GoldenBullet(Player shooter, float x, float y, float direction, float damage, float speed) {
		super(shooter, x, y, WIDTH, HEIGHT, direction, damage, speed);
		setSprite(new Sprite(Assets.golden_bullet));
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		getGameContext().spawn(
				new Star(getCenterX() - Star.WIDTH * 0.5f / 2, getCenterY() - Star.HEIGHT * 0.5f / 2, 0, 0, 0.5f));
	}

	@Override
	public void onImpact(Player target) {
		target.damage(this);
		for (int i = 0; i < 250; i++) {
			getGameContext().spawn(new Star(target.getCenterX(), target.getCenterY(),
					(float) (((Math.random() > 0.5f ? 1 : -1)) * Math.random() * 300),
					(float) (((Math.random() > 0.5f ? 1 : -1)) * Math.random() * 300)));
		}
		destroy();
	}

}
