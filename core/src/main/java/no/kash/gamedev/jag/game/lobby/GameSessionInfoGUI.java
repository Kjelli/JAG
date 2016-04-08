package no.kash.gamedev.jag.game.lobby;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.game.gamesession.GameSession;
import no.kash.gamedev.jag.game.gamesession.GameSettings.Setting;

public class GameSessionInfoGUI {
	public static final float WIDTH = 250, HEIGHT = 300;
	private static final float BORDER_WIDTH = 7;

	float x, y, width, height;
	public final BitmapFont fontLarge = Assets.font;
	public final BitmapFont font = Assets.fontSmall;

	public GlyphLayout sessionLabel;

	public GlyphLayout[] settingLabels;
	public GlyphLayout[] valueLabels;

	Sprite card_border, card_bg;

	private GameSession session;

	public GameSessionInfoGUI(float x, float y, GameSession session) {
		this.x = x;
		this.y = y;
		this.width = WIDTH;
		this.height = HEIGHT;
		this.session = session;
		this.sessionLabel = new GlyphLayout(fontLarge, "Game settings", Color.BLACK, -1, -1, false);

		settingLabels = new GlyphLayout[session.settings.settings.size()];	
		valueLabels = new GlyphLayout[session.settings.settings.size()];
		
		int index = 0;
		for (Setting<?> setting : session.settings.settings.values()) {
			settingLabels[index] = new GlyphLayout(font,
					setting.displayName + ": ", Color.BLACK, -1, -1, false);
			valueLabels[index] = new GlyphLayout(font,  setting.getSelection().displayName, Color.BLACK, -1, -1, false);
			index++;
		}

		card_border = new Sprite(Assets.card_xl_border);
		card_border.setX(x);
		card_border.setY(y - HEIGHT);
		card_bg = new Sprite(Assets.card_xl_bg);
		card_bg.setX(x);
		card_bg.setY(y - HEIGHT);
	}

	public void refresh() {
		sessionLabel.setText(fontLarge, "Game settings", Color.BLACK, -1, -1, false);
		int index = 0;
		for (Setting<?> setting : session.settings.settings.values()) {
			settingLabels[index].setText(font, setting.displayName + ": ",
					Color.BLACK, -1, -1, false);
			valueLabels[index].setText(font,  setting.getSelection().displayName, Color.BLACK, -1, -1, false);
			index++;
		}
	}

	public void draw(SpriteBatch batch) {
		card_bg.draw(batch);
		card_border.draw(batch);

		float spacing = font.getCapHeight() + 2;
		fontLarge.draw(batch, sessionLabel, x + 2 * BORDER_WIDTH, y - 1 * spacing);
		for (int i = 0; i < settingLabels.length; i++) {
			font.draw(batch, settingLabels[i], x + 2 * BORDER_WIDTH, y - (i + 3) * spacing - i/4 * spacing);
		}
		for (int i = 0; i < valueLabels.length; i++) {
			font.draw(batch, valueLabels[i], x + 22 * BORDER_WIDTH, y - (i + 3) * spacing - i/4 * spacing);
		}
	}
}
