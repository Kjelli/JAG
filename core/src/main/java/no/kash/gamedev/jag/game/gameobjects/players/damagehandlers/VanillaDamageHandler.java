package no.kash.gamedev.jag.game.gameobjects.players.damagehandlers;

import no.kash.gamedev.jag.game.gameobjects.bullets.Bullet;
import no.kash.gamedev.jag.game.gameobjects.particles.Explosion;
import no.kash.gamedev.jag.game.gameobjects.players.Player;

public class VanillaDamageHandler implements DamageHandler {

	private final Player player;

	public VanillaDamageHandler(Player player) {
		this.player = player;
	}

	@Override
	public void onDamage(Bullet bullet) {
		player.setHealth(player.getHealth() - bullet.getDamage());
	}

	@Override
	public void onDamage(Explosion explosion) {
		float damage = 100 - Math.min(player.distanceTo(explosion), 100);
		player.setHealth(player.getHealth() - damage);
	}

}
