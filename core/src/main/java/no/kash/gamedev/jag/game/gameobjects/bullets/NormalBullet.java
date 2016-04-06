package no.kash.gamedev.jag.game.gameobjects.bullets;

import com.badlogic.gdx.graphics.g2d.Sprite;

import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.game.gameobjects.players.Player;

public class NormalBullet extends AbstractBullet {

	public static final float WIDTH = 8, HEIGHT = 2;

	public NormalBullet(Player shooter, float x, float y, float direction, float damage, float speed) {
		super(shooter, x, y, WIDTH, HEIGHT, direction, damage, speed);
		setSprite(new Sprite(Assets.bullet));
	}

	@Override
	public void onImpact(Player target) {
		target.damage(this);
		destroy();
	}

}
