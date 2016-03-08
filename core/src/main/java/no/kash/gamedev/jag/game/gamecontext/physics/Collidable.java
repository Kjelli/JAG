package no.kash.gamedev.jag.game.gamecontext.physics;

import no.kash.gamedev.jag.game.gameobjects.GameObject;

public interface Collidable extends GameObject {
	void onCollide(Collision collision);
}