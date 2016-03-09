package no.kash.gamedev.jag.game.gameobjects.collectables;

import no.kash.gamedev.jag.game.gameobjects.players.Player;

public interface Collectable {

	// Can the player collect the gun? (Does the player already have the same
	// gun?)
	boolean canCollect(Player player);

	// A gun on the ground can be collected by the player
	void collect(Player player);
}
