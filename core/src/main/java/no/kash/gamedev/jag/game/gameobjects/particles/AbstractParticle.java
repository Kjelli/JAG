package no.kash.gamedev.jag.game.gameobjects.particles;

import no.kash.gamedev.jag.game.gameobjects.AbstractGameObject;

public abstract class AbstractParticle extends AbstractGameObject implements Particle {
	float timeToLiveTimer;
	boolean timedOut;

	public AbstractParticle(float x, float y, float width, float height, float timeToLive) {
		super(x, y, width, height);
		this.timeToLiveTimer = timeToLive;
	}

	@Override
	public void update(float delta) {
		if (timeToLiveTimer == -1 || timedOut) {
			// Do nothing
		} else if (timeToLiveTimer > 0) {
			timeToLiveTimer -= delta;
		} else {
			onTimeout();
			timedOut = true;
		}

		updateParticle(delta);
	}

	@Override
	public float getTimeToLive() {
		return timeToLiveTimer;
	}

	public abstract void updateParticle(float delta);

}
