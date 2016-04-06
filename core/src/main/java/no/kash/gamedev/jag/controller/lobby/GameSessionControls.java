package no.kash.gamedev.jag.controller.lobby;

import no.kash.gamedev.jag.commons.defs.Defs;
import no.kash.gamedev.jag.controller.preferences.GameSessionPreferences;
import no.kash.gamedev.jag.game.gamesession.GameMode;
import no.kash.gamedev.jag.game.gamesession.GameSession;

public class GameSessionControls {
	public GameSession session;

	public GameSessionControls() {
		GameSessionPreferences.load();

		session = new GameSession();
		session.gameMode = GameMode.values()[GameSessionPreferences.gameModeIndex];
		session.roundTime = GameSessionPreferences.roundTime;
		session.roundsToWin = GameSessionPreferences.roundsToWin;
		session.startingHealth = GameSessionPreferences.startingHealth;
		session.dropIn = GameSessionPreferences.dropIn;
		session.testMode = GameSessionPreferences.testMode;
		session.friendlyFire = GameSessionPreferences.friendlyFire;
	}

	public boolean nextOptionDropIn() {
		session.dropIn = !session.dropIn;
		GameSessionPreferences.setDropIn(session.dropIn);
		return session.dropIn;
	}

	public GameMode nextOptionGameMode() {
		GameMode[] gameModes = GameMode.values();
		GameMode returnVal = null;
		for (int i = 0; i < gameModes.length; i++) {
			if (session.gameMode == gameModes[i]) {
				int newIndex = (i + 1) % gameModes.length;
				returnVal = gameModes[newIndex];
				GameSessionPreferences.setGameModeIndex(newIndex);
				break;
			}
		}
		session.gameMode = returnVal;
		return returnVal;
	}

	public int nextOptionRoundTime() {
		int newTime = -1;
		for (int i = 0; i < Defs.ROUND_TIME_OPTIONS.length; i++) {
			if (Defs.ROUND_TIME_OPTIONS[i] == session.roundTime) {
				int newIndex = (i + 1) % Defs.ROUND_TIME_OPTIONS.length;
				newTime = Defs.ROUND_TIME_OPTIONS[newIndex];
				break;
			}
		}
		session.roundTime = newTime;
		GameSessionPreferences.setRoundTime(session.roundTime);
		return newTime;
	}

	public int nextOptionRoundsToWin() {
		int newRoundsToWin = -1;
		for (int i = 0; i < Defs.ROUND_WIN_OPTIONS.length; i++) {
			if (Defs.ROUND_WIN_OPTIONS[i] == session.roundsToWin) {
				int newIndex = (i + 1) % Defs.ROUND_WIN_OPTIONS.length;
				newRoundsToWin = Defs.ROUND_WIN_OPTIONS[newIndex];
				break;
			}
		}
		session.roundsToWin = newRoundsToWin;
		GameSessionPreferences.setRoundsToWin(session.roundsToWin);
		return newRoundsToWin;
	}

	public int nextOptionStartingHealth() {
		int newStartingHealth = -1;
		for (int i = 0; i < Defs.STARTING_HEALTH_OPTIONS.length; i++) {
			if (Defs.STARTING_HEALTH_OPTIONS[i] == session.startingHealth) {
				int newIndex = (i + 1) % Defs.STARTING_HEALTH_OPTIONS.length;
				newStartingHealth = Defs.STARTING_HEALTH_OPTIONS[newIndex];
				break;
			}
		}
		session.startingHealth = newStartingHealth;
		GameSessionPreferences.setStartingHealth(session.startingHealth);
		;
		return newStartingHealth;
	}

	public boolean nextOptionTestMode() {
		session.testMode = !session.testMode;
		GameSessionPreferences.setTestMode(session.testMode);
		return session.testMode;
	}

	public int getGameModeIndex() {
		for (int i = 0; i < GameMode.values().length; i++) {
			if (session.gameMode == GameMode.values()[i]) {
				return i;
			}
		}

		return -1;
	}

	public boolean nextOptionFriendlyFire() {
		session.friendlyFire = !session.friendlyFire;
		GameSessionPreferences.setFriendlyFire(session.friendlyFire);
		return session.friendlyFire;
	}

	public boolean nextOptionDrawNames() {
		session.drawNames = !session.drawNames;
		GameSessionPreferences.setDrawNames(session.drawNames);
		return session.drawNames;
	}
}
