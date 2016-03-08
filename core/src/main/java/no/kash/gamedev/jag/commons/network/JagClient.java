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
	private MessageListener listener;

	public JagClient() {
		client = new Client();
		init();
	}

	private void init() {
		JagKryo.register(client.getKryo());
		client.addListener(new Listener() {
			@Override
			public void connected(Connection connection) {
				id = connection.getID();
				if (listener != null) {
					listener.onConnection(connection);
				}
			}

			@Override
			public void received(Connection connection, Object object) {
				if (listener != null && object instanceof GamePacket) {
					listener.onMessage(connection, (GamePacket) object);
				}
			}

			@Override
			public void disconnected(Connection connection) {
				if (listener != null) {
					listener.onDisconnection(connection);
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

	public MessageListener getListener() {
		return listener;
	}

	public void setListener(MessageListener listener) {
		this.listener = listener;
	}

	public int getId() {
		return id;
	}

}
