package no.kash.gamedev.jag.game.lobby;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.game.gameobjects.players.PlayerInfo;

public class PlayerInfoGUI {

	public static final float WIDTH = 250, HEIGHT = 100;
	private static final float BORDER_WIDTH = 7;

	public static BitmapFont font = Assets.font;
	public static BitmapFont fontSmall = Assets.fontSmall;

	float x, y, width, height;

	Sprite card_border, card_bg, card_border_ready;
	GlyphLayout nameLabel;
	GlyphLayout idLabel;
	GlyphLayout timesPlayedLabel;
	GlyphLayout levelLabel;

	PlayerInfo info;

	public PlayerInfoGUI(float x, float y, PlayerInfo info) {
		this.x = x;
		this.y = y;
		this.width = WIDTH;
		this.height = HEIGHT;
		this.info = info;
		this.nameLabel = new GlyphLayout(font, info.name, Color.BLACK, -1, -1, false);
		this.idLabel = new GlyphLayout(fontSmall, "ID: " + info.id, Color.BLACK, -1, -1, false);
		this.timesPlayedLabel = new GlyphLayout(fontSmall, "Times played:    " + info.timesPlayed, Color.BLACK, -1, -1,
				false);
		this.levelLabel = new GlyphLayout(fontSmall, "Level:           " + info.level, Color.BLACK, -1, -1, false);
		card_border = new Sprite(Assets.card_long_border);
		card_border.setX(x);
		card_border.setY(y - HEIGHT);
		card_border_ready = new Sprite(Assets.card_long_border_ready);
		card_border_ready.setX(x);
		card_border_ready.setY(y - HEIGHT);
		card_bg = new Sprite(Assets.card_long_bg);
		card_bg.setX(x);
		card_bg.setY(y - HEIGHT);
	}

	public void draw(SpriteBatch batch) {

		float spacing = fontSmall.getCapHeight() + 2.0f;

		card_bg.draw(batch);
		if (info.ready) {
			card_border_ready.draw(batch);
		} else {
			card_border.draw(batch);
		}
		font.draw(batch, nameLabel, x + BORDER_WIDTH, y - nameLabel.height / 2);
		fontSmall.draw(batch, idLabel, x + width - idLabel.width - BORDER_WIDTH, y - idLabel.height / 2);
		fontSmall.draw(batch, timesPlayedLabel, x + BORDER_WIDTH, y - spacing * 3);
		fontSmall.draw(batch, levelLabel, x + BORDER_WIDTH, y - spacing * 2);
	}

	public void setInfo(PlayerInfo info) {
		this.info = info;
		refreshInfo();
	}

	private void refreshInfo() {
		nameLabel.setText(font, info.name, Color.BLACK, -1, -1, false);
		idLabel.setText(fontSmall, "ID: " + info.id, Color.BLACK, -1, -1, false);
		timesPlayedLabel.setText(fontSmall, "Times played:    " + info.timesPlayed, Color.BLACK, -1, -1, false);
		card_bg.setColor(info.color);
		levelLabel.setText(fontSmall, "Level:           " + info.level, Color.BLACK, -1, -1, false);
	}

	public PlayerInfo getInfo() {
		return info;
	}

	public void nudgeUp() {
		y = y + height;
		card_bg.setY(y - HEIGHT);
		card_border.setY(y - HEIGHT);
		card_border_ready.setY(y - HEIGHT);
	}

}
