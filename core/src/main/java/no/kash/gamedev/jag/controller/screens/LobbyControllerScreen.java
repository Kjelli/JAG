package no.kash.gamedev.jag.controller.screens;

import java.util.Collection;
import java.util.Map;

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
import no.kash.gamedev.jag.commons.network.JagClientPacketHandler;
import no.kash.gamedev.jag.commons.network.packets.GamePacket;
import no.kash.gamedev.jag.commons.network.packets.GameSessionUpdate;
import no.kash.gamedev.jag.commons.network.packets.PlayerUpdate;
import no.kash.gamedev.jag.commons.utils.Callback;
import no.kash.gamedev.jag.controller.JustAnotherGameController;
import no.kash.gamedev.jag.controller.lobby.ColorPicker;
import no.kash.gamedev.jag.controller.lobby.ColorPicker.ColorOption;
import no.kash.gamedev.jag.controller.lobby.GameSessionControls;
import no.kash.gamedev.jag.controller.lobby.TeamColorPicker;
import no.kash.gamedev.jag.controller.preferences.GameSessionPreferences;
import no.kash.gamedev.jag.controller.preferences.PlayerPreferences;
import no.kash.gamedev.jag.game.commons.utils.SettingClickListener;
import no.kash.gamedev.jag.game.gamesession.GameMode;
import no.kash.gamedev.jag.game.gamesession.GameSettings.Setting;

public class LobbyControllerScreen extends AbstractControllerScreen {

	private boolean gameMaster = false;
	GameSessionControls sessionControls;

	GlyphLayout lobbyLabel;
	BitmapFont font;
	Skin skin;

	public static final int STANDARD_VIEW = 1, SETTINGS_VIEW = 2;
	int currentView = 0;

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
	TextButton[] settingButtons;
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

		sessionControls = new GameSessionControls();
		lobbyLabel = new GlyphLayout(font, "Lobby");

		initStandardView();
		initSettingsView();
		refreshAndSetStandardView();

		game.setReceiver(new JagClientPacketHandler() {

			@Override
			public void handlePacket(Connection c, GamePacket m) {
				System.out.println("Received " + m.getClass());
				if (m instanceof PlayerUpdate) {
					PlayerUpdate update = (PlayerUpdate) m;
					if (update.fieldId[0] == PlayerUpdate.GAME_MASTER) {
						gameMaster = true;
						initSettingsView();
						sendGameSessionUpdate();
					}
				} else if (m instanceof GameSessionUpdate) {
					handleGameSessionUpdate((GameSessionUpdate) m);
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

	protected void handleGameSessionUpdate(GameSessionUpdate update) {
		GameMode gameModeFromUpdate = update.settings.getSelectedValue(Defs.SESSION_GM, GameMode.class);

		boolean newGameMode = sessionControls.session.settings.getSelectedValue(Defs.SESSION_GM,
				GameMode.class) != gameModeFromUpdate;
		System.out.println("New game mode:" + newGameMode);

		sessionControls.session.settings.fromPacket(update);

		if (newGameMode) {
			initStandardView();
			if (currentView == STANDARD_VIEW) {
				refreshAndSetStandardView();
			}
			sendUpdate();
		}

	}

	private void initSettingsView() {
		scrollingTable = new Table(skin);

		container = new ScrollPane(null, skin);
		container.setX(0);
		container.setY(0);
		container.setSize(stage.getWidth(), stage.getHeight() - 200);

		if (gameMaster) {

			gameMasterLabel = new Label("Game settings", skin);

			Map<String, Setting<?>> settings = sessionControls.session.settings.settings;
			settingButtons = new TextButton[settings.size()];

			int i = 0;
			for (final Setting<?> setting : settings.values()) {
				settingButtons[i] = new TextButton(setting.toString(), skin);
				settingButtons[i].addListener(new SettingClickListener(settingButtons[i], setting) {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						mySetting.selectNext();
						myButton.setText(setting.toString());
						GameSessionPreferences.update(sessionControls.session.settings);
						sendGameSessionUpdate();
					}
				});
				i++;
			}

			scrollingTable.add(gameMasterLabel).expandX().fillX().row();
			for (TextButton tb : settingButtons) {
				scrollingTable.add(tb).expand().fillX().row();
			}
		}

		preferenceLabel = new Label("Preferences (TODO)", skin);

		back = new TextButton("Back", skin);
		back.setX(0);
		back.setY(0);
		back.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				initStandardView();
				refreshAndSetStandardView();
			}
		});
		scrollingTable.add(preferenceLabel).expandX().fillX().row();

		container.setWidget(scrollingTable);
	}

	private void setSettingsView() {
		stage.getActors().clear();

		stage.addActor(container);
		stage.addActor(back);
		currentView = SETTINGS_VIEW;
	}

	private void refreshAndSetStandardView() {
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
		currentView = STANDARD_VIEW;
	}

	private void initStandardView() {
		boolean teams = sessionControls.session.settings.getSelectedValue(Defs.SESSION_GM, GameMode.class).teamBased;

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

		if (teams) {
			picker = new TeamColorPicker(stage.getWidth() - ColorOption.WIDTH * 3,
					stage.getHeight() - ColorOption.HEIGHT * 4, new Callback() {
						@Override
						public void callback() {
							sendUpdate();
						}
					});
			newColors = new TextButton("...", skin);
			picker.setInitialSelection(0);
			sendUpdate();
		} else {
			picker = new ColorPicker(stage.getWidth() - ColorOption.WIDTH * 3,
					stage.getHeight() - ColorOption.HEIGHT * 4, 3, 3, new Callback() {

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
		}

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
		int rdy = ready != null && ready.isChecked() ? 1 : 0;
		int teamId = picker.getSelectedIndex() + 1;
		PlayerUpdate update = new PlayerUpdate(1, new int[] { PlayerUpdate.PLAYER_INFO },
				new float[][] { { tp, level, xp }, { c.r, c.g, c.b }, { rdy, teamId } },
				new String[] { nameField.getText() });
		game.getClient().send(update);
	}

	protected void sendGameSessionUpdate() {
		GameSessionUpdate update = new GameSessionUpdate(sessionControls.session.settings);
		game.getClient().send(update);
	}

}
