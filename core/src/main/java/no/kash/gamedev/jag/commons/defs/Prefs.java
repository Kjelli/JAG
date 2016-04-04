package no.kash.gamedev.jag.commons.defs;

import java.security.GeneralSecurityException;

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
	
	public int expReqruied(){
		return 100 + (this.get().getInteger(Defs.PREF_PLAYER_LEVEL)*50);
	}

}
