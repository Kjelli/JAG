package no.kash.gamedev.jag.game.gamecontext.functions;

public class Cooldown {
	
	private float cooldown;
	private float cooldownTimer;
	
	private float coolDownDuration;
	
	public Cooldown(float coolDownDuration){
		this.coolDownDuration = coolDownDuration;
	}
	
	public void update(float delta){
		if (cooldownTimer > 0) {
			cooldownTimer -= delta;
		} else {
			cooldownTimer = 0;
		}
		if (coolDownDuration > 0) {
			coolDownDuration -= delta;
		} else {
			coolDownDuration = 0;
		}
	}
}
