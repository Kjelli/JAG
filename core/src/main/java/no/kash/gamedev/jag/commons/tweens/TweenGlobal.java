package no.kash.gamedev.jag.commons.tweens;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenAccessor;
import aurelienribon.tweenengine.TweenManager;
import no.kash.gamedev.jag.commons.tweens.accessors.ColorAccessor;
import no.kash.gamedev.jag.commons.tweens.accessors.Vector2Accessor;

public class TweenGlobal {
	private static TweenManager manager;
	private static Map<Class<?>, TweenAccessor<?>> accessors;

	public static void init() {
		// Required for tweening colors
		Tween.setCombinedAttributesLimit(4);
		manager = new TweenManager();
		accessors = new HashMap<>();
		accessors.put(Vector2.class, new Vector2Accessor());
		accessors.put(Color.class, new ColorAccessor());
		register();
	}

	private static void register() {
		for (Entry<Class<?>, TweenAccessor<?>> entry : accessors.entrySet()) {
			Tween.registerAccessor(entry.getKey(), entry.getValue());
		}
	}

	public static void update(float delta) {
		manager.update(delta);
	}

	public static void start(Tween tween) {
		tween.start(manager);
	}
}
