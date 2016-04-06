package no.kash.gamedev.jag.game.lobby;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.game.gameobjects.players.PlayerInfo;
import no.kash.gamedev.jag.game.gamesession.GameSession;

public class GameSessionInfoGUI {
	public static final float WIDTH = 250, HEIGHT = 300;
	private static final float BORDER_WIDTH = 7;

	float x, y, width, height;
	public final BitmapFont fontLarge = Assets.font;
	public final BitmapFont font = Assets.fontSmall;

	public GlyphLayout sessionLabel;

	public GlyphLayout gameModeLabel;
	public GlyphLayout roundTimeLabel;
	public GlyphLayout winLimitLabel;
	public GlyphLayout friendlyFireLabel;
	public GlyphLayout testModeLabel;
	public GlyphLayout dropInLabel;
	public GlyphLayout startingHealthLabel;
	public GlyphLayout drawNamesLabel;

	Sprite card_border, card_bg;

	private GameSession session;

	public GameSessionInfoGUI(float x, float y, GameSession session) {
		this.x = x;
		this.y = y;
		this.width = WIDTH;
		this.height = HEIGHT;
		this.session = session;
		this.sessionLabel = new GlyphLayout(fontLarge, "Game settings", Color.BLACK, -1, -1, false);
		this.gameModeLabel = new GlyphLayout(font, gameModeString + session.gameMode.displayName, Color.BLACK, -1, -1,
				false);
		this.roundTimeLabel = new GlyphLayout(font, roundTimeString + session.roundTime + "s", Color.BLACK, -1, -1,
				false);
		this.winLimitLabel = new GlyphLayout(font, winLimitString + session.roundsToWin, Color.BLACK, -1, -1, false);
		this.testModeLabel = new GlyphLayout(font, testModeString + (session.testMode ? "on" : "off"), Color.BLACK, -1, -1, false);
		this.dropInLabel = new GlyphLayout(font, dropInString + (session.dropIn ? "on" : "off"), Color.BLACK, -1, -1,
				false);
		this.startingHealthLabel = new GlyphLayout(font, startingHealthString + session.startingHealth, Color.BLACK, -1,
				-1, false);
		this.drawNamesLabel = new GlyphLayout(font, drawNameString + (session.drawNames ? "on" : "off"), Color.BLACK,
				-1, -1, false);

		card_border = new Sprite(Assets.card_xl_border);
		card_border.setX(x);
		card_border.setY(y - HEIGHT);
		card_bg = new Sprite(Assets.card_xl_bg);
		card_bg.setX(x);
		card_bg.setY(y - HEIGHT);
	}

	private String drawNameString = "Show names:          ";
	private String startingHealthString = "Starting health: ";
	private String dropInString = "Drop in:             ";
	private String testModeString = "Test mode:           ";
	private String winLimitString = "Win limit:       ";
	private String roundTimeString = "Round time:      ";
	private String gameModeString = "Game mode:       ";

	public void refresh() {
		sessionLabel.setText(fontLarge, "Game settings", Color.BLACK, -1, -1, false);
		gameModeLabel.setText(font, gameModeString + session.gameMode.displayName, Color.BLACK, -1, -1, false);
		roundTimeLabel.setText(font, roundTimeString + session.roundTime + "s", Color.BLACK, -1, -1, false);
		winLimitLabel.setText(font, winLimitString + session.roundsToWin, Color.BLACK, -1, -1, false);
		testModeLabel.setText(font, testModeString + (session.testMode ? "on" : "off"), Color.BLACK, -1, -1, false);
		dropInLabel.setText(font, dropInString + (session.dropIn ? "on" : "off"), Color.BLACK, -1, -1, false);
		startingHealthLabel.setText(font, startingHealthString + session.startingHealth, Color.BLACK, -1, -1, false);
		drawNamesLabel.setText(font, drawNameString + (session.drawNames ? "on" : "off"), Color.BLACK, -1, -1, false);
	}

	public void draw(SpriteBatch batch) {
		card_bg.draw(batch);
		card_border.draw(batch);

		float spacing = font.getCapHeight() + 2;
		fontLarge.draw(batch, sessionLabel, x + 2 * BORDER_WIDTH, y - 1 * spacing);
		font.draw(batch, gameModeLabel, x + 2 * BORDER_WIDTH, y - 3 * spacing);
		font.draw(batch, roundTimeLabel, x + 2 * BORDER_WIDTH, y - 4 * spacing);
		font.draw(batch, winLimitLabel, x + 2 * BORDER_WIDTH, y - 5 * spacing);
		font.draw(batch, startingHealthLabel, x + 2 * BORDER_WIDTH, y - 6 * spacing);
		font.draw(batch, drawNamesLabel, x + 2 * BORDER_WIDTH, y - 14 * spacing);
		font.draw(batch, dropInLabel, x + 2 * BORDER_WIDTH, y - 15 * spacing);
		font.draw(batch, testModeLabel, x + 2 * BORDER_WIDTH, y - 16 * spacing);
	}
}
