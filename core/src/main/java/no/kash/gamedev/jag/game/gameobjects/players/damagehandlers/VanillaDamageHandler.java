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
		System.out.print(player.getHealth() + " -> ");
		player.setHealth(player.getHealth() - bullet.getDamage());
		System.out.println(player.getHealth());
	}

	@Override
	public void onDamage(Explosion bullet) {

	}

}
