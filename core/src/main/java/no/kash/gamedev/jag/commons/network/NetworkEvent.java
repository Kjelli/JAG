package no.kash.gamedev.jag.commons.network;

import com.esotericsoftware.kryonet.Connection;

import no.kash.gamedev.jag.commons.network.packets.GamePacket;

public class NetworkEvent {
	public static final int CONNECT = 1, DISCONNECT = 2, PACKET = 3;

	public final int type;
	public final Connection connection;
	public final GamePacket packet;

	public NetworkEvent(int type, Connection connection) {
		this(type, connection, null);
	}

	public NetworkEvent(int type, Connection connection, GamePacket packet) {
		if (type == PACKET && packet == null) {
			throw new IllegalArgumentException("Packet required if network event is of type PACKET!");
		}
		this.type = type;
		this.connection = connection;
		this.packet = packet;
	}

}
