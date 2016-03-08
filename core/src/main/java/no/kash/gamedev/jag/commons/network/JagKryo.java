package no.kash.gamedev.jag.commons.network;

import com.esotericsoftware.kryo.Kryo;

import no.kash.gamedev.jag.commons.network.packets.GamePacket;
import no.kash.gamedev.jag.commons.network.packets.PlayerConnect;
import no.kash.gamedev.jag.commons.network.packets.PlayerInput;

public class JagKryo {
	public static void register(Kryo kryo) {
		kryo.register(GamePacket.class);
		kryo.register(PlayerConnect.class);
		kryo.register(PlayerInput.class);
		kryo.register(float[].class);
	}
}
