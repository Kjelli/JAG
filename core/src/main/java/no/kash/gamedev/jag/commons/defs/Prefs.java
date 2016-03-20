package no.kash.gamedev.jag.commons.defs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Prefs {

	private static Preferences prefs;

	public static Preferences get() {
		if (prefs == null) {
			prefs = Gdx.app.getPreferences(Defs.PREFERENCE_NAME);
		}
		return prefs;
	}

}
