package no.kash.gamedev.jag.game.screens;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.esotericsoftware.kryonet.Connection;

import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.commons.network.JagReceiver;
import no.kash.gamedev.jag.commons.network.packets.GamePacket;
import no.kash.gamedev.jag.commons.network.packets.PlayerConnect;
import no.kash.gamedev.jag.commons.network.packets.PlayerInput;
import no.kash.gamedev.jag.commons.network.packets.PlayerStateChange;
import no.kash.gamedev.jag.commons.network.packets.PlayerStateChangeResponse;
import no.kash.gamedev.jag.controller.JustAnotherGameController;
import no.kash.gamedev.jag.game.JustAnotherGame;
import no.kash.gamedev.jag.game.lobby.PlayerInfo;

public class LobbyScreen extends AbstractGameScreen {

	GlyphLayout lobbyLabel;
	BitmapFont font;

	Map<Integer, PlayerInfo> playerInfos;

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

		for (PlayerInfo info : playerInfos.values()) {
			info.draw(batch);
		}
	}

	@Override
	protected void onShow() {
		font = Assets.font;
		playerInfos = new HashMap<>();
		lobbyLabel = new GlyphLayout(font, "Lobby");

		game.setReceiver(new JagReceiver() {

			@Override
			public void handlePacket(Connection c, GamePacket m) {
				if (m instanceof PlayerConnect) {
					game.getServer().send(c.getID(), new PlayerStateChange(JustAnotherGameController.LOBBY_STATE));
				} else if (m instanceof PlayerStateChangeResponse) {
					if (playerInfos.containsKey(c.getID())) {
						return;
					}
					PlayerInfo pi = new PlayerInfo(0, stage.getHeight() - playerInfos.size() * PlayerInfo.HEIGHT,
							c.getID(), "Minge");
					playerInfos.put(c.getID(), pi);
					System.out.println("Adding player " + c.getID());
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
