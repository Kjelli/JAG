package no.kash.gamedev.jag.game.gamecontext.physics;

import java.util.List;

import no.kash.gamedev.jag.game.gameobjects.GameObject;

public class BruteForcePhysicsHandler implements PhysicsHandler {

	@Override
	public void collisonCheck(List<? extends GameObject> gameobjects) {
		for (int i = 0; i < gameobjects.size(); i++) {
			GameObject one = gameobjects.get(i);
			if (!(one instanceof Collidable)) {
				continue;
			}
			for (int j = 0; j < gameobjects.size(); j++) {
				GameObject other = gameobjects.get(j);
				if (!(other instanceof Collidable)) {
					continue;
				} else if (other == one) {
					continue;
				} else if (one.intersects(other)) {
					Collidable cgo1 = (Collidable) one;
					Collidable cgo2 = (Collidable) other;
					Collision col1 = new Collision(cgo1, cgo2);

					cgo1.onCollide(col1);
				}
			}
		}
	}
}
