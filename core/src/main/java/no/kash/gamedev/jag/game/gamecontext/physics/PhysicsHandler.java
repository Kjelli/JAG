package no.kash.gamedev.jag.game.gamecontext.physics;

import java.util.List;

import no.kash.gamedev.jag.game.gameobjects.GameObject;

public interface PhysicsHandler {
	void collisonCheck(List<? extends GameObject> gameobjects);
}