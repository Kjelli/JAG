package no.kash.gamedev.jag.game.lobby;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.kash.gamedev.jag.assets.Assets;

public class PlayerInfo {

	public static final float WIDTH = 250, HEIGHT = 100;

	float x, y, width, height;

	static BitmapFont font = Assets.font;

	int id;
	GlyphLayout name;

	public PlayerInfo(float x, float y, int id, String name) {
		this.x = x;
		this.y = y;
		this.width = WIDTH;
		this.height = HEIGHT;
		this.id = id;
		this.name = new GlyphLayout(font, id + ": " + name);
	}

	public void draw(SpriteBatch batch) {
		font.draw(batch, name, x, y);
	}
	
}
