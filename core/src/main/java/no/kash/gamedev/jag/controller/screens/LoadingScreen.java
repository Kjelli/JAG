package no.kash.gamedev.jag.controller.screens;

import java.io.IOException;
import java.net.UnknownHostException;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.esotericsoftware.kryonet.Connection;

import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.commons.network.MessageListener;
import no.kash.gamedev.jag.commons.network.packets.GamePacket;
import no.kash.gamedev.jag.commons.network.packets.PlayerConnect;
import no.kash.gamedev.jag.commons.network.packets.PlayerStateChange;
import no.kash.gamedev.jag.controller.JustAnotherGameController;

public class LoadingScreen extends AbstractControllerScreen {

	private static final float RETRY_TIMER_MAX = 1.0f;
	GlyphLayout loadingText;
	GlyphLayout loadingStatus;

	float retryTimer = RETRY_TIMER_MAX;

	public LoadingScreen(JustAnotherGameController game, String string) {
		super(game);
		loadingText = new GlyphLayout(Assets.font, "Loading...");
		loadingStatus = new GlyphLayout(Assets.fontSmall, string);
	}

	@Override
	protected void update(float delta) {
		game.getActionResolver().toast("Retrying in " + retryTimer + "s");

		if ((retryTimer -= delta) < 0) {
			retryTimer = RETRY_TIMER_MAX;
			if (!game.getClient().isConnected()) {
				try {
					game.getClient().connect(game.getClient().getConnectionString());
				} catch (Exception e) {
					// Do nothing
				}
			} else {
				game.getClient().broadcast(new PlayerConnect(game.getClient().getId(), "Svettleif"));
			}
		}
	}

	@Override
	protected void draw(float delta) {
		Assets.font.draw(batch, loadingText, stage.getWidth() / 2 - loadingText.width / 2, stage.getHeight() / 2);
		Assets.fontSmall.draw(batch, loadingStatus, stage.getWidth() / 2 - loadingStatus.width / 2, stage.getHeight() / 2 - loadingStatus.height * 2f);
	}

	@Override
	protected void onShow() {

		game.getClient().setListener(new MessageListener() {
			@Override
			public void onMessage(Connection c, GamePacket m) {
				if (m instanceof PlayerStateChange) {
					PlayerStateChange sc = (PlayerStateChange) m;
					handleStateChange(sc);
				}
			}

			@Override
			public void onDisconnection(Connection connection) {
				setBackgroundColor(Color.RED);
			}

			@Override
			public void onConnection(Connection c) {
				setBackgroundColor(Color.WHITE);
			}
		});

	}

}