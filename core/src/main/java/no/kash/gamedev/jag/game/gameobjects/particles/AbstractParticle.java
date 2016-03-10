package no.kash.gamedev.jag.game.gameobjects.particles;

import no.kash.gamedev.jag.game.gameobjects.AbstractGameObject;

public abstract class AbstractParticle extends AbstractGameObject implements Particle {
	float timeToLive;
	boolean timedOut;

	public AbstractParticle(float x, float y, float width, float height, float timeToLive) {
		super(x, y, width, height);
		this.timeToLive = timeToLive;
	}

	@Override
	public void update(float delta) {
		if (timeToLive == -1 || timedOut) {
			// Do nothing
		} else if (timeToLive > 0) {
			timeToLive -= delta;
		} else {
			onTimeout();
			timedOut = true;
		}

		updateParticle(delta);
	}

	@Override
	public float getTimeToLive() {
		return timeToLive;
	}

	public abstract void updateParticle(float delta);

}
