package no.kash.gamedev.jag.game.commons.utils;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import no.kash.gamedev.jag.game.gamesession.GameSettings.Setting;

public class SettingClickListener extends ClickListener {
	public TextButton myButton;
	public Setting<?> mySetting;

	public SettingClickListener(TextButton button, Setting<?> setting) {
		this.myButton= button;
		this.mySetting= setting;
	}
}
