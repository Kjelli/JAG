package no.kash.gamedev.jag.controller.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.esotericsoftware.kryonet.Connection;

import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.commons.network.NetworkListener;
import no.kash.gamedev.jag.commons.network.packets.GamePacket;
import no.kash.gamedev.jag.commons.network.packets.PlayerUpdate;
import no.kash.gamedev.jag.controller.JustAnotherGameController;

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

		nameField = new TextField("Minge", skin);
		nameField.setX(0);
		nameField.setY(stage.getHeight() - nameField.getHeight() * 3);
		nameField.setMaxLength(12);
		nameField.setTextFieldListener(new TextFieldListener() {
			
			@Override
			public void keyTyped(TextField textField, char c) {
				sendUpdate();
			}
		});

		stage.addActor(nameField);

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
		game.getClient().broadcast(new PlayerUpdate(1, new int[] { PlayerUpdate.PLAYER_SETTINGS }, new float[][] { {} },
				new String[] { nameField.getText() }));
	}

}
