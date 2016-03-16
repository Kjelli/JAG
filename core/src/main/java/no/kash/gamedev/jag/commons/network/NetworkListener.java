package no.kash.gamedev.jag.commons.network;

import com.esotericsoftware.kryonet.Connection;

import no.kash.gamedev.jag.commons.network.packets.GamePacket;

public interface NetworkListener {
	public void receivedPacket(Connection c, GamePacket m);

	public void connected(Connection c);

	public void disconnected(Connection connection);
}
