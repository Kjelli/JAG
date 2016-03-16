package no.kash.gamedev.jag.commons.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import no.kash.gamedev.jag.commons.network.packets.GamePacket;

public class JagClient {
	private final Client client;
	private int id;
	private String connectionString;
	private NetworkListener listener;
	
	private boolean connected = false;

	public JagClient() {
		client = new Client();
		init();
	}

	private void init() {
		JagKryo.register(client.getKryo());
		client.addListener(new Listener() {
			@Override
			public void connected(Connection connection) {
				connected = true;
				id = connection.getID();
				if (listener != null) {
					listener.connected(connection);
				}
			}

			@Override
			public void received(Connection connection, Object object) {
				if (listener != null && object instanceof GamePacket) {
					listener.receivedPacket(connection, (GamePacket) object);
				}
			}

			@Override
			public void disconnected(Connection connection) {
				connected = false;
				if (listener != null) {
					listener.disconnected(connection);
				}
			}
		});
	}

	public void update(int timeout) throws IOException {
		client.update(timeout);
	}

	public void connect(String address, int port) throws UnknownHostException, IOException {
		connect(InetAddress.getByName(address), port);
	}

	public void connect(InetAddress address, int port) throws UnknownHostException, IOException {
		client.start();
		client.connect(2000, address, port);
	}

	public void broadcast(GamePacket packet) {
		client.sendTCP(packet);
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

}
