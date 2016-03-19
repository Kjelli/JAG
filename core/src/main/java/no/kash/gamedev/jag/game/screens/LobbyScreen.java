package no.kash.gamedev.jag.game.screens;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.esotericsoftware.kryonet.Connection;

import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.commons.network.JagReceiver;
import no.kash.gamedev.jag.commons.network.packets.GamePacket;
import no.kash.gamedev.jag.commons.network.packets.PlayerConnect;
import no.kash.gamedev.jag.commons.network.packets.PlayerInput;
import no.kash.gamedev.jag.commons.network.packets.PlayerStateChange;
import no.kash.gamedev.jag.commons.network.packets.PlayerUpdate;
import no.kash.gamedev.jag.controller.JustAnotherGameController;
import no.kash.gamedev.jag.game.JustAnotherGame;
import no.kash.gamedev.jag.game.lobby.PlayerInfo;
import no.kash.gamedev.jag.game.lobby.PlayerInfoGUI;

public class LobbyScreen extends AbstractGameScreen {

	GlyphLayout lobbyLabel;
	BitmapFont font;

	Map<Integer, PlayerInfoGUI> playerInfos;

	public LobbyScreen(JustAnotherGame game) {
		super(game);
	}

	@Override
	protected void update(float delta) {

	}

	@Override
	protected void draw(float delta) {
		font.draw(batch, lobbyLabel, stage.getWidth() / 2 - lobbyLabel.width / 2,
				stage.getHeight() - lobbyLabel.height);

		for (PlayerInfoGUI info : playerInfos.values()) {
			info.draw(batch);
		}
	}

	@Override
	protected void onShow() {
		font = Assets.font;
		playerInfos = new HashMap<>();
		lobbyLabel = new GlyphLayout(font, "Lobby");
		stage.setViewport(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera));

		game.setReceiver(new JagReceiver() {
			@Override
			public void handlePacket(Connection c, GamePacket m) {
				if (m instanceof PlayerConnect) {
					game.getServer().send(c.getID(), new PlayerStateChange(JustAnotherGameController.LOBBY_STATE));
				} else if (m instanceof PlayerUpdate) {
					PlayerUpdate update = (PlayerUpdate) m;
					PlayerInfo info = new PlayerInfo();
					info.name = update.info[0];
					info.id = c.getID();
					info.timesPlayed = (int)update.state[0][0];
					if (!playerInfos.containsKey(c.getID())) {
						PlayerInfoGUI pi = new PlayerInfoGUI(0,
								stage.getHeight() - (playerInfos.size() + 1) * PlayerInfoGUI.HEIGHT, info);
						playerInfos.put(c.getID(), pi);
						System.out.println("Adding player " + c.getID());
					} else {
						playerInfos.get(c.getID()).setInfo(info);
					}

				}
			}

			@Override
			public void handleInput(PlayerInput input) {
				System.out.println("PlayerInput: " + input);
			}

			@Override
			public void handleDisconnection(Connection c) {
				System.out.println("Disconnected: " + c);

				playerInfos.remove(c.getID());
			}

		});
	}

}
