package no.kash.gamedev.jag.game;

import com.esotericsoftware.kryonet.Connection;

import no.kash.gamedev.jag.JagEndpoint;
import no.kash.gamedev.jag.actionresolvers.ActionResolver;
import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.commons.network.JagServerPacketHandler;
import no.kash.gamedev.jag.commons.network.JagServer;
import no.kash.gamedev.jag.commons.network.NetworkListener;
import no.kash.gamedev.jag.commons.network.packets.GamePacket;
import no.kash.gamedev.jag.commons.network.packets.PlayerInput;
import no.kash.gamedev.jag.commons.tweens.TweenGlobal;
import no.kash.gamedev.jag.game.screens.PlayScreen;
import no.kash.gamedev.jag.game.gamesession.GameSession;
import no.kash.gamedev.jag.game.screens.LobbyScreen;
import no.kash.gamedev.jag.game.screens.MapSelectionScreen;

public class JustAnotherGame extends JagEndpoint {

	private JagServerPacketHandler receiver;
	public static long ticks = 0;

	public JustAnotherGame(ActionResolver resolver) {
		super(resolver);
	}

	private JagServer server;

	@Override
	public void create() {
		init();
		setScreen(new LobbyScreen(this, new GameSession()));
	}

	private void init() {
		TweenGlobal.init();
		Assets.load();
		server = new JagServer();
		server.setListener(new NetworkListener() {

			@Override
			public void receivedPacket(Connection c, GamePacket m) {
				if (m instanceof PlayerInput) {
					PlayerInput input = (PlayerInput) m;
					if (getReceiver() != null) {
						getReceiver().handleInput(input);
					}
				} else {
					if (getReceiver() != null) {
						getReceiver().handlePacket(c, m);
					}
				}
			}

			@Override
			public void connected(Connection c) {
				System.out.println("Connection " + c.getID() + " from " + c.getRemoteAddressTCP());
			}

			@Override
			public void disconnected(Connection c) {
				if (getReceiver() != null) {
					getReceiver().handleDisconnection(c);
				}
			}
		});

		server.listen(13337);
	}

	@Override
	public void render() {
		super.render();
		ticks++;
	}

	public JagServer getServer() {
		return server;
	}

	public JagServerPacketHandler getReceiver() {
		return receiver;
	}

	public void setReceiver(JagServerPacketHandler receiver) {
		this.receiver = receiver;
	}

}
