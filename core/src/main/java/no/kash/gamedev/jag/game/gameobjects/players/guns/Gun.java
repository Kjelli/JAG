package no.kash.gamedev.jag.game.gameobjects.players.guns;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.kash.gamedev.jag.game.gameobjects.players.Player;

public interface Gun {
	Magazine getMagazine();
	
	void update(float delta);

	void shoot();

	void reload();
	
	void draw(SpriteBatch batch);

	void setAmmo(int ammo);
	
	void equip(Player player);
}
