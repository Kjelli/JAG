package no.kash.gamedev.jag.controller.screens;

import com.esotericsoftware.kryonet.Connection;

import no.kash.gamedev.jag.commons.network.JagClientPacketHandler;
import no.kash.gamedev.jag.commons.network.packets.GamePacket;
import no.kash.gamedev.jag.commons.network.packets.PlayerAvailableMaps;
import no.kash.gamedev.jag.commons.network.packets.PlayerMapVote;
import no.kash.gamedev.jag.commons.utils.selection.Selector;
import no.kash.gamedev.jag.controller.JustAnotherGameController;
import no.kash.gamedev.jag.controller.mapselection.MapSelector;

public class MapSelectionControllerScreen extends AbstractControllerScreen {

	MapSelector selector;
	boolean selectionMade = false;

	public MapSelectionControllerScreen(JustAnotherGameController game) {
		super(game);
	}

	@Override
	protected void onShow() {
		game.setReceiver(new JagClientPacketHandler() {
			@Override
			public void handlePacket(Connection c, GamePacket m) {
				if (m instanceof PlayerAvailableMaps) {
					PlayerAvailableMaps maps = (PlayerAvailableMaps) m;
					selector = new MapSelector(MapSelectionControllerScreen.this, 0, stage.getHeight() / 2);
					selector.setAvailableMaps(maps.availableMaps);
					selector.setX(stage.getWidth() / 2 - selector.getWidth() / 2);
					selector.add(stage);
				}
			}

			@Override
			public void handleDisconnection(Connection c) {
			}

			@Override
			public void handleConnection(Connection c) {
			}
		});
	}

	@Override
	protected void update(float delta) {

	}

	@Override
	protected void draw(float delta) {
		if (selector != null) {
			selector.draw(batch, 1.0f);
		}
	}

	public void select(int index) {
		game.getClient().send(new PlayerMapVote(index));
	}

}
