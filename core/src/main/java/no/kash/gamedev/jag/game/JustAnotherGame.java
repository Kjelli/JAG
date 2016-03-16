package no.kash.gamedev.jag.game;

import com.esotericsoftware.kryonet.Connection;

import no.kash.gamedev.jag.JagEndpoint;
import no.kash.gamedev.jag.actionresolvers.ActionResolver;
import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.commons.network.JagReceiver;
import no.kash.gamedev.jag.commons.network.JagServer;
import no.kash.gamedev.jag.commons.network.NetworkListener;
import no.kash.gamedev.jag.commons.network.packets.GamePacket;
import no.kash.gamedev.jag.commons.network.packets.PlayerConnect;
import no.kash.gamedev.jag.commons.network.packets.PlayerInput;
import no.kash.gamedev.jag.commons.tweens.TweenGlobal;
import no.kash.gamedev.jag.game.screens.GameScreen;

public class JustAnotherGame extends JagEndpoint {

	private JagReceiver receiver;
	public static long ticks = 0;

	public JustAnotherGame(ActionResolver resolver) {
		super(resolver);
	}

	private JagServer server;

	@Override
	public void create() {
		init();
		setScreen(new GameScreen(this));
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

	}

	@Override
	public void render() {
		super.render();
		ticks++;
	}

	public JagServer getServer() {
		return server;
	}

	public JagReceiver getReceiver() {
		return receiver;
	}

	public void setReceiver(JagReceiver receiver) {
		this.receiver = receiver;
	}

}
