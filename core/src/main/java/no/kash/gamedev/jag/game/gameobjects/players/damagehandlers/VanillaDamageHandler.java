package no.kash.gamedev.jag.game.gameobjects.players.damagehandlers;

import no.kash.gamedev.jag.game.gameobjects.bullets.Bullet;
import no.kash.gamedev.jag.game.gameobjects.bullets.NormalBullet;
import no.kash.gamedev.jag.game.gameobjects.grenades.Explosion;
import no.kash.gamedev.jag.game.gameobjects.players.Player;
import no.kash.gamedev.jag.game.gameobjects.players.status.Status;

public class VanillaDamageHandler implements DamageHandler {

	private final Player player;

	public VanillaDamageHandler(Player player) {
		this.player = player;
	}

	@Override
	public void onDamage(Bullet bullet) {
		player.setHealth(player.getHealth() - bullet.getDamage());
		if (player.getHealth() <= 0) {
			player.death(bullet.getShooter());
		}
	}

	@Override
	public void onDamage(Explosion explosion) {
		float damage = 100 - Math.min(player.distanceTo(explosion), 100);
		player.setHealth(player.getHealth() - damage);
		if (player.getHealth() <= 0) {
			player.death(explosion.getSourceGrenade().getThrower());
		}
	}

	@Override
	public void onDamage(Status status) {
		player.setHealth(player.getHealth() - status.getDamage());
		if (player.getHealth() <= 0) {
			player.death(status);
		}
	}

}
