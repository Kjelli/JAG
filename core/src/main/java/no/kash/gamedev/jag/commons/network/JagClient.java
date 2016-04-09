package no.kash.gamedev.jag.commons.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.badlogic.gdx.utils.Queue;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import no.kash.gamedev.jag.commons.network.packets.GamePacket;

public class JagClient {
	private final Client client;
	private int id;
	private String connectionString;
	private NetworkListener listener;

	private Queue<NetworkEvent> eventQueue;
	private Queue<GamePacket> packetsToSend;

	private boolean connected = false;

	public JagClient() {
		client = new Client();
		init();
		eventQueue = new Queue<>();
		packetsToSend = new Queue<>();
	}

	private void init() {
		JagKryo.register(client.getKryo());
		client.addListener(new Listener() {
			@Override
			public void connected(Connection connection) {
				connected = true;
				id = connection.getID();
				eventQueue.addLast(new NetworkEvent(NetworkEvent.CONNECT, connection));
			}

			@Override
			public void received(Connection connection, Object object) {
				if (object instanceof GamePacket) {
					eventQueue.addLast(new NetworkEvent(NetworkEvent.PACKET, connection, (GamePacket) object));
				}
			}

			@Override
			public void disconnected(Connection connection) {
				connected = false;
				eventQueue.addLast(new NetworkEvent(NetworkEvent.DISCONNECT, connection));
			}
		});
	}

	public void update(float delta) {
		if (listener == null) {
			eventQueue.clear();
		} else {
			while (eventQueue.size > 0) {
				NetworkEvent next = eventQueue.removeFirst();
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
		
		while(packetsToSend.size > 0 && client.isIdle()){
			client.sendTCP(packetsToSend.removeFirst());
		}
		
	}

	public void connect(String address, int port) throws UnknownHostException, IOException {
		connect(InetAddress.getByName(address), port);
	}

	public void connect(InetAddress address, int port) throws UnknownHostException, IOException {
		client.start();
		client.connect(2000, address, port);
	}

	public void send(GamePacket packet) {
		packetsToSend.addLast(packet);

	}

	public NetworkListener getListener() {
		return listener;
	}

	public void setListener(NetworkListener listener) {
		this.listener = listener;
	}

	public int getId() {
		return id;
	}

	public void connect(String connectionString) throws UnknownHostException, IOException {
		this.connectionString = connectionString;
		String[] elements = connectionString.split("[:]");
		String ipaddr;
		int port;
		if (elements.length == 2) {
			ipaddr = connectionString.split(":")[0];
			port = Integer.parseInt(connectionString.split(":")[1]);
		} else {
			ipaddr = connectionString;
			port = 13337;
		}
		connect(ipaddr, port);
	}

	public boolean isConnected() {
		return connected;
	}

	public String getConnectionString() {
		return connectionString;
	}

	public void disconnect() {
		client.close();
	}

}
