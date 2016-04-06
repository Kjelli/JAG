package no.kash.gamedev.jag.game.screens;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.esotericsoftware.kryonet.Connection;

import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.commons.defs.Defs;
import no.kash.gamedev.jag.commons.network.JagServerPacketHandler;
import no.kash.gamedev.jag.commons.network.packets.GamePacket;
import no.kash.gamedev.jag.commons.network.packets.GameSessionUpdate;
import no.kash.gamedev.jag.commons.network.packets.PlayerConnect;
import no.kash.gamedev.jag.commons.network.packets.PlayerInput;
import no.kash.gamedev.jag.commons.network.packets.PlayerStateChange;
import no.kash.gamedev.jag.commons.network.packets.PlayerUpdate;
import no.kash.gamedev.jag.controller.JustAnotherGameController;
import no.kash.gamedev.jag.game.JustAnotherGame;
import no.kash.gamedev.jag.game.gameobjects.players.PlayerInfo;
import no.kash.gamedev.jag.game.gamesession.GameMode;
import no.kash.gamedev.jag.game.gamesession.GameSession;
import no.kash.gamedev.jag.game.lobby.PlayerInfoGUI;

public class LobbyScreen extends AbstractGameScreen {

	GlyphLayout lobbyLabel;
	BitmapFont font;

	Map<Integer, PlayerInfoGUI> playerInfos;

	GameSession session;

	public LobbyScreen(JustAnotherGame game) {
		super(game);
		session = new GameSession();
	}

	public LobbyScreen(JustAnotherGame game, GameSession session) {
		super(game);
		this.session = session;

		session.reset();
	}

	@Override
	protected void update(float delta) {
		if (playerInfos.size() > 0) {
			boolean allReady = true;
			for (PlayerInfoGUI playerInfoGUI : playerInfos.values()) {
				if (!playerInfoGUI.getInfo().ready) {
					allReady = false;
				}
			}
			if (allReady) {
				for (PlayerInfoGUI playerInfoGUI : playerInfos.values()) {
					PlayerInfo info = playerInfoGUI.getInfo();
					session.players.put(info.id, info);
					game.getServer().send(info.id, new PlayerStateChange(JustAnotherGameController.PLAY_STATE));
				}
				game.setScreen(new PlayScreen(game, session));
			}
		}
	}

	@Override
	protected void draw(SpriteBatch batch, float delta) {
		font.draw(batch, lobbyLabel, stage.getWidth() / 2 - lobbyLabel.width / 2,
				stage.getHeight() - lobbyLabel.height);

		for (PlayerInfoGUI info : playerInfos.values()) {
			info.draw(batch);
		}
	}

	@Override
	protected void drawHud(SpriteBatch batch, float delta) {

	}

	@Override
	protected void onShow() {
		font = Assets.font;
		playerInfos = new HashMap<>();
		lobbyLabel = new GlyphLayout(font, "Lobby");
		stage.setViewport(new StretchViewport(Defs.WIDTH, Defs.HEIGHT, camera));

		game.setReceiver(new JagServerPacketHandler() {
			@Override
			public void handlePacket(Connection c, GamePacket m) {
				if (m instanceof PlayerConnect) {
					game.getServer().send(c.getID(), new PlayerStateChange(JustAnotherGameController.LOBBY_STATE));
				} else if (m instanceof PlayerUpdate) {
					PlayerUpdate update = (PlayerUpdate) m;
					PlayerInfo info = new PlayerInfo();
					info.name = update.info[0];
					info.id = c.getID();
					info.timesPlayed = (int) update.state[0][0];
					info.level = (int) update.state[0][1];
					info.xp = (int) update.state[0][2];
					info.ready = update.state[2][0] > 0;
					info.color = new Color(update.state[1][0], update.state[1][1], update.state[1][2], 1);
					if (playerInfos.isEmpty()) {
						info.gameMaster = true;
						game.getServer().send(info.id,
								new PlayerUpdate(1, new int[] { PlayerUpdate.GAME_MASTER }, null));
					}
					if (!playerInfos.containsKey(c.getID())) {
						PlayerInfoGUI pi = new PlayerInfoGUI(0,
								stage.getHeight() - (playerInfos.size() + 1) * PlayerInfoGUI.HEIGHT, info);
						playerInfos.put(c.getID(), pi);
						System.out.println("Adding player " + c.getID());
					} else {
						playerInfos.get(c.getID()).setInfo(info);
					}

				} else if (m instanceof GameSessionUpdate) {
					GameSessionUpdate update = (GameSessionUpdate) m;
					session.dropIn = update.dropIn;
					session.gameMode = GameMode.values()[update.gameModeIndex];
					session.roundsToWin = update.roundsToWin;
					session.roundTime = update.roundTime;
					session.testMode = update.testMode;
					session.startingHealth = update.startingHealth;
					session.friendlyFire = update.friendlyFire;
					session.drawNames = update.drawNames;
					System.out.println("Changed gamesession settings");
				}
			}

			@Override
			public void handleInput(PlayerInput input) {
				System.out.println("PlayerInput: " + input);
			}

			@Override
			public void handleDisconnection(Connection c) {
				System.out.println("Disconnected: " + c);

				if (playerInfos.containsKey(c.getID())) {
					PlayerInfoGUI dced = playerInfos.remove(c.getID());

					if (dced.getInfo().gameMaster) {
						if (!playerInfos.isEmpty()) {
							PlayerInfo nextGameMaster = playerInfos.values().iterator().next().getInfo();

							nextGameMaster.gameMaster = true;
							game.getServer().send(nextGameMaster.id,
									new PlayerUpdate(1, new int[] { PlayerUpdate.GAME_MASTER }, null));
						}
					}

					for (PlayerInfoGUI gui : playerInfos.values()) {
						if (gui.getInfo().id > c.getID()) {
							gui.nudgeUp();
						}
					}
				}
			}

		});
	}

}
