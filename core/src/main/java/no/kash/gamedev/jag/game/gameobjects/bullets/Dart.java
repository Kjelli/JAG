package no.kash.gamedev.jag.game.gameobjects.bullets;

import com.badlogic.gdx.graphics.g2d.Sprite;

import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.game.gameobjects.players.Player;
import no.kash.gamedev.jag.game.gameobjects.players.status.Status;
import no.kash.gamedev.jag.game.gameobjects.players.status.StatusType;

public class Dart extends AbstractBullet{
	
	protected final static int HEIGHT = 16;
	protected final static int WIDTH = 8;
	
	
	public Dart(Player shooter, float x, float y, float direction, float damage,
			float speed) {
		super(shooter, x, y, WIDTH, HEIGHT, direction, damage, speed);
		Sprite sprite = new Sprite(Assets.dart);
		setSprite(sprite);
		setRotation((float) (direction - Math.PI/2));
		
	}
	
	

	@Override
	public void onImpact(Player target) {
		target.applyStatus(new Status(shooter, StatusType.poison, 10f));
		target.damage(this);
		destroy();
	}

}
