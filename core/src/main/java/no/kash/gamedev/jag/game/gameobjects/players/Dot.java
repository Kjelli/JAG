package no.kash.gamedev.jag.game.gameobjects.players;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector;

import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.game.gamecontext.physics.tilecollisions.TileCollisionDetector;
import no.kash.gamedev.jag.game.gamecontext.physics.tilecollisions.TileCollisionListener;
import no.kash.gamedev.jag.game.gameobjects.AbstractGameObject;
import no.kash.gamedev.jag.game.gameobjects.GameObject;
import no.kash.gamedev.jag.game.levels.Level;

public class Dot extends AbstractGameObject {
	private final static int WIDTH = 1000;
	private final static int HEIGHT = 1;
	private Player player;
	public TileCollisionListener tileCollisionListener;
	boolean stillColliding = false;

	public Dot(Player player, float x, float y, float direction) {
		super(x, y, WIDTH, HEIGHT);
		this.player = player;
		Sprite dot = new Sprite(Assets.dot);
		setSprite(dot);
		setRotation(direction);

		this.tileCollisionListener = new TileCollisionListener() {
			@Override
			public void onCollide(MapObject rectangleObject, MinimumTranslationVector col) {
				stillColliding = true;
				String data = (String) rectangleObject.getProperties().get("collision_level");
				if (data != null) {
					if (Integer.parseInt(data) <= 2) {
						// TODO
						setWidth(getWidth() - 10);
						if (stillColliding) {
							stillColliding = false;
							TileCollisionDetector.checkTileCollisions(Dot.this.player.getGameContext().getLevel(),
									Dot.this, Dot.this.tileCollisionListener);
						}
					}
				}
			}

		};
	}

	@Override
	public void update(float delta) {
		setWidth(WIDTH);
		setRotation((float) (player.getRotation() + (Math.PI / 2)));
		getSprite().setOrigin(0, 0);
		setX(player.getCenterX());
		setY(player.getCenterY());
		TileCollisionDetector.checkTileCollisions(player.getGameContext().getLevel(), this, this.tileCollisionListener);
	}

}
