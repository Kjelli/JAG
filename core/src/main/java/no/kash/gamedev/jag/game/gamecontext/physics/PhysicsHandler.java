package no.kash.gamedev.jag.game.gamecontext.physics;

import java.util.Collection;

import no.kash.gamedev.jag.game.gameobjects.GameObject;

public interface PhysicsHandler {
	void collisonCheck(Collection<? extends GameObject> gameobjects);
}