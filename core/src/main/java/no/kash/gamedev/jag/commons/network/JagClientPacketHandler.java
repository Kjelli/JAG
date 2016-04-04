package no.kash.gamedev.jag.commons.network;

import com.esotericsoftware.kryonet.Connection;

import no.kash.gamedev.jag.commons.network.packets.GamePacket;
import no.kash.gamedev.jag.commons.network.packets.PlayerInput;

public interface JagClientPacketHandler {
	
	void handleConnection(Connection c);

	void handleDisconnection(Connection c);

	void handlePacket(Connection c, GamePacket m);

}
