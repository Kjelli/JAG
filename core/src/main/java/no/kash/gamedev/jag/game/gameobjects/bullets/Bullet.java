package no.kash.gamedev.jag.game.gameobjects.bullets;

import no.kash.gamedev.jag.game.gameobjects.GameObject;
import no.kash.gamedev.jag.game.gameobjects.players.Player;

public interface Bullet extends GameObject {

	float getDamage();
	Player getShooter();
	void onImpact(Player target);
	float getDirection();

}
