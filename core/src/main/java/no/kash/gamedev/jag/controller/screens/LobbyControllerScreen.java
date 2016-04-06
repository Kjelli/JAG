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
import no.kash.gamedev.jag.commons.network.JagClientPacketHandler;
import no.kash.gamedev.jag.commons.network.packets.GamePacket;
import no.kash.gamedev.jag.commons.network.packets.GameSessionUpdate;
import no.kash.gamedev.jag.commons.network.packets.PlayerUpdate;
import no.kash.gamedev.jag.commons.utils.Callback;
import no.kash.gamedev.jag.controller.JustAnotherGameController;
import no.kash.gamedev.jag.controller.lobby.ColorPicker;
import no.kash.gamedev.jag.controller.lobby.ColorPicker.ColorOption;
import no.kash.gamedev.jag.controller.lobby.GameSessionControls;
import no.kash.gamedev.jag.controller.preferences.PlayerPreferences;

public class LobbyControllerScreen extends AbstractControllerScreen {

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

	// (GameMaster settings)
	Label gameMasterLabel;
	TextButton gameMode;
	TextButton dropIn;
	TextButton roundTime;
	TextButton roundsToWin;
	TextButton testMode;
	TextButton friendlyFire;
	TextButton startingHealth;
	TextButton drawNames;
	// TextButton suddenDeath;

	Label preferenceLabel;

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
		PlayerPreferences.load();
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
						sendGameSessionUpdate();
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
			sessionControls = new GameSessionControls();

			gameMasterLabel = new Label("Game settings", skin);

			// Game mode option
			gameMode = new TextButton("Game mode: " + sessionControls.session.gameMode.displayName, skin);
			gameMode.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					gameMode.setText("Game mode: " + sessionControls.nextOptionGameMode().displayName);
					sendGameSessionUpdate();
				}
			});

			// Round time option
			roundTime = new TextButton("Round time:" + sessionControls.session.roundTime + "s", skin);
			roundTime.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					roundTime.setText("Round time: " + sessionControls.nextOptionRoundTime() + "s");
					sendGameSessionUpdate();
				}
			});

			// Rounds to win option
			roundsToWin = new TextButton("Win limit: " + sessionControls.session.roundsToWin + " rounds", skin);
			roundsToWin.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					roundsToWin.setText("Win limit: " + sessionControls.nextOptionRoundsToWin() + " rounds");
					sendGameSessionUpdate();
				}
			});

			// Starting health option
			startingHealth = new TextButton("Starting health: " + sessionControls.session.startingHealth, skin);
			startingHealth.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					startingHealth.setText("Starting health:  " + sessionControls.nextOptionStartingHealth());
					sendGameSessionUpdate();
				}
			});

			// Drop in option
			dropIn = new TextButton("Drop in: " + (sessionControls.session.dropIn ? "on" : "off"), skin);
			dropIn.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					dropIn.setText("Drop in: " + (sessionControls.nextOptionDropIn() ? "on" : "off"));
					sendGameSessionUpdate();
				}
			});

			// Test mode option
			testMode = new TextButton("Test mode: " + (sessionControls.session.testMode ? "on" : "off"), skin);
			testMode.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					testMode.setText("Test mode: " + (sessionControls.nextOptionTestMode() ? "on" : "off"));
					sendGameSessionUpdate();
				}
			});

			// Friendly fire option
			friendlyFire = new TextButton("Friendly fire: " + (sessionControls.session.friendlyFire ? "on" : "off"),
					skin);
			friendlyFire.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					friendlyFire.setText("Friendly fire: " + (sessionControls.nextOptionFriendlyFire() ? "on" : "off"));
					sendGameSessionUpdate();
				}
			});

			// Draw names option
			drawNames = new TextButton("Show names: " + (sessionControls.session.drawNames ? "on" : "off"),
					skin);
			drawNames.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					drawNames.setText("Show names: " + (sessionControls.nextOptionDrawNames() ? "on" : "off"));
					sendGameSessionUpdate();
				}
			});

			scrollingTable.add(gameMasterLabel).row();
			scrollingTable.add(gameMode).row();
			scrollingTable.add(roundTime).row();
			scrollingTable.add(roundsToWin).row();
			scrollingTable.add(startingHealth).row();
			scrollingTable.add(friendlyFire).row();
			scrollingTable.add(drawNames).row();
			scrollingTable.add(dropIn).row();
			scrollingTable.add(testMode).row();
		}

		preferenceLabel = new Label("Preferences (TODO)", skin);

		back = new TextButton("Back", skin);
		back.setX(0);
		back.setY(0);
		back.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				setStandardView();
			}
		});
		scrollingTable.add(preferenceLabel);

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

		levelLabel = new Label("Level: " + PlayerPreferences.getLevel(), skin);
		levelLabel.setX(0);
		levelLabel.setY(stage.getHeight() / 2 - levelLabel.getHeight());

		expLabel = new Label("Exp: " + PlayerPreferences.getExp() + "/" + PlayerPreferences.expReq(), skin);
		expLabel.setX(0);
		expLabel.setY((float) (stage.getHeight() / 2.5 - expLabel.getHeight()));

		nameField = new TextField(PlayerPreferences.getName(), skin);
		nameField.setX(0);
		nameField.setSize(250, 100);
		nameField.setY(stage.getHeight() - nameLabel.getHeight() * 1.5f - nameField.getHeight());
		nameField.setMaxLength(12);
		nameField.setTextFieldListener(new TextFieldListener() {

			@Override
			public void keyTyped(TextField textField, char c) {
				PlayerPreferences.setName(textField.getText());
				PlayerPreferences.save();
				sendUpdate();
			}
		});

		picker = new ColorPicker(stage.getWidth() - ColorOption.WIDTH * 3, stage.getHeight() - ColorOption.HEIGHT * 4,
				3, 3, new Callback() {

					@Override
					public void callback() {
						Color c = picker.getSelectedColor(Color.WHITE);
						PlayerPreferences.setColor(c);
						PlayerPreferences.save();
						sendUpdate();
					}
				});
		picker.setInitialSelection(PlayerPreferences.getColor());

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
		int tp = PlayerPreferences.getTimesPlayed();
		int level = PlayerPreferences.getLevel();
		float xp = PlayerPreferences.getExp();
		int rdy = ready.isChecked() ? 1 : 0;
		PlayerUpdate update = new PlayerUpdate(1, new int[] { PlayerUpdate.PLAYER_INFO },
				new float[][] { { tp, level, xp }, { c.r, c.g, c.b }, { rdy } }, new String[] { nameField.getText() });
		game.getClient().broadcast(update);
	}

	protected void sendGameSessionUpdate() {
		GameSessionUpdate update = new GameSessionUpdate();
		update.dropIn = sessionControls.session.dropIn;
		update.gameModeIndex = sessionControls.getGameModeIndex();
		update.roundsToWin = sessionControls.session.roundsToWin;
		update.startingHealth = sessionControls.session.startingHealth;
		update.testMode = sessionControls.session.testMode;
		update.friendlyFire = sessionControls.session.friendlyFire;
		update.drawNames = sessionControls.session.drawNames;
		game.getClient().broadcast(update);
	}

}
