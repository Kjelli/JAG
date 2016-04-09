package no.kash.gamedev.jag.game.screens;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.esotericsoftware.kryonet.Connection;

import no.kash.gamedev.jag.commons.defs.Defs;
import no.kash.gamedev.jag.commons.network.JagServerPacketHandler;
import no.kash.gamedev.jag.commons.network.packets.GamePacket;
import no.kash.gamedev.jag.commons.network.packets.PlayerInput;
import no.kash.gamedev.jag.game.JustAnotherGame;
import no.kash.gamedev.jag.game.gamesession.GameMode;
import no.kash.gamedev.jag.game.gamesession.GameSession;
import no.kash.gamedev.jag.game.levels.MapHandler;
import no.kash.gamedev.jag.game.levels.MiniMap;

public class MapSelectionScreen extends AbstractGameScreen {

	private static final int MAX_MAPS = 3;
	GameSession session;
	List<MiniMap> miniMaps;

	public MapSelectionScreen(JustAnotherGame game, GameSession session) {
		super(game);
		this.session = session;
	}

	@Override
	protected void update(float delta) {
		gameContext.update(delta);
		for(int i = 0; i < miniMaps.size(); i ++){
			MiniMap mm = miniMaps.get(i);
			mm.setY((float) (stage.getHeight() / 2 - mm.getEffectiveMapHeight() / 2 - 30* Math.sin(gameContext.getElapsedTime() + (i+1) * 20)));
		}
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

	@Override
	protected void onShow() {
		initMaps();

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

	private void initMaps() {
		miniMaps = new ArrayList<>();
		FileHandle[] maps = MapHandler.availableMaps();
		
		// Find compatible maps
		for (int i = 0; i < maps.length; i++) {
			MiniMap mm = MiniMap.build(MapHandler.load(maps[i]));
			boolean compatible = mm.isCompatible(session);
			if (compatible) {
				miniMaps.add(mm);
			}
		}
		// Trim the list randomly, to fit MAX_MAPS number of maps
		while(miniMaps.size() > MAX_MAPS){
			miniMaps.remove((int)(Math.random() * miniMaps.size()));
		}
		
		// Prepare maps
		for (int i = 0; i < miniMaps.size(); i++) {
			MiniMap mm = miniMaps.get(i);
			float tWidth = (stage.getWidth() - 50) / 3;
			mm.setTargetWidth(tWidth);
			mm.setX(stage.getWidth() / 2 + (i - miniMaps.size() / 2.0f) * tWidth + (i - miniMaps.size() / 2.0f) * 8f);
			mm.setY((float) (stage.getHeight() / 2 - mm.getEffectiveMapHeight() / 2 * Math.sin(gameContext.getElapsedTime())));
		}
	}

	@Override
	protected void debugDraw(ShapeRenderer renderer) {

	}

}
