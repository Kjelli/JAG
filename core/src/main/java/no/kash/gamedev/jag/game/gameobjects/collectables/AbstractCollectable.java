package no.kash.gamedev.jag.game.gameobjects.collectables;

import no.kash.gamedev.jag.game.gamecontext.physics.Collidable;
import no.kash.gamedev.jag.game.gamecontext.physics.Collision;
import no.kash.gamedev.jag.game.gameobjects.AbstractGameObject;
import no.kash.gamedev.jag.game.gameobjects.players.Player;

public abstract class AbstractCollectable extends AbstractGameObject implements Collectable, Collidable {

	boolean collected = false;

	public AbstractCollectable(float x, float y, float width, float height) {
		super(x, y, width, height);
	}

	@Override
	public void collect(Player player) {
		collected = true;
	}

	@Override
	public void onCollide(Collision collision) {
		if (collision.getTarget() instanceof Player) {
			if (!collected) {
				collect((Player) collision.getTarget());
			}
		}
	}

}
