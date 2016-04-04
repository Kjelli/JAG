package no.kash.gamedev.jag.controller;

import com.badlogic.gdx.graphics.Color;

import no.kash.gamedev.jag.commons.defs.Defs;
import no.kash.gamedev.jag.commons.defs.Prefs;

public class Player {
	
	private static String name;
	private static Color color;
	
	private static int level;
	private static int exp;
	
	private static int timesPlayed;
	
	public static void load(){
		name = Prefs.get().getString(Defs.PREF_PLAYER_NAME,"Minge");
		color = Color.valueOf(Prefs.get().getString(Defs.PREF_PLAYER_COLOR,Color.WHITE.toString()));
		level = Prefs.get().getInteger(Defs.PREF_PLAYER_LEVEL,1);
		exp = Prefs.get().getInteger(Defs.PREF_PLAYER_XP,0);
		timesPlayed = Prefs.get().getInteger(Defs.PREF_TIMES_PLAYED,0);
	}
	
	public static void save(){
		Prefs.get().putString(Defs.PREF_PLAYER_NAME,name);
		Prefs.get().putString(Defs.PREF_PLAYER_COLOR,color.toString());
		Prefs.get().putInteger(Defs.PREF_PLAYER_LEVEL,level);
		Prefs.get().putInteger(Defs.PREF_PLAYER_XP,exp);
		Prefs.get().putInteger(Defs.PREF_TIMES_PLAYED, timesPlayed);
		Prefs.get().flush();
	}
	
	public static void levelUp(){
		level++;
		save();
	}
	
	
	public static int expReq(){
		return 50 + (level*50);
	}

	public static String getName() {
		return name;
	}

	public static void setName(String name) {
		Player.name = name;
		save();
	}

	public static Color getColor() {
		return color;
	}

	public static void setColor(Color color) {
		Player.color = color;
		save();
	}

	public static int getLevel() {
		return level;
	}

	public static void setLevel(int level) {
		Player.level = level;
		save();
	}

	public static int getExp() {
		return exp;
	}

	public static void setExp(int exp) {
		while(exp>=expReq()){
			exp-=expReq();
			levelUp();
		}
		Player.exp = exp;
		save();
	}

	public static int getTimesPlayed() {
		return timesPlayed;
	}

	public static void setTimesPlayed(int timesPlayed) {
		Player.timesPlayed = timesPlayed;
	}
	
}
