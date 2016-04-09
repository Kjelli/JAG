package no.kash.gamedev.jag.controller.preferences;

import java.util.Map.Entry;

import no.kash.gamedev.jag.commons.defs.Prefs;
import no.kash.gamedev.jag.game.gamesession.GameSettings;
import no.kash.gamedev.jag.game.gamesession.GameSettings.Setting;

public class GameSessionPreferences {
	public static GameSettings settings;

	public static void load() {
		settings = new GameSettings();
		for (String key : settings.settings.keySet()) {
			settings.select(key, Prefs.get().get().get(key));
		}
	}

	public static void save() {
		for (Entry<String, Setting<?>> settingEntry : settings.settings.entrySet()) {
			Object value = settingEntry.getValue().getSelection().value;

			if (value instanceof Float) {
				Prefs.get().putFloat(settingEntry.getKey(), (Float) value);
			} else if (value instanceof Integer) {
				Prefs.get().putInteger(settingEntry.getKey(), (Integer) value);
			} else if (value instanceof Boolean) {
				Prefs.get().putBoolean(settingEntry.getKey(), (Boolean) value);
			} else if (value instanceof String) {
				Prefs.get().putString(settingEntry.getKey(), (String) value);
			} else if (value instanceof Enum) {
				int i = ((Enum<?>) value).ordinal();
				Prefs.get().putInteger(settingEntry.getKey(), i);
			}
		}
		Prefs.get().flush();
	}

	public static void put(String key, Object value) {
		settings.select(key, value);
		save();
	}

	public static <T> T get(String key, Class<T> cls) {
		return settings.getSelectedValue(key, cls);
	}

	public static void update(GameSettings settings) {
		for (Entry<String, Setting<?>> setting : settings.settings.entrySet()) {
			GameSessionPreferences.settings.select(setting.getKey(), setting.getValue().getSelection().value);
		}
		save();
	}

}
