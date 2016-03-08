package no.kash.gamedev.jag.commons.network;

import com.esotericsoftware.kryonet.Connection;

import no.kash.gamedev.jag.commons.network.packets.GamePacket;

public interface MessageListener {
	public void onMessage(Connection c, GamePacket m);

	public void onConnection(Connection c);

	public void onDisconnection(Connection connection);
}
