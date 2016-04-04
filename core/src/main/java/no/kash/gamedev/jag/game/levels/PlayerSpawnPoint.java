package no.kash.gamedev.jag.game.levels;

import com.badlogic.gdx.math.Vector2;

public class PlayerSpawnPoint {
	public Vector2 position;
	public boolean taken;

	public PlayerSpawnPoint(float x, float y) {
		position = new Vector2(x, y);
		taken = false;
	}
}
