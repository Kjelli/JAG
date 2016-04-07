package no.kash.gamedev.jag.commons.network.packets;

import java.util.Map;

import no.kash.gamedev.jag.game.gamesession.GameSettings;
import no.kash.gamedev.jag.game.gamesession.GameSettings.Setting;

public class GameSessionUpdate implements GamePacket {

	public GameSettings settings;

	public GameSessionUpdate() {

	}

	public GameSessionUpdate(GameSettings settings) {
		this.settings = settings;
	}

}
