package no.kash.gamedev.jag.game.gameobjects.particles;

import no.kash.gamedev.jag.game.gameobjects.GameObject;

public interface Particle extends GameObject {
	float getTimeToLive();

	void onTimeout();
}
