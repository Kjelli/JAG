package no.kash.gamedev.jag.controller.screens;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.esotericsoftware.kryonet.Connection;

import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.commons.network.JagClientPacketHandler;
import no.kash.gamedev.jag.commons.network.NetworkListener;
import no.kash.gamedev.jag.commons.network.packets.GamePacket;
import no.kash.gamedev.jag.commons.network.packets.PlayerConnect;
import no.kash.gamedev.jag.commons.network.packets.PlayerStateChange;
import no.kash.gamedev.jag.controller.JustAnotherGameController;

public class LoadingScreen extends AbstractControllerScreen {

	private static final float RETRY_TIMER_MAX = 1.0f;
	GlyphLayout loadingText;
	GlyphLayout loadingStatus;
	float retryTimer = RETRY_TIMER_MAX;
	int retryAttempts = 0;

	public LoadingScreen(JustAnotherGameController game, String string) {
		super(game);
		loadingText = new GlyphLayout(Assets.font, "Loading...");
		loadingStatus = new GlyphLayout(Assets.fontSmall, string);
	}

	@Override
	protected void update(float delta) {
		if ((retryTimer -= delta) < 0) {
			retryTimer = RETRY_TIMER_MAX;
			retryAttempts++;

			if (!game.getClient().isConnected()) {
				try {
					game.getClient().connect(game.getClient().getConnectionString());
				} catch (Exception e) {
					loadingStatus = new GlyphLayout(Assets.fontSmall,
							"Connection failed, retrying(" + retryAttempts + ")...");
				}
			} else {
				game.getClient().broadcast(new PlayerConnect(game.getClient().getId(), "Svettleif"));
			}
		}
	}

	@Override
	protected void draw(float delta) {
		Assets.font.draw(batch, loadingText, stage.getWidth() / 2 - loadingText.width / 2, stage.getHeight() / 2);
		Assets.fontSmall.draw(batch, loadingStatus, stage.getWidth() / 2 - loadingStatus.width / 2,
				stage.getHeight() / 2 - loadingStatus.height * 2f);
	}

	@Override
	protected void onShow() {

		game.setReceiver(new JagClientPacketHandler() {

			@Override
			public void handlePacket(Connection c, GamePacket m) {
			}

			@Override
			public void handleDisconnection(Connection c) {
				loadingStatus.setText(Assets.fontSmall, "Connection lost :(");
			}

			@Override
			public void handleConnection(Connection c) {
				loadingStatus.setText(Assets.fontSmall, "Connected! Waiting for gameserver...");
			}
		});

	}

}
