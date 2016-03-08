package no.kash.gamedev.jag.controller.input;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import aurelienribon.tweenengine.Tween;
import no.kash.gamedev.jag.commons.tweens.TweenGlobal;
import no.kash.gamedev.jag.commons.tweens.accessors.ColorAccessor;

public class Joystick {

	private final Touchpad tp;
	private final Skin tpSkin;
	private final TouchpadStyle tpStyle;

	private boolean active;

	public Joystick(float x, float y, float deadzoneRadius) {
		tpSkin = new Skin();

		tpSkin.add("touchBackground", new Texture("touchBackground.png"));
		tpSkin.add("touchKnob", new Texture("touchKnob.png"));

		tpStyle = new TouchpadStyle();

		Drawable touchBackground = tpSkin.getDrawable("touchBackground");
		Drawable touchKnob = tpSkin.getDrawable("touchKnob");

		tpStyle.background = touchBackground;
		tpStyle.background.setMinWidth(500);
		tpStyle.background.setMinHeight(500);

		tpStyle.knob = touchKnob;
		tpStyle.knob.setMinWidth(300);
		tpStyle.knob.setMinHeight(300);

		tp = new Touchpad(1.0f, tpStyle);
		tp.setX(x);
		tp.setY(y);
		tp.setColor(1.0f, 1.0f, 1.0f, 0.25f);
		tp.setBounds(25, 25, 600, 600);

		tp.addListener(new EventListener() {

			@Override
			public boolean handle(Event event) {
				boolean wasActive = active;
				active = (getXValue() != 0 || getYValue() != 0);
				if (active && !wasActive) {
					TweenGlobal.start(Tween.to(tp.getColor(), ColorAccessor.TYPE_RGBA, 0.200f).target(1, 1, 1, 0.8f));
				} else if (!active && wasActive) {
					TweenGlobal.start(Tween.to(tp.getColor(), ColorAccessor.TYPE_RGBA, 0.200f).target(1, 1, 1, 0.25f));
				}
				return true;
			}
		});

	}

	public float getXValue() {
		return tp.getKnobPercentX();
	}

	public float getYValue() {
		return tp.getKnobPercentY();
	}

	public Touchpad getTouchpad() {
		return tp;
	}

}
