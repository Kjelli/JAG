package no.kash.gamedev.jag.game.screens;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.esotericsoftware.kryonet.Connection;

import no.kash.gamedev.jag.commons.network.JagServerPacketHandler;
import no.kash.gamedev.jag.commons.network.packets.GamePacket;
import no.kash.gamedev.jag.commons.network.packets.PlayerInput;
import no.kash.gamedev.jag.game.JustAnotherGame;
import no.kash.gamedev.jag.game.gamesession.GameSession;
import no.kash.gamedev.jag.game.levels.MapHandler;
import no.kash.gamedev.jag.game.levels.MiniMap;

public class MapSelectionScreen extends AbstractGameScreen {

	GameSession session;

	public MapSelectionScreen(JustAnotherGame game, GameSession session) {
		super(game);
		this.session = session;
	}

	@Override
	protected void update(float delta) {

	}

	@Override
	protected void draw(SpriteBatch batch, float delta) {
		for (MiniMap miniMap : miniMaps) {
			miniMap.draw(batch);
		}
	}

	@Override
	protected void drawHud(SpriteBatch hudBatch, float delta) {

	}

	MiniMap[] miniMaps;

	@Override
	protected void onShow() {
		FileHandle[] maps = MapHandler.availableMaps();
		miniMaps = new MiniMap[maps.length];
		for (int i = 0; i < maps.length; i++) {
			miniMaps[i] = MiniMap.build(MapHandler.load(maps[i]));
			miniMaps[i].setTargetWidth(128);
			miniMaps[i].setX(stage.getWidth() / 2 + (i - maps.length / 2.0f) * 128 + (i- maps.length / 2.0f) * 8f);
			miniMaps[i].setY(stage.getHeight() / 2 - miniMaps[i].getEffectiveMapHeight() / 2);
		}
		game.setReceiver(new JagServerPacketHandler() {

			@Override
			public void handleInput(PlayerInput input) {
				//
			}

			@Override
			public void handleDisconnection(Connection c) {

			}

			@Override
			public void handlePacket(Connection c, GamePacket m) {

			}

		});
	}

}
