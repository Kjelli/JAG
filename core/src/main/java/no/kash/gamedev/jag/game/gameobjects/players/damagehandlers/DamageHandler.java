package no.kash.gamedev.jag.game.gameobjects.players.damagehandlers;

import no.kash.gamedev.jag.game.gameobjects.bullets.Bullet;
import no.kash.gamedev.jag.game.gameobjects.particles.Explosion;

public interface DamageHandler {
	void onDamage(Bullet bullet);

	void onDamage(Explosion bullet);
}
