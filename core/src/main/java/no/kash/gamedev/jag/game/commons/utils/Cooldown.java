package no.kash.gamedev.jag.game.commons.utils;

public class Cooldown {
	
	private float cooldown;
	private float cooldownTimer;
	
	
	public Cooldown(float coolDownDuration){
		this.cooldown = coolDownDuration;
	}
	
	public void update(float delta){
		if (cooldownTimer > 0) {
			cooldownTimer -= delta;
		} else {
			cooldownTimer = 0;
		}
		
	}
	
	public void start(){
		cooldownTimer = cooldown;
	}

	public float getCooldownTimer() {
		return cooldownTimer;
	}
	
	public boolean isOnCooldown(){
		return cooldownTimer>0;
	}
	
	public boolean isReady(){
		return !isOnCooldown();
	}

	public void reset() {
		cooldownTimer = 0;
	}


	
}
