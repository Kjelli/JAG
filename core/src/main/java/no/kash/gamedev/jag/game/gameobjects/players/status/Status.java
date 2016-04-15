package no.kash.gamedev.jag.game.gameobjects.players.status;

import no.kash.gamedev.jag.game.commons.utils.Cooldown;
import no.kash.gamedev.jag.game.gameobjects.particles.Burn;
import no.kash.gamedev.jag.game.gameobjects.particles.HealingParticle;
import no.kash.gamedev.jag.game.gameobjects.particles.Poison;
import no.kash.gamedev.jag.game.gameobjects.players.Player;

public class Status {
	public Player causer;
	public StatusType type;
	public Cooldown duration;
	public Cooldown ticker;

	public boolean finished = false;

	public Status(Player causer, StatusType type, float duration) {
		this.type = type;
		this.causer = causer;
		this.duration = new Cooldown(duration);
		this.ticker = new Cooldown(0.5f);
	}

	public void update(float delta) {
		duration.update(delta);
		ticker.update(delta);
		if (!duration.isOnCooldown()) {
			finished = true;
		}
	}

	public void effect(Player player) {

		switch (type) {
		case burn:
			player.getGameContext()
					.spawn(new Burn(player.getCenterX() + (float) (Math.random() * Player.WIDTH / 2 - Player.WIDTH / 4),
							player.getCenterY() + (float) (Math.random() * Player.HEIGHT / 2 - Player.HEIGHT / 4)));
			if (!ticker.isOnCooldown()) {
				player.damage(this);
				ticker.start();
			}
			break;
		case poison:
			player.getGameContext()
					.spawn(new Poison(
							player.getCenterX() + (float) (Math.random() * Player.WIDTH / 2 - Player.WIDTH / 4),
							player.getCenterY() + (float) (Math.random() * Player.HEIGHT / 2 - Player.HEIGHT / 4)));
			if (!ticker.isOnCooldown()) {
				player.damage(this);
				ticker.start();
			}
			break;
		case healing:
			player.removeAllStatuses();
			player.getGameContext()
					.spawn(new HealingParticle(
							player.getCenterX() + (float) (Math.random() * Player.WIDTH / 2 - Player.WIDTH / 4),
							player.getCenterY() + (float) (Math.random() * Player.HEIGHT / 2 - Player.HEIGHT / 4)));
		case slow:
			break;
		case stun:
			break;
		default:
			break;
		}

	}
	
	public boolean isNegative(){
		return type.negative;
	}

	public float getDamage() {
		return type.damage;
	}
}
