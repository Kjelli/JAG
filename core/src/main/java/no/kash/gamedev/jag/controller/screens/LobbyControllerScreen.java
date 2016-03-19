package no.kash.gamedev.jag.controller.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.utils.Align;
import com.esotericsoftware.kryonet.Connection;

import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.commons.defs.Defs;
import no.kash.gamedev.jag.commons.defs.Prefs;
import no.kash.gamedev.jag.commons.network.NetworkListener;
import no.kash.gamedev.jag.commons.network.packets.GamePacket;
import no.kash.gamedev.jag.commons.network.packets.PlayerUpdate;
import no.kash.gamedev.jag.controller.JustAnotherGameController;
import no.kash.gamedev.jag.controller.lobby.ColorPicker;
import no.kash.gamedev.jag.controller.lobby.ColorPicker.ColorOption;

public class LobbyControllerScreen extends AbstractControllerScreen {

	private boolean gameMaster;

	GlyphLayout lobbyLabel;
	BitmapFont font;

	TextField nameField;
	TextButton updateNameButton;

	public LobbyControllerScreen(JustAnotherGameController game) {
		super(game);
	}

	@Override
	protected void update(float delta) {

	}

	@Override
	protected void draw(float delta) {
		font.draw(batch, lobbyLabel, stage.getWidth() / 2 - lobbyLabel.width / 2,
				stage.getHeight() - lobbyLabel.height);
	}

	@Override
	protected void onShow() {
		Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

		Label label = new Label("Name", skin);
		label.setX(0);
		label.setAlignment(Align.left | Align.top);
		label.setY(stage.getHeight() - label.getHeight() * 1.5f);

		nameField = new TextField("Minge", skin);
		nameField.setX(0);
		nameField.setSize(250, 100);
		nameField.setY(stage.getHeight() - label.getHeight() * 1.5f - nameField.getHeight());
		nameField.setMaxLength(12);
		nameField.setTextFieldListener(new TextFieldListener() {

			@Override
			public void keyTyped(TextField textField, char c) {
				sendUpdate();
			}
		});
		stage.addActor(label);
		stage.addActor(nameField);

		ColorPicker picker = new ColorPicker(stage.getWidth() - ColorOption.WIDTH * 3,
				stage.getHeight() - ColorOption.HEIGHT * 3, 3, 3);

		picker.add(stage);

		font = Assets.font;
		lobbyLabel = new GlyphLayout(font, "Lobby");

		game.getClient().setListener(new NetworkListener() {

			@Override
			public void receivedPacket(Connection c, GamePacket m) {
			}

			@Override
			public void disconnected(Connection connection) {
				queueNextScreen(new LoadingScreen(game, "Connection lost, retrying..."));
			}

			@Override
			public void connected(Connection c) {

			}
		});

		sendUpdate();
	}

	protected void sendUpdate() {
		game.getClient()
				.broadcast(new PlayerUpdate(1, new int[] { PlayerUpdate.PLAYER_SETTINGS },
						new float[][] { { Prefs.get().getInteger(Defs.PREF_TIMES_PLAYED) } },
						new String[] { nameField.getText() }));
	}

}
