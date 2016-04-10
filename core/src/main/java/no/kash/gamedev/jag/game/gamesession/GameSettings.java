package no.kash.gamedev.jag.game.gamesession;

import static no.kash.gamedev.jag.commons.defs.Defs.BOOLEAN_OPTIONS;
import static no.kash.gamedev.jag.commons.defs.Defs.ROUND_TIME_OPTIONS;
import static no.kash.gamedev.jag.commons.defs.Defs.ROUND_WIN_OPTIONS;
import static no.kash.gamedev.jag.commons.defs.Defs.STARTING_HEALTH_OPTIONS;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import no.kash.gamedev.jag.commons.defs.Defs;
import no.kash.gamedev.jag.commons.network.packets.GameSessionUpdate;
import no.kash.gamedev.jag.controller.preferences.GameSessionPreferences;
import no.kash.gamedev.jag.game.gameobjects.players.guns.GunType;
import no.kash.gamedev.jag.game.gameobjects.players.item.ItemType;
import no.kash.gamedev.jag.game.gamesession.roundhandlers.suddendeaths.SuddenDeathType;

public class GameSettings {

	public static final Map<String, Setting<?>> DEFAULT_OPTIONS;
	static {
		DEFAULT_OPTIONS = new LinkedHashMap<String, Setting<?>>();
		DEFAULT_OPTIONS.put(Defs.SESSION_GM, new Setting<GameMode>("Game mode", GameMode.values()));
		DEFAULT_OPTIONS.put(Defs.SESSION_RTW, new Setting<Integer>("Win limit", ROUND_WIN_OPTIONS));
		DEFAULT_OPTIONS.put(Defs.SESSION_ROUNDTIME, new Setting<Integer>("Round time", ROUND_TIME_OPTIONS));
		DEFAULT_OPTIONS.put(Defs.SESSION_SUDDEN_DEATH,
				new Setting<SuddenDeathType>("Sudden death", SuddenDeathType.values()));
		DEFAULT_OPTIONS.put(Defs.SESSION_START_HP, new Setting<Integer>("Starting health", STARTING_HEALTH_OPTIONS));
		DEFAULT_OPTIONS.put(Defs.SESSION_STARTING_GUN, new Setting<GunType>("Starting gun", GunType.values()));
		DEFAULT_OPTIONS.put(Defs.SESSION_STARTING_ITEM, new Setting<ItemType>("Starting item", ItemType.values()));
		DEFAULT_OPTIONS.put(Defs.SESSION_SPAWN_GUNS, new Setting<Boolean>("Spawn guns", BOOLEAN_OPTIONS));
		DEFAULT_OPTIONS.put(Defs.SESSION_FRIENDLY_FIRE, new Setting<Boolean>("Friendly Fire", BOOLEAN_OPTIONS));
		DEFAULT_OPTIONS.put(Defs.SESSION_DROP_IN, new Setting<Boolean>("Drop in", BOOLEAN_OPTIONS));
		DEFAULT_OPTIONS.put(Defs.SESSION_DRAW_NAMES, new Setting<Boolean>("Show names", BOOLEAN_OPTIONS));
		DEFAULT_OPTIONS.put(Defs.SESSION_TEST_MODE, new Setting<Boolean>("Test mode", BOOLEAN_OPTIONS));
		DEFAULT_OPTIONS.put(Defs.SESSION_DEBUG_DRAW, new Setting<Boolean>("Draw debug", BOOLEAN_OPTIONS));
	}

	public Map<String, Setting<?>> settings;

	public GameSettings() {
		settings = new LinkedHashMap<String, Setting<?>>();
		for (Entry<String, Setting<?>> field : DEFAULT_OPTIONS.entrySet()) {
			settings.put(field.getKey(), field.getValue());
		}
	}

	public GameSettings(GameSettings other) {
		for (Entry<String, Setting<?>> entry : other.settings.entrySet()) {
			settings.put(entry.getKey(), entry.getValue());
		}
	}

	public Option<?> selectNext(String key) {
		return settings.get(key).selectNext();
	}

	public void fromPacket(GameSessionUpdate update) {
		for (Entry<String, Setting<?>> entry : update.settings.settings.entrySet()) {
			settings.get(entry.getKey()).selectedIndex = entry.getValue().selectedIndex;
		}
	}

	public void fromPreferences() {
		for (Entry<String, Setting<?>> entry : GameSessionPreferences.settings.settings.entrySet()) {
			settings.get(entry.getKey()).selectedIndex = entry.getValue().selectedIndex;
		}
	}

	public <T> Option<T> getSelection(String key, Class<T> cls) {
		return (Option<T>) settings.get(key).getSelection();
	}

	public <T> T getSelectedValue(String key, Class<T> cls) {
		return (T) settings.get(key).getSelection().value;
	}

	public static class Setting<T> {

		public String displayName;
		public Option<T>[] options;
		public int selectedIndex = 0;

		@Deprecated
		public Setting() {
			// Required by kryo
		}

		public Setting(String displayName, T[] array) {
			this.displayName = displayName;
			this.options = fromArray(array);
		}

		public Setting(String displayName, Option<T>[] options, int selectedIndex) {
			this.displayName = displayName;
			this.options = options;
			this.selectedIndex = selectedIndex;
		}

		public Option<T> getSelection() {
			return options[selectedIndex];
		}

		public Option<T> selectNext() {
			selectedIndex = (selectedIndex + 1) % options.length;
			return getSelection();
		}

		public static <T> Option<T>[] fromArray(T[] array) {
			Option<T>[] options = new Option[array.length];
			for (int i = 0; i < array.length; i++) {
				options[i] = new Option<T>(array[i]);
			}
			return options;
		}

		public void select(Object object) {
			for (int i = 0; i < options.length; i++) {
				if (options[i].value.equals(object)) {
					selectedIndex = i;
					return;
				}
			}

			if (object instanceof Integer) {
				selectedIndex = (Integer) object;
			}
		}

		@Override
		public String toString() {
			return displayName + ": " + getSelection().displayName;
		}
	}

	public static class Option<T> {
		public T value;
		public String displayName;

		@Deprecated
		public Option() {
			// Required by kryo
		}

		public Option(T value) {
			this.value = value;
			if (value instanceof Boolean) {
				displayName = ((Boolean) value ? "on" : "off");
			} else {
				displayName = value.toString();
			}
		}

		public Option(T value, String displayName) {
			this.value = value;
			this.displayName = displayName;
		}

		@Override
		public String toString() {
			return (displayName == null ? value : displayName) + "";
		}
	}

	public void select(String key, Object object) {
		settings.get(key).select(object);
	}

}
