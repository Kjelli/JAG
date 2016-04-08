package no.kash.gamedev.jag.commons.network;

import java.io.IOException;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Queue;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import no.kash.gamedev.jag.commons.network.packets.GamePacket;

public class JagServer {
	private final Server server;
	private int port;
	private NetworkListener listener;

	private Queue<NetworkEvent> eventQueue;

	public JagServer() {
		server = new Server();
		new HashMap<>();
		init();
		eventQueue = new Queue<NetworkEvent>();
	}

	private void init() {
		JagKryo.register(server.getKryo());
		server.addListener(new Listener() {
			@Override
			public void connected(Connection connection) {
				eventQueue.addLast(new NetworkEvent(NetworkEvent.CONNECT, connection));
			}

			@Override
			public void disconnected(Connection connection) {
				eventQueue.addLast(new NetworkEvent(NetworkEvent.DISCONNECT, connection));
			}

			@Override
			public void received(Connection connection, Object object) {
				if (object instanceof GamePacket) {
					eventQueue.addLast(new NetworkEvent(NetworkEvent.PACKET, connection, (GamePacket) object));
				}
			}
		});
	}

	int lastSeen;

	public void update(float delta) {
		if (listener == null) {
			eventQueue.clear();
			return;
		}
		while (eventQueue.size > 0) {
			NetworkEvent next = eventQueue.removeFirst();
			if (next == null) {
				continue;
			}
			switch (next.type) {
			case NetworkEvent.CONNECT:
				listener.connected(next.connection);
				break;
			case NetworkEvent.DISCONNECT:
				listener.disconnected(next.connection);
				break;
			case NetworkEvent.PACKET:
				listener.receivedPacket(next.connection, next.packet);
				break;
			}
		}
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

	public void broadcastExcept(int id, GamePacket packet) {
		server.sendToAllExceptTCP(id, packet);

	}

	public NetworkListener getListener() {
		return listener;
	}

	public void setListener(NetworkListener listener) {
		this.listener = listener;
	}

	public void stop() {
		server.stop();
	}

	public int getPort() {
		return port;
	}
}
