package no.kash.gamedev.jag.game.gameobjects.particles;

public interface Particle {
	float getTimeToLive();

	void onTimeout();
}
