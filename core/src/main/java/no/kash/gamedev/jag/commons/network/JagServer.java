package no.kash.gamedev.jag.commons.network;

import java.io.IOException;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import no.kash.gamedev.jag.commons.network.packets.GamePacket;

public class JagServer {
	private final Server server;
	private int port;
	private MessageListener listener;

	public JagServer() {
		server = new Server();
		new HashMap<>();
		init();
	}

	private void init() {
		JagKryo.register(server.getKryo());
		server.addListener(new Listener() {
			@Override
			public void connected(Connection connection) {
				if (listener != null) {
					listener.onConnection(connection);
				}
			}

			@Override
			public void disconnected(Connection connection) {
				if (listener != null) {
					listener.onDisconnection(connection);
				}
			}

			@Override
			public void received(Connection connection, Object object) {
				if (listener != null && object instanceof GamePacket) {
					listener.onMessage(connection, (GamePacket) object);
				}
			}
		});
	}

	public void update(int timeout) throws IOException {
		server.update(timeout);
	}

	public void listen(int port) {
		server.start();	
		this.port = port;
		try {
			server.bind(port);
		} catch (IOException e) {
			e.printStackTrace();
			Gdx.app.exit();
		}
	}

	public void send(int id, GamePacket packet) {
		server.sendToTCP(id, packet);
	}

	public void broadcast(GamePacket packet) {
		server.sendToAllTCP(packet);
	}

	public MessageListener getListener() {
		return listener;
	}

	public void setListener(MessageListener listener) {
		this.listener = listener;
	}

	public void stop() {
		server.stop();
	}

	public int getPort() {
		return port;
	}
}
