package no.kash.gamedev.jag.game.lobby;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.kash.gamedev.jag.assets.Assets;

public class PlayerInfoGUI {

	public static final float WIDTH = 250, HEIGHT = 100;
	private static final float BORDER_WIDTH = 7;

	public static BitmapFont font = Assets.font;
	public static BitmapFont fontSmall = Assets.fontSmall;

	float x, y, width, height;

	Sprite card_border, card_bg;
	GlyphLayout nameLabel;
	GlyphLayout idLabel;

	PlayerInfo info;

	public PlayerInfoGUI(float x, float y, PlayerInfo info) {
		this.x = x;
		this.y = y;
		this.width = WIDTH;
		this.height = HEIGHT;
		this.info = info;
		this.nameLabel = new GlyphLayout(font, info.name, Color.BLACK, -1, -1, false);
		this.idLabel = new GlyphLayout(fontSmall, "id: " + info.id, Color.BLACK, -1, -1, false);
		card_border = new Sprite(Assets.card_border);
		card_border.setX(x);
		card_border.setY(y - HEIGHT);
		card_bg = new Sprite(Assets.card_bg);
		card_bg.setX(x);
		card_bg.setY(y - HEIGHT);
	}

	public void draw(SpriteBatch batch) {
		card_bg.draw(batch);
		card_border.draw(batch);
		font.draw(batch, nameLabel, x + BORDER_WIDTH, y - nameLabel.height / 2);
		fontSmall.draw(batch, idLabel, x + BORDER_WIDTH, y - nameLabel.height / 2 - 20);
	}

	public void setInfo(PlayerInfo info) {
		this.info = info;
		refreshInfo();
	}

	private void refreshInfo() {
		idLabel.setText(fontSmall, "id: " + info.id, Color.BLACK, -1, -1, false);
		nameLabel.setText(font, info.name, Color.BLACK, -1, -1, false);
	}

}