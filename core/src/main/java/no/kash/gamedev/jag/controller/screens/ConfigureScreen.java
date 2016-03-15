package no.kash.gamedev.jag.controller.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import no.kash.gamedev.jag.commons.defs.Defs;
import no.kash.gamedev.jag.commons.tweens.TweenGlobal;
import no.kash.gamedev.jag.commons.tweens.accessors.ColorAccessor;
import no.kash.gamedev.jag.controller.JustAnotherGameController;

public class ConfigureScreen extends AbstractControllerScreen {

	Table table;
	Label label;
	TextField connectionString;
	TextButton connect;

	public ConfigureScreen(JustAnotherGameController game) {
		super(game);
	}

	@Override
	protected void update(float delta) {

	}

	@Override
	protected void draw(float delta) {

	}

	@Override
	protected void onShow() {
		setBackgroundColor(new Color(1.0f, 1.0f, 1.0f, 1.0f));
		Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

		table = new Table(skin);
		table.setFillParent(true);

		table.align(Align.center | Align.top);

		// Load previously saved connectionString, if any
		String addr = Gdx.app.getPreferences(Defs.PREFERENCE_NAME).getString(Defs.CONNECTION_ADDRESS);

		label = new Label("Connect to the game", skin);
		connectionString = new TextField(addr, skin);

		connect = new TextButton("GO", skin);
		connect.addListener(new EventListener() {
			@Override
			public boolean handle(Event event) {
				game.setScreen(new ControllerScreen(connectionString.getText(), game));
				return true;
			}
		});

		table.add(label).row();
		table.add(connectionString).fillX().row();
		table.add(connect).row();

		stage.addActor(table);
		final TweenCallback colorLoop = new TweenCallback() {
			@Override
			public void onEvent(int arg0, BaseTween<?> arg1) {
				if (arg0 == TweenCallback.COMPLETE && !isDisposed()) {
					Color newColor = new Color((float) Math.random(), (float) Math.random(), (float) Math.random(),
							1.0f);
					TweenGlobal.start(Tween.to(getBackgroundColor(), ColorAccessor.TYPE_RGBA, 1.0f)
							.target(newColor.r, newColor.g, newColor.b).setCallback(this));
				}
			}
		};

		TweenGlobal.start(Tween.to(getBackgroundColor(), ColorAccessor.TYPE_RGBA, 1.0f).target(0.0f, 0.0f, 0.0f, 1.0f)
				.setCallback(colorLoop));
	}

}
