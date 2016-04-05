package no.kash.gamedev.jag.commons.defs;

public class Defs {
	public static final float WIDTH = 800, HEIGHT = 600;

	// ################# Preference keys #################

	public static final String PREFERENCE_NAME = "JAGPREFS";
	public static final String CONNECTION_ADDRESS = "CONNECTION_ADDRESS";

	// Player preferences
	public static final String PREF_TIMES_PLAYED = "TIMES_PLAYED";
	public static final String PREF_PLAYER_NAME = "PLAYER_NAME";
	public static final String PREF_PLAYER_COLOR = "PLAYER_COLOR";
	public static final String PREF_PLAYER_LEVEL = "PLAYER_LEVEL";
	public static final String PREF_PLAYER_XP = "PLAYER_XP";

	// GameSession preferences
	public static final String PREF_SESSION_GM_INDEX = "SESSION_GM_INDEX";
	public static final String PREF_SESSION_RTW = "SESSION_RTW";
	public static final String PREF_SESSION_ROUNDTIME = "SESSION_ROUNDTIME";
	public static final String PREF_SESSION_START_HP = "SESSION_START_HP";
	public static final String PREF_SESSION_DROP_IN = "SESSION_DROP_IN";
	public static final String PREF_SESSION_TEST_MODE = "SESSION_TEST_MODE";

	// ################# Game Definitions #################
	public static final int[] ROUND_TIME_OPTIONS = new int[] { 30, 60, 90, 120, 180, 240, 300, 600, -1 };
	public static final int[] ROUND_WIN_OPTIONS = new int[] { 1, 3, 5, 7, 9, 11, 13, 15, -1 };
	public static final int[] STARTING_HEALTH_OPTIONS = new int[] { 1, 25, 50, 100, 250, -1 };
}
