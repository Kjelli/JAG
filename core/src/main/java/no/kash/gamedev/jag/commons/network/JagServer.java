package no.kash.gamedev.jag.commons.network;

import java.io.IOException;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Queue;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import no.kash.gamedev.jag.commons.network.QueuedPacket.Type;
import no.kash.gamedev.jag.commons.network.packets.GamePacket;

public class JagServer {
	private final Server server;
	private int port;
	private NetworkListener listener;

	private Queue<NetworkEvent> eventQueue;
	private Queue<QueuedPacket> packetQueue;

	public JagServer() {
		server = new Server();
		new HashMap<>();
		init();
		eventQueue = new Queue<>();
		packetQueue = new Queue<>();
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

	public void update(float delta) {

		while (eventQueue.size > 0) {
			NetworkEvent next = eventQueue.removeFirst();
			if (next == null) {
				continue;
			}

			switch (next.type) {
			case NetworkEvent.CONNECT:
				if (listener != null) {
					listener.connected(next.connection);
				}
				break;
			case NetworkEvent.DISCONNECT:
				if (listener != null) {
					listener.disconnected(next.connection);
				}
				break;
			case NetworkEvent.PACKET:
				if (listener != null) {
					listener.receivedPacket(next.connection, next.packet);
				}
				break;
			}
		}

		while (packetQueue.size > 0) {
			QueuedPacket next = packetQueue.removeFirst();

			switch (next.type) {
			case BROADCAST:
				server.sendToAllTCP(next.packet);
				break;
			case BROADCAST_EXCEPT:
				server.sendToAllExceptTCP(next.id, next.packet);
				break;
			case SEND:
				server.sendToTCP(next.id, next.packet);
				break;
			default:
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
		packetQueue.addLast(new QueuedPacket(id, packet, Type.SEND));
	}

	public void broadcast(GamePacket packet) {
		packetQueue.addLast(new QueuedPacket(-1, packet, Type.BROADCAST));
	}

	public void broadcastExcept(int id, GamePacket packet) {
		packetQueue.addLast(new QueuedPacket(-1, packet, Type.BROADCAST_EXCEPT));
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
