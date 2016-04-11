package no.kash.gamedev.jag.commons.defs;

public class Defs {
	public static final float WIDTH = 1200, HEIGHT = 800;

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
	public static final String SESSION_GM = "SESSION_GM";
	public static final String PREF_SESSION_GM_INDEX = "SESSION_GM_INDEX";
	public static final String SESSION_RTW = "SESSION_RTW";
	public static final String SESSION_ROUNDTIME = "SESSION_ROUNDTIME";
	public static final String SESSION_START_HP = "SESSION_START_HP";
	public static final String SESSION_DROP_IN = "SESSION_DROP_IN";
	public static final String SESSION_TEST_MODE = "SESSION_TEST_MODE";
	public static final String SESSION_FRIENDLY_FIRE = "SESSION_FRIENDLY_FIRE";
	public static final String SESSION_DRAW_NAMES = "SESSION_DRAW_NAMES";
	public static final String SESSION_DEBUG_DRAW = "SESSION_DEBUG_DRAW";
	public static final String SESSION_SUDDEN_DEATH = "SESSION_SUDDEN_DEATH";
	public static final String SESSION_STARTING_GUN = "SESSION_STARTING_GUN";
	public static final String SESSION_STARTING_ITEM = "SESSION_STARTING_ITEM";
	public static final String SESSION_SPAWN_GUNS = "SESSION_SPAWN_GUNS";

	// Misc
	public static final Boolean[] BOOLEAN_OPTIONS = new Boolean[] { true, false };

	// ################# Game Definitions #################
	public static final Integer[] ROUND_TIME_OPTIONS = new Integer[] { 60, 90, 120, 180, 240, 300, 600, -1, 30  };
	public static final Integer[] ROUND_WIN_OPTIONS = new Integer[] { 3, 5, 7, 9, 11, 13, 15, -1, 1 };
	public static final Integer[] STARTING_HEALTH_OPTIONS = new Integer[] { 100 ,150, 200, 250, -1, 1, 25, 50, };



}
