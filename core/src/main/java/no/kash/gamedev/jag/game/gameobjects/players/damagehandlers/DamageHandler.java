package no.kash.gamedev.jag.game.gameobjects.players.damagehandlers;

import no.kash.gamedev.jag.game.gameobjects.bullets.Bullet;
import no.kash.gamedev.jag.game.gameobjects.bullets.NormalBullet;
import no.kash.gamedev.jag.game.gameobjects.grenades.Explosion;
import no.kash.gamedev.jag.game.gameobjects.players.status.Status;

public interface DamageHandler {
	void onDamage(Bullet bullet);

	void onDamage(Explosion bullet);

	void onDamage(Status status);
}
