package no.kash.gamedev.jag.commons.network;

import com.esotericsoftware.kryonet.Connection;

import no.kash.gamedev.jag.commons.network.packets.GamePacket;

public interface NetworkListener {
	public void receivedPacket(Connection connection, GamePacket packet);

	public void connected(Connection connection);

	public void disconnected(Connection connection);
}
