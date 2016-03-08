package no.kash.gamedev.jag.game.gamecontext.physics;

import java.util.Collection;

import no.kash.gamedev.jag.game.gameobjects.GameObject;

public class BruteForcePhysicsHandler implements PhysicsHandler {

	@Override
	public void collisonCheck(Collection<? extends GameObject> gameobjects) {
		for (GameObject one : gameobjects) {
			if (!(one instanceof Collidable)) {
				continue;
			}
			for (GameObject other : gameobjects) {
				if (!(other instanceof Collidable)) {
					continue;
				} else if (other == one) {
					continue;
				} else if (one.intersects(other)) {
					Collidable cgo1 = (Collidable) one;
					Collidable cgo2 = (Collidable) other;
					Collision col1 = new Collision(cgo1, cgo2);
					Collision col2 = new Collision(cgo2, cgo1);

					cgo1.onCollide(col1);
					cgo2.onCollide(col2);
				}
			}
		}
	}
}
