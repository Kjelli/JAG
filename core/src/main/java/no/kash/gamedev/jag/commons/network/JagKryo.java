package no.kash.gamedev.jag.commons.network;

import com.esotericsoftware.kryo.Kryo;

import no.kash.gamedev.jag.commons.network.packets.PlayerUpdate;
import no.kash.gamedev.jag.game.gamesession.roundhandlers.PlayerNewStats;
import no.kash.gamedev.jag.commons.network.packets.GamePacket;
import no.kash.gamedev.jag.commons.network.packets.PlayerConnect;
import no.kash.gamedev.jag.commons.network.packets.PlayerInput;
import no.kash.gamedev.jag.commons.network.packets.PlayerStateChange;
import no.kash.gamedev.jag.commons.network.packets.PlayerStateChangeResponse;

public class JagKryo {
	public static void register(Kryo kryo) {
		kryo.register(GamePacket.class);
		kryo.register(PlayerConnect.class);
		kryo.register(PlayerInput.class);
		kryo.register(PlayerUpdate.class);
		kryo.register(PlayerStateChange.class);
		kryo.register(PlayerStateChangeResponse.class);
		kryo.register(PlayerNewStats.class);
		kryo.register(float[].class);
		kryo.register(float[][].class);	
		kryo.register(int[].class);
		kryo.register(String[].class);
	}
}
