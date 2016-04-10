package no.kash.gamedev.jag.commons.network;

import java.util.LinkedHashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Texture;
import com.esotericsoftware.kryo.Kryo;

import no.kash.gamedev.jag.commons.network.packets.GamePacket;
import no.kash.gamedev.jag.commons.network.packets.GameSessionUpdate;
import no.kash.gamedev.jag.commons.network.packets.PlayerAvailableMaps;
import no.kash.gamedev.jag.commons.network.packets.PlayerConnect;
import no.kash.gamedev.jag.commons.network.packets.PlayerInput;
import no.kash.gamedev.jag.commons.network.packets.PlayerMapVote;
import no.kash.gamedev.jag.commons.network.packets.PlayerNewStats;
import no.kash.gamedev.jag.commons.network.packets.PlayerStateChange;
import no.kash.gamedev.jag.commons.network.packets.PlayerStateChangeResponse;
import no.kash.gamedev.jag.commons.network.packets.PlayerUpdate;
import no.kash.gamedev.jag.game.gameobjects.players.guns.GunType;
import no.kash.gamedev.jag.game.gamesession.GameMode;
import no.kash.gamedev.jag.game.gamesession.GameSettings;
import no.kash.gamedev.jag.game.gamesession.GameSettings.Option;
import no.kash.gamedev.jag.game.gamesession.GameSettings.Setting;
import no.kash.gamedev.jag.game.gamesession.roundhandlers.suddendeaths.SuddenDeathType;

public class JagKryo {
	public static void register(Kryo kryo) {
		kryo.register(GamePacket.class);
		kryo.register(PlayerConnect.class);
		kryo.register(PlayerInput.class);
		kryo.register(PlayerUpdate.class);
		kryo.register(PlayerStateChange.class);
		kryo.register(PlayerStateChangeResponse.class);
		kryo.register(PlayerNewStats.class);
		kryo.register(PlayerMapVote.class);
		kryo.register(PlayerAvailableMaps.class);
		kryo.register(GameSessionUpdate.class);
		kryo.register(GameSettings.class);
		kryo.register(GameMode.class);
		kryo.register(SuddenDeathType.class);
		kryo.register(GunType.class);
		kryo.register(Texture.class);
		kryo.register(Setting.class);
		kryo.register(Option.class);
		kryo.register(Option[].class);
		kryo.register(Map.class);
		kryo.register(LinkedHashMap.class);
		kryo.register(float[].class);
		kryo.register(float[][].class);
		kryo.register(int[].class);
		kryo.register(String[].class);
	}
}
