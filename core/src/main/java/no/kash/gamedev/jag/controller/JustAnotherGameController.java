package no.kash.gamedev.jag.controller;

import com.esotericsoftware.kryonet.Connection;

import no.kash.gamedev.jag.JagEndpoint;
import no.kash.gamedev.jag.actionresolvers.ActionResolver;
import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.commons.defs.Defs;
import no.kash.gamedev.jag.commons.defs.Prefs;
import no.kash.gamedev.jag.commons.network.JagClient;
import no.kash.gamedev.jag.commons.network.JagClientPacketHandler;
import no.kash.gamedev.jag.commons.network.JagServerPacketHandler;
import no.kash.gamedev.jag.commons.network.NetworkListener;
import no.kash.gamedev.jag.commons.network.packets.GamePacket;
import no.kash.gamedev.jag.commons.network.packets.PlayerInput;
import no.kash.gamedev.jag.commons.network.packets.PlayerStateChange;
import no.kash.gamedev.jag.commons.network.packets.PlayerStateChangeResponse;
import no.kash.gamedev.jag.commons.tweens.TweenGlobal;
import no.kash.gamedev.jag.controller.screens.AbstractControllerScreen;
import no.kash.gamedev.jag.controller.screens.ConfigureScreen;
import no.kash.gamedev.jag.controller.screens.ControllerScreen;
import no.kash.gamedev.jag.controller.screens.LoadingScreen;
import no.kash.gamedev.jag.controller.screens.LobbyControllerScreen;
import no.kash.gamedev.jag.game.gamesession.roundhandlers.PlayerNewStats;

public class JustAnotherGameController extends JagEndpoint {
	public static final int PLAY_STATE = 1, LOBBY_STATE = 2;

	ActionResolver resolver;
	JagClient client;
	private JagClientPacketHandler receiver;

	public JustAnotherGameController(ActionResolver resolver) {
		super(resolver);
	}

	@Override
	public void create() {
		init();
		setScreen(new ConfigureScreen(this));
		client.setListener(new NetworkListener() {

			@Override
			public void receivedPacket(Connection c, GamePacket m) {
				if (getReceiver() != null) {
					if (m instanceof PlayerStateChange) {
						PlayerStateChange sc = (PlayerStateChange) m;
						handleStateChange(sc);
					} else if (m instanceof PlayerNewStats) {
						PlayerNewStats stats = (PlayerNewStats) m;
						Prefs.get().putInteger(Defs.PREF_PLAYER_XP,
								Prefs.get().getInteger(Defs.PREF_PLAYER_XP, 0) + stats.xp);
						Prefs.get().flush();

					} else {
						getReceiver().handlePacket(c, m);
					}
				}
			}

			@Override
			public void connected(Connection c) {
				if (getReceiver() != null) {
					getReceiver().handleConnection(c);
				}
			}

			@Override
			public void disconnected(Connection c) {

				if (getReceiver() != null) {
					getReceiver().handleDisconnection(c);
				}
				((AbstractControllerScreen) getScreen()).queueNextScreen(
						new LoadingScreen(JustAnotherGameController.this, "Connection lost, retrying..."));

			}
		});
	}

	public void setReceiver(JagClientPacketHandler receiver) {
		this.receiver = receiver;
	}

	protected void handleStateChange(PlayerStateChange sc) {
		getActionResolver().toast("Changing state : " + sc.stateId);
		boolean failed = false;
		switch (sc.stateId) {
		case JustAnotherGameController.PLAY_STATE:
			((AbstractControllerScreen) getScreen()).queueNextScreen(new ControllerScreen(this));
			break;
		case JustAnotherGameController.LOBBY_STATE:
			((AbstractControllerScreen) getScreen()).queueNextScreen(new LobbyControllerScreen(this));
			break;
		default:
			getActionResolver().toast("Unknown gamestate: " + sc.stateId);
			failed = true;
		}

		if (!failed) {
			getClient().broadcast(new PlayerStateChangeResponse(sc.stateId));
		}
	}

	protected JagClientPacketHandler getReceiver() {
		return receiver;
	}

	private void init() {
		TweenGlobal.init();
		Assets.load();
		client = new JagClient();
	}

	public JagClient getClient() {
		return client;
	}

}
