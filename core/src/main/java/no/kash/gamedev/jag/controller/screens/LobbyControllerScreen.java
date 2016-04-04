package no.kash.gamedev.jag.controller.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
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
import no.kash.gamedev.jag.commons.network.NetworkListener;
import no.kash.gamedev.jag.commons.network.packets.GamePacket;
import no.kash.gamedev.jag.commons.network.packets.PlayerStateChange;
import no.kash.gamedev.jag.commons.network.packets.PlayerUpdate;
import no.kash.gamedev.jag.commons.utils.Callback;
import no.kash.gamedev.jag.controller.JustAnotherGameController;
import no.kash.gamedev.jag.controller.Player;
import no.kash.gamedev.jag.controller.lobby.ColorPicker;
import no.kash.gamedev.jag.controller.lobby.ColorPicker.ColorOption;
import no.kash.gamedev.jag.game.gamesession.roundhandlers.PlayerNewStats;

public class LobbyControllerScreen extends AbstractControllerScreen {

	// TODO implement gamesetting changer aka gameMaster
	private boolean gameMaster;

	GlyphLayout lobbyLabel;
	BitmapFont font;

	TextField nameField;

	GlyphLayout levelLabel;
	GlyphLayout expLabel;

	TextButton updateNameButton;
	TextButton newColors;
	CheckBox ready;
	ColorPicker picker;
	
	

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
		font.draw(batch, levelLabel, 0, stage.getHeight() / 2 - levelLabel.height);
		font.draw(batch, expLabel, 0, (float) (stage.getHeight() / 2.5 - expLabel.height));
	}

	@Override
	protected void onShow() {
		Player.load();
		Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

		Label label = new Label("Name", skin);
		label.setX(0);
		label.setAlignment(Align.left | Align.top);
		label.setY(stage.getHeight() - label.getHeight() * 1.5f);

		nameField = new TextField(Player.getName(), skin);
		nameField.setX(0);
		nameField.setSize(250, 100);
		nameField.setY(stage.getHeight() - label.getHeight() * 1.5f - nameField.getHeight());
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
		picker.setInitialSelection(
				Player.getColor());

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

		picker.add(stage);
		stage.addActor(label);
		stage.addActor(nameField);
		stage.addActor(newColors);
		stage.addActor(ready);
		font = Assets.font;
		lobbyLabel = new GlyphLayout(font, "Lobby");

		levelLabel = new GlyphLayout(font, "Level: " + Player.getLevel());
		expLabel = new GlyphLayout(font, "Exp: " + Player.getExp() + "/" + Player.expReq());

		game.setReceiver(new JagClientPacketHandler() {

			@Override
			public void handlePacket(Connection c, GamePacket m) {

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
