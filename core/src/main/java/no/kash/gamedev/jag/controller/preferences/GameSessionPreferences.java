package no.kash.gamedev.jag.controller.preferences;

import no.kash.gamedev.jag.commons.defs.Defs;
import no.kash.gamedev.jag.commons.defs.Prefs;

public class GameSessionPreferences {
	public static int gameModeIndex;
	public static int roundsToWin;
	public static int roundTime;
	public static int startingHealth;

	public static boolean dropIn;
	public static boolean testMode;
	public static boolean friendlyFire;
	public static boolean drawNames;

	public static void load() {
		gameModeIndex = Prefs.get().getInteger(Defs.PREF_SESSION_GM_INDEX, 0);
		roundsToWin = Prefs.get().getInteger(Defs.PREF_SESSION_RTW, Defs.ROUND_TIME_OPTIONS[0]);
		roundTime = Prefs.get().getInteger(Defs.PREF_SESSION_ROUNDTIME, Defs.ROUND_TIME_OPTIONS[0]);
		startingHealth = Prefs.get().getInteger(Defs.PREF_SESSION_START_HP, Defs.STARTING_HEALTH_OPTIONS[3]);
		dropIn = Prefs.get().getBoolean(Defs.PREF_SESSION_DROP_IN, true);
		testMode = Prefs.get().getBoolean(Defs.PREF_SESSION_TEST_MODE, false);
		friendlyFire = Prefs.get().getBoolean(Defs.PREF_SESSION_FRIENDLY_FIRE, false);
		drawNames = Prefs.get().getBoolean(Defs.PREF_SESSION_DRAW_NAMES, true);
	}

	public static void save() {
		Prefs.get().putInteger(Defs.PREF_SESSION_GM_INDEX, gameModeIndex);
		Prefs.get().putInteger(Defs.PREF_SESSION_RTW, roundsToWin);
		Prefs.get().putInteger(Defs.PREF_SESSION_ROUNDTIME, roundTime);
		Prefs.get().putInteger(Defs.PREF_SESSION_START_HP, startingHealth);
		Prefs.get().putBoolean(Defs.PREF_SESSION_DROP_IN, dropIn);
		Prefs.get().putBoolean(Defs.PREF_SESSION_TEST_MODE, testMode);
		Prefs.get().putBoolean(Defs.PREF_SESSION_FRIENDLY_FIRE, friendlyFire);
		Prefs.get().putBoolean(Defs.PREF_SESSION_DRAW_NAMES, drawNames);
		Prefs.get().flush();
	}

	public static int getGameModeIndex() {
		return gameModeIndex;
	}

	public static void setGameModeIndex(int gameModeIndex) {
		GameSessionPreferences.gameModeIndex = gameModeIndex;
		save();
	}

	public static int getRoundsToWin() {
		return roundsToWin;
	}

	public static void setRoundsToWin(int roundsToWin) {
		GameSessionPreferences.roundsToWin = roundsToWin;
		save();
	}

	public static int getRoundTime() {
		return roundTime;
	}

	public static void setRoundTime(int roundTime) {
		GameSessionPreferences.roundTime = roundTime;
		save();
	}

	public static int getStartingHealth() {
		return startingHealth;
	}

	public static void setStartingHealth(int startingHealth) {
		GameSessionPreferences.startingHealth = startingHealth;
		save();
	}

	public static boolean isDropIn() {
		return dropIn;
	}

	public static void setDropIn(boolean dropIn) {
		GameSessionPreferences.dropIn = dropIn;
		save();
	}

	public static boolean isTestMode() {
		return testMode;
	}

	public static void setTestMode(boolean testMode) {
		GameSessionPreferences.testMode = testMode;
		save();
	}

	public static void setFriendlyFire(boolean friendlyFire) {
		GameSessionPreferences.friendlyFire = friendlyFire;
		save();
	}

	public static void setDrawNames(boolean drawNames) {
		GameSessionPreferences.drawNames = drawNames;
		save();
	}

}
