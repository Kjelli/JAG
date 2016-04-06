package no.kash.gamedev.jag.game.gameobjects.players.status;

import no.kash.gamedev.jag.game.commons.utils.Cooldown;
import no.kash.gamedev.jag.game.gameobjects.particles.Burn;
import no.kash.gamedev.jag.game.gameobjects.players.Player;

public class Status {
	public Player causer;
	public StatusType type;
	public Cooldown duration;

	public boolean finished = false;

	public Status(Player causer, StatusType type, float duration) {
		this.type = type;
		this.causer = causer;
		this.duration = new Cooldown(duration);
	}

	public void update(float delta) {
		duration.update(delta);
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
			player.damage(this);
			break;
		case slow:
			break;
		case stun:
			break;
		default:
			break;
		}

	}

	public float getDamage() {
		return type.damage;
	}
}
