package no.kash.gamedev.jag.controller.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.esotericsoftware.kryonet.Connection;

import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.commons.defs.Defs;
import no.kash.gamedev.jag.commons.defs.Prefs;
import no.kash.gamedev.jag.commons.network.JagClientPacketHandler;
import no.kash.gamedev.jag.commons.network.packets.GamePacket;
import no.kash.gamedev.jag.commons.network.packets.PlayerUpdate;
import no.kash.gamedev.jag.commons.utils.Callback;
import no.kash.gamedev.jag.controller.JustAnotherGameController;
import no.kash.gamedev.jag.controller.Player;
import no.kash.gamedev.jag.controller.lobby.ColorPicker;
import no.kash.gamedev.jag.controller.lobby.ColorPicker.ColorOption;
import no.kash.gamedev.jag.controller.lobby.GameSessionControls;

public class LobbyControllerScreen extends AbstractControllerScreen {

	// TODO implement gamesetting changer aka gameMaster
	private boolean gameMaster = false;
	GameSessionControls sessionControls;

	GlyphLayout lobbyLabel;
	BitmapFont font;
	Skin skin;

	// StandardView

	Label nameLabel;
	TextField nameField;

	Label levelLabel;
	Label expLabel;

	TextButton updateNameButton;
	TextButton newColors;
	CheckBox ready;
	ColorPicker picker;

	TextButton settings;

	// SettingsView
	ScrollPane container;
	Table scrollingTable;
	TextButton gameMode;
	TextButton dropIn;
	TextButton roundTime;
	TextButton roundsToWin;
	TextButton suddenDeath;
	TextButton testMode;
	TextButton startingHealth;
	TextButton back;

	boolean firstFrame = true;

	public LobbyControllerScreen(JustAnotherGameController game) {
		super(game);
	}

	@Override
	protected void update(float delta) {
		if (firstFrame) {
			sendUpdate();
			firstFrame = false;
		}
	}

	@Override
	protected void draw(float delta) {
		font.draw(batch, lobbyLabel, stage.getWidth() / 2 - lobbyLabel.width / 2,
				stage.getHeight() - lobbyLabel.height);
	}

	@Override
	protected void onShow() {
		Player.load();
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		font = Assets.font;

		lobbyLabel = new GlyphLayout(font, "Lobby");

		initStandardView();
		initSettingsView();
		setStandardView();
		// Testing purposes:
		// setSettingsView();

		game.setReceiver(new JagClientPacketHandler() {

			@Override
			public void handlePacket(Connection c, GamePacket m) {
				if (m instanceof PlayerUpdate) {
					PlayerUpdate update = (PlayerUpdate) m;
					if (update.fieldId[0] == PlayerUpdate.GAME_MASTER) {
						gameMaster = true;
						initSettingsView();
						setStandardView();
					}
				}
			}

			@Override
			public void handleDisconnection(Connection c) {
			}

			@Override
			public void handleConnection(Connection c) {
			}
		});
		
		sendUpdate();

	}

	private void initSettingsView() {
		scrollingTable = new Table(skin);

		container = new ScrollPane(null, skin);
		container.setX(0);
		container.setY(0);
		container.setSize(stage.getWidth(), stage.getHeight() - 200);

		if (gameMaster) {
			gameMode = new TextButton("Gamemode: ", skin);
			gameMode.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					if (gameMode.getText().equals("Gamemode: STANDARD")) {
						gameMode.setText("Gamemode: SMASH");
					} else {
						gameMode.setText("Gamemode: STANDARD");
					}
					gameMode.setText("WALLA");
				}
			});
			roundTime = new TextButton("Round time: 30s", skin);
			suddenDeath = new TextButton("Sudden death: ON", skin);
			scrollingTable.add(gameMode).row();
			scrollingTable.add(roundTime).row();
			scrollingTable.add(suddenDeath).row();
		}

		back = new TextButton("Back", skin);
		back.setX(0);
		back.setY(0);
		back.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				setStandardView();
			}
		});

		container.setWidget(scrollingTable);
	}

	private void setSettingsView() {
		stage.getActors().clear();
		stage.addActor(container);
		stage.addActor(back);
	}

	private void setStandardView() {

		stage.getActors().clear();

		// Add to stage
		picker.add(stage);
		stage.addActor(levelLabel);
		stage.addActor(expLabel);
		stage.addActor(nameLabel);
		stage.addActor(nameField);
		stage.addActor(newColors);
		stage.addActor(ready);
		stage.addActor(settings);

	}

	private void initStandardView() {

		settings = new TextButton("Settings", skin);
		settings.setX(0);
		settings.setY(0);
		settings.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				setSettingsView();
			}
		});

		nameLabel = new Label("Name", skin);
		nameLabel.setX(0);
		nameLabel.setAlignment(Align.left | Align.top);
		nameLabel.setY(stage.getHeight() - nameLabel.getHeight() * 1.5f);

		levelLabel = new Label("Level: " + Player.getLevel(), skin);
		levelLabel.setX(0);
		levelLabel.setY(stage.getHeight() / 2 - levelLabel.getHeight());

		expLabel = new Label("Exp: " + Player.getExp() + "/" + Player.expReq(), skin);
		expLabel.setX(0);
		expLabel.setY((float) (stage.getHeight() / 2.5 - expLabel.getHeight()));

		nameField = new TextField(Player.getName(), skin);
		nameField.setX(0);
		nameField.setSize(250, 100);
		nameField.setY(stage.getHeight() - nameLabel.getHeight() * 1.5f - nameField.getHeight());
		nameField.setMaxLength(12);
		nameField.setTextFieldListener(new TextFieldListener() {

			@Override
			public void keyTyped(TextField textField, char c) {
				Player.setName(textField.getText());
				Player.save();
				sendUpdate();
			}
		});

		picker = new ColorPicker(stage.getWidth() - ColorOption.WIDTH * 3, stage.getHeight() - ColorOption.HEIGHT * 4,
				3, 3, new Callback() {

					@Override
					public void callback() {
						Color c = picker.getSelectedColor(Color.WHITE);
						Player.setColor(c);
						Player.save();
						sendUpdate();
					}
				});
		picker.setInitialSelection(Player.getColor());

		newColors = new TextButton("More colors", skin);
		newColors.setX(picker.getX());
		newColors.setWidth(picker.getWidth());
		newColors.setHeight(newColors.getHeight() * 2);
		newColors.setY(picker.getY() - newColors.getHeight());

		newColors.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				picker.shuffle();
				return true;
			}
		});

		ready = new CheckBox("READY", skin);
		ready.setX(stage.getWidth() / 2 - ready.getWidth() / 2);
		ready.setY(0);
		ready.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				sendUpdate();
			}

		});

	}

	protected void sendUpdate() {
		Color c = picker.getSelectedColor(new Color(Color.WHITE));
		int tp = Player.getTimesPlayed();
		int level = Player.getLevel();
		float xp = Player.getExp();
		int rdy = ready.isChecked() ? 1 : 0;
		game.getClient().broadcast(new PlayerUpdate(1, new int[] { PlayerUpdate.PLAYER_INFO },
				new float[][] { { tp, level, xp }, { c.r, c.g, c.b }, { rdy } }, new String[] { nameField.getText() }));
	}

}
